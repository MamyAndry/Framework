package helper;

import java.lang.reflect.*;
//import java.util.Vector;
import annotation.*;
import java.util.ArrayList;

public class DaoHelper{

//METHODS

    public static String turnIntoCapitalLetter(String text){
        return text.substring(0,1).toUpperCase().concat(text.substring(1));
    }

    public static String getTableName(String str) { //Return the name of the Table 
        return str.split("\\.")[str.split("\\.").length - 1];
    }
    
    public static Method[] getGetters(Object obj) throws Exception{
        String[] list = getFields(obj);
        Method[] res = new Method[list.length];
        for(int i = 0 ; i < list.length ; i++){
            res[i] = obj.getClass().getMethod("get" + list[i]);
        }
        return res;
    }

    public static Method[] getSetters(Object obj,Object argument) throws Exception{
        String[] list = getFields(obj);
        Method[] res = new Method[list.length];
        for(int i = 0 ; i < list.length ; i++){
            res[i] = obj.getClass().getMethod("set" + list[i] , argument.getClass());
        }
        return res;
    }

    public static String[] convertIntoArray(ArrayList<String> list){
        String[] res = new String[list.size()];
        for(int i = 0 ; i < list.size() ; i++){
            res[i] = list.get(i);
        }
        return res;

    }
    
    public static String[] getFields(Object obj){
        ArrayList<String> res = new ArrayList<String>();
        for(int i = 0 ; i < obj.getClass().getDeclaredFields().length ; i++){
            if(obj.getClass().getDeclaredFields()[i].isAnnotationPresent(AnnotationColumn.class))
                res.add(turnIntoCapitalLetter(obj.getClass().getDeclaredFields()[i].getName()));
        }
        return convertIntoArray(res);

    }
    
    public static String[] getColumns(Object obj){
        String[] res = new String[obj.getClass().getDeclaredFields().length];
        for(int i = 0 ; i < obj.getClass().getDeclaredFields().length ; i++){
            if(obj.getClass().getDeclaredFields()[i].isAnnotationPresent(AnnotationColumn.class)){
                AnnotationColumn col = obj.getClass().getDeclaredFields()[i].getAnnotation(AnnotationColumn.class);
                if(col.nom().equals(""))
                    res[i] = turnIntoCapitalLetter(obj.getClass().getDeclaredFields()[i].getName());
                else
                    res[i] = turnIntoCapitalLetter(col.nom());
            }
        }
        return res;

    }

    public static String[] getTypeName(Object obj){
        String[] res = new String[obj.getClass().getDeclaredFields().length];
        for (int i = 0; i < obj.getClass().getDeclaredFields().length; i++) {
            res[i] = obj.getClass().getDeclaredFields()[i].getGenericType().getTypeName();
        }
        return res;
    }

    public static String getPKName(Object obj){
        String res = "";
        for(int i = 0 ; i < obj.getClass().getDeclaredFields().length ; i++){
            if(obj.getClass().getDeclaredFields()[i].isAnnotationPresent(AnnotationColumn.class)){
                AnnotationColumn col = obj.getClass().getDeclaredFields()[i].getAnnotation(AnnotationColumn.class);
                if(col.nom().equals("") && col.isPrimaryKey() == true)
                    res = turnIntoCapitalLetter(obj.getClass().getDeclaredFields()[i].getName());
                else if(col.nom().equals("") == false && col.isPrimaryKey() == true)
                    res = turnIntoCapitalLetter(col.nom());
            }
        }
        return res;
    }
        public static Method getPK(Object obj) throws Exception{
        String res = "";
        for(int i = 0 ; i < obj.getClass().getDeclaredFields().length ; i++){
            if(obj.getClass().getDeclaredFields()[i].isAnnotationPresent(AnnotationColumn.class)){
                AnnotationColumn col = obj.getClass().getDeclaredFields()[i].getAnnotation(AnnotationColumn.class);
                if(col.nom().equals("") && col.isPrimaryKey() == true)
                    res = turnIntoCapitalLetter(obj.getClass().getDeclaredFields()[i].getName());
            }
        }
        return obj.getClass().getMethod("get" + res);

    }
}