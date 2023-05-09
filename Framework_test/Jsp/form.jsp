
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <link rel="stylesheet" href="style.css">
        <title>Form</title>
    </head>
    <body>
        <div>
            <form method="post" action="save-emp.do">
                <input type="text" name="nom" value="So">
                <input type="text" name="prenom" value="Rona">
                <input type="submit" value="Valider">
            </form>
        </div>

        <div>
            <form method="get" action="emp-one.do">
                <input type="text" name="test" value="Soa">
                <input type="submit" value="Valider">
            </form>
        </div>

    </body>
</html>
