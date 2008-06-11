package org.wittydev.math;

import java.util.Enumeration;
import java.util.Iterator;
import java.util.NoSuchElementException;

import org.wittydev.util.DataTools;

public class CombinationsEnum implements Enumeration, Iterator{
	Object[] s;
	int subsetSize;
	
	long combNum=-1;
	byte[] posArr;
	int currentIdx=-1;
	Object current;
	
	
	public CombinationsEnum(Object[] s, int subsetSize) {
		this.s=s;
		this.subsetSize=subsetSize;
		reset();
	}
	
	
	
	public long combinationsNumber(){
		if (combNum<0) combNum=CombinatoricsUtil.combinationsNumber(s.length, subsetSize);
		return combNum;
	}
	
	public boolean hasNext() {
		return currentIdx<combinationsNumber()-1;
	}
	public Object next() {
		if (currentIdx >= combinationsNumber()-1) throw new NoSuchElementException();
		
		if ( posArr==null){
			posArr=new byte[s.length];
			for (int i=0; i<subsetSize; i++){
				posArr[i]=1;
			}
		}else
			posArr=CombinatoricsUtil.nextCombinationsArray(posArr);
		
		int cnt=0;
		Object[] obj=new Object[subsetSize];
		for (int i=0; i<posArr.length; i++)
			if (posArr[i]==1)obj[cnt++]=s[i];
		
		current = obj;
		currentIdx++;
		return obj;
	}
	public void remove() {
		throw new UnsupportedOperationException();
	}
	public boolean hasMoreElements() {
		return hasNext();
	}
	public Object nextElement() {
		return next();
	}
	
	public Object current(){
		return current;
	}
	public Object currentIndex(){
		return currentIdx;
	}
	
	public void reset(){
		combNum=-1;
		currentIdx=-1;
		posArr=null;
		current=null;
	}
	
	public static void main(String[] args) {
		String[] objs = {"A", "B", "C", "D"};
		CombinationsEnum cs = new CombinationsEnum(objs, 3);
		
		for (; cs.hasNext(); )
			System.out.println(DataTools.arrayToString(cs.next()));
	}

}
