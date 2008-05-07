package org.wittydev.config;

/**
 * Title:
 * Description:
 * Copyright:    Copyright (c) 2002
 * Company:
 * @author
 * @version 1.0
 */

public interface ObjectResolver {
    public Object resolveComponentReference(String componentPath, String scope, Object[] resolverArgs);

}