package org.wittydev.util;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

public class OrderedMap implements Map{
    java.util.List list;
    java.util.List entryList;
    java.util.Map map;

    public OrderedMap() {
    		this(HashMap.class);
    }

    public OrderedMap(Class mapClass) {
        try {
			this.map = (Map)mapClass.newInstance();
		} catch (InstantiationException e) {
			throw new RuntimeException ("Wrong map class: "+mapClass, e);
		} catch (IllegalAccessException e) {
			throw new RuntimeException ("Wrong map class: "+mapClass, e);
		}
        this.list = new java.util.ArrayList();
    }


    public int size(){
        return map.size();
    }
    public boolean isEmpty(){
        return map.isEmpty();
    }
    public boolean containsKey(Object key){
        return map.containsKey(key);
    }
    public boolean containsValue(Object value){
        return map.containsValue(value);
    }
    public Object get(Object key){
        return map.get(key);
    }
    public Object put(Object key, Object value){
    	Object obj=map.put(key, value);
    	list.add(key);
        resetEntryList();
        return obj;
    }
    ////
    public Object keyAt(int i){
        return list.get(i);
    }

    public Object entryAt(int i){
        return map.get(list.get(i));
    }
    /////

    
    public Object remove(Object key){
    	Object obj=map.remove( key );
    	list.remove(key);
    	resetEntryList();
    	return obj;
    }
    

    public void clear(){
        map.clear();
        list.clear();
        resetEntryList();
    }

    public Collection values(){
    	if ( entryList ==null ){
    		entryList = new ArrayList();
        	for (int i=0; i<list.size(); i++)
        		entryList.add(map.get(list.get(i)));
    	}
    	return entryList;
    }
    
    public void putAll(Map t){
    	for (Iterator it=t.keySet().iterator(); it.hasNext();){
    		Object key=it.next();
    		Object value=t.get(key);
    		put(key, value);
    	}
    		
    }
    public Set keySet(){
        return new HashSet(list); 
    }
    public Set entrySet(){
    	return new HashSet(values());
    }
    /*public interface Entry {
    	public Object getKey();
    	public Object getValue();
    	public Object setValue(Object value);
	    public boolean equals(Object o);
    	public int hashCode();
    }*/

    public boolean equals(Object o){
        return map.equals(o);
    }
    public int hashCode(){
        return map.hashCode();
    }

    protected void resetEntryList(){
    	entryList=null;
    }


}