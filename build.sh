tomcat=$HOME/Documents/apache-tomcat-10.0.27/bin
tomcatWebapps=~/Documents/apache-tomcat-10.0.27/webapps
frameworkPATH=~/Documents/Framework/Framework/build/web/WEB-INF/classes/
frameworkJavaPATH=~/Documents/Framework/Framework/
frameworktestPATH=~/Documents/Framework/Framework_test/web/
frameworktestJavaPATH=~/Documents/Framework/Framework_test/src/java/
lib=$HOME/Documents/LIBRARY

cd $frameworkJavaPATH

    find -name '*.java' > src.txt
mkdir temp
javac -cp $lib/servlet-api.jar -d temp @src.txt
rm src.txt
cd temp
jar -cf ../framework.jar .
cd ../
rm -r temp
cp framework.jar $frameworktestPATH/WEB-INF/lib/
mv framework.jar ~/Documents/LIBRARY

cd $frameworktestJavaPATH

find -name '*.java' > src.txt
javac -cp $frameworktestPATH/WEB-INF/lib/framework.jar -d $frameworktestPATH/WEB-INF/classes @src.txt
rm src.txt
cd $frameworktestPATH
jar -cf Project.war .
mv Project.war $tomcatWebapps

cd $tomcat
./catalina.sh stop
./catalina.sh start
