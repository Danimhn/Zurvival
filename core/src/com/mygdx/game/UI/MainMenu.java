package com.mygdx.game.UI;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.mygdx.game.Box2D_Bodies.B2dStaticRectangleObjectGenerator;
import com.mygdx.game.Box2D_Bodies.Soldier;
import com.mygdx.game.DataHandler;
import com.mygdx.game.MathTools;
import com.mygdx.game.MyGdxGame;
import com.mygdx.game.Screens.GameScreen;
import com.mygdx.game.Screens.ShopScreen;
import com.mygdx.game.Screens.StartScreen;
import com.mygdx.game.Weapons.HandGun;
import com.mygdx.game.Weapons.Knife;

import javax.xml.crypto.Data;

public class MainMenu {
    public Sprite[] sprites;
    private DataHandler dataHandler;
    private TextureAtlas atlas;
    private boolean characterScreen = false;
    private Sprite man;
    private Sprite woman;
    private Sprite mute;
    private Sprite unMute;
    private Sprite choose;
    private Sprite back;
    private StartScreen screen;
    private boolean run = false;


    public MainMenu(StartScreen startScreen, DataHandler dataHandler)  {
        this.dataHandler = dataHandler;
        screen = startScreen;
        atlas = new TextureAtlas("UI/MainMenu.txt");
        man = new Sprite(atlas.findRegion("man icon"));
        man.setBounds(0,0,Gdx.graphics.getHeight()/5f, Gdx.graphics.getHeight()/5f);
        man.setCenter(2*Gdx.graphics.getHeight()/15f, Gdx.graphics.getHeight()/2f);
        woman = new Sprite(atlas.findRegion("girl icon"));
        woman.setBounds(0,0,Gdx.graphics.getHeight()/5f,Gdx.graphics.getHeight()/5f);
        woman.setCenter(11*Gdx.graphics.getHeight()/30f, Gdx.graphics.getHeight()/2f);
        choose = new Sprite(atlas.findRegion("Choose"));
        choose.setBounds(0,0,2*Gdx.graphics.getWidth()/3f, Gdx.graphics.getWidth()/10f);
        choose.setCenter(Gdx.graphics.getWidth()/3f,
                Gdx.graphics.getHeight()/2f + man.getHeight()/2 + choose.getHeight());
        back = new Sprite(atlas.findRegion("Back"));
        back.setBounds(0,0,Gdx.graphics.getWidth()/4f, Gdx.graphics.getWidth()/12f);
        back.setCenter(Gdx.graphics.getWidth()/7f,
                Gdx.graphics.getHeight()-back.getHeight());
        Sprite topMenu = new Sprite(atlas.findRegion("MainMenuT"));
        topMenu.setBounds(0, 0, Gdx.graphics.getWidth()/1.3f, 3.26f*Gdx.graphics.getHeight()/10f);
        Sprite bottomMenu = new Sprite(atlas.findRegion("MainMenuB"));
        bottomMenu.setBounds(0, 0, Gdx.graphics.getWidth()/1.3f, 1.5f*Gdx.graphics.getHeight()/10f);
        Sprite boss = new Sprite(atlas.findRegion("bossIcon"));
        boss.setBounds(0, 0, Gdx.graphics.getHeight()/4f, Gdx.graphics.getHeight()/4f);
        boss.setCenter(Gdx.graphics.getWidth()/2f, boss.getHeight()/2);
        Sprite lZombie = new Sprite(atlas.findRegion("LeftZombie"));
        lZombie.setBounds(0, 0, Gdx.graphics.getHeight()/7f, Gdx.graphics.getHeight()/7f);
        lZombie.setCenter(Gdx.graphics.getWidth()/2f - boss.getWidth()/2, lZombie.getHeight()/2);
        Sprite rZombie = new Sprite(atlas.findRegion("RightZombie"));
        rZombie.setBounds(0, 0, Gdx.graphics.getHeight()/7f, Gdx.graphics.getHeight()/7f);
        rZombie.setCenter(Gdx.graphics.getWidth()/2f + boss.getWidth()/2, rZombie.getHeight()/2);
        Sprite newGame = new Sprite(atlas.findRegion("New_Game"));
        newGame.setBounds(0, 0, Gdx.graphics.getWidth()/3f, 0.6f*Gdx.graphics.getHeight()/10f);
        Sprite resume = new Sprite(atlas.findRegion("Resume"));
        if(!dataHandler.isSaved()){
            resume.setAlpha(.5f);
        }
        resume.setBounds(0, 0, Gdx.graphics.getWidth()/3f, 0.6f*Gdx.graphics.getHeight()/10f);
        Sprite run = new Sprite(atlas.findRegion("Run"));
        run.setBounds(0, 0, Gdx.graphics.getWidth()/6f, 0.6f*Gdx.graphics.getHeight()/10f);
        Sprite collection = new Sprite(atlas.findRegion("Collection"));
        collection.setBounds(0, 0, Gdx.graphics.getWidth()/3f, 0.6f*Gdx.graphics.getHeight()/10f);
        topMenu.setCenter(Gdx.graphics.getWidth()/2f, Gdx.graphics.getHeight()/2f + bottomMenu.getHeight()/2);
        bottomMenu.setCenter(Gdx.graphics.getWidth()/2f, Gdx.graphics.getHeight()/2f - topMenu.getHeight()/2);
        float menuTopY = topMenu.getY() + topMenu.getHeight();
        newGame.setCenter(Gdx.graphics.getWidth()/2f, menuTopY - 0.905f*Gdx.graphics.getHeight()/10f);
        resume.setCenter(Gdx.graphics.getWidth()/2f, menuTopY - 1.875f*Gdx.graphics.getHeight()/10f);
        run.setCenter(Gdx.graphics.getWidth()/2f, menuTopY - 2.835f*Gdx.graphics.getHeight()/10f);
        collection.setCenter(Gdx.graphics.getWidth()/2f, menuTopY - 3.78f*Gdx.graphics.getHeight()/10f);
        Sprite whiteBase = new Sprite(atlas.findRegion("WhiteBase"));
        whiteBase.setBounds(0, 0, Gdx.graphics.getWidth()*.15f, Gdx.graphics.getWidth()*.15f);
        whiteBase.setCenter(5*whiteBase.getWidth()/4f, Gdx.graphics.getHeight() - 3*whiteBase.getHeight()/4f);
        mute = new Sprite(atlas.findRegion("MuteIcon"));
        mute.setBounds(0, 0, Gdx.graphics.getWidth()*.12f, Gdx.graphics.getWidth()*.12f);
        mute.setCenter(5*whiteBase.getWidth()/4f, Gdx.graphics.getHeight() - 3*whiteBase.getHeight()/4f);
        unMute = new Sprite(atlas.findRegion("UnmuteIcon"));
        unMute.setBounds(0, 0, Gdx.graphics.getWidth()*.12f, Gdx.graphics.getWidth()*.12f);
        unMute.setCenter(5*whiteBase.getWidth()/4f, Gdx.graphics.getHeight() - 3*whiteBase.getHeight()/4f);

        sprites = new Sprite[]{topMenu, bottomMenu, newGame, resume, run, collection, whiteBase, boss, lZombie,
        rZombie};
    }

    public void draw(SpriteBatch batch) {
        if(!characterScreen) {
            for (Sprite sprite : sprites) {
                sprite.draw(batch);
            }
            if(screen.myGame.mute){
                mute.draw(batch);
            }else {
                unMute.draw(batch);
            }
        } else {
            man.draw(batch);
            woman.draw(batch);
            choose.draw(batch);
            back.draw(batch);
            for (int i = sprites.length - 3; i < sprites.length; i++) {
                sprites[i].draw(batch);
            }
        }
    }

    public void checkButtons(Vector2 vector2, MyGdxGame myGame) {
        if(!characterScreen && !screen.currentScreen.equals("Collection")) {
            float menuTopY = sprites[0].getY() + sprites[0].getHeight();
            float buttonsX = Gdx.graphics.getWidth() / 2f - 0.569f * Gdx.graphics.getWidth() / 2;
            float buttonsWidth = 0.569f * Gdx.graphics.getWidth();
            float buttonHeight = 0.85f * Gdx.graphics.getHeight() / 10f;
            if (MathTools.pointInRect(new float[]{buttonsX, menuTopY - 1.33f * Gdx.graphics.getHeight() / 10f,
                    buttonsWidth, buttonHeight}, vector2)) {
                characterScreen = true;
            } else if (MathTools.pointInRect(new float[]{buttonsX, menuTopY - 2.3f * Gdx.graphics.getHeight() / 10f,
                    buttonsWidth, buttonHeight}, vector2) && dataHandler.isSaved()) {
                startGame(myGame, true, null, "Classic");
            } else if (MathTools.pointInRect(new float[]{buttonsX, menuTopY - 3.26f * Gdx.graphics.getHeight() / 10f,
                    buttonsWidth, buttonHeight}, vector2)) {
                characterScreen = true;
                run = true;
            } else if (MathTools.pointInRect(new float[]{buttonsX, menuTopY - 4.2f * Gdx.graphics.getHeight() / 10f,
                    buttonsWidth, buttonHeight}, vector2)) {
                screen.collectionScreen.setupSprites();
                screen.currentScreen = "Collection";
            }
            if(MathTools.pointInRect(getSpriteRect(sprites[sprites.length - 4]), vector2)&& Gdx.input.justTouched()){
                myGame.mute = !myGame.mute;
            }
        } else if(characterScreen){
            checkCharacter(vector2, myGame);
        }
    }

    private void startGame(MyGdxGame myGame, boolean load, String sex, String mode) {
    // Setting up map related variables
        TmxMapLoader mapLoader = new TmxMapLoader();
        TiledMap map = mapLoader.load("game_map1.tmx");
        OrthogonalTiledMapRenderer mapRenderer = new OrthogonalTiledMapRenderer(map, 1/Soldier.PPM);
        // Setting up Box2D variables
        World b2dWorld = new World(new Vector2(0, 0), true);
        B2dStaticRectangleObjectGenerator mapObjectgen = new B2dStaticRectangleObjectGenerator(b2dWorld);
        mapObjectgen.createObjects(map.getLayers().get(3));
        if(mode == "Run"){
            Soldier player = new Soldier(b2dWorld, sex);
            disposeTextures();
            screen.collectionScreen.disposeTextures();
            GameScreen gameScreen = new GameScreen(myGame, 1, b2dWorld, mapRenderer, player,
                    "Game", mode);
            myGame.setScreen(gameScreen);
        } else if(!load){
            Soldier player = new Soldier(b2dWorld, sex);
            disposeTextures();
            screen.collectionScreen.disposeTextures();
            GameScreen gameScreen = new GameScreen(myGame, 1, b2dWorld, mapRenderer, player,
                    "Game", mode);
            myGame.setScreen(gameScreen);
        } else {
            disposeTextures();
            screen.collectionScreen.disposeTextures();
            Soldier player = getLoadPlayer(b2dWorld);
            GameScreen gameScreen = new GameScreen(myGame, dataHandler.getlevel(),
                    b2dWorld, mapRenderer, player, "Shop", mode);
            myGame.setScreen(gameScreen);
        }
    }

    public void checkCharacter(Vector2 vector2, MyGdxGame myGame){
        if(MathTools.pointInRect(getSpriteRect(man), vector2)){
            if(run){
                startGame(myGame, true, "M", "Run");
            }else {
                startGame(myGame, false, "M", "Classic");
            }
        } else if(MathTools.pointInRect(getSpriteRect(woman), vector2)){
            if(run){
                startGame(myGame, true, "F", "Run");
            }else {
                startGame(myGame, false, "F", "Classic");
            }
        }else if(MathTools.pointInRect(getSpriteRect(back), vector2)){
            characterScreen = false;
        }
    }

    private float[] getSpriteRect (Sprite sprite){
        return new float[]{sprite.getX(), sprite.getY(), sprite.getWidth(), sprite.getHeight()};
    }

    private Soldier getLoadPlayer(World b2dWorld) {
        return dataHandler.getPlayer(b2dWorld);
    }

    private int getLoadLevel() {
        return dataHandler.getlevel();
    }

    public void disposeTextures(){
        for (Sprite sprite:sprites){
            sprite.getTexture().dispose();
        }
        man.getTexture().dispose();
        woman.getTexture().dispose();
        choose.getTexture().dispose();
        back.getTexture().dispose();
        atlas.dispose();
    }


}
