/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package etu2060.framework.servlet;

import annotation.AnnotationUrl;
import etu2060.framework.Mapping;
import java.lang.reflect.*;
import etu2060.framework.ModelView;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.PrintWriter;
import java.util.HashMap;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.Method;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.logging.Level;
import java.util.logging.Logger;

public class FrontServlet extends HttpServlet {
    HashMap<String,Mapping> MappingUrls;

//SETTERS
    public void setMappingUrls(HashMap<String, Mapping> MappingUrls){
        this.MappingUrls = MappingUrls;
    }

//GETTERS
    public HashMap<String, Mapping> getMappingUrls() {
        return MappingUrls;
    }
//METHODS
    public ArrayList<String> findClasses(File directory, String packageName) throws ClassNotFoundException {
        ArrayList<String> classes = new ArrayList<String>();
//        System.out.println("directory : "+directory+" packageName : "+packageName);
        if (!directory.exists()){
            return classes;
        }
        File[] files = directory.listFiles();
        for (File file : files){
            if (file.isDirectory()){
                classes.addAll(findClasses(file, packageName + "." + file.getName()));
            }else if (file.getName().endsWith(".class")){
                classes.add(packageName + '.' + file.getName().substring(0, file.getName().length() - 6));
            }
        }
        System.out.println("classes : "+classes);
        return classes;
    }

    public ArrayList<String> getClasses(String packageName) throws ClassNotFoundException, IOException, URISyntaxException{
        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        String path = packageName.replace('.', '/');
        System.out.println(path);
        Enumeration<URL> resources = classLoader.getResources(path);
        ArrayList<File> dirs = new ArrayList<File>();
        while (resources.hasMoreElements()){
            URL resource = resources.nextElement();
            URI uri = new URI(resource.toString());
            dirs.add(new File(uri.getPath()));
//            System.out.println(resource);
        }
        ArrayList<String> classes = new ArrayList<String>();
        for (File directory : dirs){
            classes.addAll(findClasses(directory, packageName));
        }
//        System.out.println(classes.size());
        return classes;
    }


    @Override
    public void init() throws ServletException{
        HashMap<String,Mapping> temp = new HashMap<String,Mapping>();
        try{
            System.out.println("modelPackage = " + getInitParameter("modelPackage"));
            ArrayList<String> list = getClasses(getInitParameter("modelPackage").trim());
            for(String element : list){
               Method[] methods = Class.forName(element).getDeclaredMethods();
               //        System.out.println(classes.size());
               for(Method m : methods){
                   if(m.isAnnotationPresent(AnnotationUrl.class)){
                       AnnotationUrl annotation = m.getAnnotation(AnnotationUrl.class);
                       temp.put(annotation.url(),new Mapping(element ,m.getName()));
                   }
               }
            }
            this.setMappingUrls(temp);

        } catch (ClassNotFoundException ex) {
            Logger.getLogger(FrontServlet.class.getName()).log(Level.SEVERE, null, ex);
            ex.printStackTrace();
        } catch (IOException ex) {
            Logger.getLogger(FrontServlet.class.getName()).log(Level.SEVERE, null, ex);
            ex.printStackTrace();
        } catch (URISyntaxException ex) {
            Logger.getLogger(FrontServlet.class.getName()).log(Level.SEVERE, null, ex);
            ex.printStackTrace();
        } catch (Exception e){
            Logger.getLogger(FrontServlet.class.getName()).log(Level.SEVERE, null, e);
            e.printStackTrace();
        }
    }



    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        PrintWriter out = response.getWriter();
        out.println("<!DOCTYPE html>");
        out.println("<html>");
        out.println("<head>");
        out.println("<title>FrontServlet</title>");
        out.println("</head>");
        out.println("<body>");
        out.println("<h3>Servlet FrontServlet at " + request.getContextPath() + "</h3>");
        try{
            String values[] = request.getRequestURI().split("/");
            String key = values[values.length-1];
            out.print("<p>");
            out.print(key);
            out.print("</br>");
            out.println(this.getMappingUrls());
            out.print("</br>");
            out.print("</p>");
             if(this.getMappingUrls().containsKey(key)){
                Mapping map = this.getMappingUrls().get(key);
                out.print("</br>");
                out.print("value = " + this.getMappingUrls().get(key));
                out.print("</br>");
                String method = map.getMethods();
                //  out.print("</br>"+method);
                Object obj = Class.forName(map.getClassName()).getConstructor().newInstance();
                Method m = obj.getClass().getDeclaredMethod(method);
                ModelView view = (ModelView) m.invoke( obj , (Object[]) null);
                // out.print(view.getView());
               request.getRequestDispatcher(view.getView()).forward(request,response);
             }
        }catch(Exception e){
            e.printStackTrace(out);
            // out.print(e.getMessage());
        }
        out.println("</body>");
        out.println("</html>");
    }


    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

}
