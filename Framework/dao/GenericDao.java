package dao;

import java.sql.*;
import java.util.ArrayList;
import java.lang.reflect.*;
import helper.Helper;

public class GenericDao{

//METHODS

    public static void save(Connection con,Object obj) throws Exception{
        String query = "INSERT INTO " + Helper.getTableName(obj.getClass().getName()) + " VALUES(";
        String[] typeName = Helper.getTypeName(obj);
        Method[] getters = Helper.getGetters(obj);
        for (int i = 0; i < typeName.length; i++) {
            if (typeName[i].equals("java.lang.String")) {
                query += "'" + getters[i].invoke(obj,(Object[]) null) + "'";
            } else if (typeName[i].equals("java.util.Date") || typeName[i].equals("java.sql.Date")) {
                query += "TO_DATE('" + getters[i].invoke(obj, (Object[]) null) + "','YYYY-MM-DD')";
            } else if (typeName[i].equals("double")|| typeName[i].equals("int")) {
                query += "" + getters[i].invoke(obj, (Object[]) null);
            }else if (typeName[i].equals("java.time.LocalTime")) {
                query += "'" + getters[i].invoke(obj, (Object[]) null)+"'";
            }
            if(i < typeName.length-1){
                query += ",";
            }
        }
        query += ")";
        System.out.println(query);
        Statement stmt =  con.createStatement();
        stmt.executeUpdate(query);
    }

    public static void update(Connection con,Object obj) throws Exception {
        String id = Helper.getPKName(obj);
        String tableName = Helper.getTableName(obj.getClass().getName());
        String query = "UPDATE "+ tableName +" SET ";
        String[] typeName = Helper.getTypeName(obj);
        String[] fields = Helper.getFields(obj);
        Method[] getters = Helper.getGetters(obj);
        for (int i = 0; i < typeName.length; i++) {
            if (typeName[i].equals("java.lang.String")) {
                query += fields[i] + " = '" + getters[i].invoke(obj,(Object[]) null) + "'";
            } else if (typeName[i].equals("java.util.Date") || typeName[i].equals("java.sql.Date")) {
                query += fields[i] + " = TO_DATE('" + getters[i].invoke(obj, (Object[]) null) + "','YYYY-MM-DD')";
            } else if (typeName[i].equals("double")|| typeName[i].equals("int")) {
                query += fields[i] + " = " + getters[i].invoke(obj, (Object[]) null);
            }else if (typeName[i].equals("java.time.LocalTime")) {
                query += fields[i] + " = '" + getters[i].invoke(obj, (Object[]) null)+"'";
            }
            if(i < typeName.length-1){
                query += ",";
            }
        }
        String condition = " WHERE " + id +" = '" + Helper.getPK(obj).invoke(obj, (Object[]) null)+"'";
        query += condition;
        System.out.println(query);
        Statement stmt = con.createStatement();
        stmt.executeUpdate(query);
    }

    public static void delete(Connection con,Object obj) throws Exception {
        String idTable = Helper.getPKName(obj);
        String tableName = Helper.getTableName(obj.getClass().getName());
        String query = "DELETE FROM " + tableName ;
        String condition = " WHERE " + idTable  +" = '" + Helper.getPK(obj).invoke(obj, (Object[]) null)+"'";
        query += condition;
        System.out.println(query);
        Statement stmt = con.createStatement();
        stmt.executeUpdate(query);
    }

    public static void deleteById(Connection con,Object obj,Object id) throws Exception {
        String idTable = Helper.getPKName(obj);
        String tableName = Helper.getTableName(obj.getClass().getName());
        String query = "DELETE FROM " + tableName ;
        String condition = " WHERE " + idTable  +" = '" + id +"'" ;
        query += condition;
        System.out.println(query);
        Statement stmt = con.createStatement();
        stmt.executeUpdate(query);
    }

    public static <T> ArrayList<T> findAll(Connection con,Object obj) throws Exception {
        ArrayList<T> list = new ArrayList<T>();
        String query = "SELECT * FROM " + Helper.getTableName(obj.getClass().getName());
        System.out.println(query);
        Statement stmt = con.createStatement();
        ResultSet rs = stmt.executeQuery(query);
        ResultSetMetaData rsmd = rs.getMetaData();
        while (rs.next()) {
            T temp = (T)obj.getClass().getDeclaredConstructor().newInstance((Object[]) null);
            ArrayList<Field> attribut = Helper.getColumnFields(temp);
            for (int col = 1; col <= rsmd.getColumnCount(); col++) {
                Class<?> fieldType = attribut.get(col-1).getType();
                if(fieldType.getName().equals("int"))
                    temp.getClass().getDeclaredMethod("set" + Helper.turnIntoCapitalLetter(attribut.get(col-1).getName()) , fieldType ).invoke( temp , Integer.parseInt(rs.getString(col)) );
                else if(fieldType.getName().equals("double"))
                    temp.getClass().getDeclaredMethod("set" + Helper.turnIntoCapitalLetter(attribut.get(col-1).getName()) , fieldType ).invoke( temp , Double.parseDouble(rs.getString(col)) );
                else if(fieldType.getName().equals("java.util.Date") || fieldType.getName().equals("java.sql.Date"))
                    temp.getClass().getDeclaredMethod("set" + Helper.turnIntoCapitalLetter(attribut.get(col-1).getName()) , fieldType ).invoke( temp , Date.valueOf(rs.getString(col)) );
                else
                    temp.getClass().getDeclaredMethod("set" + Helper.turnIntoCapitalLetter(attribut.get(col-1).getName()) , fieldType ).invoke( temp , fieldType.cast(rs.getString(col)) );
            }
            list.add(temp);
        }
        return list;

    }

    public static <T> T findById(Connection con,Object obj,Object id) throws Exception {
        String idTable = Helper.getPKName(obj);
        String tableName = Helper.getTableName(obj.getClass().getName());
        String query = "SELECT * FROM " + tableName ;
        String condition = " WHERE " + idTable + " = '" + id + "'";
        query += condition;
        System.out.println(query);
        Statement stmt = con.createStatement();
        ResultSet rs = stmt.executeQuery(query);
        ResultSetMetaData rsmd = rs.getMetaData();
        rs.next();
        T temp = (T)obj.getClass().getDeclaredConstructor().newInstance((Object[]) null);
        ArrayList<Field> attribut = Helper.getColumnFields(temp);
        for (int col = 1; col <= rsmd.getColumnCount(); col++) {
            Class<?> fieldType = attribut.get(col-1).getType();
            if(fieldType.getName().equals("int"))
                temp.getClass().getDeclaredMethod("set" + Helper.turnIntoCapitalLetter(attribut.get(col-1).getName()) , fieldType ).invoke( temp , Integer.parseInt(rs.getString(col)) );
            else if(fieldType.getName().equals("double"))
                temp.getClass().getDeclaredMethod("set" + Helper.turnIntoCapitalLetter(attribut.get(col-1).getName()) , fieldType ).invoke( temp , Double.parseDouble(rs.getString(col)) );
            else if(fieldType.getName().equals("java.util.Date") || fieldType.getName().equals("java.sql.Date"))
                temp.getClass().getDeclaredMethod("set" + Helper.turnIntoCapitalLetter(attribut.get(col-1).getName()) , fieldType ).invoke( temp , Date.valueOf(rs.getString(col)) );
            else
                temp.getClass().getDeclaredMethod("set" + Helper.turnIntoCapitalLetter(attribut.get(col-1).getName()) , fieldType ).invoke( temp , fieldType.cast(rs.getString(col)) );
        }
        return temp;
    }
}
