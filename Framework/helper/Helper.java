package helper;

import java.lang.reflect.*;
import annotation.*;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

public class Helper{

//METHODS

    public static String turnIntoCapitalLetter(String text){
        return text.substring(0,1).toUpperCase().concat(text.substring(1));
    }

    public static String getTableName(Object obj) { //Return the name of the Table
        if(obj.getClass().isAnnotationPresent(Table.class)){
            Table annotation = obj.getClass().getAnnotation(Table.class);
            if(!annotation.table().equals(""))return annotation.table();
        }
        String str = obj.getClass().getName();
        return str.split("\\.")[str.split("\\.").length - 1];
    }

    public static Method[] getGetters(Object obj) throws Exception{
        List<Field> list = getColumnFields(obj);
        Method[] res = new Method[list.size()];
        int i = 0;
        for(Field field : list){
            res[i] = obj.getClass().getDeclaredMethod("get" + turnIntoCapitalLetter(field.getName()));
            i++;
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

    public static List<Field> getColumnFields(Object obj){
        List<Field> res = new ArrayList<Field>();
        for(int i = 0 ; i < obj.getClass().getDeclaredFields().length ; i++){
            if(obj.getClass().getDeclaredFields()[i].isAnnotationPresent(Column.class))
                res.add(obj.getClass().getDeclaredFields()[i]);
        }
        return res;
    }

    public static String[] getColumns(Object obj){
        List<Field> lst = getColumnFields(obj);
        String[] res = new String[lst.size()];
        int i = 0;
        for(Field field : lst){
            Column col = field.getAnnotation(Column.class);
            if(col.name().equals(""))
                res[i] = turnIntoCapitalLetter(field.getName());
            else
                res[i] = turnIntoCapitalLetter(col.name());
            i++;
        }
        return res;
    }

    public static String[] getTypeName(Object obj){
        List<Field> lst = getColumnFields(obj);
        String[] res = new String[lst.size()];
        int i = 0;
        for (Field field : lst) {
            res[i] = field.getType().getName();
            i++;
        }
        return res;
    }

    public static String getPKName(Object obj){
        String res = "";
        for(int i = 0 ; i < obj.getClass().getDeclaredFields().length ; i++){
            if(obj.getClass().getDeclaredFields()[i].isAnnotationPresent(Column.class)){
                Column col = obj.getClass().getDeclaredFields()[i].getAnnotation(Column.class);
                if(col.name().equals("") && col.isPrimaryKey() == true)
                    res = turnIntoCapitalLetter(obj.getClass().getDeclaredFields()[i].getName());
                else if(col.name().equals("") == false && col.isPrimaryKey() == true)
                    res = turnIntoCapitalLetter(col.name());
            }
        }
        return res;
    }
    
    public static Method getPK(Object obj) throws Exception{
        String res = "";
        for(int i = 0 ; i < obj.getClass().getDeclaredFields().length ; i++){
            if(obj.getClass().getDeclaredFields()[i].isAnnotationPresent(Column.class)){
                Column col = obj.getClass().getDeclaredFields()[i].getAnnotation(Column.class);
                if(col.isPrimaryKey() == true)
                    res = turnIntoCapitalLetter(obj.getClass().getDeclaredFields()[i].getName());
            }
        }
        return obj.getClass().getMethod("get" + res);

    }

    public boolean checkIfRedundant(String mot,Enumeration<String> lst){
        while(lst.hasMoreElements()){
            String str = lst.nextElement();
            if(str.equals(mot)) return true;
        }
        return false;
    }
}
