/* ==================================================================
 * This file is part of JavaDictClient - a Java client for the Dict 
 * protocol (RFC2229)
 * Copyright © 2003 Ramon Casha
 *
 * Licensed under the GNU LGPL v2.1. You can find the text of this
 * license at http://www.gnu.org/copyleft/lesser.html
 * ================================================================== */
package mt.rcasha.dict.client;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

/**
 * A 152 response from the server. Extends SingleResponse, adding the parsing of 
 * the returned lines into a Map of Lists. The map key is a database name, the 
 * map value is a List, each element of which is a String.
 *
 * @author Ramon Casha (ramon.casha@linux.org.mt)
 */
public class MatchResponse extends SingleResponse {
    
    /** Map of Lists. */
    private HashMap dbResults = new HashMap();
    
    /** Creates a new instance of MatchResponse 
     * @param client Instance of DictClient
     * @param line First line from server
     * @throws IOException Thrown if a network error occurs
     * @throws DictException Thrown when a dict exception occurs
     */
    public MatchResponse(DictClient client, String line) throws DictException, IOException {
        super(client, line);
        Iterator lit = getLines().iterator();
        while (lit.hasNext()) {
            String l = (String) lit.next();
            ResponseStringIterator rsi = new ResponseStringIterator(l);
            String db = rsi.nextString();
            String matchword = rsi.nextString();
            ArrayList list = (ArrayList) dbResults.get(db);
            if (list == null) {
                list = new ArrayList();
                dbResults.put(db, list);
            }
            list.add(matchword);
        }
    }
    
    /** Get the resulting Map of Lists.
     * @return Value of property dbResults.
     */
    public HashMap getDbResults() {
        return dbResults;
    }
    
}
