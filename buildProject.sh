tomcatBin=$HOME/Documents/apache-tomcat-10.0.27/bin
tomcatWebapps=$HOME/Documents/apache-tomcat-10.0.27/webapps
frameworkTestPATH=$HOME/ITU/Mr_Naina/Framework/Framework_test/
lib=$HOME/Documents/LIBRARY

cd $frameworkTestPATH

find -name '*.java' > src.txt

javac -parameters -cp $lib/framework.jar -d temporary/WEB-INF/classes @src.txt
rm src.txt

if [ -f Jsp/css ]
then
cp  Jsp/css ./temporary
fi

cp Jsp/*.jsp ./temporary

cp web.xml temporary/WEB-INF

mkdir temporary/WEB-INF/lib

cp $lib/framework.jar temporary/WEB-INF/lib/framework.jar
cp $lib/gson.jar temporary/WEB-INF/lib/gson.jar

cd temporary 
jar -cf Project.war .
mv Project.war $tomcatWebapps

cd ../
rm -R temporary

cd $tomcatBin
./catalina.sh stop
./catalina.sh start