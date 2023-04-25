package helper;

import org.w3c.dom.*;
import javax.xml.parsers.*;
import java.io.*;

public class XmlParser {
    public static String[] getValues() {
        String[] res = new String[3];
        DocumentBuilderFactory factory=DocumentBuilderFactory.newInstance();
        try {
            DocumentBuilder builder= factory.newDocumentBuilder();
            Document document=builder.parse(new File("conf.xml"));
            document.getDocumentElement().normalize();
            Element root= document.getDocumentElement();
            NodeList nList=document.getElementsByTagName("database");
            Node node = nList.item(0);
            if(node.getNodeType() == Node.ELEMENT_NODE) {
                Element el= (Element) node;
                res[0] = el.getElementsByTagName("dbname").item(0).getTextContent();
                res[0] = el.getElementsByTagName("username").item(0).getTextContent();
                res[0] = el.getElementsByTagName("password").item(0).getTextContent();
            }
            
        } catch (Exception e) {
            //TODO: handle exception
            e.printStackTrace();
        }
        return res;
    }
}
