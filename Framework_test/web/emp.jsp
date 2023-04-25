<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Emp JSP</title>
    </head>
    <body>
        <h1>IT WORKS!!!</h1>
        <%
            out.print(request.getAttribute("nom"));
            out.print("<br>");
            out.print(request.getAttribute("prenom"));
        %>
    </body>
</html>
