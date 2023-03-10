package test;

import dao.DbConnection;
import comp.ComposantLibele;
import comp.*;
import dao.*;
import helper.*;
import java.util.ArrayList;
import java.time.LocalDate;
import java.lang.reflect.*;
import java.sql.*;

public class Main{
    public static String getTabName(String str) { //Return the name of the Table 
        return str.split("\\.")[str.split("\\.").length - 1];
    }
    
    public static String turnIntoCapitalLetter(String text){
        return text.substring(0,1).toUpperCase().concat(text.substring(1));
    }
    
    public static void main(String[] args){
        String mot = "mota";
        ComposantLibele cl = new ComposantLibele();
        // DbConnection db = new DbConnection("mamisoa","prom15");
        DbConnection db = new DbConnection();
        Connection con = null;
        try{
            con = db.connectToPostgres();
            ComposantLibele obj = new ComposantLibele();
            // ArrayList<ComposantLibele> list = obj.findAll(con);
            // ArrayList<ComposantLibele> list = GenericDao.findAll(con,obj);
             ComposantLibele element = GenericDao.findById(con,obj,"P0004");
            // for(ComposantLibele element : list)
            //     System.out.println(element.getNom());
                System.out.println(element.getNom());
        }
        catch(Exception e){
            e.printStackTrace();
        }
        finally{
            try{
                con.close();
            }
            catch(Exception ex){
                ex.printStackTrace();
            }
        }
    }
}