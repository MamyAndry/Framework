package etu2060.framework;

import java.util.HashMap;


public class ModelView {
    String url;
    HashMap<String,Object> data = new HashMap<String,Object>();

//GETTERS
    public String getUrl() {
        return url;
    }
    public HashMap<String, Object> getData(){
        return this.data;
    }

//SETTERS
    public void setUrl(String view) {
        this.url = view;
    }
    public void setData(HashMap<String,Object> lst){
        this.data = lst;
    }

//CONSTRUCTOR
    public ModelView(String v) {
        this.setUrl(v);
    }
    public ModelView(){}

//METHOD
    public void addItem(String key , Object value){
        this.getData().put(key,value);
    }



}
