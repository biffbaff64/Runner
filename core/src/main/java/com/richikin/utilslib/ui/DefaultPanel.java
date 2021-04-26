package com.richikin.utilslib.ui;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.Disposable;
import com.richikin.enumslib.StateID;
import com.richikin.utilslib.LibApp;
import com.richikin.utilslib.exceptions.NotImplementedException;
import com.richikin.utilslib.graphics.SimpleDrawable;
import com.richikin.utilslib.logging.StateManager;
import com.richikin.utilslib.maths.SimpleVec2;
import com.richikin.utilslib.maths.SimpleVec2F;
import com.richikin.utilslib.maths.Vec2;
import com.richikin.utilslib.maths.Vec2F;
import com.richikin.utilslib.physics.Direction;

/**
 * Basic UI Panel class.
 * All other panels should extend this class.
 */
public abstract class DefaultPanel implements IDefaultUIPanel, Disposable
{
    protected TextureRegion  textureRegion;
    protected SimpleDrawable image;
    protected String         nameID;
    protected boolean        isActive;

    private final StateManager state;

    public DefaultPanel()
    {
        this.image        = new SimpleDrawable();
        this.image.size.x = 16;
        this.image.size.y = 16;
        this.state        = new StateManager();
        this.isActive     = false;
        this.nameID       = "unnamed";
    }

    @Override
    public void open()
    {
        setup();
    }

    @Override
    public void close()
    {
        dispose();
    }

    @Override
    public void draw()
    {
        if (isActive && (image != null))
        {
            image.draw();
        }
    }

    @Override
    public boolean nameExists(String _nameID)
    {
        return _nameID.equals(this.nameID);
    }

    @Override
    public void setWidth(int _width)
    {
        image.size.x = _width;
    }

    @Override
    public int getWidth()
    {
        return image.size.x;
    }

    @Override
    public void setHeight(int _height)
    {
        image.size.y = _height;
    }

    @Override
    public int getHeight()
    {
        return image.size.y;
    }

    @Override
    public Vec2 getSize()
    {
        return image.size;
    }

    @Override
    public Vec2F getPosition()
    {
        return image.position;
    }

    @Override
    public void setPosition(float x, float y)
    {
        image.position.x = x;
        image.position.y = y;
    }

    @Override
    public String getNameID()
    {
        return nameID;
    }

    @Override
    public boolean getActiveState()
    {
        return isActive;
    }

    @Override
    public void activate()
    {
        isActive = true;
    }

    @Override
    public void deactivate()
    {
        isActive = false;
    }

    @Override
    public StateID getState()
    {
        return state.peek();
    }

    @Override
    public void setState(StateID _state)
    {
        state.set(_state);
    }


    // -------------------------------------------------------------------
    // Empty methods from IDefaultPanel interface
    @Override
    public void initialise(TextureRegion _region, String _nameID, Object... args)
    {
        throw new NotImplementedException("ERROR - method not implemented!");
    }

    @Override
    public void set(SimpleVec2F xy, SimpleVec2F distance, Direction direction, SimpleVec2F speed)
    {
        throw new NotImplementedException("ERROR - method not implemented!");
    }

    @Override
    public void setup()
    {
        throw new NotImplementedException("ERROR - method not implemented!");
    }

    @Override
    public void populateTable()
    {
        throw new NotImplementedException("ERROR - method not implemented!");
    }

    @Override
    public void setPauseTime(final int _time)
    {
        throw new NotImplementedException("ERROR - method not implemented!");
    }

    @Override
    public void forceZoomOut()
    {
        throw new NotImplementedException("ERROR - method not implemented!");
    }

    // -------------------------------------------------------------------
    // From Disposable Interface
    @Override
    public void dispose()
    {
    }
}
