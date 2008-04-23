package org.wittydev.test.pkg2;

/**
 * Title:
 * Description:
 * Copyright:    Copyright (c) 2002
 * Company:
 * @author
 * @version 1.0
 */

public class BeanB {
    String[] beanBStringArr;
    int[] beanBIntArr;
    Integer[] beanBIntObjectArr;

    public BeanB() {}

    public String [] getBeanBStringArr(){
        return this.beanBStringArr;
    }
    public int [] getBeanBIntArr(){
        return this.beanBIntArr;
    }
    public Integer [] getBeanBIntObjectArr(){
        return this.beanBIntObjectArr;
    }
    public void setBeanBStringArr(String[] beanBStringArr){
        this.beanBStringArr=beanBStringArr;
    }
    public void setBeanBIntArr(int[] beanBIntArr){
        this.beanBIntArr=beanBIntArr;
    }
    public void setBeanBIntObjectArr(Integer[] beanBIntObjectArr){
        this.beanBIntObjectArr=beanBIntObjectArr;
    }


}