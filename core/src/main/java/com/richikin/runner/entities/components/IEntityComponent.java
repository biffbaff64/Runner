package com.richikin.runner.entities.components;

import com.badlogic.gdx.math.Rectangle;
import com.richikin.enumslib.ActionStates;

public interface IEntityComponent
{
    void setActionState(ActionStates action);

    ActionStates getActionState();

    void tidy(int index);

    Rectangle getCollisionRectangle();

    void setCollisionObject(int xPos, int yPos);

    void setCollisionObject(float xPos, float yPos);

    float getTopEdge();

    float getRightEdge();
}
