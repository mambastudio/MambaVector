/*
 * The MIT License
 *
 * Copyright 2022 jmburu.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package mamba.overlayselect.drag;

import java.util.function.Consumer;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.input.MouseEvent;
import mamba.base.MambaShape;
import mamba.base.MambaShapeAbstract;
import mamba.base.engine.MEngine;

/**
 *
 * @author jmburu
 */
public abstract class MDrag2 extends MambaShapeAbstract<MEngine> implements MDragShape<MEngine> {
    
    private Consumer<MouseEvent> consume;
    private final MambaShape<MEngine> ownerShape;
    
    protected MDrag2(MambaShape<MEngine> ownerShape)
    {
        this.ownerShape = ownerShape;
        this.setEngine(ownerShape.getEngine2D());
        this.setGraphicContext(ownerShape.getGraphicsContext());
    }
    
     //for ui editor such as mouse editing (utilises the global transforms) - NOT NEEDED HERE
    @Override
    public ObservableList<MDrag2> initDragHandles()
    {
        return FXCollections.emptyObservableList();
    }
    
    @Override
    public void updateDragHandles()
    {
       
    }

    @Override
    public boolean isComplete() {
        return true;
    }
    
    @Override
    public void setOnMouseDrag(Consumer<MouseEvent> consume)
    {
        this.consume = consume;
    }
    
    @Override
    public Consumer<MouseEvent> getOnMouseDrag()
    {
        return consume;
    }
    
    @Override
    public MambaShape<MEngine> getOwnerShape()
    {
        return ownerShape;
    }
}
