package org.wittydev.bubble;
import javax.naming.Name;
import javax.naming.Binding;
import javax.naming.NamingException;
import javax.naming.Context;
import java.util.Hashtable;
import javax.naming.NamingEnumeration;
import javax.naming.NameParser;
import javax.naming.CompositeName;
import javax.naming.NameNotFoundException;
import javax.naming.InvalidNameException;
import javax.naming.NameAlreadyBoundException;
import javax.naming.NotContextException;
import  java.util.Enumeration;
import  javax.naming.NameClassPair;

import org.wittydev.config.ConfigEntry;
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

public class BubbleContext implements BubbleNameContext, BubbleService, BubbleServiceListener {
//, java.io.Serializable

    Hashtable children=new Hashtable();
    NameParser nameParser =new BubbleNameParser();

    String nameInNameSpace;

    protected ConfigEntry configEntry;

    protected BubbleContext parent;
    /*public BubbleContext(String nameInNameSpace){
        this.nameInNameSpace=nameInNameSpace;
    }*/
    public BubbleContext(){}
    public BubbleContext(BubbleContext parent){
        this.parent=parent;
    }

    public Object lookup(Name name) throws NamingException{
        return lookup_0(name, true);
    }
    public Object lookup_0(Name name, boolean errOnNotFound) throws NamingException{
        //name=cleanName(name);

        name=cleanName(name);
        if (name.isEmpty()) {
            // Asking to look up this context itself. Create and return
            // a new instance with its own independent environment.
            //return (new BubbleContext(this.parent));
            return this;
        }
        Object nextComponent=children.get(name.get(0));
        if ( nextComponent==null ) {

            if ( errOnNotFound )
                throw new NameNotFoundException(name + " not found [0]");
            else
                return null;
        }
        if ( name.size() == 1 )
            return nextComponent;
        else if ( !(nextComponent instanceof Context ) ){
            if ( errOnNotFound )
                throw new NameNotFoundException(name + " not found [1]");
            else
                return null;
        }else{
            name=(Name)name.clone();
            name.remove(0);
            return  ((BubbleContext)nextComponent).lookup_0( name, errOnNotFound );
        }


        /*Object answer = null;//bindings.get(name);
        if (answer == null) throw new NameNotFoundException(name + " not found");

        return answer;*/
    }
    public void bind(Name name, Object obj) throws NamingException{
        name=cleanName(name);
        if (name.isEmpty() )
            throw new InvalidNameException("Cannot bind empty name");

        if (lookup_0( name, false) != null)
            throw new NameAlreadyBoundException("Use rebind to override ["+name+"]");

        if ( name.size() == 1 ){
            //startObjectIW(name.get(0), obj);
            /*if ( obj instanceof BubbleServiceListener ){
                try{
                    ((BubbleServiceListener)obj).startService();
                }catch(WDException iwe){
                    internalLogWarning("Not bound "+name.get(0));
                    throw new NamingException(iwe.getMessage() );
                }
            }*/
            children.put( name.get(0), obj );
            if ( obj instanceof BubbleContext ) ((BubbleContext)obj).setParent(this);
        }else{
            Object child=children.get(name.get(0));
            //BubbleContext childCtx;
            Context childCtx;
            if ( child==null ){
                //childCtx=new BubbleContext(this);
                //children.put( name.get(0),  childCtx);
                childCtx=createSubcontext( name.get(0) );
                //startObjectIW(obj, this, name.get(0), null);
                startObjectIW(childCtx, this, name.get(0), null);
                /*if ( childCtx instanceof BubbleServiceListener ){
                    try{
                        ((BubbleServiceListener)childCtx).startService();
                    }catch(WDException nfje){
                        internalLogWarning("Not bound "+name.get(0));
                        throw new NamingException(nfje.getMessage() );
                    }
                }*/
            //}else if ( child instanceof BubbleContext )
            //    childCtx=(BubbleContext)child;
            }else if ( child instanceof Context )
                childCtx=(Context)child;
            else
                throw new NamingException ("Invalid path, some path component is already defined as object: "+name);
            name.remove(0);
            childCtx.bind( name, obj );
        }
        //bindings.put(name, obj);
    }
    public void rebind(Name name, Object obj) throws NamingException{
        name=cleanName(name);
        if (name.isEmpty() )
            throw new InvalidNameException("Cannot bind empty name");
        if ( name.size() == 1 )
            children.put( name.get(0), obj );
        else{
            Context childCtx=(Context)children.get(name.get(0));
            name=(Name)name.clone();
            name.remove(0);
            childCtx.rebind( name, obj );
        }
        //bindings.put(name, obj);
    }
    public void unbind(Name name) throws NamingException{
        name=cleanName(name);
        if (name.isEmpty() ) {
        throw new InvalidNameException("Cannot unbind empty name");
        }
        Object removed;
        if ( name.size() == 1 ){
            removed=children.remove( name.get(0));
            stopObject(removed);
        }else{
            Context childCtx=(Context)children.get(name.get(0));
            if ( childCtx==null ) return;
            name=(Name)name.clone();
            name.remove(0);
            childCtx.unbind( name);
        }

        //bindings.remove(name);
    }
    public void rename(Name oldName, Name newName) throws NamingException{
        oldName=cleanName(oldName);
        newName=cleanName(newName);
        if (oldName .isEmpty() || newName .isEmpty()) {
        throw new InvalidNameException("Cannot rename empty name");
        }
        // Check if new name exists
        //if (bindings.get(newname) != null) {
        if (lookup_0( newName, false ) != null)
            throw new NameAlreadyBoundException(newName  +" is already bound");

        // Check if old name is bound
        Object oldBinding = lookup_0( oldName, false);//bindings.remove(oldname);
        if (oldBinding == null)
            throw new NameNotFoundException(oldName  + " not bound");
        unbind (oldName);
        bind(newName, oldBinding);

        /*if ( name.size() == 1 )
            children.put( newName.get(0), oldBinding);
        else{
            Context childCtx=(Context)children.get(name.get(0));
            if ( childCtx==null ) return;
            name=(Name)((Name)name.clone()).remove(0);
            childCtx.unbind( name);
        }*/

        //bindings.put(newname, oldBinding);
    }
    public NamingEnumeration list(Name name) throws NamingException{
        //return null;
        name=cleanName(name);
        if (name.isEmpty()) {
            // listing this context
            return new BubbleNames(children.keys());
        }
        // Perhaps "name" names a context
        Object target = lookup(name);
        if (target instanceof Context) {
            return ((Context)target).list("");
        }
        throw new NotContextException(name + " cannot be listed");
    }
    public NamingEnumeration listBindings(Name name) throws NamingException{
        name=cleanName(name);
        if (name.isEmpty()) {
            // listing this context
            return new BubbleBindings(children.keys());
        }
        // Perhaps "name" names a context
        Object target = lookup(name);
        if (target instanceof Context) {
        return ((Context)target).listBindings("");
        }
        throw new NotContextException(name + " cannot be listed");
    }


    public void destroySubcontext(Name name) throws NamingException{
        unbind(name);
    }
    public Context createSubcontext(Name name) throws NamingException{
        Context ctx= new BubbleContext(this);
        bind(name, ctx);
        return ctx;
    }

    public Object lookupLink(Name name) throws NamingException{
        // Does not treat links specially
        return lookup(name);
    }
    public NameParser getNameParser() throws NamingException{
        return nameParser;
    }
    public NameParser getNameParser(Name name) throws NamingException{
        //return myParser;
        return nameParser;
    }
    public Name composeName(Name name, Name prefix) throws NamingException{
        //return null;
        return prefix.addAll(name);
    }
    public String composeName(String name, String prefix)	    throws NamingException{
        //return composeName(name, new CompositeName(prefix));
        return prefix + name;
    }
    public Object addToEnvironment(String propName, Object propVal)	throws NamingException{
        return null;
    }
    public Object removeFromEnvironment(String propName)	throws NamingException{
        return null;
    }
    public Hashtable getEnvironment() throws NamingException{
        return null;
    }
    public void close() throws NamingException{
        //children=null;
    }
    public String getNameInNamespace() throws NamingException{
        return nameInNameSpace;
    }

    public String getAbsoluteName() throws NamingException{
        if ( getParent() ==null ) return getNameInNamespace();
        return getParent().getAbsoluteName()+"/"+getNameInNamespace();

    }

    protected Name cleanName(Name name) throws InvalidNameException {
        name=(Name)name.clone();
        if ( !name.isEmpty() && name.get(0).length()==0)name.remove(0);
        if ( !name.isEmpty() && name.get(name.size()-1).length()==0)name.remove(name.size()-1);
        return name;
    }





    public Object lookup(String name) throws NamingException{
        return lookup(getNameParser().parse(name));
    }
    public Object lookup_0(String name, boolean errOnNotFound) throws NamingException{
        return lookup_0(getNameParser().parse(name), errOnNotFound);
    }
    public NamingEnumeration listBindings(String name) throws NamingException{
        return listBindings(getNameParser().parse(name));
    }
    public void destroySubcontext(String name) throws NamingException{
        destroySubcontext( getNameParser().parse(name));
    }
    public Context createSubcontext(String name) throws NamingException{
        return createSubcontext( new CompositeName (name));
    }
    public Object lookupLink(String name) throws NamingException{
        return lookupLink( new CompositeName (name));
    }
    public NameParser getNameParser(String name) throws NamingException{
        return nameParser;
    }
    public void bind(String name, Object obj) throws NamingException{
        bind( getNameParser().parse(name), obj );
    }
    public void rebind(String name, Object obj) throws NamingException{
        rebind( getNameParser().parse(name), obj );
    }
    public void unbind(String name) throws NamingException{
        unbind( getNameParser().parse(name) );
    }
    public void rename(String oldName, String newName) throws NamingException{
        rename( new CompositeName(oldName ), new CompositeName(newName) );
    }
    public NamingEnumeration list(String name) throws NamingException{
        return list(getNameParser().parse(name));
    }
    public void internalLogError(Throwable t){
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

    public boolean isLoggingDebug(){
        return LoggingService.getDefaultLogger().isLoggingDebug();
    }
    public boolean isLoggingWarning(){
        return LoggingService.getDefaultLogger().isLoggingWarning();
    }
    public boolean isLoggingError(){
        return LoggingService.getDefaultLogger().isLoggingError();
    }
    public boolean isLoggingInfo(){
        return LoggingService.getDefaultLogger().isLoggingInfo();
    }

    public BubbleContext getParent() {
        return this.parent;
    }
    public void setParent(BubbleContext parent) {
        this.parent=parent;
    }
    public BubbleContext getRootContext() {
        //return lookup_0();
        BubbleContext ctx=parent;
        if ( ctx==null)
            ctx=this;
        else{
            while ( ctx!=null && ctx.getParent()!=null ){
                ctx=ctx.getParent();
            }
        }
        return ctx;
    }

	protected boolean bcStarted;
    public void startService(BubbleServiceEvent event) throws WDException{
        if (event!=null) {
            nameInNameSpace=event.getName();
            if ( event.getParentContext()!= null && getParent()!=null &&
                 event.getParentContext()!= getParent()   )
                internalLogWarning("Invalid operation: Trying to set parent to non-orfan context!");
            else if ( getParent()==null && event.getParentContext()!=null )
                setParent( event.getParentContext() );

            if ( configEntry==null )this.configEntry=event.getConfigEntry();

        }

        for ( Enumeration e= children.keys(); e.hasMoreElements(); ){
            //try{
            String key=(String)e.nextElement();
            Object obj= children.get(key);//e.nextElement();
            if ( obj instanceof BubbleServiceListener  ){
                BubbleServiceEvent childEvent=null;
                if (event!=null)
                    childEvent=new BubbleServiceEvent(  event.getService(),
                                                        this,
                                                        key,
                                                        null );
                ((BubbleServiceListener)obj).startService(childEvent);
            }
            //}catch(Exception ex){}
        }
        bcStarted=true;
    }
	public void stopService(){
        bcStarted=false;
        for ( Enumeration e= children.elements(); e.hasMoreElements(); ){

            Object obj=e.nextElement();
            if ( obj instanceof BubbleService){
                ((BubbleServiceListener)obj).stopService();
            }
        }

    }

	public ConfigEntry getConfiguration(){
        return configEntry;
    }
	public boolean isRunning(){
        return this.bcStarted;
    }

    protected void stopObject(Object obj){
        if ( obj==null) return;
        if ( obj instanceof BubbleServiceListener ){
            if ( obj instanceof BubbleService && ((BubbleService)obj).isRunning() )
                ((BubbleServiceListener)obj).stopService();
        }
    }

    protected void startObjectIW(Object obj, BubbleContext parentContext, String name, ConfigEntry conf) throws NamingException{
        try{
            startObject(obj, parentContext, name, conf);
        }catch(WDException iwe){
            internalLogWarning("Not bound "+name);
            throw new NamingException(iwe.getMessage() );
        }catch(Exception e){
            internalLogWarning(e);
            internalLogWarning("Not bound "+name);
            throw new NamingException(e.getMessage() );
        }
    }

    protected void startObject(Object obj, BubbleContext parentContext, String name, ConfigEntry conf) throws Exception{
        if (isLoggingDebug())
            internalLogDebug("Starting Bean "+name);
        if ( getParent()!=null && getParent() != this ){
        // if (getRootContext() !=null)///hmmm quale e' meglio?
            // In questo modo mi assicuro che e' il contesto di root che esegue l'operazione
            getParent().startObject( obj, parentContext, name, conf );
        }else{
            if ( obj==null) return;
            if ( obj instanceof BubbleServiceListener ){
                if (  !(obj instanceof BubbleService) || !((BubbleService)obj).isRunning() ){
                    BubbleServiceEvent event = new BubbleServiceEvent(this, parentContext, name, conf);
                    ((BubbleServiceListener)obj).startService(event);
                }
            }
        }
    }

    public class BubbleNames implements javax.naming.NamingEnumeration{
        Enumeration _enum;
        public BubbleNames(Enumeration _enum){
            this._enum=_enum;
        }
        public boolean hasMoreElements(){
            return _enum.hasMoreElements();
        }

        public Object nextElement(){
            String name = (String)_enum.nextElement();
            String className = children.get(name).getClass().getName();
            return new NameClassPair(name, className);
        }
        public Object next() throws NamingException{
            return nextElement() ;
        }
        public boolean hasMore() throws NamingException{
            return _enum.hasMoreElements();
        }
        public void close() throws NamingException{
        }

    }


    class BubbleBindings implements NamingEnumeration {
        Enumeration _enum;
        BubbleBindings (Enumeration _enum) {
            this._enum = _enum;
        }
        public boolean hasMoreElements() {
            return _enum.hasMoreElements();
        }
        public boolean hasMore() throws NamingException {
            return hasMoreElements();
        }
        public Object nextElement() {
            String name = (String)_enum.nextElement();
            return new Binding(name, children.get(name));
        }
        public Object next() throws NamingException {
            return nextElement();
        }
        public void close() {
        }
    }
   /*public static void main(String[] args) throws Exception{
        BubbleContext ctx=new BubbleContext(null);
        String path="aa/bb/cc";
        ctx.bind( path, "bau" );
        String path2="aa";
        for ( Enumeration e=ctx.list( path2 ) ; e.hasMoreElements();){
            System.out.println("==>"+e.nextElement());
        }
    }*/
    /*public static void main_0(String[] args) throws Exception{
        CompositeName name= new CompositeName("/aa/bb/cc/d");
        System.out.println(
                    name.get(0)
                    );

        System.out.println(
                    name.addAll(2, new CompositeName("ddd/eee") )
                    );
        System.out.println(
                    new CompositeName ("").getAll().hasMoreElements()
                    );


    }*/
    /*public static void main (String[] args){
        System.out.println(new java.util.Date(1092141291000l));
    }*/
}