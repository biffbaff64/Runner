package com.richikin.runner.physics.aabb;

public interface ICollisionListener
{
    void onPositiveCollision(CollisionObject cobjHitting);

    void onNegativeCollision();
}
