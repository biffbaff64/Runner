package com.richikin.runner.entities.components;

import com.badlogic.gdx.math.Rectangle;
import com.richikin.enumslib.ActionStates;

public interface IEntityComponent
{
    ActionStates getActionState();

    void setActionState(ActionStates action);

    void tidy(int index);

    Rectangle getCollisionRectangle();

    void setCollisionObject(int xPos, int yPos);

    void setCollisionObject(float xPos, float yPos);

    float getTopEdge();

    float getRightEdge();
}
