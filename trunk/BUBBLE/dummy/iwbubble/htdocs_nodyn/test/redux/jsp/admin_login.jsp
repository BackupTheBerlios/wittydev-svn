<%
	String dummy=request.getParameter("redux_admin_password");
	boolean logged=false;//=(dummy2!=null && dummy2.equals("true"));
	
	if ( dummy!=null ){
		logged=dummy.equals("dudette");
		session.setAttribute( "redux_admin_logged", ""+logged );
	}
	if ( logged ){%>
<form method="post">
	<input type="submit" value="GO!">
</form>
<script> document.forms[0].submit();</script>
	<%}else{%>
<form method="post">
	Inserire la password per accedere all'area protetta: <input type="password" name="redux_admin_password">
</form>
	<%}

	/*out.println("<li>"+request.getRequestURI());
	out.println("<li>"+request.getRequestURL());
	out.println("<li>"+request.getPathInfo());
	
	out.println("<hr>");
	for ( java.util.Enumeration e=request.getAttributeNames(); e.hasMoreElements();){
		String key=(String)e.nextElement();
		Object val=request.getAttribute(key);
		out.println("<li>"+key+":"+val);
	}
	%><hr><%
	for ( java.util.Enumeration e=request.getHeaderNames(); e.hasMoreElements();){
		String key=(String)e.nextElement();
		Object val=request.getHeader(key);
		out.println("<li>"+key+":"+val);
	}*/

%>