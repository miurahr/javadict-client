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
import java.util.List;

/**
 * Represents a single response from the dict server, which consists of a
 * numeric status code, optional data following the status code, and optional
 * lines following the first.
 *
 * @see "RFC2229"
 * @author Ramon Casha (ramon.casha@linux.org.mt)
 */
public class SingleResponse {

    /** Actual characters of the status code */
    private final char[] statusChars = new char[3];
    /** Status code */
    private final int status;
    /** Reference to calling client instance */
    private final DictClient client;
    /** The first line, converted to tokens */
    private final ArrayList<String> parameters = new ArrayList<String>();
    /** The first line, following the status code */
    private final String firstLine;
    /** Lines following the first one */
    private final ArrayList<String> lines = new ArrayList<String>();

    /** Create an instance of SingleResponse (or a derived class) based on the data
     * waiting in the socket
     * @param client Instance of DictClient
     * @return the created instance
     * @throws IOException Thrown if a network error occurs
     * @throws DictException Thrown when a dict exception occurs
     */
    static SingleResponse getInstance(DictClient client) throws DictException, IOException {
        // read the first line.
        String line = client.readLine();
        int status = Integer.parseInt(line.substring(0, 3));
        SingleResponse resp;
        switch (status) {
            case Status.SUCCESS_HELLO:
                resp = new ConnectResponse(client, line);
                break;
            case Status.INFO_DEFINITION:
                resp = new DefinitionResponse(client, line);
                break;
            case Status.INFO_MATCH:
                resp = new MatchResponse(client, line);
                break;
            default:
                resp = new SingleResponse(client, line);
                break;
        }
        return resp;
    }

    /** Creates a new instance of DictResponse
     * @param client Instance of DictClient
     * @param line First line from server
     * @throws IOException Thrown if a network error occurs
     * @throws DictException Thrown when a dict exception occurs
     */
    protected SingleResponse(DictClient client, String line) throws DictException, IOException {
        this.client = client;
        // get the status code
        line.getChars(0, 3, statusChars, 0);
        status = Integer.parseInt(line.substring(0, 3));
        // put the rest in firstLine and in parameters
        firstLine = line.substring(3);
        ResponseStringIterator it = new ResponseStringIterator(firstLine);
        while (it.hasNext()) {
            parameters.add(it.nextString());
        }
        // if the status code indicates this this status has multiple lines,
        // read 'em all.
        if (isMultiLine()) {
            readMoreLines();
        }
    }

    /**
     * Reads multiple lines until we meet a single '.' on a line by itself.
     * @throws IOException Thrown if a network error occurs
     */
    private void readMoreLines() throws IOException {
        while (true) {
            String line = client.readLine();
            if (line.startsWith("..")) {
                lines.add(line.substring(1));
            } else if (line.equals(".")) {
                break;
            } else {
                lines.add(line);
            }
        }
    }

    /**
     * Get the textual information (the lines following the first) as a single
     * string with newline characters between the lines returned.
     * @return a multi-line string
     */
    public String getTextualInformation() {
        StringBuffer sb = new StringBuffer();
        String sep = "";
        for (String line : lines) {
            sb.append(sep);
            sb.append(line);
            sep = "\n";
        }
        return sb.toString();
    }

    /** Convert to a string (mainly for debugging purposes)  
     * @return a string representation 
     */
    @Override
    public String toString() {
        StringBuffer sb = new StringBuffer();
        sb.append(statusChars);
        sb.append(" ");
        sb.append(firstLine);
        for (String line : lines) {
            sb.append("\n");
            sb.append(line);
        }
        return sb.toString();
    }

    /** Get the status as an array of exactly 3 characters (each char is '0'-'9')
     * @see Status
     * @return the status characters
     */
    public char[] getStatusChars() {
        return statusChars;
    }

    /** Returns the status as an int
     * @see Status
     * @return the status number
     */
    public int getStatus() {
        return status;
    }

    /** @return a List of the tokens in the first line */
    public List<String> getParameters() {
        return parameters;
    }

    /** @return the specified parameter value 
     * @param index the parameter number
     */
    public String getParameter(int index) {
        return parameters.get(index);
    }

    /** @return the part of the first line following the status code. */
    public String getFirstLine() {
        return firstLine;
    }

    /** @return the list of lines following the first, if any. */
    public List<String> getLines() {
        return lines;
    }

    /** @return Does the status indicate an error condition?
     * @see Status#isError(int)
     */
    public boolean isError() {
        return Status.isError(status);
    }

    /** @return Does the status indicate that other {@link SingleResponse}s follow this one?
     * @see Status#isFollowed(int)
     */
    public boolean isFollowed() {
        return Status.isFollowed(status);
    }

    /** @return Does the status indicate that more lines follow the first?
     * @see Status#isMultiLine(int)
     */
    public boolean isMultiLine() {
        return Status.isMultiLine(status);
    }
}
