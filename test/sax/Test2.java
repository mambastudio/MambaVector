/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sax;

import mamba.base.parser.svg.DummyEntityResolver;
import java.io.IOException;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

/**
 *
 * @author jmburu
 */
public class Test2 {
    public static void main(String... args) throws ParserConfigurationException, SAXException, IOException 
    {
        SAXParserFactory factory = SAXParserFactory.newInstance();
        SAXParser saxParser = factory.newSAXParser();
        saxParser.getXMLReader().setEntityResolver(new DummyEntityResolver());
        saxParser.getXMLReader().setContentHandler(new Test2Handler());
        saxParser.getXMLReader().parse(new InputSource(Test.class.getResourceAsStream("Test2SVG.svg")));
    }
}
