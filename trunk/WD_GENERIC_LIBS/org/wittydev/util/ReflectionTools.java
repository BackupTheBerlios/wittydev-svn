package org.wittydev.util;
import java.util.StringTokenizer;
import java.util.Hashtable;
import java.util.Dictionary;
import java.util.Map;
import java.util.Set;
import java.util.Vector;
import java.util.List;
import java.lang.reflect.Constructor;
import java.lang.reflect.Array;
/**
 * Title:
 * Description:
 * Copyright:    Copyright (c) 2002
 * Company:
 * @author
 * @version 1.0
 */

public class ReflectionTools {

    public static Object valueOf( Class clazz, Object value)
                                                throws IllegalAccessException,
                                                InstantiationException,
                                                NoSuchMethodException,
                                                java.lang.reflect.InvocationTargetException  {
        return valueOf(clazz, value, ",");
    }
    public static Object valueOf( Class clazz, Object value, String arraySeparator)
                                                throws IllegalAccessException,
                                                InstantiationException,
                                                NoSuchMethodException,
                                                java.lang.reflect.InvocationTargetException  {
        Object result;
        if ( value==null) return null;
        if ( clazz.isAssignableFrom( value.getClass() ) )return value;
        if ( clazz.isArray() ){
            //if ( clazz.isAssignableFrom( value.getClass() ) )
            //    result=value;
            //else
            if ( !(value instanceof String) ) value=value.toString();
            result = convToArrayObject( (String)value, clazz, arraySeparator  );
        //}else if ( clazz.isInstance(Dictionary.class )){
        }else if ( Dictionary.class.isAssignableFrom( clazz )){
            if ( !(value instanceof String) ) value=value.toString();
            result = convToDictionaryObject( (String)value, clazz, arraySeparator  );
        //}else if ( clazz.isInstance(Map.class) ){
        }else if ( Map.class.isAssignableFrom(clazz) ){
            if ( !(value instanceof String) ) value=value.toString();
            result = convToMapObject( (String)value, clazz, arraySeparator  );
        //}else if ( clazz.isInstance(java.util.List.class) ){
        }else if ( Set.class.isAssignableFrom(clazz) ){
            if ( !(value instanceof String) ) value=value.toString();
            result = convToSetObject( (String)value, clazz, arraySeparator  );
        //}else if ( clazz.isInstance(java.util.List.class) ){
        }else if ( java.util.List.class.isAssignableFrom(clazz) ){
            if ( !(value instanceof String) ) value=value.toString();
            result = convToListObject( (String)value, clazz, arraySeparator  );
        }else{

            if ( clazz.isPrimitive() )
                clazz=getNonPrimitiveClass( clazz );


            //if ( clazz.isAssignableFrom( value.getClass() ) ) {
            if ( clazz.isAssignableFrom( value.getClass() ) ) {
                result=value;
            }else if ( value instanceof String )
                result= convObject( (String)value, clazz );
            else
                result= convObject( value.toString(), clazz );
        }
        return result;
    }

    public static boolean isPrimitiveWrapperClass( Class clazz ){
        if ( clazz == Boolean.class || clazz == Double.class || clazz == Short.class ||
            clazz == Long.class || clazz == Integer.class || clazz == Character.class ||
            clazz == Byte.class )
            return true;
        else
            return false;
    }
    public static Class getNonPrimitiveClass( Class clazz ){
        //java.lang.Integer
        if ( clazz == boolean.class )
            clazz=Boolean.class;
        else if ( clazz == double.class )
            clazz = Double.class;
        else if ( clazz == short.class )
            clazz = short.class;
        else if ( clazz == long.class )
            clazz = Long.class;
        else if ( clazz == int.class )
            clazz = Integer.class;
        else if ( clazz == char.class )
            clazz = Character.class;
        else if ( clazz == byte.class )
            clazz = Byte.class;
        return clazz;
    }
    static Object convObject(String value, Class yoClass )
                                        throws IllegalAccessException,
                                                NoSuchMethodException,
                                                java.lang.reflect.InvocationTargetException,
                                                InstantiationException {
        if ( value==null ) return null;
        //try {
            if ( yoClass==null )  yoClass=String.class;
            if  ( yoClass==String.class) return value;
            if ( yoClass==Number.class )  return new java.math.BigDecimal(value);

            //if ( yoClass.isPrimitive() ) yoClass= yoClass.getDeclaringClass();
            //System.out.println(yoClass+"=="+yoClass.isPrimitive()+"===============================>"+yoClass.getDeclaringClass());
            if ( yoClass == boolean.class )
                yoClass=Boolean.class;
            else if ( yoClass ==  byte.class )
                yoClass=Byte.class;
            else if ( yoClass ==  char.class)
                yoClass=Character.class;
            else if ( yoClass ==  short.class)
                yoClass=Short.class;
            else if ( yoClass ==  int.class)
                yoClass=Integer.class;
            else if ( yoClass ==  long.class)
                yoClass=Long.class;
            else if ( yoClass ==  float.class)
                yoClass=Float.class;
            else if ( yoClass ==  double.class)
                yoClass=Double.class;

            Constructor constructor= yoClass.getConstructor( new Class[]{String.class});
            return constructor.newInstance(new String[]{value});
        /*}catch (Exception e){
            //if ( loggingError )
            LoggingService.getDefaultLogger().logError(ReflectionTools.class,"Class: "+yoClass+", value: "+value);
            LoggingService.getDefaultLogger().logError(ReflectionTools.class,e);
            throw new IWException (e.getMessage(), IWException.CODE_SYSTEM_EXCEPTION);
        }*/
   }
    public static Object convToArrayObject(String value, Class arrClass, String separator )
                                                throws InstantiationException,
                                                java.lang.reflect.InvocationTargetException,
                                                NoSuchMethodException, IllegalAccessException {

        if (value==null ) return null;
        if (separator==null || separator.length()==0) separator = ",";

        if ( arrClass==null) arrClass=String.class;
        StringTokenizer tokenizer=new StringTokenizer ( value,  separator );
        int len=tokenizer.countTokens();
        Object objArr= Array.newInstance( arrClass.getComponentType(), len );

        for (int i=0; i<len; i++){
            //objArr[i]=convObject((String)tokenizer.nextElement(), arrClass.getComponentType());
            Array.set(objArr, i, convObject((String)tokenizer.nextElement(), arrClass.getComponentType()));
        }

        return objArr;
    }

    public static List convToListObject(String value, Class listClass, String separator )
                                                throws InstantiationException,
                                                java.lang.reflect.InvocationTargetException,
                                                NoSuchMethodException, IllegalAccessException {
        if (value==null ) return null;
        if (separator==null || separator.length()==0) separator = ",";

        if ( listClass==null || listClass == List.class ) listClass=Vector.class;
        StringTokenizer tokenizer=new StringTokenizer ( value,  separator );
        int len=tokenizer.countTokens();


        List list=(List)listClass.newInstance();

        for (int i=0; i<len; i++){
            //objArr[i]=convObject((String)tokenizer.nextElement(), arrClass.getComponentType());
            //Array.set(objArr, i, convObject((String)tokenizer.nextElement(), arrClass.getComponentType()));
            list.add( tokenizer.nextElement() );
        }

        return list;
    }



    public static Dictionary convToDictionaryObject(String value, Class dictClass, String separator )
                                            throws IllegalAccessException, InstantiationException{
        if (value==null ) return null;
        if (separator==null || separator.length()==0) separator = ",";

        if ( dictClass==null || dictClass == Dictionary.class ) dictClass=Hashtable.class;
        Dictionary dict=(Dictionary)dictClass.newInstance();
//System.out.println("====>"+dict);
        StringTokenizer tokenizer=new StringTokenizer ( value,  separator );
        int len=tokenizer.countTokens();


        for (int i=0; i<len; i++){
            //objArr[i]=convObject((String)tokenizer.nextElement(), arrClass.getComponentType());
            //Array.set(objArr, i, convObject((String)tokenizer.nextElement(), arrClass.getComponentType()));
            String val=(String)tokenizer.nextElement();
            int pos=val.indexOf('=');
            if ( pos>0 ) dict.put( val.substring( 0, pos ), val.substring( pos+1 ) );

        }

        return dict;
    }
    public static Map convToMapObject(String value, Class mapClass, String separator )
                                            throws IllegalAccessException, InstantiationException{
        if (value==null ) return null;
        if (separator==null || separator.length()==0) separator = ",";

        if ( mapClass==null || mapClass == Map.class ) mapClass=java.util.HashMap.class;
        Map map=(Map)mapClass.newInstance();

        StringTokenizer tokenizer=new StringTokenizer ( value,  separator );
        int len=tokenizer.countTokens();


        for (int i=0; i<len; i++){
            //objArr[i]=convObject((String)tokenizer.nextElement(), arrClass.getComponentType());
            //Array.set(objArr, i, convObject((String)tokenizer.nextElement(), arrClass.getComponentType()));
            String val=(String)tokenizer.nextElement();
            int pos=val.indexOf('=');
            if ( pos>0 ) map.put( val.substring( 0, pos ), val.substring( pos+1 ) );

        }

        return map;
    }
    public static Set convToSetObject(String value, Class setClass, String separator )
                                                throws InstantiationException,
                                                java.lang.reflect.InvocationTargetException,
                                                NoSuchMethodException, IllegalAccessException {
        if (value==null ) return null;
        if (separator==null || separator.length()==0) separator = ",";

        if ( setClass==null || setClass == Set.class ) setClass=Set.class;

        StringTokenizer tokenizer=new StringTokenizer ( value,  separator );
        int len=tokenizer.countTokens();

        Set set=(Set)setClass.newInstance();

        for (int i=0; i<len; i++){
            //objArr[i]=convObject((String)tokenizer.nextElement(), arrClass.getComponentType());
            //Array.set(objArr, i, convObject((String)tokenizer.nextElement(), arrClass.getComponentType()));
            set.add( tokenizer.nextElement() );
        }

        return set;
    }

    public static void main(String[] args) throws Exception{
        String bau="aaaa=,nnn=dddd,";
        Object obj=convToDictionaryObject( bau, java.util.Dictionary.class, ",");
        System.out.println(obj+"=="+obj.getClass() );

        Object obj2=convToMapObject( bau, java.util.Map.class, ",");
        System.out.println(obj2+"=="+obj2.getClass() );

        Object obj3=convToListObject( bau, java.util.ArrayList.class, ",");
        System.out.println(obj3+"=="+obj3.getClass() );

    }
}