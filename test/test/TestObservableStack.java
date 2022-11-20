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
package test;

import java.util.Arrays;
import javafx.collections.ListChangeListener;
import mamba.util.MObservableStack;

/**
 *
 * @author user
 */
public class TestObservableStack {
    public static void main(String... args)
    {
        test2();
        
    }
    
    private static void test1()
    {
        MObservableStack<Integer> obs = new MObservableStack<>();
        obs.addListener((ListChangeListener.Change<? extends Integer> c) -> {
            if (c.next() && c.wasAdded()) {
                if (c.getAddedSize() != 10) {
                    throw new IllegalStateException("Test 1 failed!");
                }
                System.out.println(c.getAddedSubList());
            }
        });

        obs.addListener((ListChangeListener.Change<? extends Integer> c) -> {
            if (c.next() && c.wasRemoved()) {
                if (c.getRemovedSize() != 10) {
                    throw new IllegalStateException("Test 2 failed!");
                }
                System.out.println(c.getRemoved());
            }
        });

        obs.addAll(Arrays.asList(2, 3, 4, 5, 6, 7, 1, 54, 23, 121));
        obs.removeAll();
    }
    
    public static void test2()
    {
        MObservableStack<Integer> obs = new MObservableStack<>();
        obs.addListener((ListChangeListener.Change<? extends Integer> c) -> {
            if (c.next() && c.wasAdded()) {                
                System.out.println("added   " +c.getAddedSubList());
            }
        });

        obs.addListener((ListChangeListener.Change<? extends Integer> c) -> {
            if (c.next() && c.wasRemoved()) {               
                System.out.println("removed " +c.getRemoved());
            }
        });
        obs.addAll(Arrays.asList(2, 3, 4, 5, 6, 7, 1, 54, 23, 121));
        obs.pop();
        System.out.println(obs.get());
    }
}
