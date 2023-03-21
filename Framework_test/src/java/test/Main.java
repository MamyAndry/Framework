package test;

import dao.DbConnection;
import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.sql.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Enumeration;

public class Main{
    public static String getTabName(String str) { //Return the name of the Table 
        return str.split("\\.")[str.split("\\.").length - 1];
    }
    
    public static String turnIntoCapitalLetter(String text){
        return text.substring(0,1).toUpperCase().concat(text.substring(1));
    }
    
    public static ArrayList<Class> findClasses(File directory, String packageName) throws ClassNotFoundException{
        ArrayList<Class> classes = new ArrayList<Class>();
        if (!directory.exists())
        {
            return classes;
        }
        File[] files = directory.listFiles();
        for (File file : files)
        {
            if (file.isDirectory())
            {
                classes.addAll(findClasses(file, packageName + "." + file.getName()));
            }
            else if (file.getName().endsWith(".class"))
            {
                classes.add(Class.forName(packageName + '.' + file.getName().substring(0, file.getName().length() - 6)));
            }
        }
        return classes;
    }

    public static ArrayList<Class> getClasses(String packageName) throws ClassNotFoundException, IOException, URISyntaxException{
        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        String path = packageName.replace('.', '/');
        Enumeration<URL> resources = classLoader.getResources(path);
        ArrayList<File> dirs = new ArrayList<File>();
        while (resources.hasMoreElements())
        {
            URL resource = resources.nextElement();
            URI uri = new URI(resource.toString());
            dirs.add(new File(uri.getPath()));
        }
        ArrayList<Class> classes = new ArrayList<Class>();
        for (File directory : dirs)
        {
            classes.addAll(findClasses(directory, packageName));
        }

        return classes;
    }
    public static void main(String[] args){
        String mot = "mota";
        try{
            ArrayList<Class> temp = getClasses("src/java/");
            for(Class c : temp)
                System.out.println(c);
        }
        catch(Exception e){
            e.printStackTrace();
        }
        finally{
            try{
            }
            catch(Exception ex){
                ex.printStackTrace();
            }
        }
    }
}