<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <link rel="stylesheet" href="style.css">
        <title>Emp JSP</title>
    </head>
    <body>
        <h1>IT WORKS!!!</h1>
        <%
            out.print(request.getAttribute("id"));
            out.print("<br>");
        %>
    </body>
</html>
