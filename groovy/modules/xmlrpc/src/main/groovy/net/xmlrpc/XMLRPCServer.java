/*

 Copyright 2004 (C) John Wilson. All Rights Reserved.

 Redistribution and use of this software and associated documentation
 ("Software"), with or without modification, are permitted provided
 that the following conditions are met:

 1. Redistributions of source code must retain copyright
    statements and notices.  Redistributions must also contain a
    copy of this document.

 2. Redistributions in binary form must reproduce the
    above copyright notice, this list of conditions and the
    following disclaimer in the documentation and/or other
    materials provided with the distribution.

 3. The name "groovy" must not be used to endorse or promote
    products derived from this Software without prior written
    permission of The Codehaus.  For written permission,
    please contact info@codehaus.org.

 4. Products derived from this Software may not be called "groovy"
    nor may "groovy" appear in their names without prior written
    permission of The Codehaus. "groovy" is a registered
    trademark of The Codehaus.

 5. Due credit should be given to The Codehaus -
    http://groovy.codehaus.org/

 THIS SOFTWARE IS PROVIDED BY THE CODEHAUS AND CONTRIBUTORS
 ``AS IS'' AND ANY EXPRESSED OR IMPLIED WARRANTIES, INCLUDING, BUT
 NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND
 FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED.  IN NO EVENT SHALL
 THE CODEHAUS OR ITS CONTRIBUTORS BE LIABLE FOR ANY DIRECT,
 INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR
 SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION)
 HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT,
 STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED
 OF THE POSSIBILITY OF SUCH DAMAGE.

 */

package groovy.net.xmlrpc;

import groovy.lang.Closure;
import groovy.lang.GroovyClassLoader;
import groovy.lang.GroovyObject;
import groovy.lang.GroovyObjectSupport;
import groovy.lang.GroovyRuntimeException;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.Method;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import uk.co.wilson.net.http.MinMLHTTPServer;
import uk.co.wilson.net.xmlrpc.XMLRPCMessageProcessor;

/**
 * @author John Wilson (tug@wilson.co.uk)
 *
 */
public class XMLRPCServer extends GroovyObjectSupport {
private byte[] base64 = new byte[600];
{
	for (int i = 0; i != this.base64.length; i++) {
		this.base64[i] = (byte)i;
	}
}
public byte[] getBase64() { return this.base64;} // bodge to allow testing

	private static byte[] host;
	static {
		try {
			host  = ("Host: " + InetAddress.getLocalHost().getHostName() +"\r\n").getBytes();
		} catch (UnknownHostException e) {
			host = "Host: unknown\r\n ".getBytes();
		}
	}
	private static final byte[] userAgent = "User-Agent: Groovy XML-RPC\r\n".getBytes();
	private static final byte[] contentTypeXML = "Content-Type: text/xml\r\n".getBytes();
	private static final byte[] contentLength = "Content-Length: ".getBytes();
	private static final byte[] startResponse = ("<?xml version=\"1.0\" encoding=\"ISO-8859-1\"?>\n" + 
												 "<methodResponse>\n" +
												 "\t<params>\n" +
												 "\t\t<param>\n").getBytes();
	private static final byte[] endResponse = ("\n" +
											   "\t\t</param>\n" +
											   "\t</params>\n" +
											   "</methodResponse>").getBytes();
	private static final byte[] startError = ("<?xml version=\"1.0\"?>\n" + 
											  "<methodResponse>\n" +
											  "\t<fault>\n" +
											  "\t\t<value>\n" +
											  "\t\t\t<struct>\n" +
											  "\t\t\t\t<member>\n" +
											  "\t\t\t\t\t<name>faultCode</name>\n" +
											  "\t\t\t\t\t<value><int>0</int></value>\n" +
											  "\t\t\t\t</member>\n" +
											  "\t\t\t\t<member>\n" +
											  "\t\t\t\t\t<name>faultString</name>\n" +
											  "\t\t\t\t\t<value><string>").getBytes();
	
	private static final byte[] endError = ("</string></value>\n" +
											"\t\t\t\t</member>\n" +
											"\t\t\t</struct>\n" +
											"\t\t</value>\n" +
											"\t</fault>\n" +
											"</methodResponse>\n").getBytes();
	
	private MinMLHTTPServer server = null;
	private final int minWorkers;
	private final int maxWorkers;
	private final int maxKeepAlives;
	private final int workerIdleLife;
	private final int socketReadTimeout;
	private final StringBuffer propertyPrefix = new StringBuffer();
	private final Map registeredMethods = new HashMap();

	/**
	 * @param minWorkers
	 * @param maxWorkers
	 * @param maxKeepAlives
	 * @param workerIdleLife
	 * @param socketReadTimeout
	 */
	public XMLRPCServer(final int minWorkers,
						final int maxWorkers,
						final int maxKeepAlives,
						final int workerIdleLife,
						final int socketReadTimeout)
	{
		this.minWorkers = minWorkers;
		this.maxWorkers = maxWorkers;
		this.maxKeepAlives = maxKeepAlives;
		this.workerIdleLife = workerIdleLife;
		this.socketReadTimeout = socketReadTimeout;
	}
	
	/**
	 * 
	 */
	public XMLRPCServer() {
		this(2, 10, 8, 60000, 60000);
	}
	
	/**
	 * @param serverSocket
	 */
	public void startServer(final ServerSocket serverSocket) throws IOException {
		if (this.server != null) stopServer();
		
		final MinMLHTTPServer server = new MinMLHTTPServer(serverSocket,
								                           this.minWorkers, 
														   this.maxWorkers, 
														   this.maxKeepAlives, 
														   this.workerIdleLife, 
														   this.socketReadTimeout) {

			/* (non-Javadoc)
			 * @see uk.co.wilson.net.MinMLSocketServer#makeNewWorker()
			 */
			protected Worker makeNewWorker() {
				return new HTTPWorker() {
					protected void processPost(final InputStream in,
											   final OutputStream out,
											   final String uri,
											   final String version)
						throws Exception
					{
						
						try {
						final StringBuffer buffer = new StringBuffer();
						final XMLRPCMessageProcessor requestParser = new XMLRPCMessageProcessor();
							
							out.write(version.getBytes());
							out.write(okMessage);
							out.write(userAgent);
							out.write(host);
							out.write(contentTypeXML);
							writeKeepAlive(out);
							out.write(contentLength);
							
							requestParser.parseMessage(in);
							
							final String methodName = requestParser.getMethodname();
							final List params = requestParser.getParams();
							final Object closure = XMLRPCServer.this.registeredMethods.get(methodName);
							
							if (closure == null) throw new GroovyRuntimeException("XML-RPC method " + methodName + " is not supported on this server");
							
							Object result = ((Closure)closure).call(params.toArray());
							
							if (result == null) result = new Integer(0);
							
							XMLRPCMessageProcessor.emit(buffer, result);
							
//							System.out.println(buffer.toString());
							
							final byte[] response = buffer.toString().getBytes("ISO-8859-1");
							
							out.write(String.valueOf(startResponse.length + response.length + endResponse.length).getBytes());
							out.write(endOfLine);
							out.write(endOfLine);
							out.write(startResponse);
							out.write(response);
							out.write(endResponse);
						}
						catch (final Throwable e) {
//						e.printStackTrace();
						final String message = e.getMessage();
						final byte[] error = ((message == null) ? endError.getClass().getName() : e.getMessage()).getBytes();
							
							out.write(String.valueOf(startError.length + error.length + endError.length).getBytes());
							out.write(endOfLine);
							out.write(endOfLine);
							out.write(startError);
							out.write(error);
							out.write(endError);
						}
					}
				};
			}
		};
		
		this.server = server;
		
		new Thread() {
			public void run() {
				server.start();
			}
		}.start();
	}
	
	public void stopServer() throws IOException {
		this.server.shutDown();
������G��ÅA1���H(f�^�@�K
�-�$�
dQ)Iրb��������z��-C	���X�E�0����5'�E]�vF��V�3At	�M�����3��#�v(���u�z\�������4?�Vm���~"�K��O��&�"L}��WqǠ�,gv�\��}v��4���:C����H 3Og�#h�e�+WK|۝�0�M[>C��oo0#�N:��X�(���
n���I�g<Udnu�k��p�P�|_�3�)��\��f@�z���H��u�6��v ��V������d�eR0=|0��7�lMX<��ᩲ��Y�qp}o���VK�/�z�<�т>��UQnѤY�Y�8�w��`;�h�tB�H��܀�j�LV��/8�ETd�S��^TF
�������������bV�[gM��,D��Q�9Ɔ�jb��_{p�n7��9��%�J?�nX�)!��r9a�HaQ
p����rů��nw��
F�U�h�?�uh����Z�۲,`9�����jn8�2j�,b�j�>,��Lʍ23�6Ѥj�aa����%��t�B��h�Dꆯ˘��ԝw���rӌm����oA�wtj%���~.p�`�c���
���o=z�.�U!���e��~!�����	Y�6�D�J����ȶ;H�����>
<�%e<wr�`[��-��~f�e>���-��v��xN��&�r��p��ue�����C�C{(W?�9
8�˻���~6�ǟ�p���9��\>+���xn��M┩w��+jJ{�	�?b⫞�����!߀��l��v���E�f�H��/�/̱#{ ��a�����F��n���''��cc�t�t�W�wp�n��~�;��!�PQ��)	�/�O�^�[(��`뛈�v���|�pR��"�z���I[&_��������´uj��ک��QA>�B����|*�-0��M)(����'c`�a����N��� �R�;p��1{s����'F �(ٗ��^y�w�o�J��ՙ�����7A��]��,��MM�~f���T8��+�݄a�껒}�
�o�o��7����/�a�#�W������wh����<iO����j`������;UT����;u�y���6�8�y���h��*�s�-s�|K~�����#A��#�̼}��|$�v^y�ؗ�զ�9�L��X)��嚭�,�pI=Za�q���j��,�I�>�iOQ��T����Mi����f���PM�T'v���3+s�����z�K����h�WI�Ők��zsg��JV6? ���'��a��������F`0%c�dQ�vW��%IY�OtF��6�rҒ�ք*�Y���)T���qTA�P�͌�V���T�7 �����~>�mӥ�c׷D���8�HI����5j���#P�v��5f��\~���X��*3�S�̕��mBdf�*Ò����!�k��/V�Š��5N��@F[���Z��W?��͗�{g��%+k������=�hN������-���ӷ5�3';%�cA3� ����Dz����	67?���N;�7�_�v­�J��׬~\�
��n�E�=s�[���NJ���a���"��6<٣��^[�X�v��
�5#��!�(a��ޗߏgbH�W�HCҪ2��J�!��|Ǔk:�؛��ւrn)�R�ɵ*QmMm����Q����M.ˌ�=xC��m26;w����C��P!�^th�Eu��ݓ�>���Ъ[���~A�J��q�6,����Y�@�'ū[J��v|�%=�%dj')G�����d�+Tk0�N^o�m�����ʆ����NG���0�[��ui��F� �?�x�}I~Q��u�S�Ð�
nL��'�a"���^�e6?,���\�g2�T�'�Ȓ��I��Wa�J�Y��������+��υ�<�o�qh���'Ҫ��%�cB���8Jw�p�����	Jg�`��3k�#���n.Qr�c����XQ�~�(��
��<�M�CsT�|	sm�ǳ@'h�
�3�N����J'.=V�+YJ������N��G<Te%���n�����L�|���NA�"�{���YK�Ʌ�k�ioKWx\K�ƷM�; i~��KT�Q��CͫDq�n�s�bN������c>8~@dFn�~T�@F���E��6�\��%@�lQO���{�{m�\�"�Z���y3O��mD�7N+F�#m�{2!	��>���N�.ϩm��
�
�"I�I������Rd�in���͑JV�YE\�h�z��M�j��V#Ukp�օo�6\�o�o
x��!�A�(1!n֙~�C�g��W���㜳�k�G�[{�1��0n�D&RPdbUL'Z0�#�`�KS�K�<���-J<Oɕq|�E�m�zΗn�cd�ioJ�sL,$ǚU-y�=T<Y"xQ�N2+��uI�=��K&��b�p)�8�����6���l2(�6�I�Fg���oP�	�n��`�r��VQ=�R)�@�d�� �[�,]7����Y�bz�L���!�p�8V����E�UN-¤y���S����YQ������p}���x�0�ӖgzWJ�j^�e��]V�-�X�p	3Ɣb��~�\��.W�-\f�P���'��{k�k�%�y�O1�	f	�ś�U�Ȏ��X��_	/o�\�|Q섙r/��0�9�.�1��!#��ّ
�}0��L��DO�ʀr]�!/8cP�B��@�,&��O��ׂ`���;�#V�Hc�H�_$�SvS�!'���0�z9��[��',�c,݃�U����.���ME�U�
%�rW4�!������9
ĨY��������X2��