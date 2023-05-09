package etu2060.framework.servlet;

import annotation.AnnotationUrl;
import etu2060.framework.Mapping;
import java.lang.reflect.*;
import etu2060.framework.ModelView;
import helper.Helper;
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
import java.sql.Date;
import java.util.function.*;

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
        HashMap<String,Mappin   g> temp = new HashMap<String,Mapping>();
        try{
            // System.out.println("modelPackage = " + getInitParameter("modelPackage"));
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

    public static ArrayList<Object> getFunctionArgument(HttpServletRequest request , Method method) throws Exception{
        ArrayList<Object> lst = new ArrayList<Object>();
        Enumeration<String> query = request.getParameterNames();
        Parameter[] param = method.getParameters();
        while(query.hasMoreElements()){
            String attribut = query.nextElement();
            String value = request.getParameter(attribut);
            for(int i = 0 ; i < param.length ; i++){
                if(param[i].getName().equals(attribut)){
                    Class<?> fieldType = param[i].getType();
                    Object temp = fieldType.getDeclaredConstructor(String.class).newInstance(value);
                    lst.add(temp);
                }
            }
        }
        return lst;
    }

    public static Object setDynamic(HttpServletRequest request , String className , Object obj) throws Exception{
        obj = Class.forName(className).getConstructor().newInstance();
        Enumeration<String> query = request.getParameterNames();
        while(query.hasMoreElements()){
            String attribut = query.nextElement();
            String value = request.getParameter(attribut);
            for(int i = 0 ; i < obj.getClass().getDeclaredFields().length ; i++){
                if(obj.getClass().getDeclaredFields()[i].getName().equals(attribut)){
                    Class<?> fieldType = obj.getClass().getDeclaredFields()[i].getType();
                    Object temp = fieldType.getDeclaredConstructor(String.class).newInstance(value);
                    obj.getClass().getDeclaredMethod("set" + Helper.turnIntoCapitalLetter(attribut) , fieldType ).invoke( obj , temp );
                }
            }
        }
        return obj;
    }
    /**
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException
     */
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
            String[] values = request.getRequestURI().split("/");
            // out.print(query);
            String key = values[values.length-1];
            out.print("<p>");
            out.println(this.getMappingUrls());
            out.print("</p>");
            if(this.getMappingUrls().containsKey(key)){
                Mapping map = this.getMappingUrls().get(key);
                String method = map.getMethods();
                Object obj = Class.forName(map.getClassName()).getConstructor().newInstance();
                Method[] listMethod = obj.getClass().getDeclaredMethods();
                Method m = null;
                int i = 0;
                while(!listMethod[i].getName().equals(method)){
                    i++;
                }
                m = listMethod[i];
                ArrayList<Object> args = new ArrayList<Object>();
                // Verify if there are data sent
                if(request.getParameterNames().nextElement() != null){
                    obj = setDynamic(request , map.getClassName() , obj);
                    args = getFunctionArgument( request , m);

                }
                ModelView view = (ModelView) m.invoke( obj , (Object[]) args.toArray());if(view.getData() != null){
                    for(String dataKey : view.getData().keySet()){
                        request.setAttribute(dataKey , view.getData().get(dataKey));
                        out.print("<p>");
                        out.println(dataKey);
                        out.print("</p>");
                    }
                }
                request.getRequestDispatcher(view.getUrl()).forward(request,response);
        }
        }catch(Exception e){
            e.printStackTrace(out);
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
