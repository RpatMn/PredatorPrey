package Homework2023.RPat;

import genDevs.modeling.content;
import genDevs.modeling.message;
import GenCol.entity;
import GenCol.DEVSQueue;
import simView.*;

public class BridgeSegment extends ViewableAtomic
{
    protected DEVSQueue westbound_q;
    protected DEVSQueue eastbound_q;
    entity car;
    entity nextJob;
    protected double RT;
    public double lightDuration;
    boolean occupied;
    String iState;
    
    public BridgeSegment() {
        this("BridgeSegment", "test", 1.0);
    }
    
    public BridgeSegment(final String name, final String phase, final double time) {
        super(name);
         nextJob = null;
         RT = 0.0;
         this.lightDuration = time;
         this.iState = phase;
        addInport("west_in");
        addInport("east_in");
        addOutport("west_out");
        addOutport("east_out");
    }
    
    public void initialize() {
         RT =  lightDuration;
         westbound_q = new DEVSQueue();
         eastbound_q = new DEVSQueue();
         holdIn( iState,  lightDuration);
        
    }
    
	@SuppressWarnings("unchecked")
	public void deltext(final double e, final message x) {
         Continue(e);
        for (int i = 0; i < x.getLength(); i++) {
            if ( messageOnPort(x, "west_in", i)) {
                 car = x.getValOnPort("west_in", i);
                if ( phaseIs("WEST_GREEN")&& ta() >= 10) {
                     RT =  ta() - 10.0;
                     holdIn("WEST_BUSY", 10.0);
                     nextJob =  car;
                }
                else {
                    eastbound_q.add(car);
                }
            }
            else if ( messageOnPort(x, "east_in", i)) {
                 car = x.getValOnPort("east_in", i);
                if ( phaseIs("EAST_GREEN")&& ta() >= 10) {
                     RT =  ta() - 10.0;
                     holdIn("EAST_BUSY", 10.0);
                     nextJob =  car;
                }
                else {
                     westbound_q.add(car);
                }
            }
        }
    }
    
    public void deltint() {
        
    	 if ( phaseIs("WEST_GREEN")) {     	
             RT =  lightDuration;
             if( RT >= 10.0) {
            	if(!westbound_q.isEmpty()) {
            		RT = RT - 10.0;
                    nextJob = (entity) westbound_q.first();
                    holdIn("EAST_BUSY", 10.0);
                    westbound_q.remove();}
            	else {
            		holdIn("EAST_GREEN", RT);
            	}
             }else {
            	 holdIn("EAST_GREEN", RT);
             }
    	 }
    	 else if(phaseIs("EAST_GREEN")) {
             RT =  lightDuration;
             if( RT >= 10.0) {
            	if(!eastbound_q.isEmpty()) {
            		RT = RT - 10.0;
                    nextJob = (entity) eastbound_q.first();
                    holdIn("WEST_BUSY", 10.0);
                    eastbound_q.remove();}
            	else {
            		holdIn("WEST_GREEN", RT );
            	}
             }else {
            	 holdIn("WEST_GREEN", RT );
             }
    	 }
    	 else if(phaseIs("WEST_BUSY")) {
    		 if(RT>=10) {
    			 if(!eastbound_q.isEmpty()) {
    				 RT=RT-10;
    				 nextJob=(entity)eastbound_q.first();
    				 holdIn("WEST_BUSY", RT);
    				 eastbound_q.remove();}
    			 else {
    	            		holdIn("WEST_GREEN", RT );
    	            	}
    	            }
    	      else if(RT<10){
    	         if(RT==0) {
    	            		RT=lightDuration;
    	            		holdIn(("EAST_GREEN"), RT);
    	            	}
    	          else {
    	            		holdIn(("WEST_GREEN"), RT);
    	            	}
    	            }
    	    	 }
    		 else if(phaseIs("EAST_BUSY")) {
    	         	
                 //RT =  lightDuration;
                if( RT >= 10.0) {
                	if(!westbound_q.isEmpty()) {
                		RT = RT - 10.0;
                        nextJob = (entity) westbound_q.first();
                        holdIn("EAST_BUSY", 10.0);
                        westbound_q.remove();}
                	else {
                		holdIn("EAST_GREEN", RT );
                	}
                }
                else if(RT<10){
                	if(RT==0) {
                		RT=lightDuration;
                		holdIn(("WEST_GREEN"), RT);
                	}
                	else {
                		holdIn(("EAST_GREEN"), RT);
                	}
                }
        	 }
    	 }
            	
    
    
    public message out() {
        final message m = new message();
        if ( phaseIs("WEST_BUSY")) {
            
            content con =  makeContent("west_out", nextJob);
            m.add((Object)con);
        }
        else if ( phaseIs("EAST_BUSY")) {
            
            content con =  makeContent("east_out", nextJob);
            m.add((Object)con);
        }
        return m;
    }
}
    
  