package etu2060.framework;

import java.util.HashMap;
import java.util.List;
import java.util.ArrayList;

public class ModelView {
    String url;
    HashMap<String,Object> data = new HashMap<String,Object>();
    HashMap<String,Object> session = new HashMap<String,Object>();
    boolean isJson;
    boolean invalidateSession = false;
    List<String> sessionToDelete = new ArrayList<String>();

//GETTERS
    public String getUrl() {
        return url;
    }
    public HashMap<String, Object> getData(){
        return this.data;
    }
    public HashMap<String, Object> getSession(){
        return this.data;
    }
    public boolean getIsJson(){
        return isJson;
    }
//SETTERS
    public void setUrl(String view) {
        this.url = view;
    }
    public void setData(HashMap<String,Object> lst){
        this.data = lst;
    }
    public void setSession(HashMap<String, Object> session) {
        this.session = session;
    }

    public void setIsJson(boolean b){
        this.isJson = b;
    }
    public List<String> getSessionToDelete() {
        return sessionToDelete;
    }
//CONSTRUCTOR
    public ModelView(String v) {
        this.setUrl(v);
    }
    public ModelView(boolean v) {
        this.setIsJson(v);
    }
    public ModelView(){}

//METHOD
    public void addItem(String key , Object value){
        this.getData().put(key,value);
    }
    public void addSessionItem(String key , Object value){
        this.getSession().put(key,value);
    }
    public void invalidateSession(){
        this.invalidateSession = true;
    }
    public boolean checkInvalidateSession(){
        return this.invalidateSession;
    }
    public void addSessionToDelete(String del){
        this.getSessionToDelete().add(del);
    }



}
