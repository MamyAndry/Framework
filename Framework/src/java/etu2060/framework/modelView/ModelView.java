/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package etu2060.framework.modelView;

/**
 *
 * @author mamisoa
 */
public class ModelView {
    String view;
    
//SETTERS
    public void setView(String view) {
        this.view = view;
    }
    
//GETTERS
    public String getView() {
        return view;
    }
    
//CONSTRUCTOR
    public ModelView(){
    }
    
    public ModelView(String view) {
        this.setView(view);
    }


}
