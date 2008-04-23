package org.wittydev.util.cache;

import java.util.Enumeration;
import java.io.IOException;
import java.util.NoSuchElementException;

import org.wittydev.logging.LoggingService;


/**
 * Title:
 * Description:   Thi class is entirly based on java.util.Hashtable
 *              implementation!
 *              List of Modifications:
 *                - All synchronization removed
 *                - Added expirationTime feature for each entry
 * Copyright:    Copyright (c) 2002
 * Company:
 * @author
 * @version 1.0
 */


public class LRUCache extends java.util.Dictionary implements Cache, Cloneable, java.io.Serializable {
    Thread cacheMonitorThread;


    /**
     * The hash table data.
     */
    private transient LRUCacheEntry table[];

    /**
     * The total number of entries in the hash table.
     */
    private transient int count;

    /**
     * Rehashes the table when count exceeds this threshold.
     */
    private int threshold;

    /**
     * The load factor for the hashtable.
     */
    private float loadFactor;

    private long expirationTime;

    /** use serialVersionUID from JDK 1.0.2 for interoperability */
///////////////    private static final long serialVersionUID = 1421746759512286392L;

    /**
     * Constructs a new, empty hashtable with the specified initial
     * capacity and the specified load factor.
     *
     * @param      initialCapacity   the initial capacity of the hashtable.
     * @param      loadFactor        a number between 0.0 and 1.0.
     * @exception  IllegalArgumentException  if the initial capacity is less
     *               than or equal to zero, or if the load factor is less than
     *               or equal to zero.
     * @since      JDK1.0
     */
    public LRUCache(int initialCapacity, float loadFactor, long expirationTime) {
	if ((initialCapacity <= 0) || (loadFactor <= 0.0)) {
	    throw new IllegalArgumentException();
	}
	this.loadFactor = loadFactor;
        this.expirationTime = expirationTime;
	table = new LRUCacheEntry[initialCapacity];
	threshold = (int)(initialCapacity * loadFactor);
    }

    /**
     * Constructs a new, empty hashtable with the specified initial capacity
     * and default load factor.
     *
     * @param   initialCapacity   the initial capacity of the hashtable.
     * @since   JDK1.0
     */
    public LRUCache(int initialCapacity, long expirationTime) {
	this(initialCapacity, 0.75f, expirationTime);
    }


    /**
     * Constructs a new, empty hashtable with a default capacity and load
     * factor.
     *
     * @since   JDK1.0
     */
    public LRUCache(long expirationTime) {
	this(101, 0.75f, expirationTime);
    }


    /**
     * Constructs a new, empty hashtable with a default capacity and load
     * factor.
     *
     * @since   JDK1.0
     */
    public LRUCache() {
	this(101, 0.75f, 60000);
    }

    /**
     * Returns the number of keys in this hashtable.
     *
     * @return  the number of keys in this hashtable.
     * @since   JDK1.0
     */
    public int size() {
	return count;
    }

    /**
     * Tests if this hashtable maps no keys to values.
     *
     * @return  <code>true</code> if this hashtable maps no keys to values;
     *          <code>false</code> otherwise.
     * @since   JDK1.0
     */
    public boolean isEmpty() {
	return count == 0;
    }

    /**
     * Returns an enumeration of the keys in this hashtable.
     *
     * @return  an enumeration of the keys in this hashtable.
     * @see     java.util.Enumeration
     * @see     java.util.LRUCache#elements()
     * @since   JDK1.0
     */
    public Enumeration keys() {
	return new LRUCacheEnumerator(table, true);
    }

    /**
     * Returns an enumeration of the values in this hashtable.
     * Use the Enumeration methods on the returned object to fetch the elements
     * sequentially.
     *
     * @return  an enumeration of the values in this hashtable.
     * @see     java.util.Enumeration
     * @see     java.util.LRUCache#keys()
     * @since   JDK1.0
     */
    public Enumeration elements() {
	return new LRUCacheEnumerator(table, false);
    }

    /**
     * Tests if some key maps into the specified value in this hashtable.
     * This operation is more expensive than the <code>containsKey</code>
     * method.
     *
     * @param      value   a value to search for.
     * @return     <code>true</code> if some key maps to the
     *             <code>value</code> argument in this hashtable;
     *             <code>false</code> otherwise.
     * @exception  NullPointerException  if the value is <code>null</code>.
     * @see        java.util.LRUCache#containsKey(java.lang.Object)
     * @since      JDK1.0
     */
    public boolean contains(Object value) {
	if (value == null) {
	    throw new NullPointerException();
	}

	LRUCacheEntry tab[] = table;
	for (int i = tab.length ; i-- > 0 ;) {
	    for (LRUCacheEntry e = tab[i] ; e != null ; e = e.next) {
		if (e.value.equals(value)) {
		    return true;
		}
	    }
	}
	return false;
    }

    /**
     * Tests if the specified object is a key in this hashtable.
     *
     * @param   key   possible key.
     * @return  <code>true</code> if the specified object is a key in this
     *          hashtable; <code>false</code> otherwise.
     * @see     java.util.LRUCache#contains(java.lang.Object)
     * @since   JDK1.0
     */
    public boolean containsKey(Object key) {
	LRUCacheEntry tab[] = table;
	int hash = key.hashCode();
	int index = (hash & 0x7FFFFFFF) % tab.length;
	for (LRUCacheEntry e = tab[index] ; e != null ; e = e.next) {
	    if ((e.hash == hash) && e.key.equals(key)) {
		return true;
	    }
	}
	return false;
    }

    /**
     * Returns the value to which the specified key is mapped in this hashtable.
     *
     * @param   key   a key in the hashtable.
     * @return  the value to which the key is mapped in this hashtable;
     *          <code>null</code> if the key is not mapped to any value in
     *          this hashtable.
     * @see     java.util.LRUCache#put(java.lang.Object, java.lang.Object)
     * @since   JDK1.0
     */
    public Object get(Object key) {
	LRUCacheEntry tab[] = table;
	int hash = key.hashCode();
	int index = (hash & 0x7FFFFFFF) % tab.length;
	for (LRUCacheEntry e = tab[index] ; e != null ; e = e.next) {
	    if ((e.hash == hash) && e.key.equals(key)) {
		if ( expirationTime>0 )
                  e.expirationTime=System.currentTimeMillis()+expirationTime;
//LoggingService.getDefaultLogger().logDebug(this, "===>"+table.length);
                return e.value;
	    }
	}
	return null;
    }

    /**
     * Rehashes the contents of the hashtable into a hashtable with a
     * larger capacity. This method is called automatically when the
     * number of keys in the hashtable exceeds this hashtable's capacity
     * and load factor.
     *
     * @since   JDK1.0
     */
    protected void rehash() {
	int oldCapacity = table.length;
	LRUCacheEntry oldTable[] = table;

	int newCapacity = oldCapacity * 2 + 1;
	LRUCacheEntry newTable[] = new LRUCacheEntry[newCapacity];

	threshold = (int)(newCapacity * loadFactor);
	table = newTable;

	//LoggingService.getDefaultLogger().logDebug(this, "rehash old=" + oldCapacity + ", new=" + newCapacity + ", thresh=" + threshold + ", count=" + count);

	for (int i = oldCapacity ; i-- > 0 ;) {
	    for (LRUCacheEntry old = oldTable[i] ; old != null ; ) {
		LRUCacheEntry e = old;
		old = old.next;

		int index = (e.hash & 0x7FFFFFFF) % newCapacity;
		e.next = newTable[index];
		newTable[index] = e;
	    }
	}
    }

    /**
     * Maps the specified <code>key</code> to the specified
     * <code>value</code> in this hashtable. Neither the key nor the
     * value can be <code>null</code>.
     * <p>
     * The value can be retrieved by calling the <code>get</code> method
     * with a key that is equal to the original key.
     *
     * @param      key     the hashtable key.
     * @param      value   the value.
     * @return     the previous value of the specified key in this hashtable,
     *             or <code>null</code> if it did not have one.
     * @exception  NullPointerException  if the key or value is
     *               <code>null</code>.
     * @see     java.lang.Object#equals(java.lang.Object)
     * @see     java.util.LRUCache#get(java.lang.Object)
     * @since   JDK1.0
     */
    public Object put(Object key, Object value) {
	// Make sure the value is not null
        if (value == null) {
	    throw new NullPointerException();
	}



	// Makes sure the key is not already in the hashtable.
	LRUCacheEntry tab[] = table;
	int hash = key.hashCode();
	int index = (hash & 0x7FFFFFFF) % tab.length;
//LoggingService.getDefaultLogger().logDebug(this, "=====================>Index: "+index );
	for (LRUCacheEntry e = tab[index] ; e != null ; e = e.next) {
	    if ((e.hash == hash) && e.key.equals(key)) {
		Object old = e.value;
		e.value = value;
                return old;
	    }
	}

	if (count >= threshold) {
	    // Rehash the table if the threshold is exceeded
	    rehash();
	    return put(key, value);
	}

	// Creates the new entry.
	LRUCacheEntry e = new LRUCacheEntry();
	e.hash = hash;
	e.key = key;
	e.value = value;
        e.expirationTime=(expirationTime>0)?(System.currentTimeMillis()+expirationTime):-1;
	e.next = tab[index];
	tab[index] = e;
	count++;
        if ( cacheMonitorThread==null || !cacheMonitorThread.isAlive()){
          cacheMonitorThread= new CacheMonitor();//new Thread(new CacheMonitor() );
          cacheMonitorThread.setDaemon(true);
          cacheMonitorThread.setPriority(Thread.NORM_PRIORITY-1);
          cacheMonitorThread.start();
        }
	return null;

    }



    /**
     * Removes the key (and its corresponding value) from this
     * hashtable. This method does nothing if the key is not in the hashtable.
     *
     * @param   key   the key that needs to be removed.
     * @return  the value to which the key had been mapped in this hashtable,
     *          or <code>null</code> if the key did not have a mapping.
     * @since   JDK1.0
     */
    public Object remove(Object key) {
	LRUCacheEntry tab[] = table;
	int hash = key.hashCode();
	int index = (hash & 0x7FFFFFFF) % tab.length;
	for (LRUCacheEntry e = tab[index], prev = null ; e != null ; prev = e, e = e.next) {
	    if ((e.hash == hash) && e.key.equals(key)) {
		if (prev != null) {
		    prev.next = e.next;
		} else {
		    tab[index] = e.next;
		}
		count--;
		return e.value;
	    }
	}
	return null;
    }

    /**
     * Clears this hashtable so that it contains no keys.
     *
     * @since   JDK1.0
     */
    public void clear() {
        LRUCacheEntry tab[] = table;
        for (int index = tab.length; --index >= 0; )
            tab[index] = null;
        count = 0;
    }

    /**
     * Creates a shallow copy of this hashtable. The keys and values
     * themselves are not cloned.
     * This is a relatively expensive operation.
     *
     * @return  a clone of the hashtable.
     * @since   JDK1.0
     */
    public Object clone() {
	try {
	    LRUCache t = (LRUCache)super.clone();
	    t.table = new LRUCacheEntry[table.length];
	    for (int i = table.length ; i-- > 0 ; ) {
		t.table[i] = (table[i] != null)
		    ? (LRUCacheEntry)table[i].clone() : null;
	    }
	    return t;
	} catch (CloneNotSupportedException e) {
	    // this shouldn't happen, since we are Cloneable
	    throw new InternalError();
	}
    }

    /**
     * Returns a rather long string representation of this hashtable.
     *
     * @return  a string representation of this hashtable.
     * @since   JDK1.0
     */
    public String toString() {
	int max = size() - 1;
	StringBuffer buf = new StringBuffer();
	Enumeration k = keys();
	Enumeration e = elements();
	buf.append("{");

	for (int i = 0; i <= max; i++) {
	    String s1 = k.nextElement().toString();
	    String s2 = e.nextElement().toString();
	    buf.append(s1 + "=" + s2);
	    if (i < max) {
		buf.append(", ");
	    }
	}
	buf.append("}");
	return buf.toString();
    }

    /**
     * WriteObject is called to save the state of the hashtable to a stream.
     * Only the keys and values are serialized since the hash values may be
     * different when the contents are restored.
     * iterate over the contents and write out the keys and values.
     */
    private void writeObject(java.io.ObjectOutputStream s)
        throws IOException
    {
	// Write out the length, threshold, loadfactor
	s.defaultWriteObject();

	// Write out length, count of elements and then the key/value objects
	s.writeInt(table.length);
	s.writeInt(count);
	for (int index = table.length-1; index >= 0; index--) {
	    LRUCacheEntry entry = table[index];

	    while (entry != null) {
		s.writeObject(entry.key);
		s.writeObject(entry.value);
		entry = entry.next;
	    }
	}
    }

    /**
     * readObject is called to restore the state of the hashtable from
     * a stream.  Only the keys and values are serialized since the
     * hash values may be different when the contents are restored.
     * Read count elements and insert into the hashtable.
     */
    private void readObject(java.io.ObjectInputStream s)
         throws IOException, ClassNotFoundException
    {
	// Read in the length, threshold, and loadfactor
	s.defaultReadObject();

	// Read the original length of the array and number of elements
	int origlength = s.readInt();
	int elements = s.readInt();

	// Compute new size with a bit of room 5% to grow but
	// No larger than the original size.  Make the length
	// odd if it's large enough, this helps distribute the entries.
	// Guard against the length ending up zero, that's not valid.
	int length = (int)(elements * loadFactor) + (elements / 20) + 3;
	if (length > elements && (length & 1) == 0)
	    length--;
	if (origlength > 0 && length > origlength)
	    length = origlength;

	table = new LRUCacheEntry[length];
	count = 0;
        // Da Implementare
        //if (bongo)
        //{
        //  new conillio();
        //}
	// Read the number of elements and then all the key/value objects
	for (; elements > 0; elements--) {
	    Object key = s.readObject();
	    Object value = s.readObject();
	    put(key, value);
	}
    }



    class CacheMonitor extends Thread {
      boolean stopItWillYa=false;
      Object dummyFlag=new Object();
      //public synchronized void run(){
      public void run(){
          if ( expirationTime >0 )
           // while (!stopItWillYa && size()>=0){
           // cacheMonitorThread==this: serve al posto della synchrinizazione
            while (!stopItWillYa && size()>=0 && cacheMonitorThread==this){
              removeExpiredCacheElements();
              synchronized(dummyFlag){
                try {
                  dummyFlag.wait(expirationTime<1000?1000:expirationTime);
                }catch(InterruptedException  ie){}
              }
            }
      }
      private void removeExpiredCacheElements(){
          LoggingService.getDefaultLogger().logDebug(this, "Removing expired Elements from cache. "+count);
          long time= System.currentTimeMillis() ;
          stopItWillYa=(count<=0);
          //for ( int i=0; i<count; i++ ){
          for ( int i=0; i<table.length; i++ ){
          /* LoggingService.getDefaultLogger().logDebug(this, "i="+i+" == table[i]: "+table[i]+" == "+
                      ((table[i]!=null)?""+table[i].expirationTime:"")
                      );*/
            if ( table[i]!=null && table[i].expirationTime<time )
               synchronized(LRUCache.this){
                  remove( table[i].key );
               }
          }
      }
    }
}

/**
 * A hashtable enumerator class.  This class should remain opaque
 * to the client. It will use the Enumeration interface.
 */
class LRUCacheEnumerator implements Enumeration {
    boolean keys;
    int index;
    LRUCacheEntry table[];
    LRUCacheEntry entry;

    LRUCacheEnumerator(LRUCacheEntry table[], boolean keys) {
	this.table = table;
	this.keys = keys;
	this.index = table.length;
    }

    public boolean hasMoreElements() {
	if (entry != null) {
	    return true;
	}
	while (index-- > 0) {
	    if ((entry = table[index]) != null) {
		return true;
	    }
	}
	return false;
    }

    public Object nextElement() {
	if (entry == null) {
	    while ((index-- > 0) && ((entry = table[index]) == null));
	}
	if (entry != null) {
	    LRUCacheEntry e = entry;
	    entry = e.next;
	    return keys ? e.key : e.value;
	}
	throw new NoSuchElementException("LRUCacheEnumerator");
    }
}


class LRUCacheEntry {
    int hash;
    Object key;
    Object value;
    LRUCacheEntry next;

    long expirationTime;

    protected Object clone() {
	LRUCacheEntry entry = new LRUCacheEntry();
	entry.hash = hash;
	entry.key = key;
	entry.value = value;
        entry.expirationTime = expirationTime;
	entry.next = (next != null) ? (LRUCacheEntry)next.clone() : null;
	return entry;
    }
}


