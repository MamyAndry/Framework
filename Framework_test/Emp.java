package sorona;

import annotation.*;
import etu2060.framework.ModelView;
import java.util.HashMap;
import dao.*;
import java.sql.*;

@AnnotationTable()
public class Emp {
    // @AnnotationColumn(isPrimaryKey=true)
    Integer id = 1;
    
    @AnnotationColumn()
    String nom;
    
    @AnnotationColumn()
    String prenom;

    @AnnotationColumn()
    Integer[] option;

//SETTERS
    public void setNom(String n){
        this.nom = n;
    }
    public void setPrenom(String p){
        this.prenom = p;
    }
    public void setOption(Integer[] c){
        this.option = c;
    }
    public void setId(Integer i){
        this.id = i;
    }

//GETTERS
    public String getNom(){
        return this.nom;
    }
    public String getPrenom(){
        return this.prenom;
    }
    public Integer[] getOption(){
        return this.option;
    }
    public Integer getId(){
        return this.id;
    }

//CONSTRUCTOR


//METHODS

    @AnnotationUrl(url = "emp-all.do")
    public ModelView findAll(){
        ModelView m = new ModelView("emp.jsp");
        return m;
        }

    @AnnotationUrl(url = "emp-one.do")
    public ModelView find(String test){
        ModelView m = new ModelView("emp2.jsp");
        m.addItem("id", test);
        return m;
    }


    @AnnotationUrl(url = "save-emp.do")
    public ModelView save(){
        ModelView m = new ModelView("emp.jsp");
        HashMap<String,Object> lst = new HashMap<String,Object>();
        m.setData(lst);
        m.addItem("nom",this.getNom());
        m.addItem("prenom",this.getPrenom());
        m.addItem("option", this.getOption());
        // Connection con = null;
        // try{
        //     con = new DbConnection("mamisoa","prom15","test").connectToPostgres();
        //     GenericDao.save(con,this);
        // }
        // catch(Exception e){
        //     e.printStackTrace();
        // }
        // finally{
        //     try{
        //         con.close();
        //     }catch(Exception ex){}
        // }
        return m;
    }
}
