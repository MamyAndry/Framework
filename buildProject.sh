tomcatBin=$HOME/Documents/apache-tomcat-10.0.27/bin
tomcatWebapps=$HOME/Documents/apache-tomcat-10.0.27/webapps
frameworkTestPATH=$HOME/Documents/Framework/Framework_test/
lib=$HOME/Documents/LIBRARY

cd $frameworkTestPATH

find -name '*.java' > src.txt

if [ -f temporary ]
then
mkdir temporary/WEB-INF
mkdir temporary/WEB-INF/classes
mkdir temporary/WEB-INF/lib
else
mkdir temporary
mkdir temporary/WEB-INF
mkdir temporary/WEB-INF/classes
mkdir temporary/WEB-INF/lib
fi

javac -parameters -cp $lib/framework.jar -d temporary/WEB-INF/classes @src.txt
rm src.txt

cp Jsp/*.jsp ./temporary

cp web.xml temporary/WEB-INF

cp $lib/framework.jar temporary/WEB-INF/lib/framework.jar

cd temporary 
jar -cf Project.war .
mv Project.war $tomcatWebapps

cd ../
rm -R temporary

cd $tomcatBin
./catalina.sh stop
./catalina.sh start