package sorona;

import etu2060.framework.annotation.Url;
import etu2060.framework.ModelView;
import java.util.HashMap;

public class Dept {
    Integer id = 2;

    @Url(url = "dept-all.do")
    public ModelView findAll(){
        ModelView m = new ModelView("dept.jsp");
        HashMap<String,Object> lst = new HashMap<String,Object>();
        m.setData(lst);
        m.addItem("mpampiasa",1);
        return m;
    }
}
