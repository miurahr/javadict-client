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

/**
 * A 151 response from the server. Extends SingleResponse, adding the parsing of 
 * the first line into named tokens. Use {@link #getTextualInformation()} for the
 * actual text of the definition.
 *
 * @author Ramon Casha (ramon.casha@linux.org.mt)
 */
public class DefinitionResponse extends SingleResponse {
    
    /** Word retrieved from database */    
    private String word;
    /** Database code */    
    private String database;
    /** Database description */    
    private String dbDescription;
    
    /** Creates a new instance of Definition
     * @param client Instance of DictClient
     * @param line First line from server
     * @throws IOException Thrown if a network error occurs
     * @throws DictException Thrown when a dict exception occurs
     */
    public DefinitionResponse(DictClient client, String line) throws DictException, IOException {
        super(client, line);
        this.word = getParameter(0);
        this.database = getParameter(1);
        this.dbDescription = getParameter(2);
    }
    
    /** Get the database code from which this definition was retrieved.
     * @return Value of property database.
     */
    public java.lang.String getDatabase() {
        return database;
    }
    
    /** Get the actual word retrieved
     * @return Value of property word.
     */
    public java.lang.String getWord() {
        return word;
    }
    
    /** Get the description of the database.
     * @return Value of property dbDescription.
     */
    public java.lang.String getDbDescription() {
        return dbDescription;
    }
    
}
