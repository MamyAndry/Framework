package etu2060.framework;

public class Mapping {
    String className;
    String methods;

//GETTERS
    public String getClassName() {
        return className;
    }

    public String getMethods() {
        return methods;
    }

//SETTERS
    public void setClassName(String className) {
        this.className = className;
    }

    public void setMethods(String methods) {
        this.methods = methods;
    }
    
//CONSTRUCTOR
    public Mapping(String className, String methods) {
        this.setClassName(className);
        this.setMethods(methods);
    }
    
}
