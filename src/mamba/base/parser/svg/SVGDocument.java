/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mamba.base.parser.svg;

import java.util.ArrayList;
import mamba.base.MambaShape;

/**
 *
 * @author jmburu
 */
public class SVGDocument implements SVGElement{
    public double width, height;
    public String xmlns = null;
    public String xmlns_xlink = null;
    
    public ArrayList<MambaShape> elements;
    
    public SVGDocument()
    {
        width = 0; height = 0;
        elements = new ArrayList();
    }
        
    public boolean isRoot()
    {
        return xmlns != null && xmlns_xlink != null;
    }
    
    public void setXmlns(String xmlns, String xmlns_xlink)
    {
        this.xmlns = xmlns;
        this.xmlns_xlink = xmlns_xlink;
    }
    
    public void addShape(MambaShape shape)
    {
        elements.add(shape);
    }
}
