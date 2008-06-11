package org.wittydev.jndi;

import java.util.Enumeration;

import javax.naming.Context;
import javax.naming.NameClassPair;
import javax.naming.NamingException;

public class JNDIUtil {
	
	//private String offseter="==";
	//private String pointer=">";
	private String offseter="*";
	private String pointer="*";
	static JNDIUtil jndiUtil; 
	public static JNDIUtil getInstance(){
		if ( jndiUtil ==null )jndiUtil = new JNDIUtil();
		return jndiUtil;
	}
	

	public void printTree ( Context ctx  )  throws NamingException{
		printTree (ctx, true, null, true );
	}

	public void printTree (
							Context ctx, 
							boolean recursive,
							String[]namingContextClasses, 
							boolean autoResolveNamingContextClasses 
							)  throws NamingException{
		printTree (ctx, recursive, namingContextClasses, autoResolveNamingContextClasses, 0);
	}
	
	public void printTree (
							Context ctx, 
							boolean recursive, 
							String[]namingContextClasses,
							boolean autoResolveNamingContextClasses,
							int depth
							) throws NamingException{
		
		String offset="";
		for (int i=0; i<depth; i++)offset+=offseter;
		offset+=pointer;
		
		for (Enumeration e=ctx.list(""); e.hasMoreElements();){
			NameClassPair ncp=(NameClassPair)e.nextElement();
			
			System.out.println(offset+" "+ncp.getName()+" ["+ncp.getClassName()+"]");
			
			if (recursive){
				boolean isNC=false;
				for ( int i=0; namingContextClasses!=null && i<namingContextClasses.length; i++ ){
					if ( namingContextClasses[i]!=null && namingContextClasses[i].equals(ncp.getClassName())){
						isNC=true;
						break;
					}
				}
				
				if ( !isNC && autoResolveNamingContextClasses ){
					try {
						Class cls=Object.class.forName(ncp.getClassName());
						if (cls!=null && Context.class.isAssignableFrom(cls)) isNC=true;
					} catch (ClassNotFoundException e1) {
						//e1.printStackTrace();
					}
				}
				
				if (isNC){
					Context subCtx=(Context)ctx.lookup(ncp.getName());
					printTree(subCtx, recursive, namingContextClasses, autoResolveNamingContextClasses, depth+1);
				}
			
			}
		}
	
	}	
	
}	
