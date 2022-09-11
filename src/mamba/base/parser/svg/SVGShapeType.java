/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mamba.base.parser.svg;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 *
 * @author jmburu
 */
public enum SVGShapeType {
    
        RECT("rect"),
        CIRCLE("circle");
        
        private final String shapeName;
        private SVGShapeType(String shape)
        {
            this.shapeName = shape;
        }
        
        public boolean compare(String string)
        {
            return string.equals(shapeName);
        }
        
        //store the enums in the following map
        private static final Map<String, SVGShapeType> BY_CODE_MAP = new LinkedHashMap<>();
        //automatic call to store enums in map
        static {
            for (SVGShapeType platformSpecificID : SVGShapeType.values()) {
                BY_CODE_MAP.put(platformSpecificID.shapeName, platformSpecificID);
            }
        }

        //scour through the available platform based on code
        public static SVGShapeType forShape(String value) {
            return BY_CODE_MAP.get(value);
        }
    
}
