package com.mygdx.game.Actions;

import com.badlogic.gdx.math.Bezier;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.actions.MoveToAction;

public class ArcToAction extends MoveToAction {

    private Vector2 p0, p1, p2, p3;
    final Vector2 p1transform = new Vector2(0.2f, 0.8f);
    final Vector2 p2transform = new Vector2(0.1f, 1.6f);
    private Bezier bezier;
    Vector2 pos = new Vector2(0,0);


    @Override
    protected void begin() {
        super.begin();

//        if(getStartX() > getX()){
//
//        }
        p0 = new Vector2(getStartX(), getStartY());
        p3 = new Vector2(getX(), getY());
        Vector2 difference = new Vector2(p3.x - p0.x, p3.y - p0.y);
        p1 = new Vector2(p0.x + difference.x*p1transform.x, p0.y + difference.y*p1transform.y);
        p2 = new Vector2(p0.x + difference.x*p2transform.x, p0.y + difference.y*p2transform.y);

        bezier = new Bezier<>(p0, p1, p2, p3);

    }

    @Override
    protected void update (float percent) {
        float x, y;
        if (percent == 0) {
            System.out.println("%: 0");
            x = getStartX();
            y = getStartY();
        } else if (percent == 1) {
            System.out.println("%: 1");
            x = getX();
            y = getY();
        } else {
            if (percent <= 1f) {
                bezier.valueAt(pos, percent);
            }
            x = pos.x;
            y = pos.y;
            System.out.println("%: " + percent + ", bezvalue: " + pos.x + ", " + pos.y);
        }
        actor.setPosition(x, y, getAlignment());
    }

}
