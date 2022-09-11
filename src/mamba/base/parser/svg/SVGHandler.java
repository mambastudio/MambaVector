/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mamba.base.parser.svg;

import java.util.Arrays;
import java.util.List;
import javafx.scene.paint.Color;
import mamba.base.engine.shape.MCircle;
import mamba.base.engine.shape.MRectangle;
import static mamba.base.parser.svg.SVGShapeType.CIRCLE;
import static mamba.base.parser.svg.SVGShapeType.RECT;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

/**
 *
 * @author jmburu
 */
public class SVGHandler extends DefaultHandler{
    private final SVGDocument document;
    
    public SVGHandler(SVGDocument document)
    {
        this.document = document;
    }
    
    @Override
    public void startElement(String uri, String lName, String qName, Attributes attr) throws SAXException {
        if(qName.equals("svg"))
        {
            String xmlns = attr.getValue("xmlns");
            String xmlns_xlink = attr.getValue("xmlns:xlink");
            document.setXmlns(xmlns, xmlns_xlink);
        }
        else if(RECT.compare(qName))
        {
            double x = 0, y = 0, width, height, rx = 0, ry = 0, stroke_width = 0;
            Color fill = null, stroke = null;
            
            
            if(attr.getIndex("x") != -1)
                x = Double.valueOf(attr.getValue("x"));
            if(attr.getIndex("y") != -1)
                y = Double.valueOf(attr.getValue("y"));
            width = Double.valueOf(attr.getValue("width"));
            height = Double.valueOf(attr.getValue("height"));
            
            if(attr.getIndex("rx") != -1)
                rx = Double.valueOf(attr.getValue("rx"));
            if(attr.getIndex("ry") != -1)
                ry = Double.valueOf(attr.getValue("ry"));
            
            //style (css)
            if(attr.getIndex("style") != -1)
            {
                String[] cssAttr = attr.getValue("style").split("\\s*[:;]\\s*");
                List<String> cssAttrList = Arrays.asList(cssAttr);
                
                if(cssAttrList.contains("fill"))
                {
                    fill = Color.web(cssAttrList.get(cssAttrList.indexOf("fill")+1));
                    System.out.println(fill);
                }
                else if(cssAttrList.contains("stroke"))
                    stroke = Color.web(cssAttrList.get(cssAttrList.indexOf("stroke")+1));
                else if(cssAttrList.contains("stroke-width"))
                    stroke_width = Double.valueOf(cssAttrList.get(cssAttrList.indexOf("stroke-width")+1));
            }   
            MRectangle rectangle = new MRectangle(x, y, width, height, rx, ry, fill, stroke, stroke_width);
            document.addShape(rectangle);
        }
        else if(CIRCLE.compare(qName))
        {
            MCircle circle = new MCircle();
            document.addShape(circle);
        }
        
        
    }
    
    
    public SVGDocument getSVG()
    {
        return document;
    }
}
