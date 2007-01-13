/* ==================================================================
 * This file is part of JavaDictClient - a Java client for the Dict 
 * protocol (RFC2229)
 * Copyright © 2003-2007 Ramon Casha
 *
 * Licensed under the GNU LGPL v2.1. You can find the text of this
 * license at http://www.gnu.org/copyleft/lesser.html
 * ================================================================== */
package mt.rcasha.dict.client;

import java.io.IOException;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * Represents a complete response from the dict server, which may consist of
 * several {@link SingleResponse}s (status codes each with its own data).
 *
 * @author Ramon Casha (ramon.casha@linux.org.mt)
 */
public class Response {
    
    /** Log class. */
    private static final Log log = LogFactory.getLog(Response.class);

    /** List of all responses returned */
    private ArrayList<SingleResponse> responses = new ArrayList<SingleResponse>();
    
    /** Creates a new instance of Response. Reads all {@link SingleResponse}s for the last command. 
     * @param client Instance of DictClient
     * @throws IOException Thrown if a network error occurs
     * @throws DictException Thrown when a dict exception occurs
     */
    public Response(DictClient client) throws IOException, DictException {
        SingleResponse resp = SingleResponse.getInstance(client);
        responses.add(resp);
        while (resp.isFollowed()) {
            resp = SingleResponse.getInstance(client);
            responses.add(resp);
        }
        if (client.getThrowExceptions()) {
            for ( SingleResponse sr : responses ) {
                if (sr.isError()) {
                    throw new StatusException(sr.getStatus(), sr.getFirstLine());
                }
            }
        }
    }
    
    /** @return a list of all responses */
    public ArrayList<SingleResponse> getResponses() {
        return responses;
    }
    
    /** @return a list of all SingleResponses matching the desired status code. List may be
     * empty.
     * @param statusCode the status code to filter by.
     */
    public ArrayList<? extends SingleResponse> getResponses(int statusCode) {
        ArrayList<SingleResponse> nuList = new ArrayList<SingleResponse>();
        for ( SingleResponse resp : responses ) {
            if (resp.getStatus() == statusCode) {
                nuList.add(resp);
            }
        }
        return nuList;
    }
    
    /** Return a single response matching the desired status code. Fails if there are less
     * or more than exactly one such SingleResponse.
     * @param statusCode Status code to match
     * @throws NoSuchResponseException Thrown if no responses with the specified status were returned.
     * @throws TooManyResponsesException Thrown if more than exactly one response with the specified status was returned.
     * @throws DictException Sundry other exceptions
     * @return A SingleResponse object having the specified status code.
     */
    public SingleResponse getResponse(int statusCode) throws NoSuchResponseException, TooManyResponsesException, DictException {
        List<? extends SingleResponse> l = getResponses(statusCode);
        if (l.size() == 0) {
            throw new NoSuchResponseException();
        }
        if (l.size() > 1) {
            throw new TooManyResponsesException();
        }
        return l.get(0);
    }

    /** Return a string representation. Mainly for debugging. 
     * @return a string representation.
     */
    public String toString() {
        StringBuffer sb = new StringBuffer();
        Iterator<SingleResponse> it = responses.iterator();
        while (it.hasNext()) {
            sb.append(it.next());
            if (it.hasNext()) {
                sb.append("\n");
            }
        }
        return sb.toString();
    }
}
