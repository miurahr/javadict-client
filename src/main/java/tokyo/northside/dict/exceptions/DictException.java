/* ==================================================================
 * This file is part of JavaDictClient - a Java client for the Dict 
 * protocol (RFC2229)
 * Copyright © 2003-2007 Ramon Casha
 *
 * Licensed under the GNU LGPL v2.1. You can find the text of this
 * license at http://www.gnu.org/copyleft/lesser.html
 * ================================================================== */
package tokyo.northside.dict.exceptions;

/**
 * General-purpose "Dict" exception class.
 * @author Ramon Casha (ramon.casha@linux.org.mt)
 */
public class DictException extends Exception {

    /**
     * Creates a new instance of <code>DictException</code> without detail message.
     */
    public DictException() {
    }

    /**
     * Constructs an instance of <code>DictException</code> with the specified detail message.
     * @param msg the detail message.
     */
    public DictException(String msg) {
        super(msg);
    }

    /**
     * Creates a new instance of <code>DictException</code> without detail message.
     * @param cause the throwable that caused this exception
     */
    public DictException(Throwable cause) {
        super(cause);
    }

    /**
     * Constructs an instance of <code>DictException</code> with the specified detail message.
     * @param cause the throwable that caused this exception
     * @param msg the detail message.
     */
    public DictException(String msg, Throwable cause) {
        super(msg, cause);
    }
}
