<strong>1</strong> - Mila asiana "extension do" daholo ny "AnnotationUrl"
    oh: emp-all.do
2 - Rehefa manao "type d'attribut":
    a) Raha "int" dia "Integer"
    b) Raha "double" dia "Double"
3 - Ireo fonction manana anjara asa ao amin'y frontServlet dia ampiana annotation @AnnotationUrl
4 - Mamorona "init-param" ao anatin'ny "web.xml" ary ampio an'ireto
    oh: <param-name>modelPackage</param-name>
        <param-value>anaran'ny package ho lalovina</param-value>
5 - Tsy maintsy atao mitovy ny anaran'ny "argument" an'ny "method" sy "name" an'ny "input"
    oh: - public type method( type_argument arg)
        - <input type="text" name="arg">
6 - Tsy maintsy atao mitovy ny anaran'ny "attribut" an'ny "classe" sy "name" an'ny "input"
    oh: - public Integer attr1
        - <input type="number" name="attr1">
        - public String attr2
        - <input type="text" name="attr2">
