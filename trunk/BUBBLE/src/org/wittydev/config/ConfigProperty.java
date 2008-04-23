package org.wittydev.config;
import java.lang.reflect.Method;
/**
 * Title:
 * Description:
 * Copyright:    Copyright (c) 2002
 * Company:
 * @author
 * @version 1.0
 */

public class ConfigProperty{
    public static byte TYPE_REF=1;
    public static byte TYPE_JNDI=2;
    public static byte TYPE_VALUE=0;

    Class type;
    Method setterMethod;
    //boolean ref;
    int valueType;


    String name;
    String stringValue;
    //Object value;

    public Class getType(){
        return type;
    }
    public Method getSetterMethod(){
        return setterMethod;
    }

    public boolean isReference(){
        //return ref;
        return valueType==TYPE_REF;
    }
    public boolean isJNDIReference(){
        //return ref;
        return valueType==TYPE_JNDI;
    }

    public String getName(){
        return name;
    }

    public String getStringValue(){
        return stringValue ;
    }
    /*public Object getValue(){
        return value;
    }*/
    public String toString(){
        return "["+type+"]"+name+"="+stringValue ;
    }
}
