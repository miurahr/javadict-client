/* ==================================================================
 * This file is part of JavaDictClient - a Java client for the Dict 
 * protocol (RFC2229)
 * Copyright © 2003-2007 Ramon Casha
 *
 * Licensed under the GNU LGPL v2.1. You can find the text of this
 * license at http://www.gnu.org/copyleft/lesser.html
 * ================================================================== */
package tokyo.northside.dict.client;

import java.util.ArrayList;
import java.util.Iterator;

/**
 * Special iterator which parses a line returned from the dict server and
 * splits it into tokens. This class takes care of quoting etc.
 *
 * @author Ramon Casha (ramon.casha@linux.org.mt)
 */
public class ResponseStringIterator implements Iterator<String> {

    /** Actual iterator */
    private final Iterator<String> it;

    /** @return whether there are more tokens */
    public boolean hasNext() {
        return it.hasNext();
    }
    
    /** @return the next token */
    public String next() {
        return it.next();
    }
    
    /** @return the next token */
    public String nextString() {
        return it.next();
    }
    
    /** @return the next token converted to an int */
    public int nextInt() {
        return Integer.parseInt(nextString());
    }
    
    /** Removes the current token. */
    public void remove() {
        it.remove();
    }
    
    /** Creates a new instance of ResponseIterator 
     *@param line String to parse
     */
    public ResponseStringIterator(String line) {
        ArrayList<String> al = new ArrayList<>();
        boolean inQuotes = false;
        StringBuilder cword = new StringBuilder();
        for (int i = 0; i < line.length(); i++) {
            char c = line.charAt(i);
            if (c == '"') {
                inQuotes = !inQuotes;
                continue;
            }
            if (Character.isWhitespace(c) && !inQuotes) {
                if (cword.length() > 0) {
                    al.add(cword.toString());
                    cword.setLength(0);
                }
                continue;
            }
            cword.append(c);
        }
        // add the last word
        if (cword.length() > 0) {
            al.add(cword.toString());
        }
        it = al.iterator();
    }
    
}
