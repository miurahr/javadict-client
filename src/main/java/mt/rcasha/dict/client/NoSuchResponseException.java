/* ==================================================================
 * This file is part of JavaDictClient - a Java client for the Dict 
 * protocol (RFC2229)
 * Copyright © 2003-2007 Ramon Casha
 *
 * Licensed under the GNU LGPL v2.1. You can find the text of this
 * license at http://www.gnu.org/copyleft/lesser.html
 * ================================================================== */
package mt.rcasha.dict.client;

/**
 * Thrown when the {@link Response#getResponse(int)} function finds no 
 * responses of the specified type.
 *
 * @author Ramon Casha (ramon.casha@linux.org.mt)
 */
public class NoSuchResponseException extends DictException {
    
    /** Creates a new instance of NoSuchResponseException */
    public NoSuchResponseException() {
        super(java.util.ResourceBundle.getBundle("org/dict/client/DictMessages").getString("NoSuchResponse"));
    }
    
}
