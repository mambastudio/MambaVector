/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mamba.base.parser.svg;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

/**
 *
 * @author jmburu
 */
public class SVGParser {
    SAXParser saxParser = null;
    SVGDocument svgDocument = null;
    InputSource inputSource = null;
    
    public SVGParser(Class clazz, String file) throws IOException
    {
        svgDocument = new SVGDocument();
        
        try {
            SAXParserFactory factory = SAXParserFactory.newInstance();
            saxParser = factory.newSAXParser();
            saxParser.getXMLReader().setEntityResolver(new DummyEntityResolver());
            saxParser.getXMLReader().setContentHandler(new SVGHandler(svgDocument));
            inputSource = new InputSource(clazz.getResourceAsStream(file));
        } catch (ParserConfigurationException | SAXException ex) {
            Logger.getLogger(SVGParser.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public SVGParser(File file)
    {
        svgDocument = new SVGDocument();
        
        try {
            SAXParserFactory factory = SAXParserFactory.newInstance();
            saxParser = factory.newSAXParser();
            saxParser.getXMLReader().setEntityResolver(new DummyEntityResolver());
            saxParser.getXMLReader().setContentHandler(new SVGHandler(svgDocument));
            inputSource = new InputSource(new FileInputStream(file));
        } catch (ParserConfigurationException | SAXException |  IOException ex) {
            Logger.getLogger(SVGParser.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public SVGDocument parseSVG()
    {
        try {
            saxParser.getXMLReader().parse(inputSource);
            return svgDocument;
        } catch (SAXException | IOException ex) {
            Logger.getLogger(SVGParser.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }
}
