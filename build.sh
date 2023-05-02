tomcatBin=$HOME/Documents/apache-tomcat-10.0.27/bin
tomcatWebapps=$HOME/Documents/apache-tomcat-10.0.27/webapps
frameworkCodePATH=$HOME/Documents/Framework/Framework/
frameworkTestWebPATH=$HOME/Documents/Framework/Framework_test/web/
frameworkTestCodePATH=$HOME/Documents/Framework/Framework_test/src/java/
lib=$HOME/Documents/LIBRARY

cd $frameworkCodePATH

find -name '*.java' > src.txt
mkdir temp
javac -cp $lib/servlet-api.jar -d temp @src.txt
rm src.txt
cd temp
jar -cf ../framework.jar .
cd ../
rm -r temp
cp framework.jar $frameworkTestWebPATH/WEB-INF/lib/
mv framework.jar ~/Documents/LIBRARY

cd $frameworkTestCodePATH

find -name '*.java' > src.txt
javac -cp $frameworkTestWebPATH/WEB-INF/lib/framework.jar -d $frameworkTestWebPATH/WEB-INF/classes @src.txt
rm src.txt
cd $frameworkTestWebPATH
jar -cf Project.war .
mv Project.war $tomcatWebapps

cd $tomcatBin
./catalina.sh stop
./catalina.sh start
