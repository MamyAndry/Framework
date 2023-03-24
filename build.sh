apachePATH=~/Documents/apache-tomcat-10.0.27/webapps/Test_Framework/WEB-INF/
frameworkPATH=~/Documents/Framework/Framework/build/web/WEB-INF/classes
frameworktestPATH=~/Documents/Framework/Framework_test/build/web/WEB-INF/classes
cd $frameworkPATH
jar -cf framework.jar .
cp framework.jar $apachePATH/lib
cp framework.jar ~/Documents/LIBRARY
cd $frameworktestPATH
jar -cf classes.war .
cp classes.war $apachePATH/classes
