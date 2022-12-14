/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mamba.treeview;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.Shape;
import mamba.base.MambaShape;
import mamba.base.MambaHierarchyData;

/**
 *
 * @author jmburu
 */
public class MambaShapeData implements MambaHierarchyData
{
    private final MambaShape shape;
    private final StringProperty name;
    private final ObservableList<MambaShapeData> children = FXCollections.observableArrayList();

    public MambaShapeData(MambaShape shape)
    {
        this.shape = shape;
        this.name = new SimpleStringProperty(shape.getName());
    }

    public String getName()
    {
        return name.get();
    }

    public void setName(String string)
    {
        name.set(string);
    }

    public StringProperty propertyName()
    {
        return name;
    }

    public void add(MambaShape shape)
    {
        if(this.shape.canHaveChildren())
            getChildren().add(new MambaShapeData(shape));
        else
            throw new UnsupportedOperationException("cannot add child to");
    }

    public Shape getDisplay(MambaShapeData display)
    {
        if(children.isEmpty())
        {
            Circle circle = new Circle(10);
            circle.setFill(Color.AQUAMARINE);
            return circle;
        }
        else
        {
            Rectangle rectangle = new Rectangle(10, 10);
            rectangle.setFill(Color.BLUEVIOLET);
            return rectangle;
        }
    }

    @Override
    public ObservableList<MambaShapeData> getChildren() {
        return children;
    }

    @Override
    public String toString()
    {
        return name.get();
    }


}
