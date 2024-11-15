package com.mygdx.game.Screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.ScreenUtils;
import com.mygdx.game.MoveSets.MoveSet;
import com.mygdx.game.MyChessGame;
import com.mygdx.game.Utils.Helpers;

import java.util.List;

public class CharacterSelectScreen implements Screen{
    final MyChessGame game;
    BitmapFont font;
    SpriteBatch batch;
    Stage stage;
    Skin skin = new Skin(Gdx.files.internal("skins/uiskin.json"));
    TextButton button1, button2, button3;
    List<MoveSet> availableMoveSets;

    public CharacterSelectScreen(final MyChessGame game, final Stage stage) {
        this.game = game;
        this.stage = stage;
        Gdx.input.setInputProcessor(stage);

        batch = new SpriteBatch();
        Table root = new Table();
        root.defaults().space(10f);
        root.setFillParent(true);
        stage.addActor(root);

        availableMoveSets = Helpers.GetRandomMoveSets(0,15);

        button1 = new TextButton("Click for\nGreen Character",skin);
        button1.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent changeEvent, Actor actor) {
                Gdx.app.log("CharacterSelect","button1 Stop poking me! How rude!!!");
                game.setScreen(new GameScreen(game, game.stage));
                Gdx.app.log("MainMenu", "Input received, creating GameScreen instance.");
                dispose();
            }
        });
        root.add(button1);

        button2 = new TextButton("Click for\nRed Character",skin);
        button2.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent changeEvent, Actor actor) {
                Gdx.app.log("CharacterSelect","button2 Stop poking me! How rude!!!");
                game.setScreen(new GameScreen(game, game.stage));
                Gdx.app.log("MainMenu", "Input received, creating GameScreen instance.");
                dispose();
            }
        });
        root.add(button2);

        button3 = new TextButton("Click for\nYellow Character",skin);
        button3.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent changeEvent, Actor actor) {
                Gdx.app.log("CharacterSelect","button3 Stop poking me! How rude!!!");
                game.setScreen(new GameScreen(game, game.stage));
                Gdx.app.log("MainMenu", "Input received, creating GameScreen instance.");
                dispose();
            }
        });
        root.add(button3);

        font = new BitmapFont(); // use libGDX's default Arial font

    }

    @Override
    public void show() {

    }

    private void DeleteButtons(){
        button1.remove();
        button2.remove();
        button3.remove();
    }

    @Override
    public void render(float deltaTime) {
        ScreenUtils.clear(0.10f, 0.10f, 0.15f, 1f);
        stage.act(deltaTime);
        // begin a new batch and draw board
        batch.begin();
        //for performance reasons disable blend before drawing background
        batch.disableBlending();
        batch.enableBlending();

        stage.draw();
        batch.end();
    }


    @Override
    public void resize(int width, int height) {

    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {
        batch.dispose();
        font.dispose();
        DeleteButtons();
    }
}
