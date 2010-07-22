<%
Object bean=request.getAttribute("_ck_bean_");
if (bean!=null){
%>
	<%
			
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
	
	
	
	
		
		
	%>
	<hr>
	<a class="title-ora">toString() </a><br>
	<table border=1 width="80%"><tr class="ntext"><td>
	<pre>
	<%= bean %>
	</pre>
	</td></tr></table>
<%}else{
	out.println("NO BEAN TO ANALYSE");
}

%>