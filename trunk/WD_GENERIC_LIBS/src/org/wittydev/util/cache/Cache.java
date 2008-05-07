package org.wittydev.util.cache;
import java.util.Enumeration;
/**
 * Title:
 * Description:
 * Copyright:    Copyright (c) 2002
 * Company:       CK
 * @author
 * @version 1.0
 */

public interface Cache {
    public void clear();
    public Object get(Object key);
    public boolean isEmpty ();
    public Enumeration keys();
    public Object put(Object key, Object value);
    public Object remove ( Object key );
    public int size();

}