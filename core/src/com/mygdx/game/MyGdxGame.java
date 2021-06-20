package com.mygdx.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.mygdx.game.Screens.GameScreen;
import com.mygdx.game.Screens.StartScreen;

public class MyGdxGame extends Game implements InputProcessor {
	public final AdsController adsController;
	public SpriteBatch batch;
//	Texture img;
	private float aspectRatio;
	public static TextureAtlas uiAtlas;
	public boolean mute = false;
	
	public MyGdxGame(AdsController adsController){
		this.adsController = adsController;
	}

	public void showAd(){
		adsController.showRewardedAd();
	}
	public boolean adWatchedFully(){
		return adsController.adWatchedFully();
	}

	public void loadAd(){
		if(!isAdLoaded()) {
			adsController.loadRewardedAd();
		}
	}

	public boolean isAdLoaded(){
		return adsController.adLoaded();
	}


	
	@Override
	public void create () {
		aspectRatio = (float)Gdx.graphics.getWidth()/(float)Gdx.graphics.getHeight();
		uiAtlas = new TextureAtlas("UI/UI.txt");
		batch = new SpriteBatch();
		Gdx.input.setInputProcessor(this);
		Gdx.input.setCatchBackKey(true);
		setScreen(new StartScreen(this));
	}

	public float getAspectRatio() {
		return aspectRatio;
	}

	@Override
	public void render () {
		super.render();
	}

	@Override
	public void dispose () {
	    super.dispose();
	}

	@Override
    public void resize(int width, int height) {
	    super.resize(width, height);
    }

	public void resetBatch() {
		batch.dispose();
		batch = new SpriteBatch();
	}

	public boolean keyDown(int keycode) {
		if(keycode == Input.Keys.BACK){
			if(this.getScreen() instanceof GameScreen){
				if(((GameScreen) this.getScreen()).currentScreen.equals("Game")) {
					((GameScreen) this.getScreen()).setupOptions();
					((GameScreen) this.getScreen()).currentScreen = "Options";
				}else if(((GameScreen) this.getScreen()).currentScreen.equals("Shop")){
					((GameScreen) this.getScreen()).shopScreen.disposeTextures();
					if(GameScreen.player != null) {
						GameScreen.player.disposeTexture();
					}
					((GameScreen) this.getScreen()).zombieFactory.disposeTextures();
					((GameScreen) this.getScreen()).collectionScreen.disposeTextures();
					((GameScreen) this.getScreen()).disposeTextures();
					((GameScreen) this.getScreen()).myGame.setScreen((new StartScreen(this)));
				}else if(((GameScreen) this.getScreen()).currentScreen.equals("Collection")){
					((GameScreen) this.getScreen()).currentScreen = "Shop";
				}
			}else if(this.getScreen() instanceof StartScreen &&
					((StartScreen) this.getScreen()).currentScreen.equals("Collection")){
				((StartScreen) this.getScreen()).currentScreen = "Menu";
			}
		}
		return false;
	}

	@Override
	public boolean keyUp(int keycode) {
		return false;
	}

	@Override
	public boolean keyTyped(char character) {
		return false;
	}

	@Override
	public boolean touchDown(int screenX, int screenY, int pointer, int button) {
		return false;
	}

	@Override
	public boolean touchUp(int screenX, int screenY, int pointer, int button) {
		return false;
	}

	@Override
	public boolean touchDragged(int screenX, int screenY, int pointer) {
		return false;
	}

	@Override
	public boolean mouseMoved(int screenX, int screenY) {
		return false;
	}

	@Override
	public boolean scrolled(int amount) {
		return false;
	}
}
