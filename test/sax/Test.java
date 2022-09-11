/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sax;

import java.io.IOException;
import java.util.List;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import org.xml.sax.SAXException;

/**
 *
 * @author jmburu
 */
public class Test {
    
    public static void main(String... args) throws ParserConfigurationException, SAXException, IOException 
    {
        SAXParserFactory factory = SAXParserFactory.newInstance();
        SAXParser saxParser = factory.newSAXParser();
        BaeldungHandler baeldungHandler = new BaeldungHandler();
        
        saxParser.parse(Test.class.getResourceAsStream("Baeldung.xml"), baeldungHandler);
        
        Baeldung result = baeldungHandler.getWebsite();
        
        List<BaeldungArticle> articles = result.getArticleList();

        System.out.println(articles.size());
    }
    
    

    
}


