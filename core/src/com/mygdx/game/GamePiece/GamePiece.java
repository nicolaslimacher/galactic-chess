package com.mygdx.game.GamePiece;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.mygdx.game.Board.Board;
import com.mygdx.game.GameManager.GameManager;
import com.mygdx.game.Utils.IntPair;

public interface GamePiece {
    //get MetaData
    GameManager GetGameManager();
    Board GetBoard();
    IntPair GetIndexOnBoard();
    int GetGamePiecesID();
    boolean GetIsKing();
    boolean GetIsAlive();
    int GetHitPoints();
    void SetHitPoints(int hitPoints);
    int GetAttackPoints();
    void SetAttackPoints(int atkPoints);


    //housekeeping
    void addHpAndAttackLabels();
    void SetLabelPositions();
    String GetName();


    //interactions
    boolean HitGamePiece(GamePiece gamePiece);
    boolean GetHitAndIsFatal(int attackDamage);
    void MoveToTile(IntPair coordinates, float jumpDelay);
    void Teleport(IntPair coordinateBoardPair);
    boolean IsValidMove(IntPair intPair);
    boolean IsValidEnemyMove(IntPair intPair);
}
