package sorona;
        
import annotation.AnnotationUrl;
import etu2060.framework.ModelVIew.ModelView;

public class Dept {
    int id = 2;
    
    @AnnotationUrl(url = "dept-all")
    public ModelView findAll(){
        ModelView m = new ModelView("temp2.jsp");
        return m;
    }
}
