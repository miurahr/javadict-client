/* ==================================================================
 * This file is part of JavaDictClient - a Java client for the Dict
 * protocol (RFC2229)
 * Copyright © 2003-2007 Ramon Casha
 *
 * Licensed under the GNU LGPL v2.1. You can find the text of this
 * license at http://www.gnu.org/copyleft/lesser.html
 * ================================================================== */
package mt.rcasha.dict.client;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.Authenticator;
import java.net.PasswordAuthentication;
import java.net.URL;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;

/** Provides a simple method to get text from a dictionary via the standard URL
 * class and related classes.
 * <p>
 * You must first inform the JVM about this class, by setting or adding the string
 * "mt.rcasha" to the system property "java.protocol.handler.pkgs". See the
 * documentation for {@link java.net.URLStreamHandler} for more information.
 * <p>
 * In order to use authentication, you must create and register your instance
 * of java.net.Authenticator, to prompt the user for the password.
 *
 * <pre>dict://[user;auth-type@]host[:port]/d:word[:database[:n]]
 * dict://[user;auth-type@]host[:port]/m:word[:database[:strat[:n]]]</pre>
 * @author Ramon Casha (ramon.casha@linux.org.mt)
 */
public class DictURLConnection extends java.net.URLConnection {

    /** Host name from URL */
    private String host = null;
    /** Port number from URL */
    private int port = 2628;
    /** Username from URL */
    private String user = null;
    /** Authentication type from URL. Currently only AUTH is supported. */
    private String authType = null;
    /** Method  from URL - one of "d" or "m". */
    private String method = null;
    /** Database name from URL */
    private String database = DictClient.DATABASE_FIRST;
    /** Strategy name from URL */
    private String strategy = DictClient.STRATEGY_DEFAULT;
    /** Word to search for, from URL */
    private String word = null;
    /** Definition number from URL (unused). */
    private int defNum = 0;

    /** Creates a new instance of DictURLConnection
     * @param url dict:// format URL
     */
    public DictURLConnection(URL url) {
        super(url);
        setHost(url.getHost());
        setPort(url.getPort());
        if (port < 0) {
            setPort(url.getDefaultPort());
        }
        if (url.getPath() != null) {
            int idx = 0;
            String tmp = url.getPath() + "::::z";
            String[] tokens = tmp.split(":");
            setMethod(tokens[idx++]);
            setWord(tokens[idx++]);
            setDatabase(tokens[idx++]);
            if (method.equals("m")) {
                setStrategy(tokens[idx++]);
            }
            setDefNum(tokens[idx++]);
        }
        if (url.getUserInfo() != null) {
            StringTokenizer st = new StringTokenizer(url.getUserInfo(), ";");
            user = st.nextToken();
            authType = st.nextToken();
        }
        //dump();
    }

    /** Dump the settings to stderr */
    @SuppressWarnings("unused")
    private void dump() {
        System.err.println("H:" + host);
        System.err.println("P:" + port);
        System.err.println("U:" + user);
        System.err.println("A:" + authType);
        System.err.println("M:" + method);
        System.err.println("D:" + database);
        System.err.println("S:" + strategy);
        System.err.println("W:" + word);
        System.err.println("N:" + defNum);
    }

    @Override
    public void connect() throws IOException {
    }

    /** Container for the retrieved definition. */
    byte[] body = null;

    /** Return the retrieved body as an array of bytes (retrieving if necessary). */
    private byte[] getBody() throws IOException {
        if (body == null) {
            try {
                DictClient client = new DictClient(getHost(), getPort());
                if (authType != null) {
                    PasswordAuthentication pa = Authenticator.requestPasswordAuthentication(host,
                            null, port, "dict", "Enter shared secret", "scheme");
                    if (pa == null) {
                        throw new IOException("No password to authenticate with");
                    }
                    String password = new String(pa.getPassword());
                    client.auth(user, password);

                }
                if (method.equals("d")) {
                    StringBuffer tmp = new StringBuffer();
                    for (DefinitionResponse dr : client.getDefinitions(database, word)) {
                        tmp.append("From " + dr.getDbDescription() + " [" + dr.getDatabase()
                                + "]:\n\n");
                        tmp.append(dr.getTextualInformation());
                        tmp.append("\n\n");
                    }
                    body = tmp.toString().getBytes("UTF-8");
                }
                if (method.equals("m")) {
                    StringBuffer tmp = new StringBuffer();
                    for (Map.Entry<String, List<String>> entry : client.getMatches(database,
                            strategy, word).entrySet()) {
                        tmp.append("From " + entry.getKey() + ":\n");
                        Iterator<String> wit = entry.getValue().iterator();
                        while (wit.hasNext()) {
                            tmp.append(wit.next());
                            if (wit.hasNext()) {
                                tmp.append(", ");
                            }
                        }
                        tmp.append("\n\n");
                    }
                    body = tmp.toString().getBytes("UTF-8");
                }
            } catch (DictException e) {
                throw new IOException(e.toString());
            }
        }
        return body;
    }

    /** Return an input stream for the body (as UTF-8 bytes) */
    @Override
    public InputStream getInputStream() throws IOException {
        return new ByteArrayInputStream(getBody());
    }

    /** return always true */
    @Override
    public boolean getDoInput() {
        return true;
    }

    /**
     * @return "text/plain"
     */
    @Override
    public String getContentType() {
        return "text/plain";
    }

    /**
     * @return always false
     */
    @Override
    public boolean getDoOutput() {
        return false;
    }

    /**
     * @return "UTF-8"
     */
    @Override
    public String getContentEncoding() {
        return "UTF-8";
    }

    /**
     * @return the length of the body in bytes
     */
    @Override
    public int getContentLength() {
        try {
            return getBody().length;
        } catch (IOException e) {
            return 0;
        }
    }

    /** Getter for property method.
     * @return Value of property method.
     *
     */
    public java.lang.String getMethod() {
        return method;
    }

    /** Setter for property method.
     * @param method New value of property method.
     *
     */
    private void setMethod(java.lang.String method) {
        if (!method.startsWith("/"))
            throw new RuntimeException("Invalid method");
        this.method = method.substring(1).toLowerCase();
    }

    /** Getter for property host.
     * @return Value of property host.
     *
     */
    public java.lang.String getHost() {
        return host;
    }

    /** Setter for property host.
     * @param host New value of property host.
     *
     */
    private void setHost(java.lang.String host) {
        this.host = host;
    }

    /** Getter for property port.
     * @return Value of property port.
     *
     */
    public int getPort() {
        return port;
    }

    /** Setter for property port.
     * @param port New value of property port.
     *
     */
    private void setPort(int port) {
        this.port = port;
    }

    /** Getter for property user.
     * @return Value of property user.
     *
     */
    public java.lang.String getUser() {
        return user;
    }

    /** Setter for property user.
     * @param user New value of property user.
     *
     */
    @SuppressWarnings("unused")
    private void setUser(java.lang.String user) {
        this.user = user;
    }

    /** Getter for property authType.
     * @return Value of property authType.
     *
     */
    public java.lang.String getAuthType() {
        return authType;
    }

    /** Setter for property authType.
     * @param authType New value of property authType.
     *
     */
    @SuppressWarnings("unused")
    private void setAuthType(java.lang.String authType) {
        this.authType = authType;
    }

    /** Getter for property database.
     * @return Value of property database.
     *
     */
    public java.lang.String getDatabase() {
        return database;
    }

    /** Setter for property database.
     * @param database New value of property database.
     *
     */
    private void setDatabase(java.lang.String database) {
        this.database = database;
    }

    /** Getter for property strategy.
     * @return Value of property strategy.
     *
     */
    public java.lang.String getStrategy() {
        return strategy;
    }

    /** Setter for property strategy.
     * @param strategy New value of property strategy.
     *
     */
    private void setStrategy(java.lang.String strategy) {
        if (strategy == null || strategy.equals("")) {
            strategy = DictClient.STRATEGY_DEFAULT;
        }
        this.strategy = strategy;
    }

    /** Getter for property word.
     * @return Value of property word.
     *
     */
    public java.lang.String getWord() {
        return word;
    }

    /** Setter for property word.
     * @param word New value of property word.
     *
     */
    private void setWord(java.lang.String word) {
        this.word = word;
    }

    /** Getter for property defNum.
     * @return Value of property defNum.
     *
     */
    public int getDefNum() {
        return defNum;
    }

    /** Setter for property defNum.
     * @param defNum New value of property defNum.
     *
     */
    private void setDefNum(String tmp) {
        if (tmp.length() > 0) {
            defNum = Integer.parseInt(tmp);
        }
    }

}
