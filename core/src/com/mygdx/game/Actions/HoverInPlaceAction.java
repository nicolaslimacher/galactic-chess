package com.mygdx.game.Actions;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.actions.MoveByAction;
import com.badlogic.gdx.scenes.scene2d.actions.SequenceAction;

public class HoverInPlaceAction {

    public SequenceAction Hover;

    public HoverInPlaceAction() {
        float pathY = MathUtils.random(1f, 4f);
        float duration = MathUtils.random(5f, 8f);
        int firstDirection = MathUtils.random(0, 1);
        MoveByAction move = Actions.moveBy(0f, -(pathY / 2), duration);
        if (firstDirection == 0) {
            Action foreverAction = Actions.forever(
                            Actions.sequence(
                                    Actions.moveBy(0f, pathY, duration),
                                    Actions.moveBy(0f, -pathY, duration)
                            )

            );
            Hover = new SequenceAction(move, foreverAction);
        } else {
            Action foreverAction = Actions.forever(
                            Actions.sequence(
                                    Actions.moveBy(0f, -pathY, duration),
                                    Actions.moveBy(0f, pathY, duration)
                            )

            );
            Hover = new SequenceAction(move, foreverAction);
        }
    }
}
