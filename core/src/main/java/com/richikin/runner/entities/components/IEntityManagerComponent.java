package com.richikin.runner.entities.components;

import com.badlogic.gdx.utils.Array;
import com.richikin.enumslib.GraphicID;
import com.richikin.utilslib.maths.SimpleVec2;
import com.richikin.utilslib.maths.Vec3;

public interface IEntityManagerComponent
{
    void init();

    void update();

    void create();

    Vec3 findCoordinates(final GraphicID targetGID);

    Array<Vec3> findMultiCoordinates(final GraphicID targetGID);

    void free();

    void free(final GraphicID gid);

    void reset();

    int getActiveCount();

    void setActiveCount(int numActive);

    GraphicID getGID();

    void setPlaceable(boolean placeable);

    void dispose();
}
