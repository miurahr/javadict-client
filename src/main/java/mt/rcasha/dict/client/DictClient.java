/* ==================================================================
 * This file is part of JavaDictClient - a Java client for the Dict 
 * protocol (RFC2229)
 * Copyright © 2003 Ramon Casha
 *
 * Licensed under the GNU LGPL v2.1. You can find the text of this
 * license at http://www.gnu.org/copyleft/lesser.html
 * ================================================================== */
package mt.rcasha.dict.client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;

import java.net.Socket;

import java.security.MessageDigest;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;


/** Dict client class. The constructors will connect to the server, then you can use
 * the various functions to retrieve a list of databases, strategies etc., and to
 * retrieve definitions. Call {@link #close()} when finished.
 * <h2>Dict commands implemented</h2>
 * <ul>
 * <li>DEFINE (see {@link #getDefinitions(String,String)})</li>
 * <li>MATCH (see {@link #getMatches(String,String,String)})</li>
 * <li>CLIENT (implicit, see constructors)</li>
 * <li>SHOW DB (see {@link #getDatabases()})</li>
 * <li>SHOW STRAT (see {@link #getStrategies()})</li>
 * <li>SHOW SERVER (see {@link #getServerInfo()})</li>
 * <li>SHOW INFO (see {@link #getDatabaseInfo(String)})</li>
 * <li>QUIT (see {@link #close()})</li>
 * <li>STATUS (see {@link #getStatus()})</li>
 * <li>HELP (see {@link #getHelp()})</li>
 * <li>AUTH (see {@link #auth(String,String)}</li>
 * </ul>
 * <h2>Dict commands NOT implemented yet</h2>
 * <ul>
 * <li>OPTION MIME</li>
 * <li>SASLAUTH</li>
 * </ul>
 * @author Ramon Casha (ramon.casha@linux.org.mt)
 */
public class DictClient {
    
    /** Log class */    
    private static final Log log = LogFactory.getLog(DictClient.class);

    /** The string by which this client identifies itself */
    public static final String USER_AGENT = "CLIENT JDict 1.0";
    
    /** Return a definition from the first database containing the word */
    public static final String DATABASE_FIRST = "!";
    /** Return all definitions from all databases */
    public static final String DATABASE_ALL = "*";
    
    /** Default strategy as defined by the server */
    public static final String STRATEGY_DEFAULT = ".";
    /** Match only the first part of the word */
    public static final String STRATEGY_PREFIX = "prefix";
    /** Match the exact word */
    public static final String STRATEGY_EXACT = "exact";
    
    /** Default dict port as per RFC2229 */    
    public static final int DEFAULT_PORT = 2628;
    
    /** Host to connect to */
    private String host = "localhost";
    /** Port number to connect to */
    private int port    = DEFAULT_PORT;
    /** Printwriter to send commands to dict server. Handles conversion to UTF-8. */
    private PrintWriter out;
    /** BufferedReader to read from the dict server. Handles conversion from UTF-8. */
    private BufferedReader in;
    /** Connected socket. */
    private Socket socket;
    /** Whether to throw a StatusException if an error-response is received. If false, will just
     * return the Response containing the error.
     */
    private boolean throwExceptions = true;
    /** Contains the server info provided at connect time */
    private ConnectResponse serverCaps;
    
    /** Connect to the dict server on localhost and the default port.
     * @throws IOException Thrown if a network error occurs
     * @throws DictException Thrown when a dict exception occurs
     */
    public DictClient() throws IOException, DictException {
        init();
    }
    
    /** Connect to the dict server on the specified host and default port.
     * @param host Host name or IP address to connect to
     * @throws IOException Thrown if a network error occurs
     * @throws DictException Thrown when a dict exception occurs
     */
    public DictClient(String host) throws IOException, DictException {
        this.host = host;
        init();
    }
    
    /** Connect to the dict server on the specified host and port.
     * @param host Host name or IP address to connect to
     * @param port Port number to connect to
     * @throws IOException Thrown if a network error occurs
     * @throws DictException Thrown when a dict exception occurs
     */
    public DictClient(String host, int port) throws IOException, DictException {
        this.host = host;
        this.port = port;
        init();
    }
    
    
    /** Sends a command and returns its complete response.
     * @param command Command line to send to the server
     * @throws IOException Thrown if a network error occurs
     * @throws DictException Thrown when a dict exception occurs
     * @return a constructed response object
     */
    protected Response send(String command) throws IOException, DictException {
        sendLine(command);
        return new Response(this);
    }
    
    /** Open the socket and prepare the reader and writer. 
     * @throws IOException Thrown if a network error occurs
     * @throws DictException Thrown when a dict exception occurs
     */
    private void init() throws IOException, DictException {
        socket = new Socket(host, port);
        
        out = new PrintWriter(new OutputStreamWriter(socket.getOutputStream(), "UTF-8"), true);
        in = new BufferedReader(new InputStreamReader(socket.getInputStream(), "UTF-8"));
        
        Response tmp = new Response(this);
        serverCaps = (ConnectResponse) tmp.getResponse(Status.SUCCESS_HELLO);
        send(USER_AGENT);
    }
    
    /** Read a single line from the dict server. The newline is stripped. 
     * @throws IOException Thrown if a network error occurs
     * @throws DictError Thrown when a dict error occurs
     * @return a line from the server.
     */
    String readLine() throws DictError, IOException {
        check();
        String line = in.readLine();
        log.debug("RCVD:" + line);
        return line;
    }
    
    /** Write a single line to the dict server, terminated by a newline.
     * @param line Line to send.
     * @throws DictError Thrown when a dict exception occurs
     */
    void sendLine(String line) throws DictError {
        check();
        log.debug("SENT:" + line);
        out.println(line);
    }
    
    /** Quit and close the socket if still open. */
    public void finalize() {
        if (socket != null) {
            try {
                close();
            } catch (Exception ignored) {
            }
        }
    }
    
    /** Checks that the socket is open, etc. Throws up if not. 
     * @throws DictError Thrown when a dict exception occurs
     */
    private void check() throws DictError {
        if (socket == null) {
            throw new DictError(ResourceBundle.getBundle("org/dict/client/DictMessages").getString("ConnectionClosed"));
        }
    }
    
    /** Close the connection to the dict server. Do not use any other functions after
     * calling close.
     * @throws IOException Thrown if a network error occurs
     * @throws DictException Thrown when a dict exception occurs
     */
    public void close() throws DictException, IOException {
        check();
        Response res = send("QUIT");
        socket.close();
        socket = null;
    }
    
    /** Get a map of all dict databases on the server. The key is the database name, value
     * is a short description.
     * @throws IOException Thrown if a network error occurs
     * @throws DictException Thrown when a dict exception occurs
     * @return a map of database codes and descriptions
     */
    public Map getDatabases() throws DictException, IOException {
        Response res = send("SHOW DATABASES");
        HashMap map = new HashMap();
        SingleResponse sr = res.getResponse(Status.INFO_DATABASES_FOUND);
        Iterator it = sr.getLines().iterator();
        while (it.hasNext()) {
            ResponseStringIterator rit = new ResponseStringIterator((String) it.next());
            map.put(rit.nextString(), rit.nextString());
        }
        return map;
    }

    /** Authenticate the user. Some servers require authentication, some allow access to
     * more databases if authenticated, while others just don't care who you are.
     * @param user User name
     * @param sharedSecret Password, aka "shared secret".
     * @throws IOException Thrown if a network error occurs
     * @throws DictException Thrown when a dict exception occurs
     * @return The response returned from the AUTH command
     */
    public Response auth(String user, String sharedSecret) throws IOException, DictException {
        return send("AUTH " + user + " " + hash(serverCaps.getMsgId() + sharedSecret));
    }

    /** Return the MD5 hash as a hex string
     * @param s String to calculate hash of
     * @return The MD5 sum as a hex string
     * @throws DictException Thrown when a dict exception occurs
     */
    private String hash(String s) throws DictException {
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            md.update(s.getBytes("UTF-8"));
            byte[] checksum = md.digest();
            StringBuffer sb = new StringBuffer();
            for (int i = 0; i < checksum.length; i++) {
                String hs =  Integer.toHexString((char) (checksum[i] & 0xFF)); 
                if (hs.length() < 2) {
                    sb.append('0');
                }
                sb.append(hs);
            }
            return sb.toString();
        } catch (Exception e) {
            throw new DictException(e);
        }
    }
    
    /** Get a map of all strategies supported by the server. The key is the strategy name, value
     * is a short description.
     * @throws IOException Thrown if a network error occurs
     * @throws DictException Thrown when a dict exception occurs
     * @return a map of strategy codes and descriptions
     */
    public Map getStrategies() throws DictException, IOException {
        Response res = send("SHOW STRATEGIES");
        HashMap map = new HashMap();
        SingleResponse sr = res.getResponse(Status.INFO_STRATEGIES_FOUND);
        Iterator it = sr.getLines().iterator();
        while (it.hasNext()) {
            ResponseStringIterator rit = new ResponseStringIterator((String) it.next());
            map.put(rit.nextString(), rit.nextString());
        }
        return map;
    }
    
    /** Gets information about the dict server software as freeform text in a multiline string. 
     * @throws IOException Thrown if a network error occurs
     * @throws DictException Thrown when a dict exception occurs
     * @return a multi-line string
     */
    public String getServerInfo() throws DictException, IOException {
        Response res = send("SHOW SERVER");
        SingleResponse sr = res.getResponse(Status.INFO_SERVER);
        return sr.getTextualInformation();
    }
    
    /** Gets information about the dict server status as a single-line string. 
     * @throws IOException Thrown if a network error occurs
     * @throws DictException Thrown when a dict exception occurs
     * @return a multi-line string
     */
    public String getStatus() throws DictException, IOException {
        Response res = send("STATUS");
        SingleResponse sr = res.getResponse(Status.SUCCESS_STATUS);
        return sr.getFirstLine();
    }
    
    /** Gets user-help from the dict server software as freeform text in a multiline string. 
     * @throws IOException Thrown if a network error occurs
     * @throws DictException Thrown when a dict exception occurs
     * @return a multi-line string
     */
    public String getHelp() throws DictException, IOException {
        Response res = send("HELP");
        SingleResponse sr = res.getResponse(Status.INFO_HELP);
        return sr.getTextualInformation();
    }
    
    /** Gets information about the specified database as freeform text in a multiline string.
     * @param db Database code.
     * @throws IOException Thrown if a network error occurs
     * @throws DictException Thrown when a dict exception occurs
     * @return a multi-line string
     */
    public String getDatabaseInfo(String db) throws IOException, DictException {
        Response res = send("SHOW INFO " + db);
        SingleResponse sr = res.getResponse(Status.INFO_DATABASE);
        return sr.getTextualInformation();
    }
    
    /** Returns the definitions of a word from the specified database.
     * The database should be a database code, {@link #DATABASE_FIRST} or
     * {@link #DATABASE_ALL}
     * @param database Database to search from.
     * @param word Word to search for.
     * @return A List of {@link DefinitionResponse}s.
     * @throws IOException Thrown if a network error occurs
     * @throws DictException Thrown when a dict exception occurs
     */
    public List getDefinitions(String database, String word) throws IOException, DictException {
        Response res = send("DEFINE \"" + database + "\" \"" + word + "\"");
        return res.getResponses(Status.INFO_DEFINITION);
    }
    
    /** Finds the matches for the specified word using the specified strategy and
     * database.
     * The database should be a database code, {@link #DATABASE_FIRST} or
     * {@link #DATABASE_ALL}
     * The strategy should be a strategy code (including {@link #STRATEGY_EXACT} or {@link
     * #STRATEGY_PREFIX}) or {@link #STRATEGY_DEFAULT}.
     * .
     * @param strategy Strategy to match the word.
     * @param database Database to search from.
     * @param word Word or expression to search for. The actual syntax depends on
     * the strategy (eg, regular expressions, globs etc).
     * @return A Map of matches. The map-key is the database-code in which the words were found. The
     * map-value is a List, each element of which is a String containing a word or
     * phrase from that database. The database-code and word can thus be used in a
     * {@link #getDefinitions(String,String)} call.
     * @throws IOException Thrown if a network error occurs
     * @throws DictException Thrown when a dict exception occurs
     */
    public Map getMatches(String database, String strategy, String word) throws IOException, DictException {
        Response res = send("MATCH \"" + database + "\" \"" + strategy + "\" \"" + word + "\"");
        MatchResponse sr = (MatchResponse) res.getResponse(Status.INFO_MATCH);
        return sr.getDbResults();
    }
    
    /** What to do in the event of a command returning an error status. If true, the
     * command throws a StatusException. If false, it returns the Response object
     * containing the error status in one of its SingleResponses.
     * @return Value of property throwExceptions.
     */
    public boolean getThrowExceptions() {
        return throwExceptions;
    }
    
    /** What to do in the event of a command returning an error status. If true, the
     * command throws a StatusException. If false, it returns the Response object
     * containing the error status in one of its SingleResponses.
     * @param throwExceptions New value of property throwExceptions.
     */
    public void setThrowExceptions(boolean throwExceptions) {
        this.throwExceptions = throwExceptions;
    }
    
}
