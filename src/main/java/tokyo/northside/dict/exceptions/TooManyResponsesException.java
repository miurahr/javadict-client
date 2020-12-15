/* ==================================================================
 * This file is part of JavaDictClient - a Java client for the Dict 
 * protocol (RFC2229)
 * Copyright © 2003-2007 Ramon Casha
 *
 * Licensed under the GNU LGPL v2.1. You can find the text of this
 * license at http://www.gnu.org/copyleft/lesser.html
 * ================================================================== */
package tokyo.northside.dict.exceptions;

import tokyo.northside.dict.client.Response;

/**
 * Thrown when the {@link Response#getResponse(int)} function finds more than one
 * response of the specified type.
 *
 * @author Ramon Casha (ramon.casha@linux.org.mt)
 */
public class TooManyResponsesException extends DictException {

    /** Creates a new instance of TooManyResponsesException */
    public TooManyResponsesException() {
        super(java.util.ResourceBundle.getBundle("tokyo/northside/dict/Messages").getString(
                "TooManyResponses"));
    }

}
