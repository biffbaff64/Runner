package com.richikin.runner.ui.panels;

import com.richikin.enumslib.ActionStates;
import com.richikin.utilslib.maths.Vec2F;
import com.richikin.utilslib.physics.Direction;

public class PanelDescriptor
{
    public String       asset;
    public Direction    direction;
    public Vec2F        distance;
    public Vec2F        position;
    public Vec2F        speed;
    public boolean      isActive;
    public ActionStates actionState;

    public PanelDescriptor(String asset)
    {
    }
}
