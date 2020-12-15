/* ==================================================================
 * This file is part of JavaDictClient - a Java client for the Dict 
 * protocol (RFC2229)
 * Copyright © 2003-2007 Ramon Casha
 *
 * Licensed under the GNU LGPL v2.1. You can find the text of this
 * license at http://www.gnu.org/copyleft/lesser.html
 * ================================================================== */
package tokyo.northside.dict;

import java.net.URL;
import java.net.URLConnection;
import java.net.URLStreamHandler;

import tokyo.northside.dict.client.DictURLConnection;

/**
 * URL Stream Handler to return DictURLConnections for dict://xxxxxx URLs.
 * @author Ramon Casha (ramon.casha@linux.org.mt)
 */
public class Handler extends URLStreamHandler {
    
    /**
     * @param u passed on to DictURLConnection
     * @return a DictURLConnection
     */
    protected URLConnection openConnection(URL u) {
        return new DictURLConnection(u);
    }
    
    /**
     * @return the default port for the Dict url.
     */
    protected int getDefaultPort() {
        return 2628;
    }
    
}
