package com.mygdx.game.Actions;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.actions.ParallelAction;
import com.badlogic.gdx.scenes.scene2d.actions.RunnableAction;
import com.badlogic.gdx.scenes.scene2d.actions.ScaleToAction;
import com.badlogic.gdx.scenes.scene2d.actions.SequenceAction;
import com.mygdx.game.Board.BoardTile;
import com.mygdx.game.GamePiece.DefaultPawn;
import com.mygdx.game.GamePiece.LandingClouds;
import com.mygdx.game.Utils.IntPair;

public class GamePieceJetpackJump extends SequenceAction {

    DefaultPawn defaultPawnToJump;

    public GamePieceJetpackJump(DefaultPawn defaultPawnToJump, IntPair coordinates, float jumpDelay) {
        this.defaultPawnToJump = defaultPawnToJump;
        Gdx.app.log("JetpackJump", "GamePiece " + defaultPawnToJump.getName() + " is moving to " + coordinates.xVal + "," + coordinates.yVal + ".");

        //squish GamePiece for cartoon-ish jump effect
        ScaleToAction squish = Actions.scaleTo(1f, 0.75f, 0.03f);

        //movement action (and undo squish)
        ArcToAction arcMove = new ArcToAction();
        arcMove.setPosition(defaultPawnToJump.board.GetBoardTilePosition(coordinates).x, defaultPawnToJump.board.GetBoardTilePosition(coordinates).y);
        arcMove.setDuration(0.6f);
        arcMove.setInterpolation(Interpolation.exp10);
        ScaleToAction unSquish = Actions.scaleTo(1f, 1f, 0.6f);
        ParallelAction jump = new ParallelAction(arcMove, unSquish);

        //adding landing cloud effect and tile bounce
        RunnableAction clouds = new RunnableAction();
        clouds.setRunnable(() -> {
            new LandingClouds(coordinates, defaultPawnToJump.gameManager);
        });
        RunnableAction tileBounce = new RunnableAction();
        tileBounce.setRunnable(() -> {
            if (defaultPawnToJump.board.GetBoardTileAtCoordinate(coordinates) != null) {
                BoardTile tile = defaultPawnToJump.board.GetBoardTileAtCoordinate(coordinates);
                tile.BounceWhenLandedOn();
            }
        });
        ParallelAction landing = new ParallelAction(clouds, tileBounce);

        //add clouds after movement
        SequenceAction jetpackJump = new SequenceAction(Actions.delay(jumpDelay), squish, jump, landing);
        defaultPawnToJump.addAction(jetpackJump);

        defaultPawnToJump.indexOnBoard = coordinates;
        defaultPawnToJump.setName("GamePiece"+coordinates.xVal+","+coordinates.yVal);
        defaultPawnToJump.SetLabelPositions();
    }










}
