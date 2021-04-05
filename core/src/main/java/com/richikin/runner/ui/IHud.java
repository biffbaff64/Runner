package com.richikin.runner.ui;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.utils.Disposable;
import com.richikin.enumslib.StateID;

public interface IHud extends Disposable
{
    void createHud();

    void update();

    void render(OrthographicCamera camera, boolean canDrawControls);

    void showControls();

    void hideControls();

    void showPauseButton(boolean state);

    void setStateID(StateID newState);

    void createHUDButtons();

    void refillItems();

    @Override
    void dispose();
}
