
package com.richikin.runner.entities.hero;

import com.richikin.enumslib.ActionStates;
import com.richikin.runner.core.App;

public class BButtonActions
{
    public BButtonActions()
    {
    }

    public void process()
    {
        App.getPlayer().setActionState(ActionStates._FIGHTING);
    }
}
