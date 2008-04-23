package org.wittydev.bubble;
import javax.naming.InitialContext;

import org.wittydev.config.ConfigEntry;
import org.wittydev.config.ConfigLoader;
import org.wittydev.config.ObjectResolver;
import org.wittydev.core.WDException;
import org.wittydev.logging.LoggingService;



/**
 * Title:
 * Description:
 * Copyright:    Copyright (c) 2002
 * Company:
 * @author
 * @version 1.0
 */

public class Architect extends  ScopeBubbleContext {//BubbleContext implements ObjectResolver{
    public static final byte DO_NOT_REGISTER_JNDI_NAMESPACE=0;  // No JNDI
    public static final byte REGISTER_JNDI_NAMESPACE=1;         // bubble:  is a domain
    public static final byte REGISTER_JNDI_NAMESPACE_AS_SUBCONTEXT_DOMAIN=2;   // bubble:/Architect is a subcontext
    public static final byte REGISTER_JNDI_NAMESPACE_AS_ROOT_SUBCONTEXT=3;       // /bubble  is a root subcontext

    ConfigLoader confLoader;
    private String configPath;
    private InitialContext iCtx;
    private String pathSeparator;
    private String[] initialServices;
    //  boolean jndiNamespaceIsSubContext=false;
    byte jndiNamespaceType=DO_NOT_REGISTER_JNDI_NAMESPACE;
    byte lastJndiNamespaceRegistrationType;

    public static final String CONFIG_PATH_KEY="CONFIG_PATH";
    public static final String NAME_SPACE="bubble";

    javax.naming.NameParser nameParser;
    public javax.naming.NameParser getNameParser() throws javax.naming.NamingException{
        if (nameParser==null) nameParser=new BubbleNameParser(new String []{NAME_SPACE});
        return nameParser;
    }
    public javax.naming.NameParser getNameParser(javax.naming.Name name) throws javax.naming.NamingException{
        return getNameParser();
    }

    public Architect() {
        super(BubbleScopeManager.DEFAULT_SCOPE, null);
    }

    public void clearConfigCache(){
        if ( confLoader!=null )confLoader.clearCache();
    }

    /*public void setConfigCacheExpirationMillis(){
        if ( confLoader!=null )confLoader.clearCache();
    }*/


    public void stopService() {
        super.stopService();
        unregisterJNDI();
    }

    public String getAbsoluteName() throws javax.naming.NamingException{
        if ( getParent() ==null )
            return "";
        else
            return super.getAbsoluteName();
    }
    public void startService() throws WDException{
        BubbleServiceEvent event= new BubbleServiceEvent(
                                                this, null,
                                                "",//getArchitectComponentPath(),
                                                null );
        startService(event);
    }
    public void startService(BubbleServiceEvent event) throws WDException{
        loadConfig();
        loadMe();
        registerJNDI();
        loadInitialServices();


        //str=System.getProperty(InitialContext.INITIAL_CONTEXT_FACTORY);
        super.startService(event);
    }

    private void loadMe() throws WDException{
        ConfigEntry ce=getConfigEntry(getArchitectComponentPath());
        if ( ce!=null ){
            super.configEntry=ce;
            getConfigLoader().fillObject( this, getArchitectComponentPath(), this, null);
        }
    }
    private void loadInitialServices(){
        if ( initialServices !=null){
            for ( int i=0; i<initialServices.length; i++ ){
                resolveName(initialServices[i], true);
            }
        }
    }
    /*protected javax.naming.Name cleanName(javax.naming.Name name) throws InvalidNameException {
        name=super.cleanName(name);
        if (name==null || name.size()==0)return name;
        String dummy=name.get( 0 );

    }*/

    public void setInitalServices(String[] initialServices ){
        this.initialServices =initialServices;
    }
    public String[] getInitalServices(){
        return this.initialServices ;
    }
    /*public boolean getJndiNamespaceIsSubContext(){
        return this.jndiNamespaceIsSubContext;
    }
    public void setJndiNamespaceIsSubContext(boolean jndiNamespaceIsSubContext){
        this.jndiNamespaceIsSubContext=jndiNamespaceIsSubContext;
    }*/
    public byte getLastJndiNamespaceRegistrationType(){
        return this.lastJndiNamespaceRegistrationType;
    }
    public byte getJndiNamespaceType(){
        return this.jndiNamespaceType;
    }
    public void setJndiNamespaceType(byte jndiNamespaceType){
        this.jndiNamespaceType=jndiNamespaceType;
    }

    private void loadConfig(){
        confLoader=new ConfigLoader();
        String cp=getConfigPath();
        if ( cp !=null ) confLoader.setConfigPath( cp );
        if ( pathSeparator !=null ) confLoader.setPathSeparator( pathSeparator  );
    }

    private void registerJNDI(){
        /*try{
            bind(getArchitectComponentPath(), this);
        }catch(javax.naming.NamingException e){
            internalLogInfo(e);
        }*/

        switch (jndiNamespaceType ){
        case REGISTER_JNDI_NAMESPACE_AS_ROOT_SUBCONTEXT:
        case REGISTER_JNDI_NAMESPACE_AS_SUBCONTEXT_DOMAIN:
            String nameSpace;
            if ( jndiNamespaceType==REGISTER_JNDI_NAMESPACE_AS_SUBCONTEXT_DOMAIN)
                nameSpace=NAME_SPACE+":";
            else //if ( jndiNamespaceType==REGISTER_JNDI_NAMESPACE_AS_ROOT_SUBCONTEXT)
                nameSpace="/"+NAME_SPACE;
            try{
                javax.naming.Context ctx=new InitialContext();
                ctx=ctx.createSubcontext(nameSpace);
                ctx.bind(getArchitectComponentPath(), this);
                /*if ( jndiNamespaceType==REGISTER_JNDI_NAMESPACE_AS_SUBCONTEXT_DOMAIN)
                    ctx.bind(":", this);
                else
                    ctx.bind(getArchitectComponentPath() , this);*/

            }catch(Exception e){
                e.printStackTrace();
            }
            internalLogInfo("JNDI name space '"+nameSpace+"' added as SubContext!");
            break;
        case REGISTER_JNDI_NAMESPACE:
            //String pkg=getClass().getPackage().getName();
            /*String str=System.getProperty(InitialContext.URL_PKG_PREFIXES);

            //String pkg=bubbleURLContextFactory.class.getPackage().getName();
            String pkg=getClass().getPackage().getName();
            if ( str==null )
                System.setProperty( InitialContext.URL_PKG_PREFIXES, pkg);
            else if (  !str.endsWith(pkg) && str.indexOf(pkg+":")<0){
                str += ":"+pkg;
                System.setProperty( InitialContext.URL_PKG_PREFIXES, str);
            }*/
            String str="";
            String pkg=getClass().getPackage().getName();
            try{
                javax.naming.InitialContext ctx=new InitialContext();
                if ( ctx.getEnvironment()!=null ){
                    str=(String)ctx.getEnvironment().get(InitialContext.URL_PKG_PREFIXES);
                }
                if ( str==null )
                    ctx.addToEnvironment( InitialContext.URL_PKG_PREFIXES, pkg);
                else if (  !str.endsWith(pkg) && str.indexOf(pkg+":")<0){
                    str += ":"+pkg;
                    ctx.addToEnvironment( InitialContext.URL_PKG_PREFIXES, str);
                }

            }catch(Exception e){
                e.printStackTrace();
            }

            internalLogInfo("JNDI name space '"+NAME_SPACE+"' registered with URLContextFactory!");
            break;
        default:
            internalLogInfo("JNDI name space '"+NAME_SPACE+"' not registered!");
        }
        lastJndiNamespaceRegistrationType=jndiNamespaceType;
    }
    private void unregisterJNDI(){
        //if ( jndiNamespaceIsSubContext ){
        switch (lastJndiNamespaceRegistrationType){
        case REGISTER_JNDI_NAMESPACE_AS_ROOT_SUBCONTEXT:
        case REGISTER_JNDI_NAMESPACE_AS_SUBCONTEXT_DOMAIN:
            String nameSpace;
            if ( jndiNamespaceType==REGISTER_JNDI_NAMESPACE_AS_SUBCONTEXT_DOMAIN)
                nameSpace=NAME_SPACE+":";
            else //if ( jndiNamespaceType==REGISTER_JNDI_NAMESPACE_AS_ROOT_SUBCONTEXT)
                nameSpace="/"+NAME_SPACE;
            try{
                javax.naming.Context ctx=new InitialContext();
                //javax.naming.Context bCtx=(javax.naming.Context)ctx.lookup(NAME_SPACE+":");
                //bCtx.unbind(getArchitectComponentPath());
                ctx.destroySubcontext(nameSpace);
            }catch(javax.naming.NamingException ne){
            }catch(Exception e){
                internalLogError(e);
            }
            internalLogInfo("JNDI name space '"+NAME_SPACE+"' destroyed!");
            break;
        }
        //}
    }

    public Object resolveName( String componentPath ){
        return resolveName(componentPath, true );
    }
    public Object resolveName( String componentPath, boolean create ){
        return resolveName(componentPath,  this, create );
    }

   /* public Object resolveName(  String componentPath,
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
            result=context.lookup_0( pathName, false );
        }catch(javax.naming.NamingException e){
            internalLogError( "Error while loading: "+componentPath );
            internalLogError( e );
            return null;
        }

        if ( result==null && create ){
            System.out.println(componentPath);
            try{
                //result=confLoader.createNewObject( componentPath, this, null );
                System.out.println("NOT FOUND Creating: ARCH["+getScopeId()+"]"+componentPath);
                result=confLoader.createNewObject( componentPath );
                try{
                    if(result!=null)bind(pathName, result);
                }catch(javax.naming.NamingException ne ){
                    internalLogError( "Error while loading: "+componentPath );
                    internalLogError( ne );
                    return null;
                }


                result=confLoader.fillObject(result, componentPath, this, null   );
                if ( result instanceof BubbleServiceListener ){
                    //javax.naming.event.NamespaceChangeListener listener;

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
                    BubbleServiceEvent event=new BubbleServiceEvent(this, parentCtx, name );
                    ((BubbleServiceListener)result).startService(event);
                }
                //try{
                //    if(result!=null)super.startObject( result );
                //}catch(Exception ne ){
                //    internalLogError( "Error while loading: "+componentPath );
                //   internalLogError( ne );
                //    return null;
                //}

            }catch(WDException iwe ){
                internalLogError( "Error while loading: "+componentPath );
                internalLogError( iwe );
            }


        }

        return result;
    }*/


    public Object resolveComponentReference(String componentPath, String scope, Object[] resolverArgs){
        BubbleContext context=
            (resolverArgs != null && resolverArgs .length>0)?(BubbleContext)resolverArgs[0]:null;

        return resolveName( componentPath, context, true );
    }

    public String getConfigPath(){
        if (configPath==null){
            configPath=System.getProperty(CONFIG_PATH_KEY);
            //configPath=System.getenv(CONFIG_PATH_KEY);
        }
        return configPath;
    }
    public void setConfigPath(String configPath){
        this.configPath=configPath;
    }

    public ConfigLoader  getConfigLoader( ) {
        return confLoader;
    }
    public String getEntryScopeId(String componentPath) throws WDException {
        if ( confLoader==null) return null;
        ConfigEntry conf=confLoader.getConfigEntry(componentPath);
        if (conf==null) return null;
        return conf.getScope();
    }

    public ConfigEntry getConfigEntry( String componentPath  ) throws WDException{
        return (confLoader==null)?null:confLoader.getConfigEntry( componentPath );
    }

    /*javax.naming.Name architectName;
    protected javax.naming.Name getArchitectComponentPathName(){
        if ( architectName ==null){
            try{
                architectName=new javax.naming.CompositeName(getArchitectComponentPath());
                architectName=super.cleanName(architectName);
            }catch(javax.naming.InvalidNameException ine){
                internalLogError(ine);
            }
        }
        return architectName;
    }*/
    protected String getArchitectComponentPath(){
        return "Architect";
    }

/*    protected javax.naming.Name cleanName(javax.naming.Name name) throws javax.naming.InvalidNameException {
        name=super.cleanName(name);

        if (getArchitectComponentPathName()==null || name == null )return name;
System.out.println("aaaa:"+name);
        if ( name.startsWith(getArchitectComponentPathName()) ){
        System.out.println("bbbbb");
            name=super.cleanName(name.getSuffix(name.size()-getArchitectComponentPathName().size()));
        }
        System.out.println("ccc:"+name);
        return name;

    }*/
    public void setLoggingDebug(boolean loggingDebug){
        LoggingService.getDefaultLogger().setLoggingDebug( loggingDebug );
    }
    public void setLoggingInfo(boolean loggingInfo){
        LoggingService.getDefaultLogger().setLoggingInfo( loggingInfo );
    }
    public void setLoggingWarning(boolean loggingWarning){
        LoggingService.getDefaultLogger().setLoggingWarning( loggingWarning );
    }
    public void setLoggingError(boolean loggingError){
        LoggingService.getDefaultLogger().setLoggingError( loggingError );
    }

    /*
    public static void main(String[] args) throws Exception {
        String path="D:\\shared\\pvcs\\IMIWEB_LIBRARIES\\beanworld";
        path+=";D:\\shared\\pvcs\\IMIWEB_LIBRARIES\\beanworld\\it.zip";
        System.setProperty( Architect.CONFIG_PATH_KEY,  path );
        Architect arch = new Architect();
        arch.startService(null);
        //System.out.println(DataTools.arrayToString(tw.configPathArr));
        //ConfigEntry ce=tw.getConfigEntry("/com/nfj/DataStore.properties");
        //ConfigEntry ce=tw.getConfigEntry("com/nfj/a.properties");
        //Thread.currentThread().sleep(1000);
        //System.out.println( "hello:"+ce.props );


        Object objA=arch.resolveName("/com/nfj/test/BeanA", true);
        Object objB=arch.resolveName("/com/nfj/test/ctx2/BeanB", false);
        Object objC=arch.resolveName("/com/nfj/test/ctx2/BeanC", false);
        System.out.println("objA: "+objA);
        System.out.println("objB: "+objB);
        System.out.println("objC: "+objC);
        System.out.println( arch.children  );
        System.out.println("objA.objC: "+((BeanA)objA).getBeanB());
        System.out.println("objA.beanCdummyString: "+((BeanA)objA).getBeanCDummyString());
        System.out.println("objC.dummyString: "+((BeanC)objC).getDummyString());
        System.out.println("objB.beanBStringArr: "+
                    DataTools.arrayToString(((org.nfj.beanworld.test.pkg2.BeanB)objB).getBeanBStringArr()));
        System.out.println("objC.beanBStringArr: "+
                    DataTools.arrayToString(((BeanC)objC).getBeanBStringArr()));

        System.out.println("objB.beanBIntArr: "+
                    DataTools.arrayToString(((BeanB)objB).getBeanBIntArr()));
        System.out.println("objC.beanBIntArr: "+
                    DataTools.arrayToString(((BeanC)objC).getBeanBIntArr()));

        System.out.println("objB.beanBIntObjectArr: "+
                    DataTools.arrayToString(((BeanB)objB).getBeanBIntObjectArr()));
        System.out.println("objC.beanBIntObjectArr: "+
                    DataTools.arrayToString(((BeanC)objC).getBeanBIntObjectArr()));

         BubbleContext    bc=arch;
         //while ( bc!=null && bc.children!=null ){
         //   String key =(String)bc.children.keys().nextElement();
         //   System.out.println("===>"+key+"==>"+bc.children);
         //   if ( bc.children.get(key) instanceof BubbleContext )
         //       bc=(BubbleContext)bc.children.get(key);
         //   else
         //       bc=null;
         //

    }*/
}