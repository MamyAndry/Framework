package dao;

import java.sql.*;
import helper.XmlParser;

public class DbConnection{
    String username;
    String password;
    String database;

    //SETTERS
    public void setUsername(String u){this.username=u;}
    public void setPassword(String p){this.password=p;}
    public void setDatabase(String d){this.database=d;}
    
    //GETTERS
    public String getUsername(){return this.username;}
    public String getPassword(){return this.password;}
    public String getDatabase(){return this.database;}
    
    //CONSTRUCTOR
    public DbConnection(){
        String[] values = XmlParser.getValues();
        this.setUsername(values[1]);
        this.setPassword(values[2]);
        this.setDatabase(values[0]);
    }

    //METHODS
    public Connection connectToOracle(){ 
        Connection con=null;    
        try{  
            Class.forName("oracle.jdbc.driver.OracleDriver");
            con=DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521:orcl",this.getUsername(),this.getPassword());

        }catch(Exception e){System.out.println(e);}
        return con;
    }

    public Connection connectToPostgres(){
        Connection con=null;
        try
        {
          Class.forName("org.postgresql.Driver");
           con = DriverManager.getConnection("jdbc:postgresql://localhost:5432/"+this.getDatabase(),this.getUsername(),this.getPassword());
        }
        catch(Exception e){e.printStackTrace();}
        return con;
    }
}