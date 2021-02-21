
package com.richikin.runner.entities.paths;

import com.richikin.runner.entities.objects.GdxSprite;

public interface IPathMover
{
    void reset();

    void setNextMove(GdxSprite spriteObject);
}
