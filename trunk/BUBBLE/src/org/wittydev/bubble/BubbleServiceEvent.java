package org.wittydev.bubble;
import org.wittydev.config.ConfigEntry;
/**
 * Title:
 * Description:
 * Copyright:    Copyright (c) 2002
 * Company:
 * @author
 * @version 1.0
 */

public class BubbleServiceEvent {//extends javax.servlet.{
    BubbleContext parentContext;
    Object service;
    ConfigEntry conf;
    String name;

    public BubbleServiceEvent(Object service, BubbleContext parentContext,
                                String name){
        this(service, parentContext, name, null);
    }
    //public BubbleServiceEvent(BubbleContext rootContext,  Object service, ConfigEntry conf){
    public BubbleServiceEvent(  Object service,
                                BubbleContext parentContext,
                                String name,
                                ConfigEntry conf ){
            //Object service, ConfigEntry conf){
        this.service=service;
        this.parentContext=parentContext;
        this.name=name;
        this.conf=conf;
    }
    public String getName() {
        return name;
    }
    public BubbleContext getParentContext() {
        return parentContext;
    }
    public BubbleContext getRootContext() {
        return (parentContext==null)?null:parentContext.getRootContext();
    }
    public Object getService() {
        return service;
    }
    public ConfigEntry getConfigEntry() {
        return conf;
    }
}