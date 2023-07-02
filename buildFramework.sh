frameworkCodePATH=$HOME/ITU/Mr_Naina/Framework/Framework/
lib=$HOME/Documents/LIBRARY

cd $frameworkCodePATH

find -name '*.java' > src.txt
mkdir temp
javac -cp $lib/servlet-api.jar:$lib/gson.jar -d temp @src.txt
rm src.txt
cp $lib/gson.jar temp/gson.jar
cd temp
jar -cf ../framework.jar .
cd ../
rm -r temp
mv framework.jar $lib
