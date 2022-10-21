/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mamba.base.mode;

/**
 *
 * @author user
 */
public class CanvasModes {
    public static enum HitModeState implements CanvasModeState{
        HIT, HIT_AND_SELECT
    }
    
    public static enum EditModeState implements CanvasModeState{
        PATH_EDIT
    }
    
    public static enum DisplayModeState implements CanvasModeState{
        DISPLAY, DISPLAY_ANIMATE
    }
}
