package com.mygdx.game.Screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Json;
import com.mygdx.game.Board.Board;
import com.mygdx.game.BoardUI.MoveConfirmationButton;
import com.mygdx.game.BoardUI.MoveConfirmationMenu;
import com.mygdx.game.BoardUI.MoveSelectButton;
import com.mygdx.game.BoardUI.MoveSelectButtonMenu;
import com.mygdx.game.MoveSets.MoveSet;
import com.mygdx.game.Pawn.PossiblePawnMove;
import com.mygdx.game.Utils.CoordinateBoardPair;
import com.mygdx.game.MyChessGame;
import com.mygdx.game.Pawn.Pawn;
import com.badlogic.gdx.scenes.scene2d.ui.*;

public class GameScreen implements Screen {
	final MyChessGame game;
	final Stage stage;
	SpriteBatch batch;
	Board board;

	//Assets

	//stage level input listener
	private final InputListener stageInputListener = new InputListener(){
		public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
			Object selectedObject = event.getTarget();
				for(Actor actor:stage.getActors()){
					if(actor.getClass()== Pawn.class) {
						Pawn pawn = (Pawn) actor;
						if (!pawn.equals(selectedObject) && pawn.isSelected) {
							pawn.isSelected = Boolean.FALSE;
							Gdx.app.log("stageListener", pawn.getName());
							if (selectedObject.getClass() != PossiblePawnMove.class) {
								stage.getRoot().findActor("possibleMovesGroup" + pawn.getName()).remove();
							}
						}
					}
				}
			return false;
			}
		};

	public GameScreen(final MyChessGame game, final Stage stage) {
		this.game = game;
		this.stage = stage;
		Gdx.input.setInputProcessor(stage);

		//adding moveset options
		Json json = new Json();
		// De-serialize to an object
		MoveSet[] moveSets = json.fromJson(MoveSet[].class, Gdx.files.internal("MoveSet.json"));
		//System.out.println("moveSet: " + moveSets.get(15).name + " index: " + moveSets.get(15).index + " possibleMoves" + moveSets.get(15).possibleMoves);


		//adding actors
		board = new Board(5, 5);
		stage.addActor(board);
		if (board.boardColumns > 0) {
			for (int i = 0; i < board.boardColumns; i++) {
				Pawn pawn = new Pawn(board, new CoordinateBoardPair(i, 0));
				pawn.setName("Pawn"+String.valueOf(i)+",0");
				stage.addActor(pawn);
			}
		}
		board.selectedMoveSet = moveSets[8];
		stage.addListener(stageInputListener);

		MoveSet[] availableMoveSets = new MoveSet[]{moveSets[1], moveSets[2]};
		MoveSelectButtonMenu menuTable = new MoveSelectButtonMenu(board, availableMoveSets);
		board.menuTable = menuTable;
		MoveConfirmationMenu confirmationMenu = new MoveConfirmationMenu(board);
		board.confirmationMenu = confirmationMenu;

		stage.addActor(menuTable);
		stage.addActor(confirmationMenu);
		batch = new SpriteBatch();
	}
	@Override
	public void render(float deltaTime) {
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		stage.act(deltaTime);
		// begin a new batch and draw board
		batch.begin();
		stage.draw();
		batch.end();
	}


    @Override
	public void resize (int width, int height) {
		stage.getViewport().update(width, height, true);
		board.sizeAndAddBoardTiles();
		for(Actor actor:stage.getActors()){
			//move pawns to new board locations based on board coordinates
			if(actor.getClass()== Pawn.class) {
				((Pawn) actor).resetStageCoordinatesFromBoardLocation();
			}
			//move possible pawn moves if shown to new board locations
			if(actor.getClass()== Group.class && actor.getName().contains("possibleMovesGroup")) {
				for (Actor possiblePawnMoveActor : ((Group) actor).getChildren()) {
					PossiblePawnMove possiblePawnMove = (PossiblePawnMove) possiblePawnMoveActor;
					possiblePawnMove.resetStageCoordinatesFromBoardLocation();
				}
			}
		}
	}

	@Override
	public void show(){
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
		stage.dispose();
	}
}
