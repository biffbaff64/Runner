package com.richikin.utilslib.entities;

import com.richikin.enumslib.ActionStates;

public class ActionSpeed
{
    public ActionStates action;
    public float        speed;

    public ActionSpeed(ActionStates action, float speed)
    {
        this.action = action;
        this.speed = speed;
    }
}
