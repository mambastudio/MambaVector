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
import javafx.geometry.Point2D;
import javafx.scene.input.MouseEvent;
import mamba.base.MambaEngine2D;
import mamba.base.MambaShape;

/**
 *
 * @author jmburu
 * 
 * should not be scalable
 * @param <Engine2D>
 * 
 */
public interface MDragShape<Engine2D extends MambaEngine2D> {
       
    public Point2D getPosition();
    public void setPostion(Point2D position);
    public void setPosition(double x, double y);
    public void setOnMouseDrag(Consumer<MouseEvent> consume);
    public Consumer<MouseEvent> getOnMouseDrag();
    public MambaShape<Engine2D> getOwnerShape();
}
