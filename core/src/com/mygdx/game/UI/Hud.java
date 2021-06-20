package com.mygdx.game.UI;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.ui.Container;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.mygdx.game.AI.Zombie;
import com.mygdx.game.Box2D_Bodies.Soldier;
import com.mygdx.game.MathTools;
import com.mygdx.game.MyGdxGame;
import com.mygdx.game.Screens.GameScreen;
import com.mygdx.game.Weapons.FlameThrower;
import com.mygdx.game.Weapons.HandGun;
import com.mygdx.game.Weapons.Knife;
import com.mygdx.game.Weapons.Riffle;

public class Hud {
    private GameScreen screen;
    private Soldier player;
    public Sprite gunSlot;
    public Sprite gunSlotL;
    public Sprite gunSlotR;
    public Sprite healthBar;
    public Sprite healthBarL;
    public Sprite healthBarR;
    public Sprite shooting;
    public Sprite options;
    public Sprite navigate;
    public Sprite arrow;
    public float arrowAngle = 90;
    public float arrowBeenDrawnFor = 2;
    public Sprite[] sprites;
    public Sprite[] inventorySprites = new Sprite[]{};
    private Container<Label> ammo;
    private Label zombiesToGo;
    private OrthographicCamera cam;
    private boolean run;
    //next time a zombie needs to be added in hundredth of a second.
    private int nextAdd = 100;
    //How long the player has survived in hundredth of a second.
    public float runTimer = 0;
    public Label runTimerLabel;
    public BitmapFont font;

    // To define sprites' bounds directly dependent on gameport size, they are defined only once
    // in the update method rather than the constructor
    private boolean boundsSet;

    public Hud(Soldier player, OrthographicCamera cam, float aspectRatio, boolean run,
               GameScreen screen){
        this.screen = screen;
        this.player = player;
        this.cam = cam;
        this.run = run;
        gunSlot = new Sprite(MyGdxGame.uiAtlas.findRegion("Gun_Slot"));
        gunSlotL = new Sprite(MyGdxGame.uiAtlas.findRegion("Gun_SlotL"));
        gunSlotR = new Sprite(MyGdxGame.uiAtlas.findRegion("Gun_SlotR"));
        healthBarL = new Sprite(MyGdxGame.uiAtlas.findRegion("Health_BarL"));
        healthBarR = new Sprite(MyGdxGame.uiAtlas.findRegion("Health_BarR"));
        healthBar = new Sprite(MyGdxGame.uiAtlas.findRegion("Health_Bar"));
        shooting = new Sprite(MyGdxGame.uiAtlas.findRegion("Shooting_Button"));
        options = new Sprite(MyGdxGame.uiAtlas.findRegion("Options_Button"));
        navigate = new Sprite(MyGdxGame.uiAtlas.findRegion("Navigate"));
        arrow = new Sprite(MyGdxGame.uiAtlas.findRegion("left-red-arrow"));
        navigate.setAlpha(0.5f);
        System.out.println("Texture :" +navigate.getTexture());
        sprites = new Sprite[]{gunSlot, gunSlotL, gunSlotR, healthBar, healthBarL, healthBarR,
                 shooting, options, navigate};
        FreeTypeFontGenerator.FreeTypeFontParameter parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
        FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal("Fonts/Blenda Script.otf"), 0);
        parameter.size = 26;
        parameter.minFilter = Texture.TextureFilter.Linear;
        parameter.magFilter = Texture.TextureFilter.Linear;
        parameter.renderCount = 3;
        font = generator.generateFont(parameter);
        generator.dispose();
        Label.LabelStyle labelStyle = new Label.LabelStyle(font, Color.BLACK);
        Label label = new Label(String.valueOf(player.getCurrentGun().getAmmo()), labelStyle);
        label.setFontScale(.15f);
        ammo = new Container<>(label);
        zombiesToGo = new Label(screen.getZombies().size() +" zombies left", labelStyle);
    }

    public void update(float dt){
        if(!boundsSet){
            this.setBounds();
            boundsSet = true;
        }
        screen.helper.setUpHudPosition(gunSlot, gunSlotL, gunSlotR, healthBar, healthBarR, healthBarL,
                shooting, options, navigate, cam, player);
        updateGunSlot();
        if(!run){
            updateZombieToGo();
        }
        Label.LabelStyle labelStyle = new Label.LabelStyle(font, Color.BLACK);
        Label label = new Label(String.valueOf(player.getCurrentGun().getAmmo()), labelStyle);
        label.setFontScale(.15f);
        ammo.setActor(label);
        ammo.setPosition(cam.position.x + label.getFontScaleX()*label.getWidth()/2f,
                gunSlot.getY() + label.getFontScaleY()*label.getHeight());
        if (run && !GameScreen.player.isDead() && dt < .2f){
            updateRunTimer(dt);
        }
    }

    private void updateZombieToGo() {
        int numberOfZombies = 0;
        for(Zombie zombie:screen.getZombies()){
            if(!zombie.isDead()){
                numberOfZombies += 1;
            }
        }
        Label.LabelStyle labelStyle = new Label.LabelStyle(font, Color.WHITE);
        zombiesToGo = new Label(numberOfZombies+" zombies  l e ft", labelStyle);
        zombiesToGo.setFontScale(.25f);
        zombiesToGo.setPosition(cam.position.x - cam.viewportWidth/2 +
                        zombiesToGo.getFontScaleX()*zombiesToGo.getWidth()/10,
                healthBar.getY() - 3*zombiesToGo.getHeight()/4);
    }

    private void updateRunTimer(float dt) {
        runTimer += dt*10;
        Label.LabelStyle labelStyle = new Label.LabelStyle(font, Color.BLACK);
        if(runTimer < 10){
            runTimerLabel = new Label("0:00:"+(int)runTimer, labelStyle);
        }else if(runTimer < 100){
            int seconds = (int) (runTimer/10);
            int hundredth = (int) (runTimer - seconds*10);
            runTimerLabel = new Label("0:0"+seconds+":"+hundredth, labelStyle);
        }else if(runTimer < 600){
            int seconds = (int) (runTimer/10);
            int hundredth = (int) (runTimer - seconds*10);
            runTimerLabel = new Label("0:"+seconds+":"+hundredth, labelStyle);
        }else{
            int minutes = (int) (runTimer/600);
            int seconds = (int) ((runTimer - minutes*600)/10);
            int hundredth = (int) (runTimer - minutes*600 - seconds*10);
            runTimerLabel = new Label(minutes+":"+seconds+":"+hundredth, labelStyle);
        }
        runTimerLabel.setFontScale(.35f);
        runTimerLabel.setPosition(cam.position.x - cam.viewportWidth/2 + runTimerLabel.getWidth()/10,
                healthBar.getY() - 3*runTimerLabel.getHeight()/4);
    }

    private void updateGunSlot() {
        inventorySprites = player.getGunSlot();
        float MainSlotWidth = 0.75f*cam.viewportHeight/15f;
        float sideSlotWidth = 0.50f*cam.viewportHeight/15f;
        for(int i = 0; i < 3; i++){
            if(inventorySprites[i] != null){
                float oldWidth = inventorySprites[i].getWidth();
                float oldHeight = inventorySprites[i].getHeight();
                if(i == 0){
                    if(oldHeight/oldWidth < 0.5){
                        inventorySprites[i].setBounds(0, 0, sideSlotWidth, 2*sideSlotWidth*oldHeight/oldWidth);
                    }else {
                        inventorySprites[i].setBounds(0, 0, sideSlotWidth, sideSlotWidth * oldHeight / oldWidth);
                    }
                    inventorySprites[i].setCenter(cam.position.x - gunSlot.getWidth()/2 - gunSlotL.getWidth()/2,
                            cam.position.y - cam.viewportHeight/2 + gunSlot.getHeight()/2 + 5);
                } else if( i == 1){
                    if(oldHeight/oldWidth < 0.5){
                        inventorySprites[i].setBounds(0, 0, MainSlotWidth, 2*MainSlotWidth*oldHeight/oldWidth);
                    }else {
                        inventorySprites[i].setBounds(0, 0, MainSlotWidth, MainSlotWidth * oldHeight / oldWidth);
                    }
                    if(player.getCurrentGun() instanceof Knife){
                        inventorySprites[i].setCenter(cam.position.x,
                                gunSlot.getY() + gunSlot.getHeight()/2);
                    }else {
                        inventorySprites[i].setCenter(cam.position.x,
                                gunSlot.getY() + gunSlot.getHeight() -
                                        inventorySprites[i].getHeight());
                    }
                }else {
                    if(oldHeight/oldWidth < 0.5){
                        inventorySprites[i].setBounds(0, 0, sideSlotWidth, 2*sideSlotWidth*oldHeight/oldWidth);
                    }else {
                        inventorySprites[i].setBounds(0, 0, sideSlotWidth, sideSlotWidth * oldHeight / oldWidth);
                    }
                    inventorySprites[i].setCenter(cam.position.x + gunSlot.getWidth()/2 + gunSlotL.getWidth()/2,
                            cam.position.y - cam.viewportHeight/2 + gunSlot.getHeight()/2 + 5);
                }
            }
        }
    }

    private void setBounds() {
        gunSlot.setBounds(0, 0, 1.27f*cam.viewportHeight/15f, 1.3f*cam.viewportHeight/15f);
        gunSlotL.setBounds(0, 0, 0.85f*cam.viewportHeight/15f, 1.3f*cam.viewportHeight/15f);
        gunSlotR.setBounds(0, 0, 0.85f*cam.viewportHeight/15f, 1.3f*cam.viewportHeight/15f);
        healthBar.setBounds(0, 0, 4.64f*cam.viewportHeight/27f, 1.44f*cam.viewportHeight/40f);
        healthBarL.setBounds(0, 0, 2.28f*cam.viewportHeight/27f, 1.44f*cam.viewportHeight/40f);
        healthBarR.setBounds(0, 0, 2.28f*cam.viewportHeight/27f, 1.44f*cam.viewportHeight/40f);
        shooting.setBounds(0, 0, cam.viewportHeight/12f, cam.viewportHeight/12f);
        options.setBounds(0, 0, cam.viewportHeight/20f, cam.viewportHeight/20f);
        navigate.setBounds(0, 0, cam.viewportWidth/3, cam.viewportWidth/3);
        arrow.setBounds(0, 0, cam.viewportWidth/3, cam.viewportWidth/4);
    }

    public boolean checkButtons(Vector2 vector2, boolean justTouch, float dt) {
        if(MathTools.pointInRect(getSpriteRect(gunSlotL), screenToPort(vector2)) && justTouch){
            player.switchWeaponL();
        }else if(MathTools.pointInRect(getSpriteRect(gunSlotR), screenToPort(vector2)) && justTouch){
            player.switchWeaponR();
        }else if(MathTools.pointInRect(new float[]{cam.position.x + ((cam.viewportWidth/2 -
                gunSlot.getWidth()/2)/2 + gunSlot.getWidth()/2 - cam.viewportWidth/20)
                - cam.viewportWidth/6 - cam.viewportWidth/5, cam.position.y - cam.viewportHeight/2
                + navigate.getHeight()/2 - cam.viewportWidth/6 + 3*cam.viewportWidth/20,
                2*cam.viewportWidth/3, 2*cam.viewportWidth/3 + gunSlot.getHeight()}, screenToPort(vector2))
                && !MathTools.pointInRect(getSpriteRect(gunSlotL), screenToPort(vector2))
                && !MathTools.pointInRect(getSpriteRect(gunSlotR), screenToPort(vector2))
                && !MathTools.pointInRect(getSpriteRect(gunSlot), screenToPort(vector2))) {
            calcVelocity(screenToPort(vector2));
            Vector2 linVelocity = calcVelocity(screenToPort(vector2));
            float newOrientation = MathTools.vectorToAngle(linVelocity);
            player.body.setTransform(player.body.getPosition(), newOrientation);
            player.body.setLinearVelocity(linVelocity);
            return true;
        }
        //Also checks if the player has a handgun to determine if the game is in Run! mode or
        //classic mode
        if(MathTools.pointInRect(getSpriteRect(shooting), screenToPort(vector2)) &&
                player.hasWeapon(HandGun.class)){
            if(player.getCurrentGun() instanceof FlameThrower ||
                    (player.getCurrentGun() instanceof Riffle && !justTouch)) {
                player.shoot(dt);
            } else if(player.getCurrentGun() instanceof Riffle && justTouch) {
                player.shoot(-1);
            }else {
                if(justTouch){
                    player.shoot(dt);
                }
            }
        }else if(MathTools.pointInRect(getSpriteRect(options), screenToPort(vector2))){
            screen.currentScreen = "Options";
            screen.setupOptions();
        }
        return false;
    }

    public Vector2 screenToPort(Vector2 vector2) {
        // Flipping y so that up is positive
        float flipY = Gdx.graphics.getHeight() - vector2.y;

        // Converting ratios
        float x =(cam.viewportWidth*vector2.x)/Gdx.graphics.getWidth();
        float y =(cam.viewportHeight*flipY)/Gdx.graphics.getHeight();

        // Adjusting values based on where the camera is
        x += cam.position.x - cam.viewportWidth/2;
        y += cam.position.y - cam.viewportHeight/2;

        return new Vector2(x, y);
    }

    private float[] getSpriteRect (Sprite sprite){
        return new float[]{sprite.getX(), sprite.getY(), sprite.getWidth(), sprite.getHeight()};
    }

    private Vector2 calcVelocity(Vector2 vector) {
        Vector2 centerOfJoyStick = new Vector2(cam.position.x + ((cam.viewportWidth/2 -
                gunSlot.getWidth()/2)/2 + gunSlot.getWidth()/2 - cam.viewportWidth/20), cam.position.y -
                cam.viewportHeight/2 + gunSlot.getHeight() + navigate.getHeight()/2 + cam.viewportWidth/5);
        // Gets the position of the second vector with respect to the centerOfJoyStick
        Vector2 distanceVector = MathTools.subtractVector2D(vector, centerOfJoyStick);

        // Figures out the x and y components of the velocity based on angle and size of the
        // distanceVector
        float x_comp = player.getmaxVelocity() * distanceVector.x / (cam.viewportWidth/3);
        float y_comp = player.getmaxVelocity() * distanceVector.y / (cam.viewportWidth/3);

        //Checks to make sure the velocity is not exceeding the maxVelocity constant
        if (Math.abs(x_comp) > player.getmaxVelocity()) {
            if (x_comp > 0) {
                x_comp = player.getmaxVelocity();
            } else {
                x_comp = -player.getmaxVelocity();
            }
        }
        if (Math.abs(y_comp) > player.getmaxVelocity()) {
            if (y_comp > 0) {
                y_comp = player.getmaxVelocity();
            } else {
                y_comp = -player.getmaxVelocity();
            }
        }
        return new Vector2(x_comp, y_comp);
    }

    public void draw(SpriteBatch batch, float delta) {
        for(Sprite sprite: sprites){
            if(!run || (sprite!= shooting && sprite!=gunSlotL && sprite!=gunSlot
                    && sprite!=gunSlotR)) {
                sprite.draw(batch);
            }
        }
        if(!run) {
            for (Sprite sprite : inventorySprites) {
                if (sprite != null) {
                    sprite.draw(batch);
                }
            }
            zombiesToGo.draw(batch, 1);
            if(player.needToDrawArrow){
                updateArrow();
                arrow.draw(batch);
                player.arrowDrawn();
                arrowBeenDrawnFor = 0;
            }if(arrowBeenDrawnFor < 2){
                updateArrow();
                arrow.draw(batch);
                arrowBeenDrawnFor += delta;
            }
            if (!(player.getCurrentGun() instanceof Knife)) {
                ammo.draw(batch, 1);
            }
        }else {
            if(runTimerLabel != null) {
                runTimerLabel.draw(batch, 1);
            }
        }
    }

    private void updateArrow() {
        if(!screen.getZombies().isEmpty()) {
            arrow.setOriginCenter();
            Vector2 vector = MathTools.subtractVector2D(screen.getZombies().get(0).getPosition(),
                    player.body.getPosition());
            float toRotate = (float) (Math.toDegrees(Math.atan2(-vector.x, vector.y)) - arrowAngle);
            arrow.rotate(toRotate);
            arrow.setCenter(cam.position.x, cam.position.y + arrow.getHeight());
            arrowAngle += toRotate;
        }
    }

    public void disposeTextures() {
        for(Sprite sprite: sprites){
            sprite.getTexture().dispose();
        }
        sprites =null;
        for(Sprite sprite:inventorySprites){
            sprite.getTexture().dispose();
        }
        inventorySprites = null;
    }

    public boolean TimeToaddZombie() {
        if(runTimer > nextAdd){
            nextAdd += 100;
            return true;
        }
        return false;
    }

    public void setPlayer(Soldier player) {
        this.player = player;
    }
}
