/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mamba.selection;

import java.util.ArrayList;
import java.util.List;
import mamba.base.MambaShape;

/**
 *
 * @author jmburu
 */
public class SelectionModel 
{    
    private final List<MambaShape> selection = new ArrayList<>();
    
    public void add(MambaShape shape) {

        // don't add duplicates or else duplicates might be added to the javafx scene graph which causes exceptions
        if( selection.contains(shape))
            return;
       
        selection.add(shape);
        
    }
    
    public void remove(MambaShape cell) 
    {
        selection.remove( cell);
    }

    public void clear() 
    {
        // clear selection list
        selection.clear();
    }

    public boolean isEmpty()  {
        return selection.isEmpty();
    }

    public boolean contains(MambaShape cell) {
        return selection.contains( cell);
    }


    public List<MambaShape> getSelection() {
        return selection;
    }
}
