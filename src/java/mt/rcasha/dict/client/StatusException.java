/* ==================================================================
 * This file is part of JavaDictClient - a Java client for the Dict 
 * protocol (RFC2229)
 * Copyright © 2003 Ramon Casha
 *
 * Licensed under the GNU LGPL v2.1. You can find the text of this
 * license at http://www.gnu.org/copyleft/lesser.html
 * ================================================================== */
package mt.rcasha.dict.client;

import java.text.MessageFormat;
import java.util.ResourceBundle;

/**
 * Exception thrown when the status code indicates an error.
 * @author Ramon Casha (ramon.casha@linux.org.mt)
 */
public class StatusException extends DictException {
    
    /** Status code */
    private int status;
    
    /** Creates a new instance of StatusException 
     * @param status status code
     * @param text additional text for messsage
     */
    public StatusException(int status, String text) {
        super(MessageFormat.format( 
            "{0}: {1}", //ResourceBundle.getBundle("org/dict/client/DictMessages").getString("StatusMessageFormat")
            new Object[] {
                Status.getMessage(status), 
                text
            }
        ));
        
        this.status = status;
    }
    
   
    /** The status code
     * @return The status code
     */
    public int getStatus() {
        return status;
    }

}
