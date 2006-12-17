/*
 * StringsService.java
 *
 * Created on 13 février 2006, 14:38
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
//import org.apache.log4j.Logger;

import java.io.FileWriter;

/**
 *
 * @author Guillaume Alleon
 */
public class StringsServiceImpl implements StringsService {
    
//  private static Logger logger=Logger.getLogger(StringsServiceImpl.class);
    
    public StringsServiceImpl() {
//      logger.debug("org.codehaus.groovy.gsoap.test.StringsServiceImpl");
    }
    
    public String concat(String s1, String s2){
        String result = new String(s1+s2);
        return  result;
    }

    public void foo(String line, String filename) {

      try {
        FileWriter fw = new FileWriter(filename);
        fw.write(line);
        fw.flush();
        fw.close(); 
      } catch (Exception ex) {
      }

    }
}
