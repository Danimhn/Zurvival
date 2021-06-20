package com.mygdx.game.Screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.mygdx.game.Box2D_Bodies.Soldier;
import com.mygdx.game.DataHandler;
import com.mygdx.game.MyGdxGame;
import com.mygdx.game.TextButtonGenerator;
import com.mygdx.game.UI.MainMenu;

public class StartScreen implements Screen {
    public String currentScreen = "Menu";;
    public MyGdxGame myGame;
    private Sprite background;
    private MainMenu mainMenu;
    public CollectionScreen collectionScreen;
    private DataHandler dataHandler;

    public StartScreen(MyGdxGame game){
        dataHandler = new DataHandler();
        collectionScreen = new CollectionScreen(this, dataHandler, "Menu");
        myGame = game;
        Texture img = new Texture("Backgrounds/background2.png");
        dataHandler = new DataHandler();
        background = new Sprite(img);
        background.setBounds(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        background.setPosition(0,0);
        mainMenu = new MainMenu(this, dataHandler);
    }

    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {
        update(delta);

        Gdx.gl.glClearColor(1, 1, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        if(currentScreen == "Menu") {
            myGame.batch.begin();
            background.draw(myGame.batch);
            mainMenu.draw(myGame.batch);
            myGame.batch.end();
        }else if(currentScreen == "Collection"){
            collectionScreen.render(myGame.batch);
        }

    }

    private void update(float delta) {
        handleInput(delta);
    }

    private void handleInput(float delta) {
        if (Gdx.input.isTouched()) {
            mainMenu.checkButtons(new Vector2(Gdx.input.getX(), Gdx.graphics.getHeight() - Gdx.input.getY()),
                    myGame);
        }
    }

    @Override
    public void resize(int width, int height) {
//        stage.getViewport().update(width, height, false);

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

        myGame.batch.dispose();
        mainMenu.disposeTextures();
    }
}
