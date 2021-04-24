package com.richikin.runner.ui.panels;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.Array;
import com.richikin.enumslib.ActionStates;
import com.richikin.utilslib.maths.Vec2F;
import com.richikin.utilslib.physics.Direction;

public class PanelManager
{
    public static class Panel
    {
        public TextureRegion textureRegion;
        public Direction     direction;
        public Vec2F         distance;
        public Vec2F         position;
        public Vec2F         speed;
        public boolean       isActive;
        public ActionStates  actionState;

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

    private Array<IUserInterfacePanel> panels;

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
        if ((panels != null) && !panels.isEmpty())
        {
            panels.get(0).update();
        }
    }

    public void draw()
    {
        if ((panels != null) && !panels.isEmpty())
        {
            panels.get(0).draw();
        }
    }

    public void addPanel(IUserInterfacePanel uiPanel)
    {
        Panel panel = new Panel();
    }
}
