// Copyright (c) 2001 The Wilson Partnership.
// All Rights Reserved.
// @(#)MinMLHTTPServer.java, 0.3, 14th October 2001
// Author: John Wilson - tug@wilson.co.uk

package uk.co.wilson.net.http;

/*
Copyright (c) 2001 John Wilson (tug@wilson.co.uk).
All rights reserved.
Redistribution and use in source and binary forms,
with or without modification, are permitted provided
that the following conditions are met:

Redistributions of source code must retain the above
copyright notice, this list of conditions and the
following disclaimer.

Redistributions in binary form must reproduce the
above copyright notice, this list of conditions and
the following disclaimer in the documentation and/or
other materials provided with the distribution.

THIS SOFTWARE IS PROVIDED BY JOHN WILSON ``AS IS'' AND ANY
EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO,
THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A
PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL JOHN WILSON
BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL,
EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED
TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE,
DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT
LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING
IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED
OF THE POSSIBILITY OF SUCH DAMAGE
*/

import uk.co.wilson.net.MinMLSocketServer;

import java.net.ServerSocket;
import java.net.Socket;

import java.io.InputStream;
import java.io.BufferedInputStream;
import java.io.OutputStream;
import java.io.BufferedOutputStream;
import java.io.IOException;

import java.util.StringTokenizer;

public abstract class MinMLHTTPServer extends MinMLSocketServer {
  public MinMLHTTPServer(final ServerSocket serverSocket,
                         final int minWorkers,
                         final int maxWorkers,
                         final int maxKeepAlives,
                         final int workerIdleLife,
                         final int socketReadTimeout)
  {
    super(serverSocket, minWorkers, maxWorkers, workerIdleLife);

    this.maxKeepAlives = maxKeepAlives;
    this.socketReadTimeout = socketReadTimeout;
  }

  private synchronized boolean startKeepAlive() {
   if (this.keepAliveCount < this.maxKeepAlives) {
      MinMLHTTPServer.this.keepAliveCount++;

      return true;
    }

    return false;
  }

  private synchronized void endKeepAlive() {
    this.keepAliveCount--;
  }

  private static class LimitedInputStream extends InputStream {
    public LimitedInputStream(final InputStream in, final int contentLength) {
      this.in = in;
      this.contentLength = contentLength;
    }

    public int available() throws IOException {
      return Math.min(this.in.available(), this.contentLength);
    }

    public void close() throws IOException {
      //
      // Don't close the input stream as there is more data
      // but skip past any unread data in this section
      //

      skip(this.contentLength);
    }

    public int read() throws IOException {
      if (this.contentLength == 0) return -1;

      this.contentLength--;

      return this.in.read();
    }

    public int read(final byte[] buffer) throws IOException {
      return read(buffer, 0, buffer.length);
     }

    public int read(final byte[] buffer, final int offset, int length) throws IOException {
      if (this.contentLength == 0) return -1;

      length = this.in.read(buffer, offset, Math.min(length, this.contentLength));

      if (length != -1) this.contentLength -= length;

      return length;
    }

    public long skip(long count) throws IOException {
      count = Math.min(count, this.contentLength);

      this.contentLength -= count;

      return this.in.skip(count);
     }

     private int contentLength;
     private final InputStream in;
  }

  protected abstract class HTTPWorker extends Worker {
    protected final void process(final Socket socket) throws Exception {
      try {
        socket.setSoTimeout(MinMLHTTPServer.this.socketReadTimeout);

        final InputStream in = new BufferedInputStream(socket.getInputStream());
        final OutputStream out = new BufferedOutputStream(socket.getOutputStream());
        int contentLength;

        do {
          contentLength = -1;

          while (readLine(in) != -1 && this.count == 0);  // skip any leading blank lines
          final StringTokenizer toks = new StringTokenizer(new String(this.buf, 0, this.count));
          final String method = toks.nextToken();
          final String uri = toks.nextToken();
          final String version = toks.hasMoreTokens() ? toks.nextToken() : "";

          while (readLine(in) != -1 && this.count != 0) {
          final String option = new String(this.buf, 0, this.count).trim().toLowerCase();

            if (option.startsWith("connection:")) {
              if (option.endsWith("keep-alive")) {
                if (!this.keepAlive)
                  this.keepAlive = MinMLHTTPServer.this.startKeepAlive();
              } else if (this.keepAlive) {
                  MinMLHTTPServer.this.endKeepAlive();
                  this.keepAlive = false;
              }
            } else if (option.startsWith("content-length:")) {
              contentLength = Integer.parseInt(option.substring(15).trim());
              //
              // This can throw NumberFormatException
              // In which case we will abort the transaction
              //
            }
          }

          if (contentLength == -1) {
            processMethod(in, out, method, uri, version);
          } else {
          final InputStream limitedIn = new LimitedInputStream(in, contentLength);

            processMethod(limitedIn, out, method, uri, version);

            limitedIn.close();  // skips unread bytes
          }

          out.flush();

        } while(contentLength != -1 && this.keepAlive);
      }
      finally {
        if (this.keepAlive == true) {
          MinMLHTTPServer.this.endKeepAlive();
          this.keepAlive = false;
        }
      }
    }

    protected void processMethod(final InputStream in,
                                 final OutputStream out,
                                 final String method,
                                 final String uri,
                                 final String version)
    throws Exception
    {
      if (method.equalsIgnoreCase("GET"))
        processGet(in, out, uri, version);
      else if (method.equalsIgnoreCase("HEAD"))
        processHead(in, out, uri, version);
      else if (method.equalsIgnoreCase("POST"))
        processPost(in, out, uri, version);
      else if (method.equalsIgnoreCase("PUT"))
        processPut(in, out, uri, version);
      else
        processOther(in, out, method, uri, version);
    }

    protected void processGet(final InputStream in,
                              final OutputStream out,
                              final String uri,
                              final String version)
      throws Exception
    {
      out.write(version.getBytes());
      out.write(errorMessage1);
      out.write(get);
      out.write(errorMessage2);
    }

    protected void processHead(final InputStream in,
                               final OutputStream out,
                               final String uri,
                               final String version)
      throws Exception
    {
      out.write(version.getBytes());
      out.write(errorMessage1);
      out.write(head);
      out.write(errorMessage2);
    }

    protected void processPost(final InputStream in,
                               final OutputStream out,
                               final String uri,
                               final String version)
      throws Exception
    {
      out.write(version.getBytes());
      out.write(errorMessage1);
      out.write(post);
      out.write(errorMessage2);
    }

    protected void processPut(final InputStream in,
                              final OutputStream out,
                              final String uri,
                              final Stri`M�U,� {�$s�Wd�C�)�ox�"�X9F9��C>ؼPE'�X�n&I{��0�(��� b���e�W
n<����N��������N���*|��IK�Sa�g�~~<8�t�;�d1C�@�;+�J�i�Snn0Mf*��D;�ThA,�������]O�ߝ�]*
?���e?0��c�U�
���)�q\ar�O�>[�N3 /��<̼�h������鿕	����}��/�S��}��E+#�?)@b�H�Z2f�Μk��J�=��,:����l�wzz���:Qy��=�_�:q�J?-	�N�Y���}�Jڷ�s�z�Z�c_�-w�U�5�$�����ʕ/A��)���^h<�b��A<gx�SG�`��%��A�XS,8_��{�Y�s׏u��r�8e|u������N
Ć��pz"��c���u�B8Hy����f���G�o���S�5�!?�B����bۀ����̡��>N,}�C5�V�Aa�.u[0Ӎ	"uu���b.�Rx��whM1ȕ�u��.>��; �ϐA��T�|�,�b4�����X}-Q����q��;��IK����6��5،��-����X�������RY�K]k�AX��O�f7v#��"TCB>>u��G�>h�+�[季�3�^��3tE㭴ݩ4Lg'��A���GG��)2�D�1��������#~(�W�s3�K%w�|�q�N�?Z�f�����D.@�`�aBFN�-�@�@���(��V |��s�UY@g�Z�̠Ee�eZ�+�����LJ��Җ��v�\l;�{�U^�l���`8�񒏴��0OA��-��/ԠݞdmKo�U)��w7P�C#
5�-��`�����(��Q�{��@D������'+�����f���N�[�
=I�m���Z����x醛"���i�����S�D��x�#���~F5��}�1����΃�)�3s:�3;�u��_$0&VwO�q��*�� ,݆g���'��H�`H�G7x�|G
�+��v"C��Ӵ�#I�g��P�5Ќ1�?����A2?�{-�y�!1K��{��j��0#�Lj�R�Re�>��|�`���$�{��v���*��ʜo�,
�+,���ζ��kgM@N&ۣH(�}�F-]}6�*��g�h#S�>��աЙvV���i�%�	i�J��7S~&��TO`��w��9�A<��m��l7̓W��U��
���vƧF��(�j���A�ԑ[	/�{�:&�� Wh�~	��a��P[��A���`���#A]�B����>�:n�c��
yTq�y���\���<D�O��VcGN.���ިj{�N��'�b����J�&Q��@ou���7Q%dkm`n��d�_�QFv��Ӓ���_*Z�W�܀B�V��$�]p|(�x�����"Ӑaۜ�x�<��[1��U���k���i)�/w�p�d鰌CU�=�(��n1"a����٪��H�,60xAJ��#�#�xP>8U�(��*�l���S�g������[����tsn��x"�s�4�a��xf�fD?�
9�|E�ø�[
�;�f��JdC.����P���v���Gn�h�v��,w�	���#����Hy�W�>�1s�8�3���@,
�b�(k}�؀oJX��G���-p�D*F.]�8{��ץ�xn��V��� �qw�.\�m^$�a����f�`p+�L0:S��C��� kw8df��u�"�XR��w(��"�cB���C��:�G��Iͦ�׉����*QUL\u�y��.��ϱ���� ��BYvXHhE����y���=�k�PyIDa�D�G���N��;�x��rjr��F�$���04�"��j�ʳ>���bL��A>�%i��������o�>�,~�J��$|�Li�p��T��,�FAPs��_{'͏/w�v�� �Ө�?y����b�w�P�����H�&������4�C<��{д��-С������AnY\d�5��s�ZM��r���~WMz�]�71��C&W�.g܎{�Pt�����$�q�z/��'��JNU�I�_
��h����b�]@?����2^��,�yJ,a��u,��XI'�}���Z�q����@	�̙�sJ2��i��|�4=�� PӋ;w[A]
����!%��P����3<]�:��;Ơ��V&-O�eb0_�V)a*n��9����Qm|�M�"���S4K[Uތ���`̿�,�h�4"�B֡
�#ə�5|����� @�*ߏ�!g G�����s���{8߰�mQw�����R[���:���0b_����灐p7��D]
%��