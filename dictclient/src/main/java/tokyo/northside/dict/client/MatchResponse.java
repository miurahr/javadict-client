/* ==================================================================
 * This file is part of JavaDictClient - a Java client for the Dict 
 * protocol (RFC2229)
 * Copyright © 2003-2007 Ramon Casha
 *
 * Licensed under the GNU LGPL v2.1. You can find the text of this
 * license at http://www.gnu.org/copyleft/lesser.html
 * ================================================================== */
package tokyo.northside.dict.client;

import tokyo.northside.dict.exceptions.DictException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

/**
 * A 152 response from the server. Extends SingleResponse, adding the parsing of 
 * the returned lines into a Map of Lists. The map key is a database name, the 
 * map value is a List, each element of which is a String.
 *
 * @author Ramon Casha (ramon.casha@linux.org.mt)
 */
public class MatchResponse extends SingleResponse {

    /** Map of Lists. */
    private final HashMap<String, List<String>> dbResults = new HashMap<>();

    /** Creates a new instance of MatchResponse 
     * @param client Instance of DictClient
     * @param line First line from server
     * @throws IOException Thrown if a network error occurs
     */
    public MatchResponse(DictClient client, String line) throws IOException {
        super(client, line);
        for (String s : getLines()) {
            ResponseStringIterator responseStringIterator = new ResponseStringIterator(s);
            String db = responseStringIterator.nextString();
            String matchWord = responseStringIterator.nextString();
            List<String> results = dbResults.computeIfAbsent(db, k -> new ArrayList<>());
            results.add(matchWord);
        }
    }

    /** Get the resulting Map of Lists.
     * @return Value of property dbResults.
     */
    public HashMap<String, List<String>> getDbResults() {
        return dbResults;
    }

}
