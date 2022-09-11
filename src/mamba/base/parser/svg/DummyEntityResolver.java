/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mamba.base.parser.svg;

import java.io.StringReader;
import org.xml.sax.EntityResolver;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

/**
 *
 * @author jmburu
 */
public class DummyEntityResolver implements EntityResolver {
    @Override
    public InputSource resolveEntity(String publicID, String systemID)
        throws SAXException {

        return new InputSource(new StringReader(""));
    }
}
