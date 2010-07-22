package org.wittydev.bubble;
import org.wittydev.config.ConfigEntry;
import org.wittydev.config.ConfigLoader;
import org.wittydev.config.ObjectResolver;
import org.wittydev.core.WDException;



/**
 * Title:
 * Description:
 * Copyright:    Copyright (c) 2002
 * Company:
 * @author
 * @version 1.0
 */

public class ScopeBubbleContext extends BubbleContext implements ObjectResolver{
    BubbleScopeManager scopeManager;
    BubbleScopeManager[] donwStreamScopeManagers;
    String inScopeId;
    /*public ScopeBubbleContext(){
    }*/
    public ScopeBubbleContext( String inScopeId,
                                BubbleScopeManager scopeManager) {
        super(scopeManager);
        this.scopeManager=scopeManager;
        this.inScopeId=inScopeId;
    }

    /*public void startService()throws NFJException {

    }*/


    public String getInScopeId(){
        return inScopeId;
    }
    public void setInScopeId(String inScopeId){
        this.inScopeId=inScopeId;
    }
    public BubbleScopeManager getScopeManager(){
        return scopeManager;
    }
    public void  getScopeManager(BubbleScopeManager scopeManager){
        this.scopeManager=scopeManager;
    }

    public BubbleScopeManager[] getDownStreamScopeManagers(){
        return donwStreamScopeManagers;
    }
    public void setScopeManager(BubbleScopeManager[] donwStreamScopeManagers){
        this.donwStreamScopeManagers=donwStreamScopeManagers;
    }

    public BubbleScopeManager getDownStreamScopeManager(String scope){
        if ( scope==null ) scope=BubbleScopeManager.DEFAULT_SCOPE;
        for ( int i=0; donwStreamScopeManagers!=null &&
                         i<donwStreamScopeManagers.length; i++)
            if ( donwStreamScopeManagers[i].getScopeId().equalsIgnoreCase(scope) )
                    return donwStreamScopeManagers[i];

        return null;
    }

    public Object resolveName( String componentPath ){
        return resolveName(componentPath, true );
    }
    public Object resolveName( String componentPath, boolean create ){
        ConfigEntry  cf;
        try{
            cf=getConfigEntry(componentPath);
        }catch(WDException iwe){
            internalLogError(iwe);
            cf=null;
        }
        if ( cf==null ) {
            internalLogWarning( "'" + componentPath+"' component not found!"  );
            return null;
        }
        String myScope=(cf.getScope()==null)?BubbleScopeManager.DEFAULT_SCOPE:cf.getScope();
        if ( scopeManager == null || getScopeId()==myScope ||
            myScope.equalsIgnoreCase( getScopeId()) ){
        //System.out.println("Resolving in scope("+getScopeId()+"): "+componentPath  );
            return resolveName(componentPath, this, create);
        }else
            return scopeManager.resolveName( componentPath, create);

        //return resolveName(componentPath,  this, create );
    }


    protected Object resolveName(  String componentPath,
                                BubbleContext context,
                                boolean create ){
        javax.naming.Name pathName;
        try{
            pathName=nameParser.parse( componentPath );
        }catch(javax.naming.NamingException na){
            internalLogError(na);
            return null;
        }
        if ( context ==null ) context = this;
        Object result;
        try{
        	
            //System.out.println("==============>before lookup_0 on ["+context+"]");
            result=context.lookup_0( pathName, false );
            //System.out.println("==============>after lookup_0 on ["+context+"]: "+result);
        }catch(javax.naming.NamingException e){
            internalLogError( "Error while loading: "+componentPath );
            internalLogError( e );
            return null;
        }

        if ( result==null && create ){
            try{
                //result=getConfigLoader().createNewObject( componentPath, this, null );
                //System.out.println("NOT FOUND Creating: ["+getScopeId()+"]"+componentPath);
                if (isLoggingDebug())
                    internalLogDebug("[Scope:"+getScopeId()+"] Creating Bean "+componentPath);
                result=getConfigLoader().createNewObject( componentPath );
                try{
                    if(result!=null)
                        bind(pathName, result);
                    else{
                    	internalLogWarning("Configuration not found for bean: "+componentPath);
                        return null;
                    }
                }catch(javax.naming.NamingException ne ){
                    internalLogError( "Error while loading: "+componentPath );
                    internalLogError( ne );
                    return null;
                }
                result=getConfigLoader().fillObject(result, componentPath, this, null );
                String name= pathName.get(pathName.size()-1);
                javax.naming.Name parentContextName=pathName.getPrefix(pathName.size()-1);
                BubbleContext parentCtx;
                if ( parentContextName.size()==0)
                    parentCtx=this;
                else{
                    try{
                        parentCtx=(BubbleContext)lookup(parentContextName);
                    }catch(javax.naming.NamingException nae){

                        internalLogError(nae);
                        return null;
                    }
                }

                try{
                    startObject( result, parentCtx, name,
                                                    getConfigLoader().getConfigEntry(componentPath)  );
                }catch(Exception ne ){
                    internalLogError( "Error while loading: "+componentPath );
                    internalLogError( ne );
                    return null;
                }
                //if ( result instanceof ServiceListener )
                //    ((ServiceListener)result).startService();
            }catch(WDException iwe ){
                internalLogError( "Error while loading: "+componentPath );
                internalLogError( iwe );
            }


        }

        return result;
    }

    /*protected void startObject(Object obj, BubbleContext parentContext, String name, ConfigEntry conf) throws Exception{
        super.startObject( obj, parentContext, name, conf );
    }*/
    public Object resolveComponentReference(String componentPath, String scope, Object[] resolverArgs){
        //BubbleContext context=
        //    (resolverArgs != null && resolverArgs .length>0)?(BubbleContext)resolverArgs[0]:null;

        //System.out.println("resolveComponentReference====>"+componentPath );
        //return resolveName( componentPath, context, true );
        return resolveName( componentPath, true );
    }
    public String getScopeId(){
        return (scopeManager==null)?BubbleScopeManager.DEFAULT_SCOPE:getScopeManager().getScopeId();
    }


    public ConfigLoader getConfigLoader(){
        return (scopeManager==null)?null:scopeManager.getConfigLoader();
    }
    public ConfigEntry getConfigEntry(String componentPath) throws WDException{
        ConfigLoader loader=getConfigLoader();
        if ( loader!=null)
            return loader.getConfigEntry(componentPath);
        else
            return null;
    }


}