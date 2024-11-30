package com.mygdx.game.Screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
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
import com.mygdx.game.HUD.HUD;
import com.mygdx.game.Manager.BattleManager;
import com.mygdx.game.Manager.RunManager;
import com.mygdx.game.Manager.Team;
import com.mygdx.game.GamePiece.GamePiece;
import com.mygdx.game.MoveSets.MoveSet;
import com.mygdx.game.Utils.Constants;
import com.mygdx.game.WranglerGiddyUp;
import com.mygdx.game.Utils.Helpers;
import com.mygdx.game.Utils.IntPair;

import java.util.ArrayList;
import java.util.List;

public class BattleScreen implements Screen {
	private static final String TAG = RunManager.class.getSimpleName();

	final WranglerGiddyUp game;
	final Stage stage;
	SpriteBatch batch;
	final BattleManager battleManager;
	HUD HUD;
	Board board;
	EnemyAI enemyAI;
	long startTime;
	Texture fightDialog;
	List<MoveSet> availableMoveSets;
	private long lastDropTimeSmall, timeToSmallCreation, lastDropTimeMedium, timeToMediumCreation;
	private final TextureRegion starryBackground;


	public BattleScreen(final WranglerGiddyUp game, final Stage stage, int playerKingID) {
		this.game = game;
		this.stage = stage;


		availableMoveSets = Helpers.GetRandomMoveSets(0,15);
		//this.availableMoveSets = availableMoveSets;
		for (MoveSet moveSet: availableMoveSets) {
			Gdx.app.log("BattleScreen", "Move Set Chosen: " + moveSet.getName() + ".");
		}

		//background stars
		//starryBackground = new TextureRegion(new Texture(Gdx.files.internal("starrybackground.png")));
		lastDropTimeSmall = TimeUtils.millis();
		timeToSmallCreation = 750L;
		lastDropTimeMedium = TimeUtils.millis();
		timeToMediumCreation = 2000L;

		//adding actors
		board = new Board(5, 5);
		stage.addActor(board);
		battleManager = new BattleManager(stage, board, availableMoveSets, this);
		stage.addActor(battleManager);
		HUD = new HUD(new SpriteBatch(), battleManager); //has own stage

		battleManager.placeStartingFriendlyGamePieces();
		battleManager.placeStartingEnemyGamePieces(game.getRunManager().getNextBattleJsonValue());


		enemyAI = new EnemyAI(battleManager);
		battleManager.enemyAI = enemyAI;

		batch = new SpriteBatch();

		//background stars
		startTime = TimeUtils.millis();

		starryBackground = battleManager.GetAssetManager().get("texturePacks/battleTextures.atlas", TextureAtlas.class).findRegion("starrybackground");
		lastDropTimeSmall = TimeUtils.millis();
		timeToSmallCreation = 750L;
		lastDropTimeMedium = TimeUtils.millis();
		timeToMediumCreation = 2000L;

		battleManager.DisplayPlayerMessage("FIGHT!", "now");

		Gdx.app.log("BattleScreen", "BattleScreen created.");


		Gdx.input.setInputProcessor(new InputMultiplexer(stage, HUD.customHUDStage));
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

		batch.setProjectionMatrix(HUD.customHUDStage.getCamera().combined);
		HUD.customHUDStage.draw();

		//check if enough time has elapsed to spawn new stars
		if (TimeUtils.timeSinceMillis(lastDropTimeSmall) > timeToSmallCreation){
			AddSmallStar(battleManager);
		}
		if (TimeUtils.timeSinceMillis(lastDropTimeMedium) > timeToMediumCreation){
			AddMediumStar(battleManager);
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

	public WranglerGiddyUp GetGame() {
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
		game.getRunManager().setCurrentBattleManager(null);
		Gdx.app.log("BattleScreen", "Dispose called.");
	}

	public void SwitchScreenEndGame(){
		//no need to dispose, called automatically in setScreen
		game.setScreen(new EndGameScreen(game));

	}

	private void AddSmallStar(BattleManager battleManager) {
		Gdx.app.log("BattleScreen", "Small star created in background.");
		Star star = new Star(battleManager, 3000000000L, 6000000000L, StarType.SMALL); //in nano 3000000000L = 3s
		star.setBounds(MathUtils.random(25, 769), MathUtils.random(100, 454), 6, 6);
		lastDropTimeSmall = TimeUtils.millis();
		timeToSmallCreation = MathUtils.random(3000, 7000);
	}

	private void AddMediumStar(BattleManager battleManager){
		Gdx.app.log("BattleScreen", "Medium star created in background.");
		Star star = new Star(battleManager, 12000000000L, 26000000000L, StarType.SMALL);
		star.setBounds(MathUtils.random(25, 769), MathUtils.random(100, 454), 6, 6);
		lastDropTimeMedium = TimeUtils.millis();
		timeToMediumCreation = MathUtils.random(10000, 15000);
	}

	public BattleManager getBattleManager(){
		return battleManager;
	}

	public HUD getHUD(){
		return HUD;
	}
}
