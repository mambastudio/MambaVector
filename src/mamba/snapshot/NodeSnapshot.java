/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mamba.snapshot;

import glyphreader.jfx.PhosphorIcon;
import javafx.scene.Node;
import javafx.scene.SnapshotParameters;
import javafx.scene.image.Image;
import javafx.scene.image.WritableImage;
import javafx.scene.paint.Color;

/**
 *
 * @author user
 */
public class NodeSnapshot {
    public static Image getSnapshot(Node node)
    {
        SnapshotParameters parameters = new SnapshotParameters();
        parameters.setFill(Color.TRANSPARENT);
        return node.snapshot(parameters, null);        
    }
    
    public static Image getPhosphorIcon(String name, double size, double rotateDegrees)
    {
        PhosphorIcon icon = new PhosphorIcon(true);
        icon.setGlyphName(name);
        icon.setGlyphSize(size);
        icon.setRotate(rotateDegrees);
        return getSnapshot(icon);
    }
}
