package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.ScreenViewport;

public class GameScreen implements Screen {
	final MyChessGame game;
	final Stage stage;
	OrthographicCamera camera;
	CustomSpriteBatch customBatch;
	Vector3 touchPos;
	Board board;

	//Assets
	Pawn pawn;
	Music rainMusic;

	public GameScreen(final MyChessGame game, final Stage stage) {
		this.game = game;
		this.stage = stage;
		Gdx.input.setInputProcessor(stage);

		// load the drop sound effect and the rain background "music"
		// dropSound = Gdx.audio.newSound(Gdx.files.internal("drop.wav"));
		rainMusic = Gdx.audio.newMusic(Gdx.files.internal("rain.mp3"));
		rainMusic.setLooping(true);


		board = new Board(5, 5);
		pawn = new Pawn(2, 0);
		Gdx.app.log("MyTag", pawn.position.toString());
		System.out.println(pawn.position.toString());
		customBatch = new CustomSpriteBatch();

		stage.addActor(board);
		stage.addActor(pawn);
	}
	@Override
	public void render(float deltaTime) {
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		stage.act(deltaTime);
		// begin a new batch and draw board
		customBatch.begin();
		//pawn.render(customBatch, board);
		stage.draw();
		customBatch.end();
	}


    @Override
	public void resize (int width, int height) {
		// See https://libgdx.com/wiki/graphics/2d/scene2d/scene2d for what true means.
		this.stage.getViewport().update(width, height, true);
	}

	@Override
	public void show(){
		//start the playback of background music when the screen is shown
		rainMusic.play();
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
		rainMusic.dispose();
		stage.dispose();
	}
}
