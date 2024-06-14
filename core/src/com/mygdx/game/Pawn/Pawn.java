package com.mygdx.game.Pawn;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Null;
import com.mygdx.game.Board.Board;
import com.mygdx.game.MoveSets.MoveSet;
import com.mygdx.game.Utils.Constants;
import com.mygdx.game.Utils.CoordinateBoardPair;
import com.mygdx.game.Utils.IntPair;

import org.w3c.dom.ranges.Range;

import java.time.temporal.ValueRange;
import java.util.ArrayList;
import java.util.List;

public class Pawn extends Actor{
    public final Board pawnBoard;
    public CoordinateBoardPair indexOnBoard;
    private final TextureRegion textureRegion;
    public boolean isSelected;
    public boolean isFriendly;
    public Group possibleMovesAndTargets;

    public int getHitPoints() {
        return hitPoints;
    }

    public void getHit(int AtkDmg){
        System.out.println(this.getName() + ": i've been hit");
        this.setHitPoints(this.getAttackPoints() - AtkDmg);
    }

    public void setHitPoints(int hitPoints) {
        this.hitPoints = hitPoints;
        this.hitPointsLabel = new Label(String.valueOf(hitPoints), skin);
    }

    public int getAttackPoints() {
        return attackPoints;
    }

    public void setAttackPoints(int attackPoints) {
        this.attackPoints = attackPoints;
        this.hitPointsLabel = new Label(String.valueOf(attackPoints), skin);
    }

    private int hitPoints;
    private int attackPoints;
    private Label hitPointsLabel;
    private Label attackPointsLabel;
    Skin skin = new Skin(Gdx.files.internal("buttons/uiskin.json"));

    public Pawn(Board board, CoordinateBoardPair CoordinateBoardPair, boolean isFriendly, int hitPoints, int attackPoints){
        Texture pawnTexture = new Texture(Gdx.files.internal("black_player.png"));
        pawnBoard = board;
        this.isSelected = false;
        this.isFriendly = isFriendly;
        this.setAttackPoints(attackPoints);
        this.setHitPoints(hitPoints);
        this.indexOnBoard = CoordinateBoardPair;
        this.textureRegion = new TextureRegion(pawnTexture, (int) Constants.TILE_SIZE, (int)Constants.TILE_SIZE);
        this.setBounds(textureRegion.getRegionX(), textureRegion.getRegionY(),
                textureRegion.getRegionWidth(), textureRegion.getRegionHeight());
        this.setPosition(board.GetBoardTilePosition(CoordinateBoardPair).x, board.GetBoardTilePosition(CoordinateBoardPair).y);
        Label hpLabel = new Label(String.valueOf(this.attackPoints), skin);
        addListener(pawnInputListener);
    }

    @Override
    public void draw(Batch batch, float parentAlpha){
        Color color = getColor();
        batch.setColor(color.r, color.g, color.b, color.a * parentAlpha);
        batch.draw(textureRegion, getX(), getY(), getOriginX(), getOriginY(),
                getWidth(), getHeight(), getScaleX(), getScaleY(), getRotation());
    }

    //TODO: split draw into possible moves and possible target
    public void drawPossibleMoves(List<CoordinateBoardPair> possibleMoves){
        for (CoordinateBoardPair possibleMoveCoordinateBoardPair : possibleMoves) {
            PossiblePawnMove possiblePawnMove = new PossiblePawnMove(this, possibleMoveCoordinateBoardPair, this.pawnBoard);
            possiblePawnMove.setName("possiblePawnMove" + String.valueOf(possibleMoveCoordinateBoardPair.GetX()) + "," + String.valueOf(possibleMoveCoordinateBoardPair.GetY()));
            this.possibleMovesAndTargets.addActor(possiblePawnMove);
        }

    }

    public void drawPossibleTargets(MoveSet moveSet){
        List<CoordinateBoardPair> possibleMoves = new ArrayList<CoordinateBoardPair>();
        for (IntPair possibleMove : moveSet.possibleMoves){
            CoordinateBoardPair newMove = new CoordinateBoardPair(this.indexOnBoard.x + possibleMove.xVal, this.indexOnBoard.y + possibleMove.yVal);
            if (isValidMove(possibleMove) && IsEnemyPawnAtBoardLocation(newMove)){
                if (IsPawnAtBoardLocation(newMove)) {
                    Target target = new Target(this, newMove, this.pawnBoard, pawnBoard.GetPawnAtCoordinate(newMove));
                    this.possibleMovesAndTargets.addActor(target);
                }
            }
        }
    }


    public List<CoordinateBoardPair> GetPossibleMoves(MoveSet moveSet){
        List<CoordinateBoardPair> possibleMoves = new ArrayList<CoordinateBoardPair>();
        for (IntPair possibleMove : moveSet.possibleMoves){
            CoordinateBoardPair newMove = new CoordinateBoardPair(this.indexOnBoard.x + possibleMove.xVal, this.indexOnBoard.y + possibleMove.yVal);
            if (isValidMove(possibleMove) && !IsPawnAtBoardLocation(newMove)){
                possibleMoves.add(newMove);
            }
        }
        return possibleMoves;
    }

    public List<CoordinateBoardPair> GetPossibleTargets(MoveSet moveSet){
        List<CoordinateBoardPair> possibleMoves = new ArrayList<CoordinateBoardPair>();
        for (IntPair possibleMove : moveSet.possibleMoves){
            CoordinateBoardPair newMove = new CoordinateBoardPair(this.indexOnBoard.x + possibleMove.xVal, this.indexOnBoard.y + possibleMove.yVal);
            if (isValidMove(possibleMove) && IsEnemyPawnAtBoardLocation(newMove)){
                possibleMoves.add(newMove);
            }
        }
        return possibleMoves;
    }

    public boolean isValidMove(IntPair intPair) {
        boolean isValid = false;
        boolean xIsValid = 0 <= this.indexOnBoard.x + intPair.xVal && this.indexOnBoard.x + intPair.xVal <= this.pawnBoard.boardColumns-1;
        boolean yIsValid = 0 <= this.indexOnBoard.y + intPair.yVal && this.indexOnBoard.y + intPair.yVal <= this.pawnBoard.boardRows-1;
        if (xIsValid && yIsValid){
            isValid = true;
        }
        return isValid;
    }

    public void move(CoordinateBoardPair coordinateBoardPair) {
        this.setPosition(pawnBoard.GetBoardTilePosition(coordinateBoardPair).x, pawnBoard.GetBoardTilePosition(coordinateBoardPair).y);
        this.indexOnBoard = coordinateBoardPair;
        this.setName("Pawn"+String.valueOf(coordinateBoardPair.x)+","+String.valueOf(coordinateBoardPair.y));
        this.SetLabelPositions();

        //set move select menu back to visible
        this.pawnBoard.selectedMoveSet = null;
        this.getStage().getRoot().findActor("MoveSelectButtonMenu").setVisible(true);
        this.getStage().getRoot().findActor("cancelButton").getParent().remove();
        this.pawnBoard.menuTable = null;
    }

    //Input method overrides NOTE: GameScreen stage input listeners will set pawn.isSelected to false
    private final InputListener pawnInputListener = new InputListener(){
        public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
            Pawn actor = (Pawn) event.getListenerActor();
            if (pawnBoard.selectedMoveSet != null) {
                actor.possibleMovesAndTargets = new Group();
                actor.possibleMovesAndTargets.setName("possibleMovesGroup" + actor.getName());
                actor.drawPossibleMoves(GetPossibleMoves(pawnBoard.selectedMoveSet));
                actor.drawPossibleTargets(pawnBoard.selectedMoveSet);
                actor.getStage().addActor(actor.possibleMovesAndTargets);
                isSelected = true;
            }
            return true;
        }
    };

    public Boolean IsPawnAtBoardLocation(CoordinateBoardPair coordinateBoardPair) {
        return pawnBoard.GetPawnAtCoordinate(coordinateBoardPair) != null;
    }
    public Boolean IsEnemyPawnAtBoardLocation(CoordinateBoardPair coordinateBoardPair) {
        return pawnBoard.GetPawnAtCoordinate(coordinateBoardPair) != null && !pawnBoard.GetPawnAtCoordinate(coordinateBoardPair).isFriendly;
    }

    public void HitPawn (Pawn enemyPawn){
        System.out.println(this.getName()+ ": I'm hitting you!");
        enemyPawn.getHit(this.attackPoints);
    }


    public void addHPandAttackLabels(){
        this.hitPointsLabel = new Label(String.valueOf(this.hitPoints), skin, "hpStatsLabel");
        this.attackPointsLabel = new Label(String.valueOf(this.attackPoints), skin, "atkStatsLabel");
        this.SetLabelPositions();

        Image hpBackground = new Image(new Texture(Gdx.files.internal("hpLabelBackground.png")));
        hitPointsLabel.getStyle().background = hpBackground.getDrawable();

        Image atkBackground = new Image(new Texture(Gdx.files.internal("atkLabelBackground.png")));
        attackPointsLabel.getStyle().background = atkBackground.getDrawable();

        this.getStage().addActor(hitPointsLabel);
        this.getStage().addActor(attackPointsLabel);
    }

    private void SetLabelPositions (){
        attackPointsLabel.setBounds(this.getX() + 2, this.getY() + 2, 20, 20);
        attackPointsLabel.setAlignment(Align.center);
        hitPointsLabel.setBounds(this.getX()+this.getWidth()-hitPointsLabel.getWidth() - 5,this.getY() + 2, 20, 20);
        hitPointsLabel.setAlignment(Align.center);
    }
}
