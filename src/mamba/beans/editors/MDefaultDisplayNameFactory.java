/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mamba.beans.editors;

import java.util.function.Function;

/**
 *
 * @author jmburu
 */
public class MDefaultDisplayNameFactory implements Function<String, String> {

    @Override
    public String apply(String t) {
        switch (t) {
            case "width":
                return "Width";
            case "height":
                return "Height";
            case "radius":
                return "Radius";
            case "arcWidth":
                return "Arc width";
            case "arcHeight":
                return "Arc height";
            case "solidColor":
                return "Solid color";
            case "strokeColor":
                return "Stroke color";
            case "strokeWidth":
                return "Stroke width";
            case "spread":
                return "Spread";
            case "offsetX":
                return "Offset X";
            case "offsetY":
                return "Offset Y";
            case "input":
                return "Input";
            case "color":
                return "Color";
            case "blurType":
                return "Blur type";
            case "topOffset":
                return "Top offset";
            case "topOpacity":
                return "Top opacity";
            case "bottomOpacity":
                return "Bottom opacity";
            case "fraction":
                return "Fraction";
            case "fillColor":
                return "Fill color";
            default:
                return t;
        }
    }
    
}
