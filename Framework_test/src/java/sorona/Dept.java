package sorona;
        
import annotation.AnnotationUrl;
import etu2060.framework.modelView.ModelView;

public class Dept {
    int id = 2;
    
//    @AnnotationUrl(url = "dept-all")
    public ModelView findAll(){
        ModelView m = new ModelView("dept.jsp");
        return m;
    }
}
