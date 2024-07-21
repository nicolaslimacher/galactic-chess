package com.mygdx.game.GamePiece;

import static java.lang.Math.pow;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.mygdx.game.Utils.Constants;

import java.util.ArrayList;
import java.util.stream.IntStream;

import jdk.vm.ci.meta.Constant;

public class Arrow extends Actor {
    TextureRegion textureRegion;
    Vector2 p0, p1, p2, p3;
    final int bezierNodeNum = 6;
    final Vector2 p1transform = new Vector2(-0.5f, 0.6f);
    final Vector2 p2transform = new Vector2(0.2f, 1.4f);
    Group trailsGroup;
    ArrayList<ArrowTrail> trails;


    public Arrow(Vector2 pos, Stage stage) {
        this.textureRegion = new TextureRegion(new Texture(Gdx.files.internal("small_arrow.png")));
        this.setBounds(pos.x,pos.y,64,64);
        this.setOrigin(this.getWidth()/2, this.getHeight()/2);
        System.out.println("arrow made");
        this.toFront();
        stage.addActor(this);
        //this.setDebug(true);

        p0 = pos;
        p3 = this.localToStageCoordinates(new Vector2(getX()*1.2f, getY()*1.2f));
        p3.x = p3.x + 64f;
        p1 = new Vector2((p0.x + (p3.x - p0.x)) * p1transform.x , (p0.y + (p3.y - p0.y)) * p1transform.y);
        p2 = new Vector2((p0.x + (p3.x - p0.x)) * p2transform.x , (p0.y + (p3.y - p0.y)) * p2transform.y);

        trailsGroup = new Group();
        trailsGroup.setName("trailsGroup");
        trails = new ArrayList<>();

        IntStream.range(0, bezierNodeNum).forEachOrdered(n -> {
            ArrowTrail trail = new ArrowTrail(GetBezierPosition(n));
            trail.setScale(0.3f + ((0.3f / bezierNodeNum) * n));
            trails.add(trail);
            trailsGroup.addActor(trail);
        });
        this.setRotation(GetBezierCurveRotation());

        stage.addActor(trailsGroup);
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        Color color = getColor();
        batch.setColor(color.r, color.g, color.b, 1f);
        batch.draw(textureRegion, getX(), getY(), getOriginX(), getOriginY(),
            getWidth(), getHeight(), getScaleX(), getScaleY(), getRotation());
    }

    @Override
    protected void positionChanged() {
        if (this.p0 != null){
            super.positionChanged();
            p3 = new Vector2(this.getX()+this.getWidth()/2, this.getY()+this.getWidth()/2);
            p3.x = p3.x + 64f;
            p1 = new Vector2((p0.x + (p3.x - p0.x) * p1transform.x), (p0.y + (p3.y - p0.y) * p1transform.y));
            p2 = new Vector2((p0.x + (p3.x - p0.x) * p2transform.x), (p0.y + (p3.y - p0.y) * p2transform.y));

            int nodeNum = 0;
            for ( ArrowTrail arrowTrail : trails){
                Vector2 newPos = GetBezierPosition(nodeNum);
                arrowTrail.setPosition(newPos.x, newPos.y);
                nodeNum += 1;

            }

            this.setRotation(GetBezierCurveRotation());
            this.toFront();
        }
    }

    private Vector2 GetBezierPosition(int NodeNumber){
        Vector2 trailPosition = new Vector2();
        float t = (1f / (bezierNodeNum + 1)) * (NodeNumber + 1);
        trailPosition.x = (float) ((pow(1f-t,3)*p0.x) + (3f*pow(1f-t,2)*t*p1.x) + (3f*(1-t)*pow(t,2f)*p2.x) + (pow(t,3)*p3.x))-64f;
        trailPosition.y = (float) ((pow(1f-t,3)*p0.y) + (3f*pow(1f-t,2)*t*p1.y) + (3f*(1-t)*pow(t,2f)*p2.y) + (pow(t,3)*p3.y));
        return trailPosition;
    }

    private float GetBezierCurveRotation(){
        Vector2 arrowPos = new Vector2(this.getX()+this.getWidth()/2, this.getY()+this.getWidth()/2);
        ArrowTrail lastArrowTrail = trails.get(trails.size()-1);
        Vector2 lastArrowTrailPos = new Vector2(lastArrowTrail.getX() + lastArrowTrail.getWidth()/2, lastArrowTrail.getY() + lastArrowTrail.getHeight()/2);
        System.out.println("arrow point: " + arrowPos.x + ", " + arrowPos.y);
        System.out.println("arrow trail point: " + lastArrowTrailPos.x +  ", " + lastArrowTrailPos.y);

        float angle = (float) (MathUtils.radiansToDegrees * Math.atan2(arrowPos.y - lastArrowTrailPos.y, arrowPos.x - lastArrowTrailPos.x));
        //float angle = (float) Math.toDegrees(theta);

        System.out.println("rotation: " + angle);
        return angle;
    }
}
