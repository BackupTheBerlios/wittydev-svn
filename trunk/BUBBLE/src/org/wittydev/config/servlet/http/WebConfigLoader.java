package org.wittydev.config.servlet.http;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.List;

import javax.servlet.ServletContext;

import org.wittydev.config.ConfigEntry;
import org.wittydev.config.ConfigLoader;
import org.wittydev.config.ConfigPropertiesFile;
import org.wittydev.core.WDException;

public class WebConfigLoader extends ConfigLoader{
	ServletContext servletContext;
    public WebConfigLoader(ServletContext servletContext){
    	super(-1);
    	this.servletContext=servletContext;
    }
    public WebConfigLoader( ServletContext servletContext, long cacheExpirationMillis ){
    	super(cacheExpirationMillis, "properties");
    	this.servletContext=servletContext;
    }
    public WebConfigLoader( ServletContext servletContext, long cacheExpirationMillis, String propertiesFileExtention ){
    	super(cacheExpirationMillis, propertiesFileExtention);
    	this.servletContext=servletContext;
    }
	
    /**
     * 	Bubble configurations files 
     */
    
    protected void getConfigEntry_0( List v, String propertiesPath ) throws WDException {
        //System.out.println("===========>1--"+propertiesPath);
    	if ( servletContext == null){
        	internalLogWarning("ServletContext not set... Testing??");
        	return;
        }
    	//System.out.println("===========>2--"+propertiesPath);
    	try {
        	String [] dummy={"/WEB-INF/bubble/config", "/WEB-INF/bubble/localconfig"};
        	for (int i=0;i<dummy.length; i++){
        		//System.out.println("===========>3"+dummy[i]+propertiesPath);
        		InputStream is=servletContext.getResourceAsStream(dummy[i]+propertiesPath);
        		//System.out.println("===========>4"+is);
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
    	
    }

    Class[] clazz={ServletContext.class};
    
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
            Object result=null;
            try{
            	try{
            		Constructor cs=ce.getType().getConstructor(clazz);
                	result =cs.newInstance(new Object[]{servletContext});
            	}catch(NoSuchMethodException nsm){}
            	if (result==null)result =ce.getType().newInstance();
                
            }catch(InvocationTargetException ite){
            	ite.printStackTrace();
                internalLogError(ite);
                throw new WDException (WDException .CODE_SYSTEM_EXCEPTION, ite.getMessage() , ite);
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
    
    
}
