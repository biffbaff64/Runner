package com.richikin.runner.entities.characters;

import com.richikin.enumslib.GraphicID;
import com.richikin.runner.entities.objects.GdxSprite;
import com.richikin.runner.entities.objects.SpriteDescriptor;

public class SpikeBall extends GdxSprite
{
    public SpikeBall(GraphicID gid)
    {
        super(gid);
    }

    @Override
    public void initialise(SpriteDescriptor descriptor)
    {
        create(descriptor);
    }
}
