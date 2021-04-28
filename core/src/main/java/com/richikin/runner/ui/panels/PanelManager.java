package com.richikin.runner.ui.panels;

import com.badlogic.gdx.utils.Array;
import com.richikin.enumslib.ActionStates;
import com.richikin.utilslib.graphics.SimpleDrawable;
import com.richikin.utilslib.maths.Vec2F;
import com.richikin.utilslib.physics.Direction;

public class PanelManager
{
    private static final int                        _DEFAULT_PANEL = 0;
    private final        Array<IUserInterfacePanel> panels;

    public PanelManager()
    {
        this.panels = new Array<>();
    }

    /**
     * Update the current message. The current message is
     * always the one at position zero.
     */
    public void update()
    {
        if (!panels.isEmpty())
        {
            panels.get(_DEFAULT_PANEL).update();
        }
    }

    public void draw()
    {
        if (!panels.isEmpty())
        {
            panels.get(_DEFAULT_PANEL).draw();
        }
    }

    public void addPanel(IUserInterfacePanel uiPanel)
    {
        panels.add(uiPanel);
    }

    public static class Panel
    {
        public SimpleDrawable image;
        public Direction      direction;
        public Vec2F          distance;
        public Vec2F          position;
        public Vec2F          speed;
        public boolean        isActive;
        public ActionStates   actionState;

        public Panel()
        {
            direction   = new Direction();
            distance    = new Vec2F();
            position    = new Vec2F();
            speed       = new Vec2F();
            isActive    = false;
            actionState = ActionStates._NO_ACTION;
        }
    }
}
