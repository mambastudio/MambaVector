/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sax;

import java.util.Arrays;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

/**
 *
 * @author jmburu
 */
public class Test2Handler extends DefaultHandler{
    
    @Override
    public void characters(char[] ch, int start, int length) throws SAXException {
        
    }
    
    @Override
    public void startDocument() throws SAXException {
        
    }
    
    @Override
    public void startElement(String uri, String lName, String qName, Attributes attr) throws SAXException {
        //if(qName.equals("svg"))
        System.out.println(qName);
        {
            for(int i = 0; i<attr.getLength(); i++)
            {
                System.out.println(attr.getValue(i));
                
                if(attr.getQName(i).equals("style"))
                {
                    System.out.println(Arrays.toString(attr.getValue(i).split("[:;]")));
                }
            }
        }
        
    }
    
    @Override
    public void endElement(String uri, String localName, String qName) throws SAXException {
        
    }
}
