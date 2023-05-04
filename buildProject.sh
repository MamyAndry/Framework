tomcatBin=$HOME/Documents/apache-tomcat-10.0.27/bin
tomcatWebapps=$HOME/Documents/apache-tomcat-10.0.27/webapps
frameworkTestWebPATH=$HOME/Documents/Framework/Framework_test/web/
frameworkTestCodePATH=$HOME/Documents/Framework/Framework_test/src/java/
lib=$HOME/Documents/LIBRARY

cd $frameworkTestCodePATH

find -name '*.java' > src.txt
javac -parameters -cp $lib/framework.jar -d $frameworkTestWebPATH/WEB-INF/classes @src.txt
rm src.txt
cp $lib/framework.jar $frameworkTestWebPATH/WEB-INF/lib/
cd $frameworkTestWebPATH
jar -cf Project.war .
mv Project.war $tomcatWebapps

cd $tomcatBin
./catalina.sh stop
./catalina.sh start
