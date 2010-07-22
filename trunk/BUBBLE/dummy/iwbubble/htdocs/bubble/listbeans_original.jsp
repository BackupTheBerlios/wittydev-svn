<link rel=stylesheet href="/common/css/imiweb.css">

<%
	String beanPath= request.getParameter("path");
	Object bean=null;
	it.imiweb.beanworld.bubble.servlet.http.BubbleHttpServletRequest bReq= 
		(it.imiweb.beanworld.bubble.servlet.http.BubbleHttpServletRequest)request;

	if ( beanPath==null ) beanPath="";
	if ( beanPath==null || beanPath.length()==0 || beanPath.equals("/") ) {
		bean=bReq.getWebArchitect();
	}else{
		bean=bReq.getWebArchitect().resolveName(beanPath);
	}
	java.util.StringTokenizer tokenizer= new java.util.StringTokenizer(beanPath, "/");
	String dummyPath="";
	String navStr="";
	while ( tokenizer.hasMoreElements() ){
		String dummy=(String)tokenizer.nextElement();
		dummyPath+="/"+dummy;
		navStr+="/<a style=\"font-size:14pt; font-weight:normal; text-decoration:none;\" href=\"./listbeans.jsp?path="+dummyPath+"\">"+dummy+"</a>";
	}
%>
	
	<%= navStr %><br>
	<a href="./listbeans_conf.jsp?path=<%=beanPath %>">Go to configuration infos...</a>
	<hr>
<%	
	if ( bean==null ){
		out.println("Bean not found!");
	}else {
		if ( bean instanceof it.imiweb.beanworld.bubble.BubbleContext ){
	        %>
<a class="title-ora">Context's Beans List </a><br>
		<%

			it.imiweb.beanworld.bubble.BubbleContext ctx = (it.imiweb.beanworld.bubble.BubbleContext)bean;
			javax.naming.NamingEnumeration enum=ctx.list("");
			if ( !enum.hasMore() ){
%> <a class="ntext" style="text-decoration:none;">None</a> <%
			}else{
				String childrenCtx="";
				String children="";
				while(enum.hasMore()){
					
					javax.naming.NameClassPair ncp=(javax.naming.NameClassPair)enum.next();
					Object dummyChildBean=ctx.lookup( ncp.getName() );
					
					if ( dummyChildBean instanceof it.imiweb.beanworld.bubble.BubbleContext )
						childrenCtx+= "<li class=\"lista\"><a href=\"./listbeans.jsp?path="+beanPath+"/"+ncp.getName()+"\">"+ncp.getName()+"/</a>\n";
					else
						children+=    "<li class=\"lista\"><a href=\"./listbeans.jsp?path="+beanPath+"/"+ncp.getName()+"\" style=\"font-weight:normal;\">"+ncp.getName()+"</a>\n";
				}		
				
				
				out.println(childrenCtx);
				out.println(children);

			}
%><br><%
		}	
		
	        java.lang.reflect.Method[] methods = bean.getClass().getMethods();
	        java.util.Hashtable propertiesHash= new java.util.Hashtable();
	        java.util.Vector propertiesName= new java.util.Vector();

	        java.util.Hashtable methodsHash= new java.util.Hashtable();
	        java.util.Vector methodsName= new java.util.Vector();
	        
	        
	        for ( int i=0; i<methods.length; i++){
	            
	            if ( methods[i].getReturnType() != null &&
	                (methods[i].getParameterTypes()==null || methods[i].getParameterTypes().length==0) ){
	                String name = methods[i].getName();
	                String prop=null;
	                //Class clazzType=methods[i].getReturnType();
	                if (name.startsWith("is") && name.length()>2){
	                    prop=name.substring(2);
	                }else if (name.startsWith("get") && name.length()>3){
	                    prop=name.substring(3);
	                }
	                if (prop!=null){
	                    propertiesName.addElement(prop);
	                    propertiesHash.put(prop, methods[i]);
	                }else{
	                    methodsName.addElement(methods[i].getName());
	                    methodsHash.put(methods[i].getName(), methods[i]);
	                }
	            
	            }else if (methods[i].getParameterTypes()==null || methods[i].getParameterTypes().length==0 ){
	                    methodsName.addElement(methods[i].getName());
	                    methodsHash.put(methods[i].getName(), methods[i]);
	            }
	        }
	        
	        
		java.util.Collections.sort(propertiesName);

	        %>
<br><a class="title-ora">Bean Properties Values </a><br>
<table border=1 width="80%"><tr class="ntext-b"><th>Property</th><th>value</th><th>type</th></tr>
	        <%
		for ( int i=0; i<propertiesName.size(); i++){
                    Object value=null;
                    String prop=(String)propertiesName.elementAt(i);
                    Exception exc=null;
                    java.lang.reflect.Method propMethod=(java.lang.reflect.Method)propertiesHash.get(prop);
                    try{
                        value=propMethod.invoke(bean, null);
                    }catch(Exception e){
                        //value=e.getMessage();
                        exc=e;
                    }
                    if ( exc!=null ){
                    %>
<tr class=ntext><td><b><%= prop %></b></td><td color=red><%= exc.getMessage() %></td><td><%= "clazzType "%></td></tr>
                    <%
                    }else{
                    %>
<tr  class=ntext><td><b><%= prop %></b></td><td><%= (it.imiweb.util.DataTools.arrayToString(value)) %></td><td><%= propMethod.getReturnType() %></td></tr>
                    <%
                    }
		}

	        out.println("</table>");
		java.util.Collections.sort(methodsName);
	        %>
<br><a class="title-ora">Bean Methods </a><br>
<table border=1 width="80%"><tr class="ntext-b"><th>Method</th><th>return type</th></tr>
	        <%
		for ( int i=0; i<methodsName.size(); i++){
                    String prop=(String)methodsName.elementAt(i);
                    java.lang.reflect.Method method=(java.lang.reflect.Method)methodsHash.get(prop);
                    String paramsSig="(";
                    Class[] parameterTypes=method.getParameterTypes();
                    for ( int j=0; parameterTypes!=null && j<parameterTypes.length; j++){
                    	paramsSig+=parameterTypes[j].getName()+((j+1<parameterTypes.length)?", ":"");
                    }
                    paramsSig+=")";
                    
                    %>
<tr  class=ntext><td><b><%= prop+paramsSig %></b></td><td><%= method.getReturnType() %></td></tr>
                    <%
		}

	        out.println("</table>");




	}
	
%>
<hr>
<a class="title-ora">toString() </a><br>
<table border=1 width="80%"><tr class="ntext"><td>
<pre>
<%= bean %>
</pre>
</td></tr><table>