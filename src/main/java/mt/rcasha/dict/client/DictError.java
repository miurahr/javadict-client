/* ==================================================================
 * This file is part of JavaDictClient - a Java client for the Dict 
 * protocol (RFC2229)
 * Copyright © 2003 Ramon Casha
 *
 * Licensed under the GNU LGPL v2.1. You can find the text of this
 * license at http://www.gnu.org/copyleft/lesser.html
 * ================================================================== */
package mt.rcasha.dict.client;

/**
 * Runtime error for the dict client. 
 * @author  rac
 */
public class DictError extends java.lang.Error {
    
    /** Creates a new instance of <code>DictError</code> without detail message. */
    public DictError() {
    }
    
    /** Creates a new instance of <code>DictError</code> with detail message. 
     * @param message the detail message.
     */
    public DictError(String message) {
        super(message);
    }
    
    /** Creates a new instance of <code>DictError</code> with causing exception. 
     * @param cause the throwable that caused this exception
     */
    public DictError(Throwable cause) {
        super(cause);
    }
    
    /** Creates a new instance of <code>DictError</code> with causing exception and detail message. 
     * @param message the detail message.
     * @param cause the throwable that caused this exception
     */
    public DictError(String message, Throwable cause) {
        super(message, cause);
    }
}
