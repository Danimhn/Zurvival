package com.mygdx.game.Screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.Vector2;
import com.mygdx.game.DataHandler;
import com.mygdx.game.MathTools;
import com.mygdx.game.MyGdxGame;
import com.mygdx.game.Screens.GameScreen;

import java.util.ArrayList;

public class CollectionScreen {
    private TextureAtlas atlas  = new TextureAtlas("UI/Collection.txt");
    private ArrayList<Sprite> sprites;
    private Sprite background;
    private Sprite slots;
    private Screen screen;
    private DataHandler dataHandler;
    private Sprite display;
    private Sprite back;
    private Sprite discovered;
    private String backFunction;

    public CollectionScreen(Screen screen, DataHandler dataHandler, String backFuntion){
        this.screen = screen;
        this.dataHandler = dataHandler;
        this.backFunction = backFuntion;
        Texture img = new Texture("Backgrounds/background2.png");
        background = new Sprite(img);
        img = new Texture("UI/CollectionSlots.png");
        slots = new Sprite(img);
        slots.setBounds(0, 0, Gdx.graphics.getWidth(), 0.85f*Gdx.graphics.getHeight());
        slots.setCenter(Gdx.graphics.getWidth()/2f, Gdx.graphics.getHeight()/2f);
        back = new Sprite(atlas.findRegion("Back"));
        back.setBounds(0,0,Gdx.graphics.getWidth()/4f, Gdx.graphics.getWidth()/12f);
        back.setCenter(Gdx.graphics.getWidth()/7f,
                back.getHeight());
        discovered = new Sprite(MyGdxGame.uiAtlas.findRegion("Zombies Discovered"));
        discovered.setBounds(0,0,2*Gdx.graphics.getWidth()/3F, Gdx.graphics.getWidth()/10f);
        discovered.setCenter(Gdx.graphics.getWidth()/2f,
                Gdx.graphics.getHeight() - discovered.getHeight());
    }

    public void setupSprites() {
        sprites = new ArrayList<>();
        int lvl = dataHandler.getHighestLevel();
        Sprite sprite;
        if (lvl > 0){
            sprite = new Sprite(atlas.findRegion("icon1"));
            sprite.setBounds(0, 0, .180f*Gdx.graphics.getWidth(), .109f*Gdx.graphics.getHeight());
            sprite.setCenter(.216f*Gdx.graphics.getWidth(),
                    Gdx.graphics.getHeight()/2f + .066f*Gdx.graphics.getHeight());
            sprites.add(sprite);
        }
        if (lvl > 2){
            sprite = new Sprite(atlas.findRegion("icon2"));
            sprite.setBounds(0, 0, .180f*Gdx.graphics.getWidth(), .109f*Gdx.graphics.getHeight());
            sprite.setCenter(.399f*Gdx.graphics.getWidth(),
                    Gdx.graphics.getHeight()/2f + .066f*Gdx.graphics.getHeight());
            sprites.add(sprite);
        }
        if (lvl > 5){
            sprite = new Sprite(atlas.findRegion("icon3"));
            sprite.setBounds(0, 0, .180f*Gdx.graphics.getWidth(), .109f*Gdx.graphics.getHeight());
            sprite.setCenter(.601f*Gdx.graphics.getWidth(),
                    Gdx.graphics.getHeight()/2f + .066f*Gdx.graphics.getHeight());
            sprites.add(sprite);
        }
        if (lvl > 10){
            sprite = new Sprite(atlas.findRegion("icon4"));
            sprite.setBounds(0, 0, .180f*Gdx.graphics.getWidth(), .109f*Gdx.graphics.getHeight());
            sprite.setCenter(.794f*Gdx.graphics.getWidth(),
                    Gdx.graphics.getHeight()/2f + .066f*Gdx.graphics.getHeight());
            sprites.add(sprite);
        }
        if (lvl > 15){
            sprite = new Sprite(atlas.findRegion("icon5"));
            sprite.setBounds(0, 0, .180f*Gdx.graphics.getWidth(), .109f*Gdx.graphics.getHeight());
            sprite.setCenter(.216f*Gdx.graphics.getWidth(),
                    Gdx.graphics.getHeight()/2f - .049f*Gdx.graphics.getHeight());
            sprites.add(sprite);
        }
        if (lvl > 20){
            sprite = new Sprite(atlas.findRegion("icon6"));
            sprite.setBounds(0, 0, .180f*Gdx.graphics.getWidth(), .109f*Gdx.graphics.getHeight());
            sprite.setCenter(.399f*Gdx.graphics.getWidth(),
                    Gdx.graphics.getHeight()/2f - .049f*Gdx.graphics.getHeight());
            sprites.add(sprite);
        }
        if (lvl > 25){
            sprite = new Sprite(atlas.findRegion("icon7"));
            sprite.setBounds(0, 0, .180f*Gdx.graphics.getWidth(), .109f*Gdx.graphics.getHeight());
            sprite.setCenter(.601f*Gdx.graphics.getWidth(),
                    Gdx.graphics.getHeight()/2f - .049f*Gdx.graphics.getHeight());
            sprites.add(sprite);
        }
        if (lvl > 30){
            sprite = new Sprite(atlas.findRegion("icon8"));
            sprite.setBounds(0, 0, .180f*Gdx.graphics.getWidth(), .109f*Gdx.graphics.getHeight());
            sprite.setCenter(.794f*Gdx.graphics.getWidth(),
                    Gdx.graphics.getHeight()/2f - .049f*Gdx.graphics.getHeight());
            sprites.add(sprite);
        }
        if (lvl > 35){
            sprite = new Sprite(atlas.findRegion("icon9"));
            sprite.setBounds(0, 0, .180f*Gdx.graphics.getWidth(), .109f*Gdx.graphics.getHeight());
            sprite.setCenter(.216f*Gdx.graphics.getWidth(),
                    Gdx.graphics.getHeight()/2f - .165f*Gdx.graphics.getHeight());
            sprites.add(sprite);
        }
        if (lvl > 39){
            sprite = new Sprite(atlas.findRegion("icon10"));
            sprite.setBounds(0, 0, .180f*Gdx.graphics.getWidth(), .109f*Gdx.graphics.getHeight());
            sprite.setCenter(.399f*Gdx.graphics.getWidth(),
                    Gdx.graphics.getHeight()/2f - .165f*Gdx.graphics.getHeight());
            sprites.add(sprite);
        }
    }

    public void render(SpriteBatch batch){
        handleInput();

        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        batch.begin();
        background.draw(batch);
        slots.draw(batch);
        for(Sprite sprite:sprites){
            sprite.draw(batch);
        }
        back.draw(batch);
        discovered.draw(batch);
        if(display != null) {
            display.draw(batch);
        }
        batch.end();
    }

    private void handleInput() {
        if (Gdx.input.justTouched()) {
            checkButtons(new Vector2(Gdx.input.getX(), Gdx.graphics.getHeight() - Gdx.input.getY()));
        }
    }

    public void checkButtons(Vector2 vector2){
        if(MathTools.pointInRect(getSpriteRect(back), vector2)){
            if (backFunction == "Shop") {
                ((GameScreen)screen).currentScreen = "Shop";
            } else if(backFunction.equals("Menu")){
                ((StartScreen)screen).currentScreen = "Menu";
            }
        }
        for (int i = 0; i < sprites.size(); i++){
            if(MathTools.pointInRect(getSpriteRect(sprites.get(i)), vector2)){
                setDisplay(i + 1);
            }
        }
    }

    private void setDisplay(int index) {
        display = new Sprite(atlas.findRegion("icon"+index));
        display.setBounds(0, 0, .314f*Gdx.graphics.getWidth(), .194f*Gdx.graphics.getHeight());
        display.setCenter(.716f*Gdx.graphics.getWidth(),
                Gdx.graphics.getHeight()/2f + .252f*Gdx.graphics.getHeight());
    }

    public void disposeTextures() {
        atlas.dispose();
        System.out.println("Dis");
        background.getTexture().dispose();
        slots.getTexture().dispose();
        back.getTexture().dispose();
    }

    private float[] getSpriteRect (Sprite sprite){
        return new float[]{sprite.getX(), sprite.getY(), sprite.getWidth(), sprite.getHeight()};
    }
}
