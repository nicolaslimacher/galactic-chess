package com.mygdx.game.Screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.TimeUtils;
import com.mygdx.game.Board.Board;
import com.mygdx.game.EnemyAI.EnemyAI;
import com.mygdx.game.GameManager.GameManager;
import com.mygdx.game.GameManager.Team;
import com.mygdx.game.MoveSets.MoveSet;
import com.mygdx.game.GamePiece.GamePiece;
import com.mygdx.game.Utils.Constants;
import com.mygdx.game.Utils.CoordinateBoardPair;
import com.mygdx.game.MyChessGame;
import com.mygdx.game.Utils.Helpers;

import java.util.ArrayList;

public class GameScreen implements Screen {
	final MyChessGame game;
	final Stage stage;
	SpriteBatch batch;
	GameManager gameManager;
	Board board;
	EnemyAI enemyAI;
	long startTime;
	Texture fightDialog;
	MoveSet[] availableMoveSets;


	public GameScreen(final MyChessGame game, final Stage stage) {
		this.game = game;
		this.stage = stage;
		Gdx.input.setInputProcessor(stage);

		//adding moveset options
		Json json = new Json();
		// De-serialize to an object
		MoveSet[] moveSets = json.fromJson(MoveSet[].class, Gdx.files.internal("MoveSet.json"));


		//adding actors
		board = new Board(5, 5);
		ArrayList<GamePiece> friendlyPieces = new ArrayList<GamePiece>();
		if (board.boardColumns > 0) {
			for (int i = 0; i < board.boardColumns; i++) {
				GamePiece gamePiece = new GamePiece(board, new CoordinateBoardPair(i, 0), Team.FRIENDLY, 10, 1, gameManager);
				gamePiece.setName("GamePiece"+ i + ",0");
				friendlyPieces.add(gamePiece);
			}
		}
		ArrayList<GamePiece> enemyPieces = new ArrayList<GamePiece>();
		if (board.boardColumns > 0) {
			for (int i = 0; i < board.boardColumns; i++) {
				GamePiece gamePiece = new GamePiece(board, new CoordinateBoardPair(i, board.boardRows-1), Team.ENEMY, 2, 1, gameManager);
				gamePiece.setName("GamePiece"+ i + "," + (board.boardRows - 1));
				enemyPieces.add(gamePiece);
			}
		}

		availableMoveSets = Helpers.GetRandomMoveSets(0,15);
		System.out.println(availableMoveSets.length);
		for (MoveSet moveSet: availableMoveSets) {
			System.out.println(moveSet.getName());
		}
		gameManager = new GameManager(stage, board, friendlyPieces, enemyPieces, availableMoveSets);
		stage.addActor(gameManager);

		enemyAI = new EnemyAI(gameManager);
		gameManager.enemyAI = enemyAI;

		batch = new SpriteBatch();

		startTime = TimeUtils.millis();
		fightDialog = new Texture(Gdx.files.internal("fight_dialog.png"));
	}
	@Override
	public void render(float deltaTime) {
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		stage.act(deltaTime);
		// begin a new batch and draw board
		batch.begin();
		stage.draw();
		if (TimeUtils.millis() - startTime < 1000){
			batch.draw(fightDialog, Constants.SCREEN_WIDTH / 2 - 200, Constants.SCREEN_HEIGHT / 2 - 50);
		}
		batch.end();
	}


    @Override
	public void resize (int width, int height) {
		stage.getViewport().update(width, height, true);
	}

	@Override
	public void show(){
		//TODO: add music
		//start the playback of background music when the screen is shown
		//rainMusic.play();
	}

	@Override
	public void hide(){

	}
	@Override
	public void pause() {
	}

	@Override
	public void resume() {
	}
	@Override
	public void dispose() {
		//TODO: make sure im disposing relevant assets
		stage.dispose();
	}
}
