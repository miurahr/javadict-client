/*
 * TestURL.java
 *
 * Created on December 26, 2003, 7:22 AM
 */

package mt.rcasha.dict.client;

import java.net.*;
import java.io.*;

/**
 *
 * @author  rac
 */
public class TestURL {
    
    private final static String KEY = "java.protocol.handler.pkgs";

    private static void test(String url) throws IOException {
        System.out.println("*** "+url);
        URL u = new URL(url);
        BufferedReader is = new BufferedReader(new InputStreamReader((InputStream)u.getContent()));
        String line;
        while( (line=is.readLine()) != null) {
            System.out.println(line);
        }
    }
    
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws Exception {
        String pkgs = System.getProperty(KEY,"");
        if(pkgs.length()>0) {
            pkgs += "|";
        }
        pkgs += "mt.rcasha";
        System.setProperty(KEY, pkgs);
        Authenticator.setDefault(new A());
        test("dict://test;AUTH@localhost:2628/d:hello:eng-lat");
        test("dict://localhost:2628/m:hello:*");
    }
 
}

    class A extends java.net.Authenticator {
        
        protected PasswordAuthentication getPasswordAuthentication() {
            char[] pwd = { 't', 'e', 's', 't' };
            return new PasswordAuthentication("test", pwd);
        }
        
    }
