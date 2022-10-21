/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package test;

import mamba.base.mode.CanvasModes.EditModeState;
import mamba.base.mode.CanvasModes.HitModeState;
import static mamba.base.mode.CanvasModes.HitModeState.HIT;
import mamba.base.mode.CanvasModeState;

/**
 *
 * @author user
 */
public class TestGeneral {
    public static void main(String... args)
    {
        CanvasModeState mode = HIT;
        System.out.println(mode instanceof HitModeState);
    }
}
