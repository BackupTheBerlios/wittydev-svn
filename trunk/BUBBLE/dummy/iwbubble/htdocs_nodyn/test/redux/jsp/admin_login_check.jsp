<%	
	String loggedRdxAdmin=(String)session.getAttribute("redux_admin_logged");
	if ( loggedRdxAdmin==null || !loggedRdxAdmin.equals("true") ) {
		request.getRequestDispatcher("./admin_login.jsp").forward(request, response);
	}
%>
