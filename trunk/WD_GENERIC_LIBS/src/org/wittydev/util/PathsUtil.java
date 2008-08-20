package org.wittydev.util;

import java.util.StringTokenizer;

public class PathsUtil {
	public static String normalizePath(String callerPath, String objectPath) {
		return normalizePath(callerPath, objectPath, '/');
	}
	public static String normalizePath(String callerPath, String objectPath, char pathSeparator) {
		if (callerPath==null || objectPath==null || objectPath.charAt(0)==pathSeparator)
			return objectPath;
		
		int pos=callerPath.lastIndexOf(pathSeparator);
		if(pos<0)return objectPath;
		callerPath=callerPath.substring(0, pos+1);
		objectPath=callerPath+objectPath;
		
		if (objectPath.indexOf('.')>0){
			StringTokenizer dummy=new StringTokenizer(objectPath, ""+pathSeparator);
			String[] dummyArr = new String[dummy.countTokens()];
			int idx=0;
			while(dummy.hasMoreTokens()){
				dummyArr[idx]=dummy.nextToken();
				if ( dummyArr[idx].equals(".") ){
					dummyArr[idx]=null;
				}else if ( dummyArr[idx].equals("..") ){
					dummyArr[idx]=null;
					if (idx>0){
						dummyArr[idx-1]=null;
						idx--;
					}
				}else
					idx++;
				
			}
			objectPath="";
			for (int i=0; i<dummyArr.length; i++){
				if (dummyArr[i]!=null)
					objectPath+=pathSeparator+dummyArr[i];
			}
			return objectPath;
		}else
			return objectPath;
	}

}
