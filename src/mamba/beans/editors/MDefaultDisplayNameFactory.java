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
            default:
                return t;
        }
    }
    
}
