frameworkCodePATH=$HOME/ITU/Mr_Naina/Framework/Framework/
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
mv framework.jar $lib
