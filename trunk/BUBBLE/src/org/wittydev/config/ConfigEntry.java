package org.wittydev.config;
import java.util.Dictionary;
import java.util.Map;
import java.util.Vector;

import java.lang.reflect.Method;

import org.wittydev.core.WDException;
import org.wittydev.logging.LoggingService;
import org.wittydev.util.OrderedMap;

/**
 * Title:
 * Description:
 * Copyright:    Copyright (c) 2002
 * Company:
 * @author
 * @version 1.0
 */

public class ConfigEntry {
    final static String[][] SEPCIAL_REPLACE_STRINGS = new String[][]{{"\\=", "="}, {"\\:", ":"}};
    //String resultingConfig;
    String name;
    ConfigPropertiesFile[] configFiles;
    long loadedTime;
    OrderedMap props;

    Class type;
    String scopeType;

    protected ConfigEntry(){}
    public ConfigEntry(String name, ConfigPropertiesFile[] cfs) throws WDException {
        this.configFiles=cfs;
        this.name=name;
        load();
    }

    protected synchronized void load( ) throws WDException{
        //resultingConfig ="";
        String dummy="";
        props= new OrderedMap();
        // Metto tutte le configurazioni in una stringa sola
        for ( int i=0; i<configFiles.length; i++){
            dummy+=configFiles[i].getContent()+"\n";
        }



        Vector v=new Vector();
        //System.out.println(dummy);
        boolean append=false;
        for ( int i=0, lastPos=0; i<dummy.length();i++){
            if ( dummy.charAt(i)==13 || dummy.charAt(i)==10 ){
                //System.out.println(i + "=>"+(byte)dummy.charAt(i) + "=>"+lastPos );
                String str=null;
                if ( lastPos<i){
                    str=dummy.substring(lastPos, i).trim();
                    boolean newAppend=false;
                    if ( str.length()>0) {
                        String real= (newAppend=(str.charAt(str.length()-1)=='\\'))?str.substring(0, str.length()-1).trim():str;
                        if ( append ){
                            v.setElementAt( v.elementAt(v.size()-1) +real, v.size()-1);
                        }else
                            v.add( real);
                    }

                    append=newAppend;
                }
                lastPos=i+1;
            }

        }

        for ( int i=0; i<v.size(); i++){
            String str=(String)v.elementAt(i);
            if ( str.length()==0 || str.charAt(0)=='#')continue;

            int eqPos = str.indexOf("=");
            if ( eqPos<1 ) { // come primo charattere non vale evidemente
                LoggingService.getDefaultLogger().logWarning( this, "Invalid property row: "+str );
                continue;
            }
            String name;
            byte eqType=0;
            String val=str.substring( eqPos+1).trim();
            if ( str.charAt(eqPos-1)=='+' ){
                name=str.substring( 0, eqPos-1).trim();
                eqType=1;
            }else if ( str.charAt(eqPos-1)=='^' ){
                name=str.substring( 0, eqPos-1).trim();
                eqType=2;
            }else if ( str.charAt(eqPos-1)=='@' ){
                name=str.substring( 0, eqPos-1).trim();
                eqType=3;
            }else if ( str.charAt(eqPos-1)=='/' ){
                name=str.substring( 0, eqPos-1).trim();
                eqType=0;
            }else {
                name=str.substring( 0, eqPos-0).trim();
                eqType=0;
            }

            ConfigProperty pInfo=(ConfigProperty)props.get(name);

            if ( pInfo ==null ){
                pInfo=new ConfigProperty();
                pInfo.name=name;
                pInfo.stringValue=val;
                //pInfo.ref=(eqType==2);
                switch ( eqType ){
                    case 0: case 1: pInfo.valueType=ConfigProperty.TYPE_VALUE; break;
                    case 2: pInfo.valueType=ConfigProperty.TYPE_REF; break;
                    case 3: pInfo.valueType=ConfigProperty.TYPE_JNDI; break;
                }
                props.put(name, pInfo);
            }else{
                //pInfo.ref=(eqType==2);
                /*if ( pInfo.ref ) {
                    pInfo.ref=false;
                    pInfo.stringValue=null;
                }*/
                if ( pInfo.isReference() || pInfo.isJNDIReference() )
                    pInfo.stringValue=null;

                if ( eqType == 0 || eqType == 2 || eqType == 3)
                    pInfo.stringValue=val;
                else if ( eqType == 1 )
                    pInfo.stringValue=
                        (pInfo.stringValue==null && pInfo.stringValue.length()==0)?val:pInfo.stringValue+","+val;
                //pInfo.ref=(eqType==2);
                switch ( eqType ){
                    case 0: case 1: pInfo.valueType=ConfigProperty.TYPE_VALUE; break;
                    case 2: pInfo.valueType=ConfigProperty.TYPE_REF; break;
                    case 3: pInfo.valueType=ConfigProperty.TYPE_JNDI; break;
                }

            }
        }
        //for ( Enumeration e=props.

        //System .out.println(props);
        ConfigProperty pInfo = (ConfigProperty)props.remove("$class");
        if ( pInfo==null || pInfo.getStringValue()==null )
            throw new WDException(WDException.CODE_SYSTEM_EXCEPTION, "No $class defined ["+name+"]" );

        try{
            this.type=Class.forName( pInfo.getStringValue() );
        }catch(ClassNotFoundException cne){
            throw new WDException(WDException.CODE_SYSTEM_EXCEPTION, "Class not found ["+name+"]: "+pInfo.getStringValue());
        }

        pInfo = (ConfigProperty)props.remove("$scope");
        String scopeType = (pInfo==null||pInfo.getStringValue()==null)?
                                                "global":pInfo.getStringValue().toLowerCase();

        if ( !scopeType.equals("global") && !scopeType.equals("session") && !scopeType.equals("request") )
            throw new WDException(WDException.CODE_SYSTEM_EXCEPTION, "Invalid $scope type ["+name+"]: "+scopeType );
        else
            this.scopeType = scopeType;


        Method[] methods = type.getMethods();
        //for ( int i =0; i<props.size(); i++){
        for ( int i =props.size()-1; i>=0; i--){
            ConfigProperty pI=(ConfigProperty)props.entryAt(i);
            String propName="set" + (pI.getName().charAt(0)+"").toUpperCase() + pI.getName().substring(1) ;
            boolean done=false;
            for ( int j=0; j< methods.length ; j++){
                if ( methods[j].getParameterTypes().length==1 && methods[j].getName().equals(propName) ){
                    if ( done ) throw new WDException (WDException.CODE_SYSTEM_EXCEPTION, "Invalid class, only one setter is allowed: ["+
                                                    type.getName()+"."+propName +"(...)]");
                    pI.type = methods[j].getParameterTypes()[0];
                    pI.setterMethod=methods[j];
                    done=true;
                }
            }
            if ( !done ) {
                LoggingService.getDefaultLogger().logWarning(this,
                                    "setter ["+propName+"] not found in ["+type.getName()+"]"
                                    );

                props.remove(pI.getName());
            }else{
                if (
                    java.util.Dictionary.class.isAssignableFrom( pI.type ) ||
                    java.util.List.class.isAssignableFrom( pI.type ) ||
                    java.util.Set.class.isAssignableFrom( pI.type ) ||
                    java.util.Map.class.isAssignableFrom( pI.type ) ) {
                    String str=pI.getStringValue();
                    if (str!=null){
                        pI.stringValue=replace(str, SEPCIAL_REPLACE_STRINGS);
                    }

                }
            }
        }

        loadedTime= System.currentTimeMillis();
    }

    public static String replace(String dummy, String[][] repl){
        String result= dummy;
        for (int i=0; i<repl.length; i++){
            int pos=0;
            while( (pos=result.indexOf( repl[i][0], pos) ) >=0 ){
                result=result.substring(0, pos)+repl[i][1]+result.substring(pos+repl[i][0].length() );
                pos+=repl[i][1].length();
            }
        }
        return result;
    }



    public int getConfigPropertiesCount(){
        return props.size();
    }

    public ConfigProperty getConfigProperty( int idx ){
        return (ConfigProperty)props.entryAt( idx );
    }

    public ConfigProperty getConfigProperty( String name ){
        return (ConfigProperty)props.get( name );
    }


    /*public String getResultingConfig(){
        return resultingConfig;
    }*/

    public String getName(){
        return this.name;
    }
    public String getScope(){
        return this.scopeType;
    }
    public Class getType(){
        return this.type;
    }
    public long getLoadedTime(){
        return this.loadedTime;
    }

    public ConfigPropertiesFile[] getConfigFiles(){
        return this.configFiles;
    }
    public Map getPropertiesConf(){
        return this.props;
    }

    /*public static void main (String[] args){
        String bau="DERIVATIVEFIFO\\=pkg_dyn_views.CUR_DERIVATIVE_PL, SEARCHPORTFOLIOID\\=PKG_DYN_PORTFOLIO.PR_PORTFOLIO_ID, PORTFOLIO\\=PKG_DYN_PORTFOLIO.PR_PORTFOLIO_COMPOSITION, DERIVATIVEDISPO\\=pkg_dyn_views.CUR_DERIVATIVES_DISPO";
            //System.out.println(bau.replaceAll("\\=", "="));
            System.out.println( replace(bau, SEPCIAL_REPLACE_STRINGS));
    }*/
}