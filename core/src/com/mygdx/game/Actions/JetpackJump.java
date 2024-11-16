package com.mygdx.game.Actions;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.actions.ParallelAction;
import com.badlogic.gdx.scenes.scene2d.actions.RunnableAction;
import com.badlogic.gdx.scenes.scene2d.actions.ScaleToAction;
import com.badlogic.gdx.scenes.scene2d.actions.SequenceAction;
import com.mygdx.game.Board.BoardTile;
import com.mygdx.game.GamePiece.GamePiece;
import com.mygdx.game.GamePiece.LandingClouds;
import com.mygdx.game.Utils.IntPair;

public class JetpackJump extends SequenceAction {

    GamePiece gamePieceToJump;

    public JetpackJump(GamePiece gamePieceToJump, IntPair coordinates, float jumpDelay) {
        this.gamePieceToJump = gamePieceToJump;
        Gdx.app.log("JetpackJump", "GamePiece " + gamePieceToJump.getName() + " is moving to " + coordinates.xVal + "," + coordinates.yVal + ".");

        //squish GamePiece for cartoon-ish jump effect
        ScaleToAction squish = Actions.scaleTo(1f, 0.75f, 0.03f);

        //movement action (and undo squish)
        ArcToAction arcMove = new ArcToAction();
        arcMove.setPosition(gamePieceToJump.board.GetBoardTilePosition(coordinates).x, gamePieceToJump.board.GetBoardTilePosition(coordinates).y);
        arcMove.setDuration(0.6f);
        arcMove.setInterpolation(Interpolation.exp10);
        ScaleToAction unSquish = Actions.scaleTo(1f, 1f, 0.6f);
        ParallelAction jump = new ParallelAction(arcMove, unSquish);

        //adding landing cloud effect and tile bounce
        RunnableAction clouds = new RunnableAction();
        clouds.setRunnable(() -> {
            new LandingClouds(coordinates, gamePieceToJump.gameManager);
        });
        RunnableAction tileBounce = new RunnableAction();
        tileBounce.setRunnable(() -> {
            if (gamePieceToJump.board.GetBoardTileAtCoordinate(coordinates) != null) {
                BoardTile tile = gamePieceToJump.board.GetBoardTileAtCoordinate(coordinates);
                tile.BounceWhenLandedOn();
            }
        });
        ParallelAction landing = new ParallelAction(clouds, tileBounce);

        //add clouds after movement
        SequenceAction jetpackJump = new SequenceAction(Actions.delay(jumpDelay), squish, jump, landing);
        gamePieceToJump.addAction(jetpackJump);

        gamePieceToJump.indexOnBoard = coordinates;
        gamePieceToJump.setName("GamePiece"+coordinates.xVal+","+coordinates.yVal);
    }










}
