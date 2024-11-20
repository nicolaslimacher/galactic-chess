package com.mygdx.game.GamePiece;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Action;
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
import com.mygdx.game.Actions.HoverInPlaceAction;
import com.mygdx.game.Actions.MoveActionFactory;
import com.mygdx.game.Board.Board;
import com.mygdx.game.Command.Command;
import com.mygdx.game.Command.CommandType;
import com.mygdx.game.Components.AbilityComponent;
import com.mygdx.game.Manager.GameManager;
import com.mygdx.game.Manager.MoveManager;
import com.mygdx.game.Manager.Team;
import com.mygdx.game.MoveSets.MoveSet;
import com.mygdx.game.Utils.Helpers;
import com.mygdx.game.Utils.IntPair;

public class GamePiece extends Actor {
    //metadata
    public final GameManager gameManager;
    public final Board board;
    public final int gamePieceID;
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
    private boolean displayInfoShowing = false;
    private AbilityComponent.AbilityType abilityType;


    //drag and drop
    private final DragAndDrop dragAndDrop;
    private final DragAndDrop.Payload payload;
    public float preDragXPosition;
    public float preDragYPosition;

    Skin skin = new Skin(Gdx.files.internal("skins/uiskin.json"));


    public GamePiece(Board board, GameManager gameManager, int gamePieceID, IntPair coordinates, Team team, boolean isKing ) {
        //metadata
        this.gameManager = gameManager;
        this.gamePieceID = gamePieceID;
        GamePieceData gamePieceData = new GamePieceData(gamePieceID);
        this.textureRegion = gameManager.GetAssetManager().get("texturePacks/battleTextures.atlas", TextureAtlas.class).findRegion(gamePieceData.getTextureName());
        this.setBounds(textureRegion.getRegionX(), textureRegion.getRegionY(),
                textureRegion.getRegionWidth(), textureRegion.getRegionHeight());
        this.setPosition(board.GetBoardTilePosition(coordinates).x, board.GetBoardTilePosition(coordinates).y);
        this.board = board;
        this.team = team;
        this.isKing = isKing;
        this.crown = gameManager.GetAssetManager().get("texturePacks/battleTextures.atlas", TextureAtlas.class).findRegion("king_crown");
        this.isAlive = true;
        this.setName("GamePiece" + coordinates.xVal + "," + coordinates.yVal);

        //stats
        this.SetAttackPoints(gamePieceData.getAttackPoints());
        this.SetHitPoints(gamePieceData.getHitPoints());
        this.indexOnBoard = coordinates;
        this.statsLabels = new Group();
        addHPandAttackLabels();

        //components
        this.abilityType = gamePieceData.getAbility();

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

                if (gamePiece.gameManager.movedThisTurn) {
                    return null;
                }

                if (gamePiece.team == Team.FRIENDLY && gameManager.selectedMoveSet != null) {
                    Arrow arrow = new Arrow(new Vector2(gamePiece.getX() + gamePiece.getWidth() / 2, gamePiece.getY() + gamePiece.getHeight() / 2), gamePiece.getStage(), gamePiece.gameManager);
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
                        Gdx.app.log("GamePiece", "Possible moves group : " + gamePiece.possibleMovesAndTargets.getChildren() + ".");
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
                if (payload.getDragActor() != null) {
                    payload.getDragActor().remove();
                    ((Arrow) payload.getDragActor()).trailsGroup.remove();
                }
                if (target == null) {
                    gamePiece.setPosition(gamePiece.preDragXPosition, gamePiece.preDragYPosition);
                    if (gamePiece.possibleMovesAndTargets != null) {
                        gamePiece.possibleMovesAndTargets.remove();
                    }
                }
            }


        });

        //end drag and drop, adding floating hover animations
        this.addAction(new HoverInPlaceAction().Hover);

        Gdx.app.log("GamePiece", "GamePiece " + this.getName() + "created.");
    }

    private final InputListener gamePieceInputListener = new InputListener() {
        @Override
        public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
            Gdx.app.log("GamePiece", "TouchDown fired.");
            GamePiece gamePiece = (GamePiece) event.getListenerActor();
            if (!displayInfoShowing){
                gamePiece.DisplayGamePieceInfo();
                displayInfoShowing = true;
            }
            else{
                gamePiece.RemoveGamePieceInfo();
                displayInfoShowing = false;
            }
            return false;
        }

        public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
            Gdx.app.log("GamePiece", "TouchUp fired.");
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
        if (this.hitPointsLabel != null) {
            this.hitPointsLabel.remove();
        }
        this.hitPointsLabel = new Label(String.valueOf(hitPoints), skin);
    }

    public int GetAttackPoints() {
        return attackPoints;
    }

    public void SetAttackPoints(int attackPoints) {
        this.attackPoints = attackPoints;
        if (this.attackPointsLabel != null) {
            this.attackPointsLabel.remove();
        }
        this.hitPointsLabel = new Label(String.valueOf(attackPoints), skin);
    }

    public boolean HitGamePiece(GamePiece enemyGamePiece) {
        Gdx.app.log("GamePiece", "GamePiece " + this.getName() + " is hitting " + enemyGamePiece.getName() + ".");
        return enemyGamePiece.GetHitAndIsFatal(this.attackPoints);
    }

    //TODO: I returned bool for something? to move game piece to enemy location?
    public boolean GetHitAndIsFatal(int AtkDmg) {
        this.SetHitPoints(this.GetHitPoints() - AtkDmg);
        this.hitPointsLabel.remove();
        this.attackPointsLabel.remove();
        this.addHPandAttackLabels();
        if (this.hitPoints == 0) {
            this.isAlive = false;
            this.hitPointsLabel.remove();
            this.attackPointsLabel.remove();
            this.statsLabels.remove();
            this.remove();
            return true;
        }
        return false;
    }

    public void MoveToWithAction(MoveActionFactory.MoveActionType type, IntPair coordinates, float jumpDelay){
        Action moveToAction = MoveActionFactory.CreateMoveAction(type, this, coordinates, jumpDelay);
        this.addAction(moveToAction);
        this.indexOnBoard = coordinates;
        this.setName("GamePiece"+coordinates.xVal+","+coordinates.yVal);
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////
    //                                       DRAWING GAME PIECES                                  //
    ////////////////////////////////////////////////////////////////////////////////////////////////

    @Override
    public void draw(Batch batch, float parentAlpha) {
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
            batch.draw(crown, this.getX() + this.getWidth() / 2 - 16, this.getY() + this.getHeight() - 10, 32, 19);
        }
    }

    public void drawPossibleMoves(MoveSet moveSet) {
        Gdx.app.log("GamePiece", "Drawing possible moves and targets");
        for (IntPair move : MoveManager.GetPossibleMoves(this, moveSet)) {
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
        for (IntPair target : MoveManager.GetPossibleTargets(this, moveSet)) {
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

    private void addHPandAttackLabels() {
        //TODO: try using TextButton and setDisabled
        this.hitPointsLabel = new Label(String.valueOf(this.hitPoints), skin, "hpStatsLabel");
        this.attackPointsLabel = new Label(String.valueOf(this.attackPoints), skin, "atkStatsLabel");

        Image hpBackground;
        if (this.team == Team.FRIENDLY) {
            hpBackground = new Image(new Texture(Gdx.files.internal("green_hp_background.png")));
            hitPointsLabel.getStyle().fontColor = Color.BLACK;
        } else {
            hpBackground = new Image(new Texture(Gdx.files.internal("red_hp_background.png")));
        }
        hitPointsLabel.getStyle().background = hpBackground.getDrawable();

        Image atkBackground = new Image(new Texture(Gdx.files.internal("atk_background.png")));
        attackPointsLabel.getStyle().background = atkBackground.getDrawable();
        attackPointsLabel.getStyle().fontColor = Color.BLACK;

        this.SetLabelPositions();
    }

    private void SetLabelPositions() {
        attackPointsLabel.setBounds(this.getX() + 2, this.getY() + 2, 20, 20);
        attackPointsLabel.setAlignment(Align.center);
        hitPointsLabel.setBounds(this.getX() + this.getWidth() - 22, this.getY() + 2, 20, 20);
        hitPointsLabel.setAlignment(Align.center);
    }

    public void DisplayGamePieceInfo() {
        TextButton gamePieceInfo = new TextButton("", skin);

        RemoveGamePieceInfo();

        gamePieceInfo.setName("gamePieceInfo");
        gamePieceInfo.setText("NAME: " + this.getName());
        Helpers.KeepPopUpOverBoard(gamePieceInfo, this.getX() + this.getWidth() / 2 - 125, this.getY() + this.getWidth() + 10, 250, 250);
        gamePieceInfo.addListener(new InputListener() {
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

    public void RemoveGamePieceInfo() {
        //removes any gamePieceInfo buttons on screen
        if (this.getStage().getRoot().findActor("gamePieceInfo") != null) {
            this.getStage().getRoot().findActor("gamePieceInfo").remove();
        }
        Gdx.app.log("GamePiece", "GamePiece removed.");
    }

    public void setAbilityType(AbilityComponent.AbilityType abilityType){
        this.abilityType = abilityType;
    }
    public AbilityComponent.AbilityType getAbilityType(){
        return abilityType;
    }

}