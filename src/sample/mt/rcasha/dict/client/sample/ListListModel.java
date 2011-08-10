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
@SuppressWarnings("serial")
public class ListListModel<E> extends AbstractListModel<E> {

    private final List<E> list;

    public ListListModel(final List<E> theList) {
        list = theList;
    }

    @Override
    public E getElementAt(int index) {
        return list.get(index);
    }

    @Override
    public int getSize() {
        return list.size();
    }

}
