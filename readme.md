1 - Soloy anaty ny "buildProject.sh" ny chemin an'i Tomcat sy Library misy an'i servlet-api
    oh: -tomcatBin="chemin absolu" an'i "bin" an'i "tomcat"
        -tomcatWebapps="chemin absolu" an'i "webapps" an'i "tomcat"
        -frameworkTestWebPATH="chemin absolu" an'i "WEB-INF"
        -frameworkTestCodePATH="chemin absolu" an'i "classe" ho "compiler" an'i "code"
        -lib="chemin absolu" an'i "library" nasianao an'ny "servlet-api.jar"
2 - Ilay "build.sh" no runena rehefa manampy fiovana ao amin'ny Framework-test
3 - Ireo fonction manana anjara asa ao amin'y frontServlet dia ampiana annotation @AnnotationUrl
4 - Eo ambanin'ny "init-param" ao amin'ny "web.xml" "ModelPackage" no asiana ny "package" ho ampiasaina
    oh:     <param-name>modelPackage</param-name>
            <param-value>anaran'ny package ho lalovina</param-value>
5 - Tsy maintsy atao mitovy ny anarana "argument" an'ny "method" sy "value" an'ny "input"
    oh: - public type method( type_argument arg)
        - <input type="text" name="arg">
