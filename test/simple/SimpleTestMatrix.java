/*
 * The MIT License
 *
 * Copyright 2022 user.
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
package simple;

import javafx.geometry.Point2D;
import mamba.base.math.MTransform;

/**
 *
 * @author user
 */
public class SimpleTestMatrix {
    public static void main(String... args)
    {
        MTransform t = MTransform.scale(2, 2).createConcatenation(MTransform.translate(20, 20)).createConcatenation(MTransform.scale(5, 2));
        Point2D p = new Point2D(1, 1);
        Point2D tp = t.transform(p);
        
        System.out.println(p);
        System.out.println(tp);
        System.out.println(t.inverseTransform(tp));
    }
}
