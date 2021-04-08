package com.richikin.runner.entities.hero;

import com.badlogic.gdx.utils.Disposable;
import com.richikin.enumslib.ActionStates;

public class ActionButtonHandler implements Disposable
{
    private       ActionStates actionMode;
    private       ActionStates previousActionMode;
    private       ActionStates futureActionMode;

    public ActionButtonHandler()
    {
        this.actionMode         = ActionStates._NO_ACTION;
        this.previousActionMode = ActionStates._NO_ACTION;
    }

    public void setAction()
    {
    }

    public void update()
    {
//        if (App.getPlayer().isOnFloorButton)
//        {
//            if (!App.gameProgress.keyCount.isEmpty())
//            {
//                if (getActionMode() == ActionStates._NO_ACTION)
//                {
//                    setActionMode(ActionStates._OFFER_ABXY_A);
//                    setFutureActionMode(ActionStates._PRESS_FLOOR_SWITCH);
//                    App.getPlayer().setLink(App.getPlayer().collisionObject.contactSprite.spriteNumber);
//                }
//            }
//        }
//        else if (App.getPlayer().collision.isNextTo(GraphicID.G_VILLAGER) > 0)
//        {
//            if (getActionMode() == ActionStates._NO_ACTION)
//            {
//                setActionMode(ActionStates._OFFER_ABXY_X);
//                setFutureActionMode(ActionStates._TALK_TO_VILLAGER);
//            }
//        }
//        else if (App.getPlayer().collision.isNextTo(GraphicID.G_MYSTERY_CHEST) > 0)
//        {
//            if (getActionMode() == ActionStates._NO_ACTION)
//            {
//                setActionMode(ActionStates._OFFER_ABXY_A);
//                setFutureActionMode(ActionStates._OPEN_MYSTERY_BOX);
//            }
//        }
//        else if ((App.getPlayer().collision.isNextTo(GraphicID.G_TREASURE_CHEST) > 0)
//                && (App.getPlayer().collisionObject.contactSprite != null)
//                && (App.getPlayer().collisionObject.contactSprite.getSpriteAction() == ActionStates._STANDING))
//        {
//            if (getActionMode() == ActionStates._NO_ACTION)
//            {
//                setActionMode(ActionStates._OFFER_ABXY_A);
//                setFutureActionMode(ActionStates._OPEN_TREASURE_CHEST);
//            }
//        }
//        else
//        {
//            removeAction();
//            App.getPlayer().setLink(0);
//        }
    }

    public ActionStates getActionMode()
    {
        return actionMode;
    }

    public void setActionMode(ActionStates mode)
    {
        previousActionMode = actionMode;
        actionMode         = mode;
    }

    public void setFutureActionMode(ActionStates mode)
    {
        futureActionMode = mode;
    }

    public ActionStates getPreviousActionMode()
    {
        return previousActionMode;
    }

    public ActionStates getFutureActionMode()
    {
        return futureActionMode;
    }

    public void removeAction()
    {
        if (previousActionMode != ActionStates._NO_ACTION)
        {
            setActionMode(previousActionMode);

            previousActionMode = ActionStates._NO_ACTION;
        }
        else
        {
            actionMode = ActionStates._NO_ACTION;
        }
    }

    @Override
    public void dispose()
    {
        actionMode         = null;
        previousActionMode = null;
    }
}
