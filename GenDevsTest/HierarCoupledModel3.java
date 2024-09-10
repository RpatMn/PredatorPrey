/*      Copyright 2002 Arizona Board of regents on behalf of 
 *                  The University of Arizona 
 *                     All Rights Reserved 
 *         (USE & RESTRICTION - Please read COPYRIGHT file) 
 * 
 *  Version    : DEVSJAVA 2.7 
 *  Date       : 08-15-02 
 */ 


package GenDevsTest;

import java.awt.*;
import java.util.*;
import GenCol.*;
import genDevs.modeling.*;
import genDevs.simulation.*;
import simView.*;

public class HierarCoupledModel3 extends ViewableDigraph
{
    public HierarCoupledModel3()
    {
        super("HierarCoupledModel3");
        addInport("in");
        addOutport("out");
        HierarCoupledModel1 hc = new HierarCoupledModel1();
        HierarAtomicModel third = new HierarAtomicModel("third");
        add(hc);
        add(third);
        //
        addCoupling(third, "out", this, "out");
        addCoupling(hc, "out", third, "start");
        addCoupling(hc, "out", this, "out");
        addCoupling(this, "in", hc, "in");
    }

    /**
     * Automatically generated by the SimView program.
     * Do not edit this manually, as such changes will get overwritten.
     */
    public void layoutForSimView()
    {
        preferredSize = new Dimension(636, 249);
        if((ViewableComponent)withName("third")!=null)
             ((ViewableComponent)withName("third")).setPreferredLocation(new Point(401, 181));
        if((ViewableComponent)withName("HierarCoupledModel1")!=null)
             ((ViewableComponent)withName("HierarCoupledModel1")).setPreferredLocation(new Point(25, 33));
    }
}
