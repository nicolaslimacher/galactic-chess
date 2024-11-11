package com.mygdx.game.GamePiece;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.DragAndDrop;
import com.badlogic.gdx.utils.Align;
import com.mygdx.game.Actions.GamePieceJetpackJump;
import com.mygdx.game.Board.Board;
import com.mygdx.game.Command.Command;
import com.mygdx.game.Command.CommandType;
import com.mygdx.game.GameManager.GameManager;
import com.mygdx.game.GameManager.Team;
import com.mygdx.game.MoveSets.MoveSet;
import com.mygdx.game.Utils.Constants;
import com.mygdx.game.Utils.Helpers;
import com.mygdx.game.Utils.IntPair;
import java.util.ArrayList;
import java.util.List;

public class DefaultPawn extends Actor implements GamePiece{
    //metadata
    public final GameManager gameManager;
    public final Board board;
    public IntPair indexOnBoard;
    public int gamePiecesID;
    public TextureRegion textureRegion;
    public Team team;
    public Group possibleMovesAndTargets;
    public boolean isKing;
    public final TextureRegion crown;
    public boolean isAlive;

    //stats
    public final Group statsLabels;
    public int hitPoints;
    public int attackPoints;
    public Label hitPointsLabel;
    public Label attackPointsLabel;

    //drag and drop
    public final DragAndDrop dragAndDrop;
    public final DragAndDrop.Payload payload;
    public float preDragXPosition;
    public float preDragYPosition;

    Skin skin = new Skin(Gdx.files.internal("skins/uiskin.json"));


    public DefaultPawn(Board board, GameManager gameManager, IntPair coordinates, Team team, boolean isKing, int gamePiecesID){
        //metadata
        this.gameManager = gameManager;
        this.board = board;
        this.team = team;
        this.gamePiecesID = gamePiecesID;
        Texture gamePieceTexture = new Texture(Gdx.files.internal("black_player.png"));
        this.textureRegion = new TextureRegion(gamePieceTexture, (int) Constants.TILE_SIZE, (int)Constants.TILE_SIZE);
        this.setBounds(textureRegion.getRegionX(), textureRegion.getRegionY(),
                textureRegion.getRegionWidth(), textureRegion.getRegionHeight());
        this.setPosition(board.GetBoardTilePosition(coordinates).x, board.GetBoardTilePosition(coordinates).y);

        this.isKing = isKing;
        this.crown = new TextureRegion(new Texture(Gdx.files.internal("king_crown.png")), 19, 32);
        this.isAlive = true;
        this.setName("GamePiece"+ coordinates.xVal + "," + coordinates.yVal);

        //stats
        this.SetAttackPoints(attackPoints);
        this.SetHitPoints(hitPoints);
        this.indexOnBoard = coordinates;
        this.statsLabels = new Group();
        addHpAndAttackLabels();

        //drag and drop
        this.dragAndDrop = new DragAndDrop();
        this.payload = new DragAndDrop.Payload();
        this.addListener(gamePieceInputListener);
        dragAndDrop.addSource(new DragAndDrop.Source(this) {
            @Override
            public DragAndDrop.Payload dragStart(InputEvent event, float x, float y, int pointer) {
                DefaultPawn defaultPawn = (DefaultPawn) event.getListenerActor();
                Gdx.app.log("GamePiece", "MovedThisTurn? :" + defaultPawn.gameManager.movedThisTurn + ".");

                RemoveGamePieceInfo();

                if(defaultPawn.gameManager.movedThisTurn){
                    return null;
                }

                //TODO: try show info panel on a timer, start drag will cancel timer?
                if (defaultPawn.team == Team.FRIENDLY && gameManager.selectedMoveSet != null) {
                        Arrow arrow = new Arrow(new Vector2(defaultPawn.getX() + defaultPawn.getWidth()/2, defaultPawn.getY()+ defaultPawn.getHeight()/2), defaultPawn.getStage());
                        defaultPawn.getStage().addActor(arrow);
                        payload.setDragActor(arrow);
                        arrow.toFront();
                        dragAndDrop.setDragActorPosition(arrow.getWidth() / 2, -arrow.getHeight() / 2);

                        Gdx.app.log("GamePiece", "Drag arrow created.");

                        //draw possible moves
                        //only show targets if player can move
                        if (defaultPawn.team == Team.FRIENDLY && defaultPawn.gameManager.selectedMoveSet != null) {
                            defaultPawn.possibleMovesAndTargets = new Group();
                            defaultPawn.possibleMovesAndTargets.setName("possibleMovesGroup" + defaultPawn.getName());
                            defaultPawn.drawPossibleMoves(gameManager.selectedMoveSet);
                            Gdx.app.log("GamePiece", "Possible moves group : " +  defaultPawn.possibleMovesAndTargets.getChildren() + ".");
                            defaultPawn.toFront();
                            Gdx.app.log("GamePiece", "Drag targets created.");
                        }
                }
                return payload;
            }

            @Override
            public void drag(InputEvent event, float x, float y, int pointer) {
                //RemoveGamePieceInfo(); //technically redundant?
            }

            @Override
            public void dragStop(InputEvent event, float x, float y, int pointer, DragAndDrop.Payload payload, DragAndDrop.Target target) {
                Gdx.app.log("GamePiece", "Drag stop fired, checking target.");
                DefaultPawn defaultPawn = (DefaultPawn) event.getListenerActor();
                if (payload.getDragActor() != null){
                    payload.getDragActor().remove();
                    ((Arrow) payload.getDragActor()).trailsGroup.remove();
                }
                if (target == null){
                    defaultPawn.setPosition(defaultPawn.preDragXPosition, defaultPawn.preDragYPosition);
                    if (defaultPawn.possibleMovesAndTargets != null){
                        defaultPawn.possibleMovesAndTargets.remove();
                    }
                }
            }



        });

        //end drag and drop, adding floating hover animations
        float pathY = MathUtils.random(1f, 4f);
        float duration = MathUtils.random(5f, 8f);
        int firstDirection = MathUtils.random(0,1);
        this.addAction(Actions.moveBy(0f, -(pathY/2), duration));
        if (firstDirection == 0){
            this.addAction(
                    Actions.forever(
                            Actions.sequence(
                                    Actions.moveBy(0f, pathY, duration),
                                    Actions.moveBy(0f, -pathY, duration)
                            )
                    )
            );
        }else {
            this.addAction(
                    Actions.forever(
                            Actions.sequence(
                                    Actions.moveBy(0f, -pathY, duration),
                                    Actions.moveBy(0f, pathY, duration)
                            )
                    )
            );
        }

        Gdx.app.log("GamePiece", "GamePiece " + this.getName() + "created.");
    }

    private final InputListener gamePieceInputListener = new InputListener(){
        @Override
        public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
            Gdx.app.log("GamePiece", "TouchDown fired.");
            return true;
        }
        public void touchUp(InputEvent event, float x, float y, int pointer, int button){
            Gdx.app.log("GamePiece", "TouchUp fired.");
            DefaultPawn defaultPawn = (DefaultPawn) event.getListenerActor();
            if(defaultPawn == defaultPawn.hit(x,y,false)){
                defaultPawn.DisplayGamePieceInfo();
            }
        }
    };

    public String GetName(){
        return this.getName();
    }
    ////////////////////////////////////////////////////////////////////////////////////////////////
    //                            INTERACTING WITH OTHER GAME PIECES                              //
    ////////////////////////////////////////////////////////////////////////////////////////////////

    public GameManager GetGameManager(){
        return this.gameManager;
    }

    public Board GetBoard(){
        return this.board;
    }

    public IntPair GetIndexOnBoard() {
        return this.indexOnBoard;
    }

    public int GetGamePiecesID() {
        return this.gamePiecesID;
    }

    public boolean GetIsKing() {
        return this.isKing;
    }

    public boolean GetIsAlive() {
        return this.isAlive;
    }

    public int GetHitPoints() {
        return this.hitPoints;
    }

    public void SetHitPoints(int hitPoints) {
        this.hitPoints = hitPoints;
        if (this.hitPointsLabel != null){this.hitPointsLabel.remove();}
        this.hitPointsLabel = new Label(String.valueOf(hitPoints), skin);
    }

    @Override
    public int GetAttackPoints() {
        return attackPoints;
    }


    public void SetAttackPoints(int attackPoints) {
        this.attackPoints = attackPoints;
        if (this.attackPointsLabel != null){this.attackPointsLabel.remove();}
        this.hitPointsLabel = new Label(String.valueOf(attackPoints), skin);
    }

    public boolean HitGamePiece(GamePiece gamePiece){
        Gdx.app.log("GamePiece", "GamePiece " + this.getName() + " is hitting " + gamePiece.GetName() + ".");
        return gamePiece.GetHitAndIsFatal(this.attackPoints);
    }

    //TODO: I returned bool for something? to move game piece to enemy location?
    public boolean GetHitAndIsFatal(int AtkDmg){
        this.SetHitPoints(this.GetHitPoints() - AtkDmg);
        this.hitPointsLabel.remove();
        this.attackPointsLabel.remove();
        this.addHpAndAttackLabels();
        if (this.hitPoints == 0){
            this.isAlive = false;
            this.hitPointsLabel.remove();
            this.attackPointsLabel.remove();
            this.statsLabels.remove();
            this.remove();
            return true;
        }
        return false;
    }

    public List<IntPair> GetPossibleMoves(MoveSet moveSet){
        List<IntPair> possibleMoves = new ArrayList<IntPair>();
        for (IntPair possibleMove : moveSet.possibleMoves){
            IntPair newMove = new IntPair(this.indexOnBoard.xVal + possibleMove.xVal, this.indexOnBoard.yVal + possibleMove.yVal);
            if (IsValidMove(possibleMove) && !gameManager.IsGamePieceAtBoardLocation(newMove)){
                possibleMoves.add(newMove);
            }
        }
        return possibleMoves;
    }

    public List<IntPair> GetPossibleTargets(MoveSet moveSet){
        List<IntPair> possibleMoves = new ArrayList<IntPair>();
        for (IntPair possibleMove : moveSet.possibleMoves){
            IntPair newMove = new IntPair(this.indexOnBoard.xVal + possibleMove.xVal, this.indexOnBoard.yVal + possibleMove.yVal);
            if (IsValidMove(possibleMove) && gameManager.IsTeamGamePieceAtBoardLocation(newMove, Team.ENEMY)){
                if (gameManager.IsGamePieceAtBoardLocation(newMove)) {
                    possibleMoves.add(newMove);
                }
            }
        }
        return possibleMoves;
    }

    public boolean IsValidMove(IntPair intPair) {
        boolean isValid = false;
        boolean xIsValid = 0 <= this.indexOnBoard.xVal + intPair.xVal && this.indexOnBoard.xVal + intPair.xVal <= this.board.boardColumns-1;
        boolean yIsValid = 0 <= this.indexOnBoard.yVal + intPair.yVal && this.indexOnBoard.yVal + intPair.yVal <= this.board.boardRows-1;
        if (xIsValid && yIsValid){
            isValid = true;
        }
        return isValid;
    }

    //due to mirrored game board from enemy perspective, math for checking is different
    public boolean IsValidEnemyMove(IntPair intPair) {
        boolean isValid = false;
        boolean xIsValid = 0 <= this.indexOnBoard.xVal + (-1 * intPair.xVal) && this.indexOnBoard.xVal + (-1 * intPair.xVal) <= this.board.boardColumns-1;
        boolean yIsValid = 0 <= this.indexOnBoard.yVal + (-1 * intPair.yVal) && this.indexOnBoard.yVal + (-1 * intPair.yVal) <= this.board.boardRows-1;
        if (xIsValid && yIsValid){
            isValid = true;
        }
        return isValid;
    }


    public void MoveToTile (IntPair coordinates, float jumpDelay) {
        //TODO: add switch on field "MoveStyle", default to GamePieceJetpackJump
        new GamePieceJetpackJump(this, coordinates, jumpDelay);
    }

    //currently only used for undo to instantly reset board
    public void Teleport(IntPair coordinateBoardPair) {
        this.setPosition(board.GetBoardTilePosition(coordinateBoardPair).x, board.GetBoardTilePosition(coordinateBoardPair).y);
        this.indexOnBoard = coordinateBoardPair;
        this.setName("GamePiece"+coordinateBoardPair.xVal+","+coordinateBoardPair.yVal);
        this.SetLabelPositions();
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////
    //                                       DRAWING GAME PIECES                                  //
    ////////////////////////////////////////////////////////////////////////////////////////////////

    @Override
    public void draw(Batch batch, float parentAlpha){
        Color color = getColor();
        batch.setColor(color.r, color.g, color.b, 1f);
        batch.draw(textureRegion, getX(), getY(), getOriginX(), getOriginY(),
                getWidth(), getHeight(), getScaleX(), getScaleY(), getRotation());
        //draw labels
        if (hitPointsLabel != null && attackPointsLabel != null) {
            SetLabelPositions();
            this.hitPointsLabel.draw(batch, parentAlpha);
            this.attackPointsLabel.draw(batch, parentAlpha);
        }
        if (isKing) {
            batch.draw(crown.getTexture(), this.getX() + this.getWidth() / 2 - 16, this.getY() + this.getHeight() - 10, 32, 19);
        }
    }

    public void drawPossibleMoves(MoveSet moveSet){
        Gdx.app.log("GamePiece", "Drawing possible moves and targets");
        for (IntPair move : GetPossibleMoves(moveSet)) {
            PossibleMove possibleMoveTarget = new PossibleMove(this, move, CommandType.MOVE);
            this.possibleMovesAndTargets.addActor(possibleMoveTarget);
            dragAndDrop.addTarget(new DragAndDrop.Target(possibleMoveTarget) {
                @Override
                public boolean drag(DragAndDrop.Source source, DragAndDrop.Payload payload, float x, float y, int pointer) {
                    return true;
                }

                @Override
                public void drop(DragAndDrop.Source source, DragAndDrop.Payload payload, float x, float y, int pointer) {
                    Gdx.app.log("GamePiece", "Drop fired : move.");
                    DefaultPawn defaultPawn = (DefaultPawn) source.getActor();
                    defaultPawn.gameManager.latestGamePieceCommand = new Command(defaultPawn, possibleMoveTarget.indexOnBoard, possibleMoveTarget.type, gameManager.selectedMoveSet);
                    defaultPawn.gameManager.latestGamePieceCommand.Execute();
                    possibleMoveTarget.getParent().remove();
                }
            });
            Gdx.app.log("GamePiece", "Possible moves added to possibleMovesAndTargets group.");
        }
        for (IntPair target : GetPossibleTargets(moveSet)){
            PossibleMove possibleMoveHit = new PossibleMove(this, target, CommandType.HIT);
            this.possibleMovesAndTargets.addActor(possibleMoveHit);
            dragAndDrop.addTarget(new DragAndDrop.Target(possibleMoveHit) {
                @Override
                public boolean drag(DragAndDrop.Source source, DragAndDrop.Payload payload, float x, float y, int pointer) {
                    return true;
                }

                @Override
                public void drop(DragAndDrop.Source source, DragAndDrop.Payload payload, float x, float y, int pointer) {
                    Gdx.app.log("GamePiece", "Drop fired : hit.");
                    DefaultPawn defaultPawn = (DefaultPawn) source.getActor();
                    defaultPawn.gameManager.latestGamePieceCommand = new Command(defaultPawn, possibleMoveHit.indexOnBoard, possibleMoveHit.type, gameManager.selectedMoveSet);
                    defaultPawn.gameManager.latestGamePieceCommand.Execute();
                    //if execute
                    possibleMoveHit.getParent().remove();
                }
            });
            Gdx.app.log("GamePiece", "Possible targets added to possibleMovesAndTargets group.");
        }
        this.getStage().addActor(this.possibleMovesAndTargets);
    }

    public void addHpAndAttackLabels(){
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
    }

    public void SetLabelPositions (){
        attackPointsLabel.setBounds(this.getX() + 2, this.getY() + 2, 20, 20);
        attackPointsLabel.setAlignment(Align.center);
        hitPointsLabel.setBounds(this.getX()+this.getWidth() - 22,this.getY() + 2, 20, 20);
        hitPointsLabel.setAlignment(Align.center);
    }


    public void DisplayGamePieceInfo(){
        TextButton gamePieceInfo = new TextButton("", skin);

        RemoveGamePieceInfo();

        gamePieceInfo.setName("gamePieceInfo");
        gamePieceInfo.setText("NAME: " + this.getName());
        Helpers.KeepPopUpOverBoard(gamePieceInfo, this.getX() + this.getWidth() / 2 - 125, this.getY() + this.getWidth() + 10, 250, 250);
        gamePieceInfo.addListener(new InputListener(){
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                TextButton thisButton = (TextButton) event.getListenerActor();
                thisButton.remove();
                return true;
            }
        });
        this.getStage().addActor(gamePieceInfo);
        Gdx.app.log("GamePiece", "GamePiece Info Screen created and displayed.");
    }

    public void RemoveGamePieceInfo(){
        //removes any gamePieceInfo buttons on screen
        if (this.getStage().getRoot().findActor("gamePieceInfo") != null){
            this.getStage().getRoot().findActor("gamePieceInfo").remove();
        }
        Gdx.app.log("GamePiece", "GamePiece removed.");
    }
}
