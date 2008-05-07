package org.wittydev.bubble;
import javax.naming.NameParser;
import javax.naming.Name;
import javax.naming.CompositeName;
import javax.naming.NamingException;
import java.util.Properties;

/**
 * Title:
 * Description:
 * Copyright:    Copyright (c) 2002
 * Company:
 * @author
 * @version 1.0
 */

public class BubbleNameParser implements NameParser {
    String[] domains;
    public BubbleNameParser(){}
    public BubbleNameParser(String[] domains){this.domains=domains;}

    public Name parse(String name) throws NamingException {
        if (domains!=null && name!=null ){
            for ( int i=0; i<domains.length;i++){
                if ( domains[i]!=null && name.indexOf(domains[i]+":")==0 ){
                    name=name.substring(domains[i].length()+1);
                    break;
                }
            }
        }
        return new CompositeName(name);
    }
}