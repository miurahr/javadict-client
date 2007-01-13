/* ==================================================================
 * This file is part of JavaDictClient - a Java client for the Dict
 * protocol (RFC2229)
 * Copyright © 2003-2007 Ramon Casha
 *
 * Licensed under the GNU LGPL v2.1. You can find the text of this
 * license at http://www.gnu.org/copyleft/lesser.html
 * ================================================================== */
package mt.rcasha.dict.client;

import java.util.Locale;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

/**
 * Class containing constants and static functions pertaining to the status code.
 * @author Ramon Casha (ramon.casha@linux.org.mt)
 */
public final class Status {
    
    /** Status 110 from server (informative) */
    public static final int INFO_DATABASES_FOUND = 110;
    /** Status 111 from server (informative) */
    public static final int INFO_STRATEGIES_FOUND = 111;
    /** Status 112 from server (informative) */
    public static final int INFO_DATABASE = 112;
    /** Status 113 from server (informative) */
    public static final int INFO_HELP = 113;
    /** Status 114 from server (informative) */
    public static final int INFO_SERVER = 114;
    /** Status 130 from server (informative) */
    public static final int INFO_CHALLENGE = 130;
    /** Status 150 from server (informative) */
    public static final int INFO_FOUND = 150;
    /** Status 151 from server (informative) */
    public static final int INFO_DEFINITION = 151;
    /** Status 152 from server (informative) */
    public static final int INFO_MATCH = 152;
    
    /** Status 210 from server (success) */
    public static final int SUCCESS_STATUS = 210;
    /** Status 220 from server (success) */
    public static final int SUCCESS_HELLO = 220;
    /** Status 221 from server (success) */
    public static final int SUCCESS_GOODBYE = 221;
    /** Status 230 from server (success) */
    public static final int SUCCESS_AUTH = 230;
    /** Status 250 from server (success) */
    public static final int SUCCESS_OK = 250;
    
    /** Status 330 from server (auth request) */
    public static final int AUTH_SEND_RESPONSE = 330;
    
    /** Status 420 from server (temporary error condition) */
    public static final int TMPERR_SERVER_UNAVAILABLE = 420;
    /** Status 421 from server (temporary error condition) */
    public static final int TMPERR_SERVER_SHUTDOWN = 421;
    
    /** Status 500 from server (error) */
    public static final int ERR_COMMAND_NOT_RECOGNISED = 500;
    /** Status 501 from server (error) */
    public static final int ERR_ILLEGAL_PARAMETERS = 501;
    /** Status 502 from server (error) */
    public static final int ERR_COMMAND_NOT_IMPLEMENTED = 502;
    /** Status 503 from server (error) */
    public static final int ERR_PARAMETER_NOT_IMPLEMENTED = 503;
    /** Status 530 from server (error) */
    public static final int ERR_ACCESS_DENIED = 530;
    /** Status 531 from server (error) */
    public static final int ERR_AUTH_FAILED = 531;
    /** Status 532 from server (error) */
    public static final int ERR_AUTH_UNKNOWN_MECHANISM = 532;
    /** Status 550 from server (error) */
    public static final int ERR_NO_SUCH_DATABASE = 550;
    /** Status 551 from server (error) */
    public static final int ERR_NO_SUCH_STRATEGY = 551;
    /** Status 552 from server (error) */
    public static final int ERR_NO_MATCH = 552;
    /** Status 554 from server (error) */
    public static final int ERR_NO_DATABASES = 554;
    /** Status 555 from server (error) */
    public static final int ERR_NO_STRATEGIES = 555;
    
    /** Does the status indicate that other {@link SingleResponse}s follow this one?.
     * Test for 100-199 since all 1xx codes are followed by a 250 at the end.
     * @param status the status code to check
     * @return true if other responses follow
     */
    public static boolean isFollowed(int status) {
        return (status >= 100 && status <= 199);
    }
    
    /** Does the status indicate an error condition?.
     * Test for 500-599 since those are the error codes.
     * @param status the status code to check
     * @return true if this is an error status
     */
    public static boolean isError(int status) {
        return (status >= 500 && status <= 599);
    }
    
    /** Does the status indicate that more lines follow the first?
     * @param status the status code to check
     * @return true if multiple lines follow the first.
     */
    public static boolean isMultiLine(int status) {
        switch(status) {
            case INFO_DATABASES_FOUND:
            case INFO_STRATEGIES_FOUND:
            case INFO_DATABASE:
            case INFO_SERVER:
            case INFO_DEFINITION:
            case INFO_MATCH:
                return true;
            default:
                return false;
        }
    }
    
    /** @return a text message for the specified status code, in the specified locale.
     * @param locale locale for which to retrieve the message
     * @param status the status code
     */
    public static String getMessage(int status, Locale locale) {
        try {
            ResourceBundle rb = ResourceBundle.getBundle("DictStatus", locale);
            return rb.getString("" + status);
        } catch (MissingResourceException e) {
            return "DICT STATUS " + status;
        }
    }
    
    /** @return a text message for the specified status code, in the default locale.
     * @param status the status code
     */
    public static String getMessage(int status) {
        return getMessage(status, Locale.getDefault());
    }
    
    /** prevent initialisation */
    private Status() {
    }
    
}
