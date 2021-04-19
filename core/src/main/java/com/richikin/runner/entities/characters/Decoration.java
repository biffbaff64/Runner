package com.richikin.runner.entities.characters;

import com.richikin.enumslib.GraphicID;
import com.richikin.runner.entities.objects.GdxSprite;

public class Decoration extends GdxSprite
{
    private boolean isMoveable;

    public Decoration(GraphicID gid)
    {
        super(gid);
    }
}
