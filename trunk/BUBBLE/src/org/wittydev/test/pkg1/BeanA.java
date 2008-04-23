package org.wittydev.test.pkg1;
import org.wittydev.test.pkg2.BeanB;
import org.wittydev.test.pkg3.BeanC;

/**
 * Title:
 * Description:
 * Copyright:    Copyright (c) 2002
 * Company:
 * @author
 * @version 1.0
 */

public class BeanA {
    BeanB beanB;
    String beanCString;

    public BeanA() {}

    public BeanB getBeanB(){
        return beanB;
    }
    public String getBeanCDummyString(){
        return beanCString;
    }
    public void setBeanB(BeanB beanB){
        this.beanB=beanB;
    }
    public void setBeanCDummyString(String str){
        this.beanCString=str;
    }

}