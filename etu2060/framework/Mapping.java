/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package framework;

/**
 *
 * @author mamisoa
 */
public class Mapping {
    String className;
    String methods;

    public String getClassName() {
        return className;
    }

    public String getMethods() {
        return methods;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public void setMethods(String methods) {
        this.methods = methods;
    }

    public Mapping(String className, String methods) {
        this.setClassName(className);
        this.setMethods(methods);
    }
    
}
