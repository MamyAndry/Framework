package sorona;

import annotation.*;
import etu2060.framework.FileUpload;
import etu2060.framework.ModelView;
import java.util.HashMap;
import dao.*;
import java.sql.*;

@Scope(isSingleton = "singleton")
public class Emp {
    // @AnnotationColumn(isPrimaryKey=true)
    Integer id = 1;
    
    @AnnotationColumn()
    String nom;
    
    @AnnotationColumn()
    String prenom;

    @AnnotationColumn()
    Integer[] option;

    FileUpload empUpload;

    HashMap<String,Object> session;

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
    public void setEmpUpload(FileUpload empUpload) {
        this.empUpload = empUpload;
    }
    public void setSession(HashMap<String, Object> session) {
        this.session = session;
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
    public FileUpload getEmpUpload() {
        return empUpload;
    }
    public HashMap<String, Object> getSession() {
        return session;
    }

//CONSTRUCTOR
    public Emp(){}

    public Emp(String nom , String prenom){
        this.setNom(nom);
        this.setPrenom(prenom);
    }

//METHODS

    @Url(url = "emp-all.do")
    public ModelView findAll(){
        ModelView m = new ModelView("emp.jsp");
        return m;
        }

    @Url(url = "emp-one.do")
    public ModelView find(String test){
        ModelView m = new ModelView("emp2.jsp");
        m.addItem("id", test);
        return m;
    }

    @Session()
    @Url(url = "login.do")
    public ModelView login(String profile,String pwd){
        if(profile.equals("admin") && pwd.equals("admin")){
            ModelView m = new ModelView("form.jsp");
            m.addSessionItem("profile", "admin");
            this.getSession().put("current", "profile");
            return m;
        }else if(profile.equals("user") && pwd.equals("user")){
            ModelView m = new ModelView("form.jsp");
            m.addSessionItem("profile", "user");
            return m;
        }
        else{
            return new ModelView("index.jsp");
        }
    }


    @Url(url = "invalidate.do")
    public ModelView invalidateTest(){
        ModelView m = new ModelView("testSessionInvalidate.jsp");
        m.invalidateSession();
        return m;
    }

    @Session
    @Authentification(auth = "admin")
    @Url(url = "save-emp.do")
    public ModelView save(){
        ModelView m = new ModelView("emp.jsp");
        HashMap<String,Object> lst = new HashMap<String,Object>();
        m.setData(lst);
        m.addItem("nom",this.getNom());
        m.addItem("prenom",this.getPrenom());
        m.addItem("option", this.getOption());
        m.addItem("empUpload", this.getEmpUpload());
        this.getSession().put("current", "gg");
        return m;
    }

    @Json
    @Url(url = "testJsonAnnotation.do")
    public Emp testJson(){
        return new Emp("RATSIMBAZAFY","Mamisoa");
    }
}
