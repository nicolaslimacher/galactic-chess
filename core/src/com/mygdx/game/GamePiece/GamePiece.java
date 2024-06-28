package com.mygdx.game.GamePiece;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.utils.Align;
import com.mygdx.game.Board.Board;
import com.mygdx.game.GameManager.GameManager;
import com.mygdx.game.GameManager.Team;
import com.mygdx.game.MoveSets.MoveSet;
import com.mygdx.game.Utils.Constants;
import com.mygdx.game.Utils.CoordinateBoardPair;
import com.mygdx.game.Utils.IntPair;
import java.util.ArrayList;
import java.util.List;

public class GamePiece extends Actor{
    public GameManager gameManager;
    public final Board pawnBoard;
    public CoordinateBoardPair indexOnBoard;
    public final TextureRegion textureRegion;
    public Team team;
    public Group possibleMovesAndTargets;
    private final Group statsLabels;
    private int hitPoints;
    private int attackPoints;
    private Label hitPointsLabel;
    private Label attackPointsLabel;
    Skin skin = new Skin(Gdx.files.internal("buttons/uiskin.json"));


    public GamePiece(Board board, CoordinateBoardPair CoordinateBoardPair, Team team, int hitPoints, int attackPoints, GameManager gameManager){
        this.gameManager = gameManager;
        Texture pawnTexture = new Texture(Gdx.files.internal("black_player.png"));
        pawnBoard = board;
        this.team = team;
        this.SetAttackPoints(attackPoints);
        this.SetHitPoints(hitPoints);
        this.indexOnBoard = CoordinateBoardPair;
        this.statsLabels = new Group();
        this.textureRegion = new TextureRegion(pawnTexture, (int) Constants.TILE_SIZE, (int)Constants.TILE_SIZE);
        this.setBounds(textureRegion.getRegionX(), textureRegion.getRegionY(),
                textureRegion.getRegionWidth(), textureRegion.getRegionHeight());
        this.setPosition(board.GetBoardTilePosition(CoordinateBoardPair).x, board.GetBoardTilePosition(CoordinateBoardPair).y);
        addListener(pawnInputListener);
    }

    //Input method overrides NOTE: GameScreen stage input listeners will handle selecting and deselecting pawns
    //
    private final InputListener pawnInputListener = new InputListener(){
        public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
            GamePiece actor = (GamePiece) event.getListenerActor();
            //skip input if already moved this turn
            if (actor.gameManager.movedThisTurn){
                return true;
            }
            if (actor.team == Team.FRIENDLY && gameManager.selectedMoveSet != null) {
                actor.possibleMovesAndTargets = new Group();
                actor.possibleMovesAndTargets.setName("possibleMovesGroup" + actor.getName());
                actor.drawPossibleMoves(GetPossibleMoves(gameManager.selectedMoveSet));
                actor.drawPossibleTargets(gameManager.selectedMoveSet);
                actor.getStage().addActor(actor.possibleMovesAndTargets);
                actor.gameManager.selectedGamePiece = actor;
            }
            return true;
        }
    };

    ////////////////////////////////////////////////////////////////////////////////////////////////
    //                            INTERACTING WITH OTHER GAME PIECES                              //
    ////////////////////////////////////////////////////////////////////////////////////////////////

    public int GetHitPoints() {
        return hitPoints;
    }

    public void SetHitPoints(int hitPoints) {
        this.hitPoints = hitPoints;
        if (this.hitPointsLabel != null){this.hitPointsLabel.remove();}
        this.hitPointsLabel = new Label(String.valueOf(hitPoints), skin);
    }

    public int GetAttackPoints() {
        return attackPoints;
    }

    public void SetAttackPoints(int attackPoints) {
        this.attackPoints = attackPoints;
        if (this.attackPointsLabel != null){this.attackPointsLabel.remove();}
        this.hitPointsLabel = new Label(String.valueOf(attackPoints), skin);
    }

    public boolean HitPawn (GamePiece enemyGamePiece){
        return enemyGamePiece.GetHitAndIsFatal(this.attackPoints);
    }

    //TODO: I retured bool for something? to move pawn to enemy location?
    public boolean GetHitAndIsFatal(int AtkDmg){
        this.SetHitPoints(this.GetHitPoints() - AtkDmg);
        this.hitPointsLabel.remove();
        this.attackPointsLabel.remove();
        this.addHPandAttackLabels();
        if (this.hitPoints == 0){
            this.hitPointsLabel.remove();
            this.attackPointsLabel.remove();
            this.statsLabels.remove();
            this.remove();
            return true;
        }
        return false;
    }

    public List<CoordinateBoardPair> GetPossibleMoves(MoveSet moveSet){
        List<CoordinateBoardPair> possibleMoves = new ArrayList<CoordinateBoardPair>();
        for (IntPair possibleMove : moveSet.possibleMoves){
            CoordinateBoardPair newMove = new CoordinateBoardPair(this.indexOnBoard.x + possibleMove.xVal, this.indexOnBoard.y + possibleMove.yVal);
            if (isValidMove(possibleMove) && !gameManager.IsPawnAtBoardLocation(newMove)){
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

    public boolean isValidEnemyMove(IntPair intPair) {
        boolean isValid = false;
        boolean xIsValid = 0 <= this.indexOnBoard.x + (-1 * intPair.xVal) && this.indexOnBoard.x + (-1 * intPair.xVal) <= this.pawnBoard.boardColumns-1;
        boolean yIsValid = 0 <= this.indexOnBoard.y + (-1 * intPair.yVal) && this.indexOnBoard.y + (-1 * intPair.yVal) <= this.pawnBoard.boardRows-1;
        if (xIsValid && yIsValid){
            isValid = true;
        }
        return isValid;
    }

    public void Move(CoordinateBoardPair coordinateBoardPair) {
        this.setPosition(pawnBoard.GetBoardTilePosition(coordinateBoardPair).x, pawnBoard.GetBoardTilePosition(coordinateBoardPair).y);
        this.indexOnBoard = coordinateBoardPair;
        this.setName("Pawn"+coordinateBoardPair.x+","+coordinateBoardPair.y);
        //TODO: Cap
        this.SetLabelPositions();
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////
    //                                       DRAWING GAME PIECES                                  //
    ////////////////////////////////////////////////////////////////////////////////////////////////

    @Override
    public void draw(Batch batch, float parentAlpha){
        Color color = getColor();
        batch.setColor(color.r, color.g, color.b, color.a * parentAlpha);
        batch.draw(textureRegion, getX(), getY(), getOriginX(), getOriginY(),
                getWidth(), getHeight(), getScaleX(), getScaleY(), getRotation());
    }

    public void drawPossibleMoves(List<CoordinateBoardPair> possibleMoves){
        for (CoordinateBoardPair possibleMoveCoordinateBoardPair : possibleMoves) {
            PossibleMove possibleMove = new PossibleMove(this, possibleMoveCoordinateBoardPair, this.pawnBoard);
            possibleMove.setName("possiblePawnMove" + String.valueOf(possibleMoveCoordinateBoardPair.GetX()) + "," + String.valueOf(possibleMoveCoordinateBoardPair.GetY()));
            this.possibleMovesAndTargets.addActor(possibleMove);
        }
    }

    public void drawPossibleTargets(MoveSet moveSet){
        for (IntPair possibleMove : moveSet.possibleMoves){
            CoordinateBoardPair newMove = new CoordinateBoardPair(this.indexOnBoard.x + possibleMove.xVal, this.indexOnBoard.y + possibleMove.yVal);
            if (isValidMove(possibleMove) && gameManager.IsEnemyPawnAtBoardLocation(newMove)){
                if (gameManager.IsPawnAtBoardLocation(newMove)) {
                    Target target = new Target(this, newMove, this.pawnBoard, pawnBoard.GetPawnAtCoordinate(newMove));
                    this.possibleMovesAndTargets.addActor(target);
                }
            }
        }
    }

    public void addHPandAttackLabels(){
        //TODO: try using TextButton and setDisabled
        this.hitPointsLabel = new Label(String.valueOf(this.hitPoints), skin, "hpStatsLabel");
        this.attackPointsLabel = new Label(String.valueOf(this.attackPoints), skin, "atkStatsLabel");

        Image hpBackground;
        if (this.team == Team.FRIENDLY){
            hpBackground = new Image(new Texture(Gdx.files.internal("green_hp_background.png")));
            hitPointsLabel.getStyle().fontColor = Color.BLACK;
        }else{
            hpBackground = new Image(new Texture(Gdx.files.internal("red_hp_background.png")));
        }
        hitPointsLabel.getStyle().background = hpBackground.getDrawable();

        Image atkBackground = new Image(new Texture(Gdx.files.internal("atk_background.png")));
        attackPointsLabel.getStyle().background = atkBackground.getDrawable();
        attackPointsLabel.getStyle().fontColor = Color.BLACK;

        this.SetLabelPositions();
        this.statsLabels.addActor(hitPointsLabel);
        this.statsLabels.addActor(attackPointsLabel);
        this.getStage().addActor(this.statsLabels);
    }

    private void SetLabelPositions (){
        attackPointsLabel.setBounds(this.getX() + 2, this.getY() + 2, 20, 20);
        attackPointsLabel.setAlignment(Align.center);
        hitPointsLabel.setBounds(this.getX()+this.getWidth() - 22,this.getY() + 2, 20, 20);
        hitPointsLabel.setAlignment(Align.center);
    }
}
