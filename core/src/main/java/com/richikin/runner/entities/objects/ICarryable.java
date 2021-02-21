package com.richikin.runner.entities.objects;

public interface ICarryable
{
    void updateAttachedToPlayer();

    void updateAttachedToRover();

    void explode();

    void setCollisionListener();
}
