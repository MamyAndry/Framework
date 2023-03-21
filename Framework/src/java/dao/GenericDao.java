package dao;



import java.sql.*;
import java.util.ArrayList;
import java.lang.reflect.*;
import helper.DaoHelper;

public class GenericDao{

//METHODS

    public static void save(Connection con,Object obj) throws Exception{
        String query = "INSERT INTO " + DaoHelper.getTableName(obj.getClass().getName()) + " VALUES(";
        String[] typeName = DaoHelper.getTypeName(obj); 
        Method[] getters = DaoHelper.getGetters(obj);
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
        Statement stmt =  con.createStatement();
        stmt.executeUpdate(query);
    }

    public static void update(Connection con,Object obj) throws Exception {
        String tableName = DaoHelper.getTableName(obj.getClass().getName());
        String query = "UPDATE "+ tableName +" SET ";
        String[] typeName = DaoHelper.getTypeName(obj); 
        String[] fields = DaoHelper.getFields(obj); 
        Method[] getters = DaoHelper.getGetters(obj);
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
        String condition = " WHERE id" + tableName +" = '" + obj.getClass().getMethod("getId" + tableName).invoke(obj, (Object[]) null)+"'";
        query += condition;
        Statement stmt = con.createStatement();
        stmt.executeUpdate(query);
    }

    public static void delete(Connection con,Object obj) throws Exception {
        String tableName = DaoHelper.getTableName(obj.getClass().getName());
        String query = "DELETE FROM " + tableName ;
        String condition = " WHERE id" + tableName  +" = '" + obj.getClass().getMethod("getId" + tableName).invoke(obj, (Object[]) null)+"'";
        // System.out.println(condition);
        query += condition;
        //System.out.println(query);
        Statement stmt = con.createStatement();
        stmt.executeUpdate(query);
    }

    public static <T> ArrayList<T> findAll(Connection con,Object obj) throws Exception { 
        ArrayList<T> list = new ArrayList<T>();
        String query = "SELECT * FROM " + DaoHelper.getTableName(obj.getClass().getName());
        Method[] setters = DaoHelper.getSetters(obj,query);
        Statement stmt = con.createStatement();
        ResultSet rs = stmt.executeQuery(query);
        ResultSetMetaData rsmd = rs.getMetaData();
        while (rs.next()) {
            T temp = (T)obj.getClass().getDeclaredConstructor().newInstance((Object[]) null);
            for (int col = 1; col <= rsmd.getColumnCount(); col++) {
                setters[col-1].invoke(temp,rs.getString(col));
            }
            list.add(temp);
        }
        return list;
    
    }

    public static <T> T findById(Connection con,Object obj,Object id) throws Exception {
        String tableName = DaoHelper.getTableName(obj.getClass().getName());
        String query = "SELECT * FROM " + tableName ;
        String condition = " WHERE id" + tableName  +" = '" + id +"'";
        query += condition; 
        Method[] setters = DaoHelper.getSetters(obj,query);
        Statement stmt = con.createStatement();
        ResultSet rs = stmt.executeQuery(query);
        ResultSetMetaData rsmd = rs.getMetaData();
        rs.next();
        T temp = (T)obj.getClass().getDeclaredConstructor().newInstance((Object[]) null);
        for (int col = 1; col <= rsmd.getColumnCount(); col++) {
            setters[col-1].invoke(temp,rs.getString(col));
        }
        return temp;
    }
}
