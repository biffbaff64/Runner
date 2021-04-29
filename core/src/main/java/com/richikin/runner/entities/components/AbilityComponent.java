package com.richikin.runner.entities.components;

public class AbilityComponent
{
    public boolean running;
    public boolean fighting;
    public boolean jumping;
    public boolean flying;
    public boolean falling;
    public boolean pausing;
    public boolean followsPath;
    public boolean roaming;
    public boolean targetting;
    public boolean intelligence;
    public boolean hunter;

    public AbilityComponent()
    {
        this.running      = false;
        this.fighting     = false;
        this.jumping      = false;
        this.flying       = false;
        this.falling      = false;
        this.intelligence = false;
        this.pausing      = false;
        this.roaming      = false;
        this.targetting   = false;
        this.followsPath  = false;
        this.hunter       = false;
    }
}
