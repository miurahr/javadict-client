/* ==================================================================
 * This file is part of JavaDictClient - a Java client for the Dict 
 * protocol (RFC2229)
 * Copyright © 2003 Ramon Casha
 *
 * Licensed under the GNU LGPL v2.1. You can find the text of this
 * license at http://www.gnu.org/copyleft/lesser.html
 * ================================================================== */
package mt.rcasha.dict.client.sample;

import java.util.List;
import javax.swing.AbstractListModel;

/**
 * ListModel for java.util.Lists.
 * @author Ramon Casha (ramon.casha@linux.org.mt)
 */
public class ListListModel extends AbstractListModel {

    private List list;
    
    public ListListModel(List list) {
        this.list = list;
    }
    
    public Object getElementAt(int index) {
        return list.get(index);
    }    
    
    public int getSize() {
        return list.size();
    }
    
}
