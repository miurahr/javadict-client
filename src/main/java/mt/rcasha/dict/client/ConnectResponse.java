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
import java.util.StringTokenizer;

/**
 * A 220 response from the server. Extends SingleResponse, adding the parsing of 
 * capabilities and msgid from the first line.
 * @author Ramon Casha (ramon.casha@linux.org.mt)
 */
public class ConnectResponse extends SingleResponse {
    
    /** List of capability strings */    
    private ArrayList capabilities = new ArrayList();
    /** Server-generated msgid */    
    private String msgId;
    
    /** Creates a new instance of ConnectResponse
     * @param client Instance of DictClient
     * @param line First line from server
     * @throws IOException Thrown if a network error occurs
     * @throws DictException Thrown when a dict exception occurs
     */
    public ConnectResponse(DictClient client, String line) throws DictException, IOException {
        super(client, line);
        int nParms = super.getParameters().size();
        parseCapabilities(super.getParameter(nParms - 2));
        msgId = super.getParameter(nParms - 1);
    }
    
    /** Parse the capabilities parameter
     * @param s Capabilities token
     * @throws DictException Thrown if the capabilities token is the wrong format.
     */    
    private void parseCapabilities(String s) throws DictException {
        if (!s.startsWith("<") || !s.endsWith(">")) {
            throw new DictException("Invalid server connect string");
        }
        StringTokenizer st = new StringTokenizer(s.substring(1, s.length() - 1), ".");
        while (st.hasMoreTokens()) {
            capabilities.add(st.nextToken());
        }
    }
    
    /** Whether the specified capability is present
     * @param cap capability to test for.
     * @return true if the capability is present.
     */
    public boolean hasCapability(String cap) {
        return capabilities.contains(cap);
    }
    
    /** List of capability strings
     * @return Value of property capabilities.
     */
    public ArrayList getCapabilities() {
        return capabilities;
    }
    
    /** Get the server's initial msgid. Used for authentication.
     * @return Value of property msgId.
     */
    public java.lang.String getMsgId() {
        return msgId;
    }
    
    
}
