/* ==================================================================
 * This file is part of JavaDictClient - a Java client for the Dict 
 * protocol (RFC2229)
 * Copyright © 2003-2007 Ramon Casha
 *
 * Licensed under the GNU LGPL v2.1. You can find the text of this
 * license at http://www.gnu.org/copyleft/lesser.html
 * ================================================================== */
package tokyo.northside.dict.exceptions;

import tokyo.northside.dict.client.Status;

import java.text.MessageFormat;

/**
 * Exception thrown when the status code indicates an error.
 * @author Ramon Casha (ramon.casha@linux.org.mt)
 */
public class StatusException extends DictException {

    /** Status code */
    private final int status;

    /** Creates a new instance of StatusException 
     * @param status status code
     * @param text additional text for message
     */
    public StatusException(int status, String text) {
        super(MessageFormat.format("{0}: {1}", Status.getMessage(status), text));

        this.status = status;
    }

    /** The status code
     * @return The status code
     */
    public int getStatus() {
        return status;
    }

}
