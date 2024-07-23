package com.mygdx.game.GamePiece;

import static java.lang.Math.pow;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Bezier;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.Stage;

import java.util.ArrayList;
import java.util.stream.IntStream;

public class Arrow extends Actor {
    TextureRegion textureRegion;
    private Vector2 p0, p1, p2, p3;
    private Bezier bezier;
    Vector2 pos = new Vector2(); //overwritten by bezier.valueAt
    final int arrowTrailNum = 9;
    final Vector2 p1transform = new Vector2(-0.3f, 0.8f);
    final Vector2 p2transform = new Vector2(0.1f, 1.8f);
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

        p0 = new Vector2(pos.x,pos.y);
        p3 = new Vector2(Arrow.this.getX() + Arrow.this.getWidth()/2, Arrow.this.getY() + Arrow.this.getHeight()/2);
        Vector2 difference = new Vector2(p3.x - p0.x, p3.y - p0.y);
        p1 = new Vector2(p0.x + (difference.x) * p1transform.x , p0.y + (difference.y) * p1transform.y);
        p2 = new Vector2(p0.x + (difference.x) * p2transform.x , p0.y + (difference.y) * p2transform.y);
        bezier = new Bezier<>(p0, p1, p2, p3);

        trailsGroup = new Group();
        trailsGroup.setName("trailsGroup");
        trails = new ArrayList<>();

        IntStream.range(0, arrowTrailNum).forEachOrdered(n -> {
            float t = (1f / (arrowTrailNum+1))*(n+1);;
            bezier.valueAt(pos, t);
            ArrowTrail trail = new ArrowTrail(pos);
            trail.setScale(0.3f + ((0.3f / arrowTrailNum) * n));
            trails.add(trail);
            trailsGroup.addActor(trail);
            trail.toFront();
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
            p3.set(new Vector2((this.getX() + this.getWidth()/2), (this.getY() + this.getHeight()/2)));
            Vector2 difference = new Vector2(p3.x - p0.x, p3.y - p0.y);
            p1.set(new Vector2(p0.x + (difference.x) * p1transform.x , p0.y + (difference.y) * p1transform.y));
            p2.set(new Vector2(p0.x + (difference.x) * p2transform.x , p0.y + (difference.y) * p2transform.y));


            int arrowTrailNumber = 0;
            for ( ArrowTrail arrowTrail : trails){
                float t = (1f / (arrowTrailNum+1))*(arrowTrailNumber+1);
                bezier.valueAt(pos, t); //update pos with new location, will be rewritten for each arrow trail
                arrowTrail.setPosition(pos.x - (arrowTrail.getWidth()*arrowTrail.getScaleX())/2, pos.y - (arrowTrail.getHeight()*arrowTrail.getScaleY())/2);
                arrowTrailNumber += 1;
            }

            this.setRotation(GetBezierCurveRotation());
            this.toFront();
        }
    }

    private float GetBezierCurveRotation(){
        ArrowTrail lastArrowTrail = trails.get(trails.size()-1);
        Vector2 lastArrowTrailPos = new Vector2(lastArrowTrail.getX() + (lastArrowTrail.getWidth() * lastArrowTrail.getScaleX())/2, lastArrowTrail.getY() + (lastArrowTrail.getHeight() * lastArrowTrail.getScaleY())/2);
        float angle = (float) (MathUtils.radiansToDegrees * Math.atan2(p3.y - lastArrowTrailPos.y, p3.x - lastArrowTrailPos.x));
        return angle;
    }
}
