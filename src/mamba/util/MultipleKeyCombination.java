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
package mamba.util;

import java.util.Arrays;
import java.util.List;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCombination;
import javafx.scene.input.KeyEvent;

/**
 *
 * @author jmburu
 */
public class MultipleKeyCombination extends KeyCombination{
    private static final ObservableList<KeyCode> codes = FXCollections.observableArrayList();
    
    public static void setupMultipleKeyCombination(Scene scene) {
            scene.addEventFilter(KeyEvent.KEY_PRESSED, (event) -> {
                    if (!codes.contains(event.getCode())) {
                            codes.add(event.getCode());
                    }
            });
            scene.setOnKeyReleased((event) -> {
                    codes.remove(event.getCode());
            });
    }

    private final ObservableList<KeyCode> neededCodes;

    public MultipleKeyCombination (KeyCode... codes) {
        neededCodes = FXCollections.observableArrayList(codes);
    }

    @Override
    public boolean match(KeyEvent event) {
            return codes.containsAll(neededCodes);
    }
    
    public boolean match()
    {
        return codes.containsAll(neededCodes);
    }
    
    public void addListener(ListChangeListener<KeyCode> listener)
    {
        neededCodes.addListener(listener);
    }
}
