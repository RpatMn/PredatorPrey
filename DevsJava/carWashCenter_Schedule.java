package RPat;

import genDevs.modeling.content;
import genDevs.modeling.message;
import GenCol.entity;
import GenCol.DEVSQueue;
import simView.ViewableAtomic;

public class BridgeSegment extends ViewableAtomic
{
    protected DEVSQueue westbound_q;
    protected DEVSQueue eastbound_q;
    entity car;
    entity nextJob;
    protected double RT;
    protected final int bridgeTT = 10;
    public double lightDuration;
    boolean occupied;
    String iState;
    
    public BridgeSegment() {
        this("BridgeSegment", "test", 10.0);
    }
    
    public BridgeSegment(final String name, final String phase, final double lightD) {
        super(name);
        this.nextJob = null;
        this.RT = 0.0;
        this.lightDuration = 0.0;
        this.occupied = false;
        this.iState = "";
        this.lightDuration = lightD;
        this.iState = phase;
        this.addInport("west_in");
        this.addInport("east_in");
        this.addOutport("west_out");
        this.addOutport("east_out");
    }
    
    public void initialize() {
        this.RT = this.lightDuration;
        if (this.lightDuration >= 10.0) {
            this.westbound_q = new DEVSQueue();
            this.eastbound_q = new DEVSQueue();
            this.holdIn(this.phase, this.lightDuration);
        }
    }
    
    public void deltext(final double e, final message x) {
        this.Continue(e);
        for (int i = 0; i < x.getLength(); ++i) {
            if (this.messageOnPort(x, "west_in", i)) {
                this.car = x.getValOnPort("west_in", i);
                if (this.phaseIs("WEST_GREEN")) {
                    this.RT = this.lightDuration - 10.0;
                    this.holdIn("WEST_BUSY", 10.0);
                    this.nextJob = this.car;
                }
                else {
                    this.westbound_q.add((Object)this.car);
                }
            }
            else if (this.messageOnPort(x, "east_in", i)) {
                this.car = x.getValOnPort("east_in", i);
                if (this.phaseIs("EAST_GREEN")) {
                    this.RT = this.lightDuration - 10.0;
                    this.holdIn("EAST_BUSY", 10.0);
                    this.nextJob = this.car;
                }
                else {
                    this.eastbound_q.add((Object)this.car);
                }
            }
        }
    }
    
    public void deltint() {
        while (this.RT >= 10.0) {
            if (this.phaseIs("WEST_GREEN")) {
                this.RT = this.lightDuration;
                if (!this.westbound_q.isEmpty()) {
                    this.RT -= 10.0;
                    this.nextJob = (entity)this.eastbound_q.first();
                    this.holdIn("WEST_BUSY", 10.0);
                    this.eastbound_q.remove();
                }
                else {
                    this.holdIn("EAST_GREEN", this.lightDuration);
                }
            }
            else {
                if (!this.phaseIs("EAST_GREEN")) {
                    continue;
                }
                this.RT = this.lightDuration;
                if (!this.westbound_q.isEmpty() && this.RT >= 10.0) {
                    this.RT -= 10.0;
                    this.nextJob = (entity)this.westbound_q.first();
                    this.holdIn("WEST_BUSY", 10.0);
                    this.westbound_q.remove();
                }
                else {
                    this.holdIn("WEST_GREEN", this.RT);
                }
            }
        }
        if (this.phaseIs("WEST_BUSY")) {
            if (this.RT >= 10.0) {
                if (!this.eastbound_q.isEmpty()) {
                    this.nextJob = (entity)this.eastbound_q.first();
                    this.RT -= 10.0;
                    this.holdIn("WEST_BUSY", 10.0);
                    this.eastbound_q.remove();
                }
                else {
                    this.holdIn("WEST_GREEN", this.RT);
                }
            }
            else if (this.RT != 0.0) {
                this.holdIn("WEST_GREEN", this.RT);
            }
            else {
                this.holdIn("EAST_GREEN", this.RT = this.lightDuration);
            }
        }
        else if (this.phaseIs("EAST_BUSY")) {
            if (this.RT >= 10.0) {
                if (!this.westbound_q.isEmpty()) {
                    this.nextJob = (entity)this.westbound_q.first();
                    this.RT -= 10.0;
                    this.holdIn("EAST_BUSY", 10.0);
                    this.westbound_q.remove();
                }
                else {
                    this.holdIn("EAST_GREEN", this.RT);
                }
            }
        }
        else if (this.RT != 0.0) {
            this.holdIn("EAST_GREEN", this.RT);
        }
        else {
            this.holdIn("WEST_GREEN", this.RT = this.lightDuration);
        }
    }
    
    public message out() {
        final message m = new message();
        if (this.phaseIs("WEST_BUSY")) {
            final entity car = (entity)this.westbound_q.remove();
            final content con = this.makeContent("eastbound_out", car);
            m.add((Object)con);
        }
        else if (this.phaseIs("EAST_BUSY")) {
            final entity car = (entity)this.eastbound_q.remove();
            final content con = this.makeContent("westbound_out", car);
            m.add((Object)con);
        }
        return m;
    }
    
    public String getTooltipText() {
        if (this.nextJob != null && this.iState.equals("WEST")) {
            return invokedynamic(makeConcatWithConstants:(Ljava/lang/String;ILjava/lang/String;)Ljava/lang/String;, super.getTooltipText(), this.westbound_q.size(), this.nextJob.toString());
        }
        if (this.nextJob != null && this.iState.equals("EAST")) {
            return invokedynamic(makeConcatWithConstants:(Ljava/lang/String;ILjava/lang/String;)Ljava/lang/String;, super.getTooltipText(), this.eastbound_q.size(), this.nextJob.toString());
        }
        return "simulation has not started!";
    }