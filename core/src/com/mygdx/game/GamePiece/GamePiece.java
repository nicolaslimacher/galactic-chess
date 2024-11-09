package com.mygdx.game.GamePiece;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.actions.ParallelAction;
import com.badlogic.gdx.scenes.scene2d.actions.RunnableAction;
import com.badlogic.gdx.scenes.scene2d.actions.ScaleToAction;
import com.badlogic.gdx.scenes.scene2d.actions.SequenceAction;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.DragAndDrop;
import com.badlogic.gdx.utils.Align;
import com.mygdx.game.Actions.ArcToAction;
import com.mygdx.game.Board.Board;
import com.mygdx.game.Board.BoardTile;
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

public class GamePiece extends Actor{
    //metadata
    public final GameManager gameManager;
    public final Board board;
    public IntPair indexOnBoard;
    public final TextureRegion textureRegion;
    public Team team;
    public Group possibleMovesAndTargets;
    public boolean isKing;
    private final TextureRegion crown;
    public boolean isAlive;

    //stats
    private final Group statsLabels;
    private int hitPoints;
    private int attackPoints;
    private Label hitPointsLabel;
    private Label attackPointsLabel;

    //drag and drop
    private final DragAndDrop dragAndDrop;
    private final DragAndDrop.Payload payload;
    public float preDragXPosition;
    public float preDragYPosition;

    Skin skin = new Skin(Gdx.files.internal("skins/uiskin.json"));


    public GamePiece(Board board, IntPair coordinates, Team team, boolean isKing, int hitPoints, int attackPoints, GameManager gameManager){
        //metadata
        this.gameManager = gameManager;
        Texture gamePieceTexture = gameManager.GetAssetManager().get("black_player.png", Texture.class);
        this.textureRegion = new TextureRegion(gamePieceTexture, (int) Constants.TILE_SIZE, (int)Constants.TILE_SIZE);
        this.setBounds(textureRegion.getRegionX(), textureRegion.getRegionY(),
                textureRegion.getRegionWidth(), textureRegion.getRegionHeight());
        this.setPosition(board.GetBoardTilePosition(coordinates).x, board.GetBoardTilePosition(coordinates).y);
        this.board = board;
        this.team = team;
        this.isKing = isKing;
        this.crown = new TextureRegion(new Texture(Gdx.files.internal("king_crown.png")), 19, 32);
        this.isAlive = true;
        this.setName("GamePiece"+ coordinates.xVal + "," + coordinates.yVal);

        //stats
        this.SetAttackPoints(attackPoints);
        this.SetHitPoints(hitPoints);
        this.indexOnBoard = coordinates;
        this.statsLabels = new Group();
        addHPandAttackLabels();

        //drag and drop
        this.dragAndDrop = new DragAndDrop();
        this.payload = new DragAndDrop.Payload();
        this.addListener(gamePieceInputListener);
        dragAndDrop.addSource(new DragAndDrop.Source(this) {
            @Override
            public DragAndDrop.Payload dragStart(InputEvent event, float x, float y, int pointer) {
                GamePiece gamePiece = (GamePiece) event.getListenerActor();
                Gdx.app.log("GamePiece", "MovedThisTurn? :" + gamePiece.gameManager.movedThisTurn + ".");

                //store position to place back after null drop
                gamePiece.preDragXPosition = gamePiece.getX();
                gamePiece.preDragYPosition = gamePiece.getY();

                RemoveGamePieceInfo();

                if(gamePiece.gameManager.movedThisTurn){
                    return null;
                }

                //TODO: try show info panel on a timer, start drag will cancel timer?
                if (gamePiece.team == Team.FRIENDLY && gameManager.selectedMoveSet != null) {
                        Arrow arrow = new Arrow(new Vector2(gamePiece.getX() + gamePiece.getWidth()/2,gamePiece.getY()+gamePiece.getHeight()/2), gamePiece.getStage());
                        gamePiece.getStage().addActor(arrow);
                        payload.setDragActor(arrow);
                        arrow.toFront();
                        dragAndDrop.setDragActorPosition(arrow.getWidth() / 2, -arrow.getHeight() / 2);

                        Gdx.app.log("GamePiece", "Drag arrow created.");

                        //draw possible moves
                        //only show targets if player can move
                        if (gamePiece.team == Team.FRIENDLY && gamePiece.gameManager.selectedMoveSet != null) {
                            gamePiece.possibleMovesAndTargets = new Group();
                            gamePiece.possibleMovesAndTargets.setName("possibleMovesGroup" + gamePiece.getName());
                            gamePiece.drawPossibleMoves(gameManager.selectedMoveSet);
                            Gdx.app.log("GamePiece", "Possible moves group : " +  gamePiece.possibleMovesAndTargets.getChildren() + ".");
                            gamePiece.toFront();
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
                GamePiece gamePiece = (GamePiece) event.getListenerActor();
                if (payload.getDragActor() != null){
                    payload.getDragActor().remove();
                    ((Arrow) payload.getDragActor()).trailsGroup.remove();
                }
                if (target == null){
                    gamePiece.setPosition(gamePiece.preDragXPosition, gamePiece.preDragYPosition);
                    if (gamePiece.possibleMovesAndTargets != null){
                        gamePiece.possibleMovesAndTargets.remove();
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
            GamePiece gamePiece = (GamePiece) event.getListenerActor();
            if(gamePiece == gamePiece.hit(x,y,false)){
                gamePiece.DisplayGamePieceInfo();
            }
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

    public boolean HitGamePiece(GamePiece enemyGamePiece){
        Gdx.app.log("GamePiece", "GamePiece " + this.getName() + " is hitting " + enemyGamePiece.getName() + ".");
        return enemyGamePiece.GetHitAndIsFatal(this.attackPoints);
    }

    //TODO: I returned bool for something? to move game piece to enemy location?
    public boolean GetHitAndIsFatal(int AtkDmg){
        this.SetHitPoints(this.GetHitPoints() - AtkDmg);
        this.hitPointsLabel.remove();
        this.attackPointsLabel.remove();
        this.addHPandAttackLabels();
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

    public void JetpackJump(IntPair coordinates, float jumpDelay) {
        Gdx.app.log("GamePiece", "GamePiece " + this.getName() + " is moving to " + coordinates.xVal + "," + coordinates.yVal + ".");
        //squish GamePiece for cartoon-ish jump effect
        ScaleToAction squish = Actions.scaleTo(1f, 0.75f, 0.03f);

        //movement action (and undo squish)
        ArcToAction arcMove = new ArcToAction();
        arcMove.setPosition(board.GetBoardTilePosition(coordinates).x, board.GetBoardTilePosition(coordinates).y);
        arcMove.setDuration(0.6f);
        arcMove.setInterpolation(Interpolation.exp10);
        ScaleToAction unSquish = Actions.scaleTo(1f, 1f, 0.6f);
        ParallelAction jump = new ParallelAction(arcMove, unSquish);


        //adding landing cloud effect and tile bounce
        RunnableAction clouds = new RunnableAction();
        clouds.setRunnable(() -> {
            new LandingClouds(coordinates, GamePiece.this.gameManager);
        });
        RunnableAction tileBounce = new RunnableAction();
        tileBounce.setRunnable(() -> {
            if (GamePiece.this.board.GetBoardTileAtCoordinate(coordinates) != null) {
                BoardTile tile = GamePiece.this.board.GetBoardTileAtCoordinate(coordinates);
                tile.BounceWhenLandedOn();
            }
        });
        ParallelAction landing = new ParallelAction(clouds, tileBounce);

        //add clouds after movement
        SequenceAction jetpackJump = new SequenceAction(Actions.delay(jumpDelay), squish, jump, landing);
        this.addAction(jetpackJump);

        this.indexOnBoard = coordinates;
        this.setName("GamePiece"+coordinates.xVal+","+coordinates.yVal);
        this.SetLabelPositions();
    }

    //currently only used for undo to instantly reset board
    public void teleport(IntPair coordinateBoardPair) {
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
                    GamePiece gamePiece = (GamePiece) source.getActor();
                    gamePiece.gameManager.latestGamePieceCommand = new Command(gamePiece, possibleMoveTarget.indexOnBoard, possibleMoveTarget.type, gameManager.selectedMoveSet);
                    gamePiece.gameManager.latestGamePieceCommand.Execute();
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
                    GamePiece gamePiece = (GamePiece) source.getActor();
                    gamePiece.gameManager.latestGamePieceCommand = new Command(gamePiece, possibleMoveHit.indexOnBoard, possibleMoveHit.type, gameManager.selectedMoveSet);
                    gamePiece.gameManager.latestGamePieceCommand.Execute();
                    //if execute
                    possibleMoveHit.getParent().remove();
                }
            });
            Gdx.app.log("GamePiece", "Possible targets added to possibleMovesAndTargets group.");
        }
        this.getStage().addActor(this.possibleMovesAndTargets);
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
    }

    private void SetLabelPositions (){
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
