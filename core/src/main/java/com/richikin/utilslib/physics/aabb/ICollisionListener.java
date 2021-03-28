package com.richikin.utilslib.physics.aabb;

import com.richikin.runner.physics.aabb.CollisionObject;

public interface ICollisionListener
{
    void onPositiveCollision(CollisionObject cobjHitting);

    void onNegativeCollision();
}
