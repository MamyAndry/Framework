<%-- 
    Document   : emp
    Created on : Mar 24, 2023, 10:36:57 AM
    Author     : mamisoa
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Dept JSP</title>
    </head>
    <body>
        <h1>DEPT WORKS!!!</h1>
        <%
            //int nbr = Integer.parseInt(request.getAttribute("mpampiasa"));
            //out.print(nbr); 
            out.print(request.getAttribute("mpampiasa"));
        %>
    </body>
</html>
