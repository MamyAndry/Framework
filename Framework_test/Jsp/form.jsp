<%
    out.println(request.getSession().getAttribute("current"));
    out.println("<br>");
%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <link rel="stylesheet" href="style.css">
        <title>Form</title>
    </head>
    <body>
        <h3>One</h3>
        <div>
            <form method="post" action="save-emp.do" enctype="multipart/form-data">
                <p>Nom : <input type="text" name="nom" value="So"></p>
                <p>Prenom : <input type="text" name="prenom" value="Rona"></p>
                <p>upload : <input type="file" name="empUpload"></p>
                <p>choix 1 : <input type="checkbox" name="option[]" value="1"></p>
                <p>choix 2 : <input type="checkbox" name="option[]" value="2"></p>
                <p>choix 3 : <input type="checkbox" name="option[]" value="3"></p>
                <input type="submit" value="Valider">
            </form>
        </div>
        <h3>Another One</h3>    
        <div>
            <form method="get" action="emp-one.do">
                <input type="text" name="test" value="Soa">
                <input type="submit" value="Valider">
            </form>
        </div>

    </body>
</html>
