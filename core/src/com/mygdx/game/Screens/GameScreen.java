package com.mygdx.game.Screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.TimeUtils;
import com.mygdx.game.Background.Star;
import com.mygdx.game.Background.StarType;
import com.mygdx.game.Board.Board;
import com.mygdx.game.EnemyAI.EnemyAI;
import com.mygdx.game.GameManager.GameManager;
import com.mygdx.game.GameManager.Team;
import com.mygdx.game.GamePiece.GamePiece;
import com.mygdx.game.MoveSets.MoveSet;
import com.mygdx.game.GamePiece.DefaultPawn;
import com.mygdx.game.MyChessGame;
import com.mygdx.game.Utils.Constants;
import com.mygdx.game.Utils.Helpers;
import com.mygdx.game.Utils.IntPair;

import java.util.ArrayList;
import java.util.List;

public class GameScreen implements Screen {
	final MyChessGame game;
	final Stage stage;
	SpriteBatch batch;
	GameManager gameManager;
	Board board;
	EnemyAI enemyAI;
	long startTime;
	Texture fightDialog;
	List<MoveSet> availableMoveSets;
	private long lastDropTimeSmall, timeToSmallCreation, lastDropTimeMedium, timeToMediumCreation;
	private final TextureRegion starryBackground;


	public GameScreen(final MyChessGame game, final Stage stage, List<MoveSet> availableMoveSets) {
		this.game = game;
		this.stage = stage;
		Gdx.input.setInputProcessor(stage);

		availableMoveSets = Helpers.GetRandomMoveSets(0,15);
		this.availableMoveSets = availableMoveSets;
		for (MoveSet moveSet: availableMoveSets) {
			Gdx.app.log("GameScreen", "Move Set Chosen: " + moveSet.getName() + ".");
		}

		//background stars
		starryBackground = new Texture(Gdx.files.internal("starrybackground.png"));
		lastDropTimeSmall = TimeUtils.millis();
		timeToSmallCreation = 750L;
		lastDropTimeMedium = TimeUtils.millis();
		timeToMediumCreation = 2000L;

		//adding actors
		board = new Board(5, 5);
		stage.addActor(board);
		gameManager = new GameManager(stage, board, availableMoveSets, this);
		stage.addActor(gameManager);
		ArrayList<GamePiece> friendlyPieces = new ArrayList<>();

		if (board.boardColumns > 0) {
			for (int i = 0; i < board.boardColumns; i++) {
				DefaultPawn defaultPawn;
				if (i == 2){
					//add king
					defaultPawn = new DefaultPawn(board, gameManager, new IntPair(i, 0), Team.FRIENDLY, true,  1);
				}else {
					//add pawns
					defaultPawn = new DefaultPawn(board, gameManager, new IntPair(i, 0), Team.FRIENDLY, false,  1);
				}
				friendlyPieces.add(defaultPawn);
				stage.addActor(defaultPawn);
                }


            }

		//add two kings for enemy team
		ArrayList<GamePiece> enemyPieces = new ArrayList<>();

//		GamePiece enemyKing1 = new GamePiece(board, new CoordinateBoardPair(1, 4), Team.ENEMY, true,1, 1, gameManager);
//		enemyPieces.add(enemyKing1);
//		stage.addActor(enemyKing1);

		DefaultPawn enemyKing1 = new DefaultPawn(board, gameManager, new IntPair(2, 4), Team.ENEMY, true,1);
		enemyPieces.add(enemyKing1);
		stage.addActor(enemyKing1);

//		GamePiece enemyKing2 = new GamePiece(board, new CoordinateBoardPair(3, 4), Team.ENEMY, true,1, 1, gameManager);
//		enemyPieces.add(enemyKing2);
//		stage.addActor(enemyKing2);

		gameManager.friendlyGamePieces = friendlyPieces;
		gameManager.enemyGamePieces = enemyPieces;

		enemyAI = new EnemyAI(gameManager);
		gameManager.enemyAI = enemyAI;

		batch = new SpriteBatch();

		//background stars
		startTime = TimeUtils.millis();

		starryBackground = gameManager.GetAssetManager().get("texturePacks/battleTextures.atlas", TextureAtlas.class).findRegion("starrybackground");
		lastDropTimeSmall = TimeUtils.millis();
		timeToSmallCreation = 750L;
		lastDropTimeMedium = TimeUtils.millis();
		timeToMediumCreation = 2000L;

		gameManager.DisplayPlayerMessage("FIGHT!", "now");

		Gdx.app.log("GameScreen", "GameScreen created.");
	}
	@Override
	public void render(float deltaTime) {
		ScreenUtils.clear(0.10f, 0.10f, 0.15f, 1f);
		stage.act(deltaTime);
		// begin a new batch and draw board
		batch.begin();
		//for performance reasons disable blend before drawing background
		batch.disableBlending();
		batch.draw(starryBackground,0,0, Constants.SCREEN_WIDTH, Constants.SCREEN_HEIGHT);
		batch.enableBlending();

		stage.draw();
		batch.end();

		//check if enough time has elapsed to spawn new stars
		if (TimeUtils.timeSinceMillis(lastDropTimeSmall) > timeToSmallCreation){
			AddSmallStar(gameManager);
		}
		if (TimeUtils.timeSinceMillis(lastDropTimeMedium) > timeToMediumCreation){
			AddMediumStar(gameManager);
		}
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

	public MyChessGame GetGame() {
		return game;
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
		fightDialog.dispose();
		batch.dispose();
		Gdx.app.log("GameScreen", "Dispose called.");
	}

	public void SwitchScreenEndGame(){
		//no need to dispose, called automatically in setScreen
		game.setScreen(new EndGameScreen(game));

	}

	private void AddSmallStar(GameManager gameManager) {
		Gdx.app.log("GameScreen", "Small star created in background.");
		Star star = new Star(gameManager, 3000000000L, 6000000000L, StarType.SMALL); //in nano 3000000000L = 3s
		star.setBounds(MathUtils.random(25, 769), MathUtils.random(100, 454), 6, 6);
		lastDropTimeSmall = TimeUtils.millis();
		timeToSmallCreation = MathUtils.random(3000, 7000);
	}

	private void AddMediumStar(GameManager gameManager){
		Gdx.app.log("GameScreen", "Medium star created in background.");
		Star star = new Star(gameManager, 12000000000L, 26000000000L, StarType.SMALL);
		star.setBounds(MathUtils.random(25, 769), MathUtils.random(100, 454), 6, 6);
		lastDropTimeMedium = TimeUtils.millis();
		timeToMediumCreation = MathUtils.random(10000, 15000);
	}
}
