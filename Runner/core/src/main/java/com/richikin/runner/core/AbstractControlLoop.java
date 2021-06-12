package com.richikin.runner.core;

import com.richikin.utilslib.core.IControlLoop;

public abstract class AbstractControlLoop implements IControlLoop
{
    public AbstractControlLoop()
    {
    }

    @Override
    public abstract void update();
}
