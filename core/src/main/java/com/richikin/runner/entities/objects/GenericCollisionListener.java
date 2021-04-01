package com.richikin.runner.entities.objects;

import com.badlogic.gdx.utils.Disposable;
import com.richikin.enumslib.ActionStates;
import com.richikin.enumslib.GraphicID;
import com.richikin.runner.physics.aabb.CollisionObject;
import com.richikin.utilslib.physics.aabb.ICollisionListener;
import com.richikin.utilslib.logging.Trace;

public class GenericCollisionListener implements ICollisionListener, Disposable
{
    private GdxSprite parent;

    public  GenericCollisionListener(GdxSprite _parent)
    {
        this.parent = _parent;
    }

    @Override
    public void onPositiveCollision(CollisionObject cobjHitting)
    {
        if (cobjHitting.gid == GraphicID.G_LASER)
        {
            parent.setActionState(ActionStates._KILLED);
        }
        else
        {
            if (cobjHitting.gid == GraphicID.G_PLAYER)
            {
                parent.collisionObject.isHittingPlayer = true;
                parent.setActionState(ActionStates._HURT);
            }
        }

        parent.collisionObject.setInvisibility(1000);
    }

    @Override
    public void onNegativeCollision()
    {
    }

    /**
     * Releases all resources of this object.
     */
    @Override
    public void dispose()
    {
        Trace.__FILE_FUNC();

        parent = null;
    }
}
