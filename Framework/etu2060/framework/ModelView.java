package etu2060.framework;

import java.util.HashMap;


public class ModelView {
    String url;
    HashMap<String,Object> data = new HashMap<String,Object>();
    HashMap<String,Object> session = new HashMap<String,Object>();
    boolean isJson;

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
        return this.isJson;
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
    public void setJson(boolean isJson) {
        this.isJson = isJson;
    }

//CONSTRUCTOR
    public ModelView(String v) {
        this.setUrl(v);
    }
    public ModelView(boolean bl) {
        this.setJson(bl);
    }
    public ModelView(){}

//METHOD
    public void addItem(String key , Object value){
        this.getData().put(key,value);
    }
    public void addSessionItem(String key , Object value){
        this.getSession().put(key,value);
    }




}
