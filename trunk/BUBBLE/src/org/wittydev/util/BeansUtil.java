package org.wittydev.util;
import java.util.Dictionary;
import java.util.Map;
import java.util.List;
import java.lang.reflect.InvocationTargetException;

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

public class BeansUtil {

    public static Object resolveValueIW( Object rootObject, String valuePath) throws WDException {
        try{
            return resolveValue( rootObject, valuePath );
        }catch(Exception e){
            String msg="Error while resolving: ["+valuePath+"] on "+rootObject;
            if (LoggingService.getDefaultLogger().isLoggingError()){
                LoggingService.getDefaultLogger().logError(BeansUtil.class, msg);
                LoggingService.getDefaultLogger().logError(BeansUtil.class, e);
            }
            throw new WDException(WDException.CODE_APPPLICATION_EXCEPTION, msg, e);
        }
    }
    public static Object resolveValue( Object rootObject, String valuePath)
                                                        throws java.beans.IntrospectionException,
                                                                   NegativeArraySizeException,
                                                                   IllegalAccessException,
                                                                   InvocationTargetException,
                                                                   NoSuchMethodException {
        if ( valuePath==null ) return null;
        valuePath=valuePath.trim();
        int pos = valuePath.indexOf('.');
        Object currObject=rootObject;
        String currPath=valuePath;
        while (pos>=0){
            String propName=currPath.substring(0, pos).trim();
            currPath=currPath.substring(pos+1).trim();
            currObject=resolveArrayProperty(currObject, propName );
            pos = currPath.indexOf('.');
        }

        currObject=resolveArrayProperty(currObject, currPath );


        return currObject;
    }
    private static Object resolveArrayProperty( Object targetObject, String valueArrPath)
                                                            throws java.beans.IntrospectionException,
                                                                   NegativeArraySizeException,
                                                                   IllegalAccessException,
                                                                   InvocationTargetException,
                                                                   NoSuchMethodException {
        int pos = valueArrPath.indexOf('[');
        String valuePath=valueArrPath;
        int idx=-1;
        if ( pos>0){
            int pos2=valueArrPath.indexOf(']');
            if ( pos2<(pos+1) ) throw new java.beans.IntrospectionException("Invalid array name (A): "+valueArrPath);
            String dummy=valueArrPath.substring(pos+1, pos2);
            try{
                idx=Integer.parseInt(dummy);
            }catch(Exception e){
                //LoggingService.getDefaultLogger().logError(this, e);
                throw new java.beans.IntrospectionException("Invalid array index (B): ["+dummy+"] in "+valueArrPath + "[err:"+e.getMessage() +"]");
            }
            if ( idx<0)
                throw new java.lang.NegativeArraySizeException("Invalid array index (C): ["+dummy+"] in "+valueArrPath);
            valuePath=valueArrPath.substring(0, pos);
        }
        Object result=resolveProperty(targetObject, valuePath);
        if ( idx >= 0){
            if ( result instanceof List )
                return ((List)result).get(idx);
            else if ( result.getClass().isArray() )
                return java.lang.reflect.Array.get( result, idx);
            else
                throw new java.beans.IntrospectionException("Invalid object not an array: "+valuePath);

        }
        return result;
    }


    private static Object resolveProperty( Object target, String propertyName) throws NoSuchMethodException,

                                                                java.lang.reflect.InvocationTargetException,
                                                                IllegalAccessException{
        //if ( propertyName!=null && propertyName.length()>0 && result!=null ){
        if ( propertyName==null || propertyName.length()<=0 || target==null ) return null;
        String propertyMethodName=propertyName.substring( 0,1).toUpperCase()+propertyName.substring( 1);
        java.lang.reflect.Method method;
        try{
            method=target.getClass().getMethod("get"+propertyMethodName, null);
        }catch(NoSuchMethodException nsme){
            try{
                method=target.getClass().getMethod("is"+propertyMethodName, null);
            }catch(NoSuchMethodException nsme2){
                if ( target instanceof Dictionary )
                    return ( ((Dictionary)target).get(propertyName) );
                if ( target instanceof Map )
                    return ( ((Map)target).get(propertyName) );
                else
                    throw nsme2;
            }
        }

        try{
            return method.invoke(target, null);
        }catch(java.lang.reflect.InvocationTargetException ite){
            throw ite;
        }catch(IllegalAccessException ia){
            throw ia;
        }


    }


    /*public static void main(String[] args) throws Exception{
        System.out.println( BeansUtil.class.getClass() + "===>" +
        BeansUtil.resolveValueIW( BeansUtil.class, "class" ));

        System.out.println( BeansUtil.class.getClass().getMethods() + "===>" +
        BeansUtil.resolveValueIW( BeansUtil.class, "class.methods" ));

        System.out.println( BeansUtil.class.getClass().getMethods()[1] + "===>" +
        BeansUtil.resolveValueIW( BeansUtil.class, "class.methods[1]" ));

        System.out.println( BeansUtil.class.getClass().getMethods()[1].getModifiers() + "===>" +
        BeansUtil.resolveValueIW( BeansUtil.class, "class.methods[1].modifiers" ));

        System.out.println( BeansUtil.class.getClass().getMethods()[1].isAccessible() + "===>" +
        BeansUtil.resolveValueIW( BeansUtil.class, "class.methods[1].accessible" ));

System.setProperty("baubau", "yoyoy");
System.setProperty("Baubau", "yayaya");

        System.out.println( System.getProperties().get("baubau") + "===>" +
        BeansUtil.resolveValueIW( System.getProperties(), "baubau" ));

        System.out.println( System.getProperties().get("Baubau") + "===>" +
        BeansUtil.resolveValueIW( System.getProperties(), "Baubau" ));

    }*/


}






