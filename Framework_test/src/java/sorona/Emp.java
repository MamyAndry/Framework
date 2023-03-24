package sorona;

import annotation.AnnotationUrl;
import etu2060.framework.modelView.ModelView;
        
public class Emp {
    int id = 1;
    
    @AnnotationUrl(url = "emp-all")
    public ModelView findAll(){
        ModelView m = new ModelView("emp.jsp");
        return m;
    }
}
