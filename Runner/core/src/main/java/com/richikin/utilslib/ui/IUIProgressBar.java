package com.richikin.utilslib.ui;

import com.badlogic.gdx.graphics.Color;

public interface IUIProgressBar
{
    void draw(int x, int y);

    void updateSlowDecrement();

    void updateSlowDecrementWithWrap(int wrap);

    boolean updateSlowIncrement();

    void setHeightColorScale(float height, Color color, float scale);

    void setHeight(float _height);

    boolean hasRefillRoom();

    void setColor(Color color);

    float getSpeed();

    void setSpeed(float speed);

    void setSubInterval(int subInterval);

    void setAddInterval(int addInterval);
}
