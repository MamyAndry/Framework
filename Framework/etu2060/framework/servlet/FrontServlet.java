package etu2060.framework.servlet;

import etu2060.framework.annotation.Url;
import etu2060.framework.annotation.Scope;
import etu2060.framework.annotation.Session;
import etu2060.framework.annotation.Json;
import etu2060.framework.annotation.Authentification;

import etu2060.framework.Mapping;
import etu2060.framework.ModelView;
import etu2060.framework.FileUpload;

import java.lang.reflect.*;

import jakarta.servlet.http.Part;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import jakarta.servlet.annotation.*;

import java.io.PrintWriter;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;

import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.sql.Date;
import java.util.HashMap;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Collection;
import java.util.Collections;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.google.gson.Gson;

@MultipartConfig(
  fileSizeThreshold = 1024 * 1024 * 1, // 1 MB
  maxFileSize = 1024 * 1024 * 10,      // 10 MB
  maxRequestSize = 1024 * 1024 * 100   // 100 MB
)

public class FrontServlet extends HttpServlet {
    HashMap< String , Mapping > MappingUrls;
    HashMap< String , Object > singleton;
    String sessionName;
    String sessionProfile;
    String sessionFields;

//SETTERS
    public void setMappingUrls(HashMap<String, Mapping> MappingUrls){
        this.MappingUrls = MappingUrls;
    }
    public void setSingleton(HashMap< String , Object > singleton){
        this.singleton = singleton;
    }
    public void setSessionName(String sessionName) {
        this.sessionName = sessionName;
    }
    public void setSessionProfile(String sessionProfile) {
        this.sessionProfile = sessionProfile;
    }
    public void setSessionFields(String sessionFields) {
        this.sessionFields = sessionFields;
    }

//GETTERS
    public HashMap<String, Mapping> getMappingUrls() {
        return MappingUrls;
    }
    public HashMap< String , Object >  getSingleton() {
        return singleton;
    }
    public String getSessionName() {
        return sessionName;
    }
    public String getSessionProfile() {
        return sessionProfile;
    }
    public String getSessionFields() {
        return sessionFields;
    }

//METHODS
    public static String capitalize(String text){
        return text.substring(0,1).toUpperCase().concat(text.substring(1));
    }

    public ArrayList<String> findClasses(File directory, String packageName) throws ClassNotFoundException {
        ArrayList<String> classes = new ArrayList<String>();
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
        return classes;
    }

    public ArrayList<String> getClasses(String packageName) throws ClassNotFoundException, IOException, URISyntaxException{
        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        String path = packageName.replace('.', '/');
        Enumeration<URL> resources = classLoader.getResources(path);
        ArrayList<File> dirs = new ArrayList<File>();
        while (resources.hasMoreElements()){
            URL resource = resources.nextElement();
            URI uri = new URI(resource.toString());
            dirs.add(new File(uri.getPath()));
        }
        ArrayList<String> classes = new ArrayList<String>();
        for (File directory : dirs){
            classes.addAll(findClasses(directory, packageName));
        }
        return classes;
    }


    @Override
    public void init() throws ServletException{
        HashMap< String , Mapping > temp = new HashMap<String,Mapping>();
        HashMap< String , Object > single = new HashMap< String , Object >();
        try{
            ArrayList<String> list = getClasses(getInitParameter("modelPackage").trim());
            String sessName = getInitParameter("Session_name").trim();
            String sessProf = getInitParameter("Session_profile").trim();
            String session = getInitParameter("Session_fields").trim();

            this.setSessionName(sessName);
            this.setSessionProfile(sessProf);
            this.setSessionFields(session);

            for(String element : list){
                Class<?> obj = Class.forName(element);
                if( obj.isAnnotationPresent(Scope.class) ){
                    Scope scope = obj.getAnnotation(Scope.class);
                    if(scope.isSingleton().equals("singleton")){
                        single.put(element , null);
                    }
                }
               Method[] methods = obj.getDeclaredMethods();
               for(Method method : methods){
                   if(method.isAnnotationPresent(Url.class)){
                       Url annotation = method.getAnnotation(Url.class);
                       temp.put(annotation.url(),new Mapping(element ,method.getName()));
                   }
               }
            }
            this.setSingleton(single);
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

    public FileUpload uploadTreatment(Collection<Part> lst , Field field) throws Exception{
        FileUpload res = new FileUpload();
        Part filePart = null; 
        String fieldName = field.getName();
        for(Part part : lst){
            if(part.getName().equals(fieldName)){
                filePart = part;
                break;
            }
        }
        InputStream input = filePart.getInputStream();
        ByteArrayOutputStream buffers = new ByteArrayOutputStream();
        byte[] buffer = new byte[(int)filePart.getSize()];
        int read = 0;
        while( (read = input.read( buffer , 0 , buffer.length)) != -1){
            buffers.write(buffer, 0, read);
        }
        res.setName(getFileName(filePart));
        res.setBytes(buffers.toByteArray());
        return res;
    }

    private String getFileName(Part part) {
        String contentDisposition = part.getHeader("content-disposition");
        String[] parts = contentDisposition.split(";");
        for (String partStr : parts) {
            if (partStr.trim().startsWith("filename"))
                return partStr.substring(partStr.indexOf('=') + 1).trim().replace("\"", "");
        }
        return null;
    }
    
    public ArrayList<String> getListOfParameterNames(HttpServletRequest request){
        ArrayList<String> res = new ArrayList<String>();
        Enumeration<String> query = request.getParameterNames();
        while(query.hasMoreElements()){
            String attribut = query.nextElement();
            res.add(attribut);
        }
        return res;
    } 
    
    public ArrayList<Object> getFunctionArgument(HttpServletRequest request , Object obj ,  Method method) throws Exception{
        ArrayList<Object> lst = new ArrayList<Object>();
        Parameter[] param = method.getParameters();
        ArrayList<String> list = getListOfParameterNames(request);
        for(String attribut : list){
            for(int i = 0 ; i < param.length ; i++){
                if(attribut.contains("[]") && param[i].getName().equals(attribut.subSequence(0, attribut.toCharArray().length - 2))){
                        String[] value = request.getParameterValues(attribut);
                        Class<?> fieldType = obj.getClass().getDeclaredFields()[i].getType();
                        Class<?> componentClass = fieldType.getComponentType();
                        Object temp = Array.newInstance(componentClass , value.length);
                        for(int j = 0 ; j < value.length ; j++){
                            System.out.println("value = " + value[j] ); 
                            Array.set( temp , j , componentClass.getDeclaredConstructor(String.class).newInstance(value[j]));
                        }
                        lst.add(temp);
                        break;
                }else{
                    if(param[i].getName().equals(attribut)){
                        Class<?> fieldType = param[i].getType();
                        if(fieldType.getName().equals("java.sql.Date")){
                            lst.add(Date.valueOf(request.getParameter(attribut).trim()));
                        }else{
                            lst.add(fieldType.getDeclaredConstructor(String.class).newInstance(request.getParameter(attribut).trim()));
                        }
                    }
                }
            }
        }
        return lst;
    }

    public void setDefault(Object obj) throws Exception{
        for( Field field : obj.getClass().getDeclaredFields() ){
            obj.getClass().getDeclaredMethod("set" + capitalize(field.getName()) , field.getType() ).invoke(obj , (Object) null );
        }
    }

    public Object setDynamic(HttpServletRequest request , String className , Object obj) throws Exception{
        ArrayList<String> lst = getListOfParameterNames(request);
        System.out.println(lst);
        for(String attribut : lst){
            for(int i = 0 ; i < obj.getClass().getDeclaredFields().length ; i++){
                //Check if the input is a checkbox type
                if(attribut.contains("[]") && obj.getClass().getDeclaredFields()[i].getName().equals(attribut.subSequence(0, attribut.toCharArray().length - 2))){
                    String[] value = request.getParameterValues(attribut);
                    System.out.println(attribut);
                    Class<?> fieldType = obj.getClass().getDeclaredFields()[i].getType();
                    Class<?> componentClass = fieldType.getComponentType();
                    Object temp = Array.newInstance(componentClass , value.length);
                    for(int j = 0 ; j < value.length ; j++){
                        Array.set( temp , j , componentClass.getDeclaredConstructor(String.class).newInstance(value[j]));
                    }   
                    obj.getClass().getDeclaredMethod("set" + capitalize(attribut.subSequence(0, attribut.toCharArray().length - 2).toString()) , fieldType ).invoke( obj , temp );
                
                }else{
                    //Check if the input is a file type
                    if(obj.getClass().getDeclaredFields()[i].getType().getName().equals("etu2060.framework.FileUpload")){
                            FileUpload fu = uploadTreatment(request.getParts(), obj.getClass().getDeclaredFields()[i]);
                            obj.getClass().getDeclaredMethod("set" + capitalize(obj.getClass().getDeclaredFields()[i].getName()) , obj.getClass().getDeclaredFields()[i].getType() ).invoke( obj , fu );
                    
                    }else{
                        
                        if(obj.getClass().getDeclaredFields()[i].getName().equals(attribut)){
                            Class<?> fieldType = obj.getClass().getDeclaredFields()[i].getType();
                            if(fieldType.getName().equals("java.sql.Date")){
                                obj.getClass().getDeclaredMethod("set" + capitalize(attribut) , fieldType ).invoke( obj , Date.valueOf(request.getParameter(attribut).trim()) );
                            }else{
                                Object temp = fieldType.getDeclaredConstructor(String.class).newInstance(request.getParameter(attribut).trim());
                                obj.getClass().getDeclaredMethod("set" + capitalize(attribut) , fieldType ).invoke( obj , temp );
                                break;
                            }
                        }
                    }
                }
            }
        }
        return obj;
    }
    
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        PrintWriter out = response.getWriter();
        try{
            String[] values = request.getRequestURI().split("/");
            Object obj = null;
            String key = values[values.length-1];
            // out.print("<p>");
            // out.println(this.getMappingUrls());
            // out.print("</p>");
            // out.print("<p>");
            // out.println(this.getSingleton());
            // out.print("</p>");

            if(this.getMappingUrls().containsKey(key)){
                Mapping map = this.getMappingUrls().get(key);
                String methodstr = map.getMethods();
                String keySingleton = map.getClassName();

                if(this.getSingleton().containsKey(keySingleton)){
                    Object instance = this.getSingleton().get(keySingleton);
                    if(instance == null){
                        this.getSingleton().replace(keySingleton , Class.forName(keySingleton).getConstructor().newInstance());
                        instance = this.getSingleton().get(keySingleton);
                        setDefault(instance);
                        obj = instance;
                    }else{
                        obj = Class.forName(keySingleton).getConstructor().newInstance();
                        setDefault(obj);
                    }
                }else obj = Class.forName(keySingleton).getConstructor().newInstance();

                Method[] listMethod = obj.getClass().getDeclaredMethods();
                Method method = null;
                int i = 0;

                while( !listMethod[i].getName().equals(methodstr) ){
                    i++;
                }

                method = listMethod[i];
                
                String session_value = (String) request.getSession().getAttribute(this.getSessionProfile());
                
                if( method.isAnnotationPresent(Authentification.class) && !method.getAnnotation(Authentification.class).auth().isEmpty() && !method.getAnnotation(Authentification.class).auth().equals(session_value) ){
                    throw new Exception("Please authentificate yourself<br>");
                }
                ModelView view = null;

                ArrayList<Object> args = new ArrayList<Object>();
                // Verify if there are data sent
                if( request.getParameterNames().hasMoreElements()    ){
                    obj = setDynamic(request , map.getClassName() , obj);
                    args = getFunctionArgument( request , obj , method);
                }
                if( method.isAnnotationPresent(Json.class) ){
                    out.print( new Gson().toJson(method.invoke(obj , args.toArray())));
                }else{
                    
                    //Ajout de session dans la classe instancee
                    if( method.isAnnotationPresent(Session.class) ){
                        HttpSession session = request.getSession();
                        ArrayList<String> lstTemp = Collections.list(session.getAttributeNames());
                        HashMap<String,Object> lst = new HashMap<String,Object>();  
                        for(String str : lstTemp){
                            lst.put(str, session.getAttribute(str));
                        }
                        Method meth = obj.getClass().getDeclaredMethod("set"+capitalize(this.getSessionFields()), HashMap.class);
                        meth.invoke(obj , lst);
                    }

                    
                    view = (ModelView) method.invoke( obj , args.toArray());

                
                    //Gestion de session
                    if(method.isAnnotationPresent(Session.class)){
                        HttpSession session 
                        = request.getSession();
                        Method meth = obj.getClass().getDeclaredMethod("get"+capitalize(this.getSessionFields()));
                        HashMap<String,Object> lst = (HashMap<String , Object>)meth.invoke(obj); 
                        for(String str : lst.keySet()){
                            session.setAttribute(str, lst.get(str));
                        }
                    }
                    //Invalidate session
                    if( view.checkInvalidateSession() ){
                        request.getSession().invalidate();
                    }

                    if( !view.getSessionToDelete().isEmpty() ){
                     for( String str : view.getSessionToDelete()){
                        request.getSession().removeAttribute(str);
                     }   
                    }

                    //Return Json
                    if(view.getIsJson()){
                        out.print( new Gson().toJson(view.getData()));
                    }else{
                        if(view.getData() != null){
                            for(String dataKey : view.getData().keySet()){
                                request.setAttribute(dataKey , view.getData().get(dataKey));
                            }
                        }
        
                        if(view.getSession() != null){
                            for(String dataKey : view.getSession().keySet()){
                                HttpSession session = request.getSession();
                                session.setAttribute(dataKey, view.getSession().get(dataKey));
                            }
                        }
                        request.getRequestDispatcher(view.getUrl()).forward(request,response);
                    }
                }
            }
        }catch(Exception e){
            out.print("<script> alert("+e.getMessage()+")</script>");
            out.print(e.getMessage()+"<br>");
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
}