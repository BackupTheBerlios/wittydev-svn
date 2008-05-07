package org.wittydev.bubble;
import javax.naming.Context;
import javax.naming.NamingException;
import javax.naming.Name;
import javax.naming.CompositeName;

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

public class BubbleScopeManager extends BubbleContext {//implements ObjectResolver{
    public static final String DEFAULT_SCOPE="general";
    String scope;
    ScopeBubbleContext parentScope;
    public BubbleScopeManager() {
        super(null);
    }
    public BubbleScopeManager(  String scope,
                                BubbleContext parent,
                                ScopeBubbleContext parentScope ) {
        super(parent);
        this.scope=scope;
        this.parentScope = parentScope;
        if ( parentScope==null ) new NullPointerException("A BubbbleScopeManager must have a ScopeBubbleContext Object.");
    }



    public Object resolveName(  String componentPath,
                                boolean create ){

        return parentScope.resolveName( componentPath, create );
    }



    public ScopeBubbleContext getParentScope(){
        return parentScope;
    }

    public void setParentScope(ScopeBubbleContext parentScope){
        this.parentScope=parentScope;
    }

    public String getScopeId(){
        return (this.scope==null)?DEFAULT_SCOPE:this.scope;
    }
    public void setScopeId( String scope){
        this.scope=scope;
    }
    public Context createSubcontext(Name name) throws NamingException{
        Context ctx= new ScopeBubbleContext( name.get(0), this);
        bind(name, ctx);
        //startObjectIW(name.get(0), ctx );
        return ctx;
    }

    public ScopeBubbleContext getScopeBubbleContext(String inScopeId, boolean create){
        ScopeBubbleContext result=null;
        try{
            result=(ScopeBubbleContext)lookup( inScopeId);
        }catch(javax.naming.NamingException ne){}
        //return (ScopeBubbleContext)resolveName(inScopeId, create);
        if ( result==null && create) {
            //result = new ScopeBubbleContext( inScopeId, this);
            try{
                createSubcontext( new CompositeName(inScopeId) )  ;
                //bind( inScopeId, result ) ;
            }catch(javax.naming.NamingException ne){
                internalLogError(ne);
                result=null;
            }
        }

        return result;
    }

    public void bind(String name, Object obj) throws javax.naming.NamingException{
        if ( !(obj instanceof ScopeBubbleContext ) ){
            javax.naming.NamingException e= new javax.naming.NamingException("BubbleScopeManager should only have ScopeBubbleContext children!" +name);
            throw e;
        }
        super.bind(name, obj);
    }
    public ConfigLoader getConfigLoader(){
        //return (parentScope==null)?null:parentScope.getConfigLoader();
        return parentScope.getConfigLoader();
    }
    public ConfigEntry getConfigEntry(String componentPath) throws WDException {
        ConfigLoader loader=getConfigLoader();
        if ( loader!=null)
            return loader.getConfigEntry(componentPath);
        else
            return null;
    }


}