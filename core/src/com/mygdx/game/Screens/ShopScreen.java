package com.mygdx.game.Screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.mygdx.game.Box2D_Bodies.Soldier;
import com.mygdx.game.DataHandler;
import com.mygdx.game.MathTools;
import com.mygdx.game.MyGdxGame;
import com.mygdx.game.Weapons.FlameThrower;
import com.mygdx.game.Weapons.HandGun;
import com.mygdx.game.Weapons.Knife;
import com.mygdx.game.Weapons.Riffle;
import com.mygdx.game.Weapons.Weapon;

import java.util.ArrayList;

public class ShopScreen {
    private DataHandler dataHandler;
    private enum Tab {GUNS, AMMO}
    private Tab currentTab = Tab.GUNS;
    private TextureAtlas items = new TextureAtlas("UI/Shop_Items.txt");
    private TextureAtlas texts = new TextureAtlas("UI/Shop_Texts.txt");
    private TextureAtlas guns = new TextureAtlas("Man/Guns.txt");
    private Sprite[] mainTexts;
    private ArrayList<Sprite> buttons = new ArrayList<>();
    private ArrayList<Sprite> gunSprites = new ArrayList<>();
    private Label[] gunLabels;
    private ArrayList<Sprite> ammoSprites = new ArrayList<>();
    private Label[] ammoLabels;
    private Sprite background;
    private Sprite shop;
    private Sprite skull;
    private ArrayList<Sprite> bloodyHands = new ArrayList<>();
    private Soldier player;
    public  BitmapFont font1 = getFont(1);
    private  BitmapFont font2 = getFont(2);
    private  BitmapFont font3 = getFont(3);
    private int level;
    public boolean watchedAdOnce = false;
    private GameScreen gameScreen;

    public ShopScreen(Soldier player, int level, DataHandler dataHandler, GameScreen screen){
        gameScreen = screen;
        this.dataHandler = dataHandler;
        this.player = player;
        this.level = level;
        Texture img = new Texture("Backgrounds/background2.png");
        background = new Sprite(img);
        background.setBounds(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        background.setPosition(0,0);
        img = new Texture("UI/Shop.png");
        shop = new Sprite(img);
        shop.setBounds(0, 0, Gdx.graphics.getWidth(), 0.9f*Gdx.graphics.getWidth());
        shop.setCenter(Gdx.graphics.getWidth()/2f, Gdx.graphics.getHeight()/2f);
        skull = new Sprite(items.findRegion("Skull"));
        float skullDimensions = 3*(Gdx.graphics.getHeight()/2f - shop.getHeight()/2)/4f;
        skull.setBounds(0, 0, skullDimensions, skullDimensions*.85f);
        skull.setCenter(Gdx.graphics.getWidth()/2f,
                3*Gdx.graphics.getHeight()/4f + shop.getHeight()/4);
        setUpButtons();
        setUpTexts();
        setUpSprites();
        setUpBloodyHands();
    }

    private void setUpTexts() {
        float middleHeight = Gdx.graphics.getHeight()/2f;
        Sprite guns = new Sprite(texts.findRegion("Guns"));
        guns.setBounds(0, 0, 0.125f*shop.getHeight(), 0.056f*shop.getHeight());
        guns.setCenter(0.151f*Gdx.graphics.getWidth(), middleHeight + 0.237f*shop.getHeight());
        Sprite ammo = new Sprite(texts.findRegion("Ammo"));
        ammo.setBounds(0, 0, 0.175f*shop.getHeight(), 0.056f*shop.getHeight());
        ammo.setCenter(0.151f*Gdx.graphics.getWidth(), middleHeight + 0.111f*shop.getHeight());
        Sprite mainMenu = new Sprite(texts.findRegion("Main_Menu"));
        mainMenu.setBounds(0, 0, 0.22f*shop.getHeight(), 0.056f*shop.getHeight());
        mainMenu.setCenter(0.151f*Gdx.graphics.getWidth(), middleHeight - 0.019f*shop.getHeight());
        Sprite collection = new Sprite(texts.findRegion("Collection"));
        collection.setBounds(0, 0, 0.19f*shop.getHeight(), 0.056f*shop.getHeight());
        collection.setCenter(0.151f*Gdx.graphics.getWidth(), middleHeight - 0.146f*shop.getHeight());
        Sprite shop = new Sprite(texts.findRegion("Shop"));
        shop.setBounds(0, 0, 0.27f*this.shop.getHeight(), 0.12f*this.shop.getHeight());
        shop.setCenter(0.478f*Gdx.graphics.getWidth(), middleHeight + 0.416f*this.shop.getHeight());
        Sprite next = new Sprite(texts.findRegion("Next_Round"));
        next.setBounds(0, 0, buttons.get(0).getWidth()/2f, 2*buttons.get(0).getHeight()/3f);
        next.setCenter(Gdx.graphics.getWidth()/2f, buttons.get(0).getY() + buttons.get(0).getHeight()/2f);
        mainTexts = new Sprite[]{guns, ammo, mainMenu, collection, shop, next};
    }

    private void setUpBloodyHands() {
        gameScreen.helper.setUpBloodyHands(bloodyHands , items, shop);
        }

    private void setUpButtons() {
        gameScreen.helper.setUpButtons(items, shop, buttons);
    }

    //Sets up labels based on dynamic texts given and constant ones set within the method
    private void setUpLabels(String text1, String text2, String text3, String text4, String text5,
                             String text6, String text7, String text8, String text9, String text10,
                             String text11, String text12,
                             String text13, String text14) {
        Label.LabelStyle labelStyle = new Label.LabelStyle(font1, Color.BLACK);
        Label.LabelStyle labelStyle2 = new Label.LabelStyle(font2, Color.BLACK);
        Label.LabelStyle labelStyle3 = new Label.LabelStyle(font2, Color.WHITE);
        Label.LabelStyle labelStyle4 = new Label.LabelStyle(font2, Color.BLACK);
        Label.LabelStyle labelStyle5 = new Label.LabelStyle(font3, Color.WHITE);
        Label earned = new Label("You earned $"+gameScreen.calcLevelPay()+" last round",
                labelStyle3);
        earned.setPosition(0.56f*Gdx.graphics.getWidth() - earned.getWidth()/2,
                Gdx.graphics.getHeight()/2f - 0.32f*shop.getHeight()- earned.getHeight()/2);
        Label ad = new Label(text14, labelStyle4);
        ad.setPosition(0.455f*Gdx.graphics.getWidth() - ad.getWidth()/2,
                Gdx.graphics.getHeight()/2f -
                        shop.getHeight()/2 - 0.09f*shop.getHeight()- ad.getHeight()/2);
        Label cleared = new Label("You've cleared "+level+ " rounds", labelStyle5);
        cleared.setPosition(Gdx.graphics.getWidth()/2f - cleared.getWidth()/2,
                (Gdx.graphics.getHeight()/2f - shop.getHeight()/2)/3f + 0.1f*shop.getHeight());
        Label label1 = new Label(text1, labelStyle);
        label1.setPosition(0.404f*Gdx.graphics.getWidth() - label1.getWidth()/2,
                Gdx.graphics.getHeight()/2f + 0.014f*shop.getHeight() - label1.getHeight()/2);
        Label label2 = new Label(text2, labelStyle);
        label2.setPosition(0.56f*Gdx.graphics.getWidth() - label2.getWidth()/2,
                Gdx.graphics.getHeight()/2f + 0.014f*shop.getHeight() - label2.getHeight()/2);
        Label label3 = new Label(text3, labelStyle);
        label3.setPosition(0.72f*Gdx.graphics.getWidth() - label3.getWidth()/2,
                Gdx.graphics.getHeight()/2f + 0.014f*shop.getHeight() - label3.getHeight()/2);
        Label label4 = new Label(text4, labelStyle);
        label4.setPosition(0.404f*Gdx.graphics.getWidth() - label4.getWidth()/2,
                Gdx.graphics.getHeight()/2f - 0.271f*shop.getHeight()- label4.getHeight()/2);
        Label label5 = new Label(text5, labelStyle);
        label5.setPosition(0.56f*Gdx.graphics.getWidth() - label5.getWidth()/2,
                Gdx.graphics.getHeight()/2f - 0.271f*shop.getHeight() - label5.getHeight()/2);
        Label label6 = new Label(text6, labelStyle);
        label6.setPosition(0.72f*Gdx.graphics.getWidth() - label6.getWidth()/2,
                Gdx.graphics.getHeight()/2f - 0.271f*shop.getHeight() - label6.getHeight()/2);
        Label health = new Label(text10, labelStyle2);
        health.setPosition(0.58f*Gdx.graphics.getWidth(),
                Gdx.graphics.getHeight()/2f - 0.45f*shop.getHeight());
        Label money = new Label(text11, labelStyle2);
        money.setPosition(0.333f*Gdx.graphics.getWidth(),
                Gdx.graphics.getHeight()/2f - 0.45f*shop.getHeight());
        gunLabels = new Label[]{label1, label2, label3, label4, label5, label6, ad, cleared,
                health, money, earned};
        label1 = new Label(text7, labelStyle);
        label1.setPosition(0.404f*Gdx.graphics.getWidth() - label1.getWidth()/2,
                Gdx.graphics.getHeight()/2f + 0.014f*shop.getHeight() - label1.getHeight()/2);
        label2 = new Label(text8, labelStyle);
        label2.setPosition(0.56f*Gdx.graphics.getWidth() - label2.getWidth()/2,
                Gdx.graphics.getHeight()/2f + 0.014f*shop.getHeight() - label2.getHeight()/2);
        label3 = new Label(text9, labelStyle);
        label3.setPosition(0.72f*Gdx.graphics.getWidth() - label3.getWidth()/2,
                Gdx.graphics.getHeight()/2f + 0.014f*shop.getHeight() - label3.getHeight()/2);
        label4 = new Label("Hand Gun :" + player.getWeapon(HandGun.class).getAmmo(), labelStyle3);
        label4.setPosition(0.404f*Gdx.graphics.getWidth() - label4.getWidth()/2,
                Gdx.graphics.getHeight()/2f + 0.28f*shop.getHeight() - label4.getHeight()/2);
        label5 = new Label("Riffle :" + text12, labelStyle3);
        label5.setPosition(0.56f*Gdx.graphics.getWidth() - label5.getWidth()/2,
                Gdx.graphics.getHeight()/2f + 0.28f*shop.getHeight() - label5.getHeight()/2);
        label6 = new Label("FlameThrower :" + text13, labelStyle3);
        label6.setPosition(0.72f*Gdx.graphics.getWidth() - label6.getWidth()/2,
                Gdx.graphics.getHeight()/2f + 0.28f*shop.getHeight() - label6.getHeight()/2);
        Label label7 = new Label("x30", labelStyle3);
        label7.setPosition(0.45f*Gdx.graphics.getWidth() - label7.getWidth()/2,
                Gdx.graphics.getHeight()/2f + 0.07f*shop.getHeight() - label7.getHeight()/2);
        Label label8 = new Label("x50", labelStyle3);
        label8.setPosition(0.61f*Gdx.graphics.getWidth() - label8.getWidth()/2,
                Gdx.graphics.getHeight()/2f + 0.07f*shop.getHeight() - label8.getHeight()/2);
        Label label9 = new Label("x50", labelStyle3);
        label9.setPosition(0.68f*Gdx.graphics.getWidth() - label9.getWidth()/2,
                Gdx.graphics.getHeight()/2f + 0.07f*shop.getHeight() - label9.getHeight()/2);
        ammoLabels = new Label[]{label1, label2, label3, label4, label5, label6, label7, label8,
                label9, ad, cleared, health, money, earned};
        System.gc();
    }

    private void setUpSprites() {
        gameScreen.helper.setUpSprites(items, guns, shop, gunSprites, ammoSprites);
    }


    public void render(float delta, SpriteBatch batch) {
        update(delta);

        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        batch.begin();
        background.draw(batch);
        shop.draw(batch);
        for(Sprite hand: bloodyHands){
            hand.draw(batch);
        }
        skull.draw(batch);
        if(currentTab == Tab.GUNS) {
            for(int i = 0; i < buttons.size(); i++) {
                if(i == 1 && watchedAdOnce){
                    buttons.get(i).draw(batch, 0.5f);
                }else {
                    buttons.get(i).draw(batch);
                }
            }
            for(int i = 0; i < gunSprites.size(); i++) {
                if(i == gunSprites.size() - 1 && watchedAdOnce){
                    gunSprites.get(i).draw(batch, 0.5f);
                }else {
                    gunSprites.get(i).draw(batch);
                }
            }
            for(int i = 0; i < gunLabels.length; i++){
                if( i == 6 && watchedAdOnce){
                    gunLabels[i].draw(batch, 0.5f);
                }else {
                    gunLabels[i].draw(batch, 1);
                }
            }
        }else if(currentTab == Tab.AMMO){
            for(int i = 0; i < buttons.size() - 4; i++) {
                if(i == 1 && watchedAdOnce){
                    buttons.get(i).draw(batch, 0.5f);
                }else {
                    buttons.get(i).draw(batch);
                }
            }
            buttons.get(buttons.size() - 1).draw(batch);
            for(int i = 0; i < ammoSprites.size(); i++) {
                if(i == ammoSprites.size() - 1 && watchedAdOnce){
                    ammoSprites.get(i).draw(batch, 0.5f);
                }else {
                    ammoSprites.get(i).draw(batch);
                }
            }
            for(int i = 0; i < ammoLabels.length; i++){
                if( i == 9 && watchedAdOnce){
                    ammoLabels[i].draw(batch, 0.5f);
                }else {
                    ammoLabels[i].draw(batch, 1);
                }
            }
        }
        for(Sprite text: mainTexts){
            text.draw(batch);
        }
        batch.end();

    }

    private void update(float delta) {
        if(gameScreen.adClicked && gameScreen.myGame.adWatchedFully()) {
            gameScreen.adClicked = false;
            watchedAdOnce = true;
            int pay;
            if (level < 11) {
                pay = 500;
            } else if (level < 21) {
                pay = 750;
            } else {
                pay = 1000;
            }
            player.earn(pay);
        }
        handleInput();
        updateLabels();
    }

    // SetsUp the dynamic texts for labels
    private void updateLabels() {
        String text1;String text2;String text3;String text4;String text5;String text6;
        String text7 = "Buy $300";String text8 = "Buy $300";String text9 = "Buy $500";
        String text10 = " : "+player.getHealth()+"/100"; String text11 = " : $"+player.getMoney();
        String text12;String text13;String text14;
        if(!player.getWeapon(HandGun.class).isFullyUpgraded()){
            text1 = "Upgrade $" + player.getWeapon(HandGun.class).getNextUpgrade();
        } else {
            text1 = "Fully Upgraded";
        }
        if(!player.getWeapon(Knife.class).isFullyUpgraded()){
            text2 ="Upgrade $" + player.getWeapon(Knife.class).getNextUpgrade();
        } else {
            text2 = "Fully Upgraded";
        }
        if(player.hasWeapon(Riffle.class)){
            text12 = String.valueOf(player.getWeapon(Riffle.class).getAmmo());
            if(!player.getWeapon(Riffle.class).isFullyUpgraded()){
                text3 = "Upgrade $" + player.getWeapon(Riffle.class).getNextUpgrade();
            } else {
                text3 = "Fully Upgraded";
            }
        }else {
            text3 = "Buy $" + Riffle.price;
            text8 = "Get gun first";
            text12 = "0";
        }
        if(player.hasWeapon(FlameThrower.class)){
            text13 = String.valueOf(player.getWeapon(FlameThrower.class).getAmmo());
            if(!player.getWeapon(FlameThrower.class).isFullyUpgraded()){
                text4 = "Upgrade $" + player.getWeapon(FlameThrower.class).getNextUpgrade();
            } else {
                text4 = "Fully Upgraded";
            }
        }else {
            text4 = "Buy $" + FlameThrower.price;
            text9 = "Get gun first";
            text13 = "0";
        }
        if(player.getHealth() == 100){
            text5 = "Health is Full";
        } else{
            text5 = "Buy $150";
        }
        if(level < 11){
            text14 = "Watch an ad for $500";
        }else if(level < 21){
            text14 = "Watch an ad for $750";
        }else {
            text14 = "Watch an ad for $1000";
        }
        if(player.armorFullyUpgraded()){
            text6 = "Fully upgraded";
        }else {
            text6 = "Upgrade $"+player.getNextArmorPrice();
        }
        setUpLabels(text1, text2, text3, text4, text5, text6, text7, text8, text9, text10, text11,
                text12, text13, text14);
    }

    private void handleInput() {
        if (Gdx.input.justTouched()) {
            checkButtons(new Vector2(Gdx.input.getX(), Gdx.graphics.getHeight() - Gdx.input.getY()));
        }
    }

    private void checkButtons(Vector2 vector2) {
        checkBuyButtons(vector2);
        float sideButtonsX = Gdx.graphics.getWidth()*0.042f;
        float sideButtonsWidth = Gdx.graphics.getWidth()*0.218f;
        float sideButtonsHeight = shop.getHeight()*0.103f;
        float middleHeight = Gdx.graphics.getHeight()/2f;
        if(MathTools.pointInRect(getSpriteRect(buttons.get(0)), vector2)){
            level += 1;
            gameScreen.level += 1;
            assert level == gameScreen.level;
            gameScreen.zombiesaddAll(gameScreen.zombieFactory.createZombiesClassic(level));
            player.body.setTransform(50, 100, 0);
            gameScreen.revivedOnce = false;
            watchedAdOnce = false;
            gameScreen.currentScreen = "Game";
        }else if(MathTools.pointInRect(getSpriteRect(buttons.get(1)), vector2) && !watchedAdOnce){
            gameScreen.adClicked = true;
            gameScreen.myGame.showAd();
        }else if(MathTools.pointInRect(new float[]{sideButtonsX,
                middleHeight+0.186f*shop.getHeight(), sideButtonsWidth, sideButtonsHeight}, vector2)){
            currentTab = Tab.GUNS;
        }else if(MathTools.pointInRect(new float[]{sideButtonsX,
                middleHeight+0.06f*shop.getHeight(), sideButtonsWidth, sideButtonsHeight}, vector2)){
            currentTab = Tab.AMMO;
        } else if(MathTools.pointInRect(new float[]{sideButtonsX,
                middleHeight-0.07f*shop.getHeight(), sideButtonsWidth, sideButtonsHeight}, vector2)){
            disposeTextures();
            player.disposeTexture();
            gameScreen.zombieFactory.disposeTextures();
            gameScreen.collectionScreen.disposeTextures();
            gameScreen.disposeTextures();
            gameScreen.myGame.setScreen((new StartScreen(gameScreen.myGame)));
        } else if(MathTools.pointInRect(new float[]{sideButtonsX,
                middleHeight-0.197f*shop.getHeight(), sideButtonsWidth, sideButtonsHeight}, vector2)){
            gameScreen.collectionScreen.setupSprites();
            gameScreen.currentScreen = "Collection";
        }
    }

    private void checkBuyButtons(Vector2 vector2) {
        if(currentTab == Tab.GUNS) {
            if (MathTools.pointInRect(getSpriteRect(buttons.get(2)), vector2) &&
                    player.canAfford(player.getWeapon(HandGun.class).getNextUpgrade())
                    ) {
                player.pay(player.getWeapon(HandGun.class).getNextUpgrade());
                player.getWeapon(HandGun.class).upgrade();
                dataHandler.saveToFile(player, level);
            } else if (MathTools.pointInRect(getSpriteRect(buttons.get(3)), vector2) &&
                    player.canAfford(player.getWeapon(Knife.class).getNextUpgrade())) {
                player.pay(player.getWeapon(Knife.class).getNextUpgrade());
                player.getWeapon(Knife.class).upgrade();
                dataHandler.saveToFile(player, level);
            }else if (MathTools.pointInRect(getSpriteRect(buttons.get(4)), vector2)) {
                if(!player.hasWeapon(Riffle.class) && player.canAfford(Riffle.price)){
                    player.addWeapon(new Riffle(50), 0);
                    player.pay(Riffle.price);
                    dataHandler.saveToFile(player, level);
                } else if(player.hasWeapon(Riffle.class) &&
                        player.canAfford(player.getWeapon(Riffle.class).getNextUpgrade())) {
                    player.pay(player.getWeapon(Riffle.class).getNextUpgrade());
                    player.getWeapon(Riffle.class).upgrade();
                    dataHandler.saveToFile(player, level);
                }
            } else if (MathTools.pointInRect(getSpriteRect(buttons.get(5)), vector2)) {
                if(!player.hasWeapon(FlameThrower.class) && player.canAfford(FlameThrower.price)){
                    player.addWeapon(new FlameThrower(50), 0);
                    player.pay(FlameThrower.price);
                    dataHandler.saveToFile(player, level);
                } else if(player.hasWeapon(FlameThrower.class) &&
                        player.canAfford(player.getWeapon(FlameThrower.class).getNextUpgrade())) {
                    player.pay(player.getWeapon(FlameThrower.class).getNextUpgrade());
                    player.getWeapon(FlameThrower.class).upgrade();
                    dataHandler.saveToFile(player, level);
                }
            }else if (MathTools.pointInRect(getSpriteRect(buttons.get(6)), vector2) &&
                    player.getHealth() < 100 && player.canAfford(300)){
                player.heal();
                player.pay(150);
                dataHandler.saveToFile(player, level);
            } else if (MathTools.pointInRect(getSpriteRect(buttons.get(7)), vector2)
                    && !player.armorFullyUpgraded()){
                if(player.canAfford(player.getNextArmorPrice())) {
                    player.upgradeArmor();
                    dataHandler.saveToFile(player, level);
                }
            }
        } else if (currentTab == Tab.AMMO){
            if (MathTools.pointInRect(getSpriteRect(buttons.get(2)), vector2) &&
                    player.canAfford(300)
            ) {
                player.getWeapon(HandGun.class).addAmmo(30);
                player.pay(300);
                dataHandler.saveToFile(player, level);
            } else if (MathTools.pointInRect(getSpriteRect(buttons.get(3)), vector2) &&
                    player.canAfford(300)
            ) {
                player.getWeapon(Riffle.class).addAmmo(50);
                player.pay(300);
                dataHandler.saveToFile(player, level);
            }else if (MathTools.pointInRect(getSpriteRect(buttons.get(4)), vector2 ) &&
                    player.canAfford(500)
            ) {
                player.getWeapon(FlameThrower.class).addAmmo(50);
                player.pay(500);
                dataHandler.saveToFile(player, level);
            }
        }
    }

    private float[] getSpriteRect (Sprite sprite){
        return new float[]{sprite.getX(), sprite.getY(), sprite.getWidth(), sprite.getHeight()};
    }

    public void dispose() {
        disposeTextures();
    }

    private static BitmapFont getFont(int number) {
        FreeTypeFontGenerator.FreeTypeFontParameter parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
        FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal("Fonts/Blenda Script.otf"), 0);
        parameter.minFilter = Texture.TextureFilter.Linear;
        parameter.magFilter = Texture.TextureFilter.Linear;
        parameter.renderCount = 3;
        if(number == 1){
            parameter.size = (int) (0.021*Gdx.graphics.getWidth());
            BitmapFont font = generator.generateFont(parameter);
            generator.dispose();
            return font;
        }else if (number == 2){
            parameter.size = (int) (0.026*Gdx.graphics.getWidth());
            BitmapFont font = generator.generateFont(parameter);
            generator.dispose();
            return font;
        }else if (number == 3){
            parameter.size = (int) (0.036*Gdx.graphics.getWidth());
            BitmapFont font = generator.generateFont(parameter);
            generator.dispose();
            return font;
        }
        return null;
    }

    public void disposeTextures(){
        for(Sprite sprite:mainTexts){
            sprite.getTexture().dispose();
        }
        for(Sprite sprite:buttons){
            sprite.getTexture().dispose();
        }
        for(Sprite sprite:gunSprites){
            sprite.getTexture().dispose();
        }
        for(Sprite sprite:ammoSprites){
            sprite.getTexture().dispose();
        }
        for(Sprite sprite:bloodyHands){
            sprite.getTexture().dispose();
        }
        skull.getTexture().dispose();
        shop.getTexture().dispose();
        background.getTexture().dispose();
        guns.dispose();
        items.dispose();
        texts.dispose();
    }

    public void setPlayer(Soldier player){
        this.player = player;
    }
}
