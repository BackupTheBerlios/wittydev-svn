package org.wittydev.config;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.net.URL;

import java.util.Enumeration;
import java.util.List;
import java.util.StringTokenizer;
import java.util.Vector;
import java.util.zip.ZipFile;
import java.util.zip.ZipEntry;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;

import org.wittydev.bubble.Architect;
import org.wittydev.core.WDException;
import org.wittydev.logging.LoggingService;
import org.wittydev.util.BeansUtil;
import org.wittydev.util.DataTools;
import org.wittydev.util.PathsUtil;
import org.wittydev.util.ReflectionTools;
import org.wittydev.util.cache.LRUCache;




/**
 * Title:
 * Description:
 * Copyright:    Copyright (c) 2002
 * Company:
 * @author
 * @version 1.0
 */

public class ConfigLoader {
    LRUCache configEntriesCache;
    static File[] dummyFileArr=new File[0];
    String configPath;
    File[] configPathArr=new File[0];
    String pathSeparator;
    String propertiesFileExtension;

	Context ctx;


    public ConfigLoader(){
        this(-1);
        System.out.println("AAAAAAAAAAAAAAAAAAAAAAAAAAAPPPPPPPPPPPPPPPPPPPPPPPPPPPPP");
    }
    public ConfigLoader( long cacheExpirationMillis ){
    	this(cacheExpirationMillis, "properties");
    }
    public ConfigLoader( long cacheExpirationMillis, String propertiesFileExtention ){

        //configEntriesCache =new LRUCache(cacheExpirationMillis);
        setCacheExpirationMillis( cacheExpirationMillis );
        try {
            ctx = new InitialContext ();
        }catch(NamingException ne){
            internalLogWarning( "Errore while loading JNDI initial context! JNDI lookup disabled!");
            internalLogWarning( ne );
        }
        this.propertiesFileExtension=propertiesFileExtention;
        
        
        

    }
    long cacheExpirationMillis;

    public String getPropertiesFileExtension() {
		return propertiesFileExtension;
	}

    public void setCacheExpirationMillis(long cacheExpirationMillis){
        configEntriesCache= new LRUCache ( cacheExpirationMillis );
        this.cacheExpirationMillis=cacheExpirationMillis;
    }
    public long getCacheExpirationMillis(){
        return cacheExpirationMillis;
    }
    public void clearCache(){
        if (configEntriesCache!=null)configEntriesCache.clear();
    }

    public String getConfigPath(){
        return configPath;
    }
    public void setConfigPath(String configPath){
        this.configPath= configPath;
        configPathArr=dummyFileArr;
        if ( configPath==null || configPath.length()==0 ) {
            internalLogWarning("configPath not set!!!!");
            return;
        }

        java.util.Vector v= new java.util.Vector();
        java.util.StringTokenizer tokenizer=new java.util.StringTokenizer( configPath, getPathSeparator());
        while(tokenizer.hasMoreTokens()){
            String token=tokenizer.nextToken().trim();
            if ( token.length()>0){
                try{
                    File f= new File(token);
                    if ( f.exists()  )
                         v.add(f);
                    else
                        internalLogError("[CONFIGPATH] File or directory does not exist: "+token );
                }catch(Exception e){
                    internalLogError("[CONFIGPATH] Invalid file or directory: "+token );
                    internalLogError(e);
                }
            }
            configPathArr=new File[v.size()];
            configPathArr= (File[])v.toArray( configPathArr );
        }


    }

    public Object createAndFillNewObject( String componentPath,
                                        ObjectResolver resolver,
                                        Object[] resolverArgs ) throws WDException {
        Object result=createNewObject(componentPath);
        return fillObject(result, componentPath, resolver, resolverArgs );

    }
    public Object createNewObject( String componentPath ) throws WDException {
    /*    return createNewObject( componentPath, null, null );
    }
    public Object createNewObject(  String componentPath,
                                    ObjectResolver resolver,
                                    Object[] resolverArgs ) throws WDException {*/
        //System.out.println("Component===>"+componentPath );
        ConfigEntry ce=getConfigEntry( componentPath );//+ ".properties" );
        if ( ce==null) {
            WDException iwe=new WDException ( WDException.CODE_SYSTEM_EXCEPTION, "Not found: "+componentPath );
            internalLogError(iwe);
            throw iwe;
        }
        //System.out.println(DataTools.arrayToString(configPathArr));
        Object result;
        try{
            result =ce.type.newInstance();
        }catch(InstantiationException ie){
        	ie.printStackTrace();
            internalLogError(ie);
            throw new WDException (WDException .CODE_SYSTEM_EXCEPTION, ie.getMessage() , ie);
        }catch(IllegalAccessException iae){
        	iae.printStackTrace();
            internalLogError(iae);
            throw new WDException (WDException .CODE_SYSTEM_EXCEPTION, iae.getMessage() , iae);
        }
        //return fillObject(result, componentPath, resolver, resolverArgs);
        return result;
    }

    public Object fillObject(  Object result,
                               String componentPath,
                               ObjectResolver resolver,
                               Object[] resolverArgs ) throws WDException {

        ConfigEntry ce=getConfigEntry( componentPath );//+ ".properties" );

        if ( ce==null) {
            WDException iwe=new WDException ( WDException.CODE_SYSTEM_EXCEPTION, "Not found: "+componentPath);
            internalLogError(iwe);
            throw iwe;
        }
        if ( result==null ){
            WDException iwe=new WDException ( WDException.CODE_APPPLICATION_EXCEPTION, "Object should not be null!");
            internalLogError(iwe);
            throw iwe;
        }
        if ( !ce.getType().isAssignableFrom(result.getClass()  )  ){
        //if ( result.getClass().isAssignableFrom(ce.getType())){
            WDException iwe=new WDException ( WDException.CODE_APPPLICATION_EXCEPTION, "Object "+result.getClass()+" is not compatible with "+ce.getType()+"!");
            internalLogError(iwe);
            throw iwe;
        }


        for ( int i=0; i<ce.getConfigPropertiesCount(); i++){
            ConfigProperty cp = ce.getConfigProperty(i);
            Object param;
            Class paramClass= cp.getSetterMethod().getParameterTypes()[0];
            //if ( paramClass.isArray() && !paramClass.getComponentType().isPrimitive()
            //        && !(paramClass.getComponentType()!=String.class) ){
            if ( !cp.isJNDIReference() && !cp.isReference() &&  paramClass.isArray() ) {
                //!isSpecialType(paramClass.getComponentType()) ){
                String[] dummy;
                try{
                    dummy=(String[])ReflectionTools.valueOf( String[].class, cp.getStringValue() );
                }catch (Exception e){
                    String msg="A problem occured while loading object of type String[]: ["+cp.getStringValue() +"]";
                    internalLogWarning( msg );
                    internalLogWarning( e );
                    throw new WDException(WDException.CODE_SYSTEM_EXCEPTION, msg, e);
                }
                if ( dummy==null )
                    param=null;
                else{

                    /*System.out.println("====>"+paramClass.getComponentType()+"==>"+
                                        paramClass.getComponentType().isPrimitive()+"==>"+
                                        paramClass.getComponentType().isArray()+"==>"+
                                        isSpecialType(paramClass.getComponentType()));*/

                    if ( paramClass.getComponentType() == String.class)
                        param=dummy;
                    else if ( isSpecialType(paramClass.getComponentType()) ){
                        try{
                            param=ReflectionTools.valueOf( cp.getType(), cp.getStringValue() );
                        }catch (Exception e){
                            String msg="A problem occured while loading object of type "+cp.getType()+": ["+cp.getStringValue() +"]";
                            internalLogWarning( msg );
                            internalLogWarning( e );
                            throw new WDException(WDException.CODE_SYSTEM_EXCEPTION, msg, e);
                        }

                    }else{
                        Object[] paramArr=(Object[])java.lang.reflect.Array.newInstance(
                                                            paramClass.getComponentType(), dummy.length);
                        for( int k=0; k<dummy.length; k++){
                            //paramArr[k]=resolveComponentReference( componentRootContextPath, dummy[k]  );
                            paramArr[k]=resolver.resolveComponentReference( dummy[k], ce.getScope(), resolverArgs  );
                        }
                        param=paramArr;
                    }
                    //System.out.println("paramArr==>"+paramArr);


                }
            //}else if ( !cp.isJNDIReference() && !cp.isReference() &&
            //                paramClass.isAssignableFrom(java.util.Dictionary.class) ) {
            //}else if ( !cp.isJNDIReference() && !cp.isReference() &&
            //                paramClass.isAssignableFrom(java.util.Dictionary.class) ) {

            }else{
                long dummyCount=System.currentTimeMillis();
                param=getObj( componentPath, cp, ce.getScope(),resolver, resolverArgs);
                //System.out.println(componentPath);
            }
            try{
                if (LoggingService.getDefaultLogger().isLoggingDebug())
                    LoggingService.getDefaultLogger().logDebug(this, "Invoking "+result.getClass().getName()+"."+cp.getSetterMethod().getName()+"("+DataTools.arrayToString(param)+")");
                cp.getSetterMethod().invoke( result, new Object[]{param} );
            }catch(IllegalAccessException iae){
                String msg ="Invalid property set: ["+cp.getSetterMethod().getName()+"("+param +")]["+cp.getStringValue()+"]";
                internalLogWarning(msg);
                internalLogWarning(iae);
                throw new WDException ( WDException.CODE_SYSTEM_EXCEPTION, msg,iae);
            }catch(java.lang.reflect.InvocationTargetException ite){
                String msg ="Invalid property set: ["+cp.getSetterMethod().getName()+"("+param +")]["+cp.getStringValue()+"]";
                internalLogWarning(msg);
                internalLogWarning(ite);
                throw new WDException ( WDException.CODE_SYSTEM_EXCEPTION, msg,ite);
            }

        }

        return result;

    }

    Object getObj(String callerPath, ConfigProperty cp, String scope,ObjectResolver resolver, Object[] resolverArgs) throws WDException{
        Object result;
        //System.out.println("===================>callerPath: "+callerPath);
        //System.out.println(cp.getType()+"=====>"+cp.getStringValue());
        // se e' esplicitamente un "riferimento semplice",
        // o se non e' un "riferimento semplice" esplicito ne un "riferimento JDI" esplecito,
        // lo considero come un riferimento
        if ( cp.isReference() || (!cp.isJNDIReference() && !isSpecialType(cp.getType()) )){
            String objectPath=cp.getStringValue(), propertyName=null;
            /**/
            int pos1=objectPath.lastIndexOf("/");
            if (pos1<0)pos1=objectPath.lastIndexOf("\\");
            
            int pos2=objectPath.lastIndexOf(".");
            if( pos2>0 && pos2>pos1 ){
                propertyName=objectPath.substring(pos2+1);
                objectPath=objectPath.substring(0, pos2);
            }
            //System.out.println("==>objectPath before: "+objectPath+"-callerPath: "+callerPath);
            objectPath=normalizePath(callerPath, objectPath);
            //System.out.println("==>objectPath after: "+objectPath);
            result=resolver.resolveComponentReference( objectPath, scope, resolverArgs  );
            //System.out.println("===>"+objectPath+"==>"+result);

            if ( propertyName!=null && propertyName.length()>0 && result!=null ){
                try{
                    result = BeansUtil.resolveValueIW( result, propertyName );
                }catch(WDException iwe){
                    throw iwe;
                }
                /*propertyName=propertyName.substring( 0,1).toUpperCase()+propertyName.substring( 1);
                java.lang.reflect.Method method;
                try{
                    method=result.getClass().getMethod("get"+propertyName, null);
                }catch(NoSuchMethodException nsme){
                    try{
                        method=result.getClass().getMethod("is"+propertyName, null);
                    }catch(NoSuchMethodException nsme2){
                        String msg ="Property '"+propertyName+"' not found in ["+objectPath+"]!" ;
                        internalLogWarning(msg);
                        throw new NFJException (msg, NFJException.CODE_SYSTEM_EXCEPTION, nsme2);
                    }
                }

                try{
                    result=method.invoke(result, null);
                }catch(java.lang.reflect.InvocationTargetException ite){
                    String msg ="Problem while getting property '"+propertyName+"' not found in ["+objectPath+"]!" ;
                    internalLogWarning(msg);
                    internalLogWarning(ite);
                    throw new NFJException (msg, NFJException.CODE_SYSTEM_EXCEPTION, ite);
                }catch(IllegalAccessException ia){
                    String msg ="Problem while getting property '"+propertyName+"' not found in ["+objectPath+"]!" ;
                    internalLogWarning(msg);
                    internalLogWarning(ia);
                    throw new NFJException (msg, NFJException.CODE_SYSTEM_EXCEPTION, ia);
                }*/


            }

        }else if ( cp.isJNDIReference() ){

            if ( ctx!=null){
                try{
                    result = ctx.lookup( cp.getStringValue() );
                }catch(NamingException ne){
                    String msg ="JNDI lookup failed for: "+cp.getStringValue() ;
                    internalLogWarning(msg);
                    throw new WDException ( WDException.CODE_SYSTEM_EXCEPTION, msg, ne);
                }

            }else{
                String msg = "JNDI lookup not available, InitalContext not loaded.  ["+
                                cp.getStringValue()+"]";
                internalLogWarning( msg );
                throw new WDException ( WDException.CODE_SYSTEM_EXCEPTION, msg );
            }
        }else{
            //System.out.println(cp.getType()+"==>"+cp.getStringValue());
            try{
                result=ReflectionTools.valueOf( cp.getType(), cp.getStringValue() );
            }catch (Exception e){
                String msg="A problem occured while loading object of type "+cp.getType()+": ["+cp.getStringValue() +"]";
                internalLogWarning( msg );
                internalLogWarning( e );
                throw new WDException(WDException.CODE_SYSTEM_EXCEPTION, msg,  e);
            }

        }
        return result;

    }

    // Attenzione la gestione di "char" e "Character" non funziona
    protected boolean isSpecialType( Class type){
        // non gestisco gli  Array di Array
        //OrderedDictionary o;
        if ( type.isArray() ) return false;
        if (
            java.util.Dictionary.class.isAssignableFrom( type ) ||
            java.util.List.class.isAssignableFrom( type ) ||
            java.util.Set.class.isAssignableFrom( type ) ||
            java.util.Map.class.isAssignableFrom( type ) ) return true;
        if ( type.isPrimitive() || type==String.class) return true;
        // BigDecimal, BigInteger, Byte, Double, Float, Integer, Long, Short
        //System.out.println(type+"=========>"+type.isAssignableFrom( Number.class ) );
        //if ( type.isAssignableFrom( Number.class ) ) return true;
        if ( type==java.math.BigDecimal.class || type==java.math.BigInteger.class )return true;
        if ( ReflectionTools.isPrimitiveWrapperClass( type ) ) return true;


        return false;
    }

    public ConfigEntry getConfigEntry( String callerPath, String componentPath ) throws WDException {
    	return getConfigEntry( PathsUtil.normalizePath(callerPath, componentPath) );
    }
    public ConfigEntry getConfigEntry( String componentPath ) throws WDException {
        if ( componentPath ==null ){
            internalLogWarning("Invalid component path: null");
            return null;
        }
        
        if ( componentPath ==null || componentPath.length()==0){
            internalLogWarning("Invalid component path: null");
            return null;
        }else if (componentPath.charAt(0)!='/' && componentPath.charAt(0)!='\\' && componentPath.charAt(0)!='.' )
        	componentPath='/'+componentPath;

        //if ("./WideCommHttpServer".equals(componentPath) )throw new RuntimeException();
        
        ConfigEntry ce=(ConfigEntry)configEntriesCache.get(componentPath);
        
        if (ce==null){
            ce=getConfigEntry_0(componentPath);
            if ( ce!=null )
                configEntriesCache.put(componentPath, ce);
            else{
            	LoggingService.getDefaultLogger().logTrace(this, "Configuration not found for component: "+componentPath);
            }
            	
        }
        return ce;
    }
    protected ConfigEntry getConfigEntry_0( String componentPath ) throws WDException {

        Vector v= new Vector();
        

        String propertiesPath= componentPath+"."+getPropertiesFileExtension();
        //System.out.println(componentPath);
        internalLogDebug("Looking for:"+configPathArr);
        
        // Loading config and localconfig form META-INF/resources of the bubble jar file (getClass().getResourceAsStream(..))  
        try {
        	String [] dummy={"/META-INF/resources/config", "/META-INF/resources/localconfig"};
        	for (int i=0;i<dummy.length; i++){
        		InputStream is=getClass().getResourceAsStream(dummy[i]+propertiesPath);
				if (is!=null){
					try {
						String result=readFile(is);
	                    if ( result != null && result.length()>0) {
	                        ConfigPropertiesFile cf= new ConfigPropertiesFile(
	                        								dummy[i],
	                                                        propertiesPath,
	                                                        result, 0 );
	                        v.add(cf);
	                    }
	                }catch(IOException ioe){
	                	ioe.printStackTrace();
	                    internalLogError("Error while loading: "+propertiesPath);
	                    internalLogError(ioe);
	                }
	
				}
        	}
		} catch (Throwable e) {
			internalLogError(e);
		}
		
		// Just a protected method to override eventually 
		getConfigEntry_0(v, propertiesPath);
		
		
		// Loading configuration folders forn the CONFIG-PATH environment variable
		for ( int i=0; i<configPathArr.length; i++){
            if ( configPathArr[i].isDirectory() ){
                File f= new File( configPathArr[i], propertiesPath );
                internalLogDebug("checking for absolute path:"+f.getAbsolutePath());
                if ( f.exists() && f.canRead()  && f.isFile() ) {
                    try {
                        String result=readFile(new FileInputStream( f ));
                        if ( result != null && result.length()>0) {
                            ConfigPropertiesFile cf= new ConfigPropertiesFile(
                                                            this.configPathArr[i].getPath(),
                                                            propertiesPath,
                                                            result, f.lastModified() );

                            v.add(cf);
                        }
                    }catch(IOException ioe){
                        internalLogError("Error while loading: "+f);
                        internalLogError(ioe);
                    }
                }
            }else if ( configPathArr[i].isFile() ){
                try {

                    ZipFile zf=new ZipFile(configPathArr[i]);
                    ZipEntry ze=zf.getEntry( propertiesPath );
                    if ( ze!=null && !ze.isDirectory() ){
                        String result=readFile(zf.getInputStream(ze));
                        //if ( result != null && result.length()>0) v.add(result);
                        if ( result != null && result.length()>0) {
                            ConfigPropertiesFile cf= new ConfigPropertiesFile(
                                                    this.configPathArr[i].getPath(),
                                                    propertiesPath,
                                                    result, ze.getTime() );

                            v.add(cf);
                        }
                    }
                }catch(IOException ioe){
                    internalLogError("Error while loading: ["+configPathArr[i]+"]"+propertiesPath );
                    internalLogError(ioe);
                }
            }
        }
        if ( v==null || v.size()==0) return null;
        ConfigPropertiesFile[] cfs=(ConfigPropertiesFile[])v.toArray(new ConfigPropertiesFile[v.size()]);

        //return new ConfigEntry( propertiesPath, cfs);
        return new ConfigEntry( componentPath, cfs);
    }
    
    
    protected void getConfigEntry_0( List v, String propertiesPath ) throws WDException {
    	
    }

    protected String readFile(InputStream is) throws IOException{
        if ( is==null) return null;
        BufferedInputStream bis=new BufferedInputStream(is);

        /*BufferedReader bis=new BufferedReader(
                                    new java.io.InputStreamReader(
                                                    zf.getInputStream(ze)));*/
        //StringBuffer rsltBuf=new StringBuffer();
        String result="";
        byte[]bArr=null;
        int count=0, i=0;
        while (bis.available()>0){
            if ( bArr==null||bArr.length<bis.available() ) bArr=new byte[bis.available()];
            count=bis.read( bArr);
            if (count<0)break;
            result+=new String(bArr, 0, count);
        }
        is.close();
        return result;

    }




    public String getPathSeparator(){
        if ( pathSeparator ==null ){
            //pathSeparator = System.getProperty("path.separator", ":");
            pathSeparator=java.io.File.pathSeparator;
            if (pathSeparator ==null)pathSeparator=":";
        }

        return pathSeparator;
    }

    public void setPathSeparator(String pathSeparator){
        this.pathSeparator=pathSeparator;
    }

    public void internalLogError(Throwable t){
    	System.out.println("HEYYYYY : "+LoggingService.getDefaultLogger().isLoggingError());
        LoggingService.getDefaultLogger().logError(this, t);
    }
    public void internalLogError(String t){
        LoggingService.getDefaultLogger().logError(this, t);
    }
    public void internalLogInfo(Throwable t){
        LoggingService.getDefaultLogger().logInfo(this, t);
    }
    public void internalLogInfo(String t){
        LoggingService.getDefaultLogger().logInfo(this, t);
    }

    public void internalLogDebug(Throwable t){
        LoggingService.getDefaultLogger().logDebug(this, t);
    }
    public void internalLogDebug(String t){
        LoggingService.getDefaultLogger().logDebug(this, t);
    }
    public void internalLogWarning(Throwable t){
        LoggingService.getDefaultLogger().logWarning(this, t);
    }
    public void internalLogWarning(String t){
        LoggingService.getDefaultLogger().logWarning(this, t);
    }
    
    private String normalizePath(String callerPath, String objectPath) {
    	return PathsUtil.normalizePath(callerPath, objectPath);
	}

    public static void main (String[] args) throws Exception{
        ConfigLoader tw= new ConfigLoader();
        // String pat="c:;d:;e";
        //String path="D:\\shared\\pvcs\\IMIWEB_LIBRARIES\\beanworld";
        //path+=";D:\\shared\\pvcs\\IMIWEB_LIBRARIES\\beanworld\\it.zip";
        String path="";
        tw.setConfigPath( path );
        //ConfigEntry ce=tw.getConfigEntry("/it/imiweb/DataStore.properties");
        ConfigEntry ce=tw.getConfigEntry("/Architect");
        //ConfigEntry ce=tw.getConfigEntry("it/imiweb/a.properties");
        Thread.currentThread().sleep(1000);
        System.out.println("CE==>"+ce);

        //tw.findPropertiesFiles( "it/imiweb/a.properties");
       //Object r=tw.findPropertiesFiles( "it/imiweb/a.properties") ;
        //System.out.println( DataTools.arrayToString(r));
        //String s=tw.getClass().getSimpleName()+".class";
        //System.out.println(tw.getClass().getResource(s));
    }



}





