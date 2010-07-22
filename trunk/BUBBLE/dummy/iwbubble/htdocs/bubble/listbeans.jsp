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
		}
		request.setAttribute("_ck_bean_", bean);
%><br>

<jsp:include page="./listbeans_object_info.jsp" flush="true"/>
	
	<%}%>	
	
	