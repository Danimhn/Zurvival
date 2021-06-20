package com.mygdx.game.Screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.ai.GdxAI;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.mygdx.game.AI.Zombie;
import com.mygdx.game.AI.ZombieFactory;
import com.mygdx.game.Box2D_Bodies.Soldier;
import com.mygdx.game.DataHandler;
import com.mygdx.game.MathTools;
import com.mygdx.game.MyGdxGame;
import com.mygdx.game.UI.Hud;

import java.util.ArrayList;

public class GameScreen implements Screen {
    private ArrayList<Sprite> optionSprites = new ArrayList<>();
    private ArrayList<Sprite> gameOverSprites = new ArrayList<>();
    private Sprite blackScreen;
    private ArrayList<Sprite> transitionSprites = new ArrayList<>();
    private float beenTransitioningFor = 1;
    private boolean transitioning;
    private OrthographicCamera cam;
    public MyGdxGame myGame;
    private Viewport gameport;
    private float aspectRatio;
    private DataHandler dataHandler = new DataHandler();

    // Tiled map related variables
    private TmxMapLoader mapLoader;

    private OrthogonalTiledMapRenderer mapRenderer;

    //Box2d related variables
    World b2dWorld;
    Box2DDebugRenderer b2dRenderer;
    public static Soldier player;
    public static int playerRadius = 8;


    public static float game_World_Width = 3200;
    public static float game_World_Height = 3200;
    private float pToWHeightRatio = 1/5f;

    private ArrayList<Zombie> zombies = new ArrayList<Zombie>();
    public ZombieFactory zombieFactory;
    private Hud hud;
    public int level;

    public ShopScreen shopScreen;
    public CollectionScreen collectionScreen;
    public String currentScreen;
    private String mode;
    private boolean gameOver = false;
    private Music zombieNoise;

    private TextureAtlas optionsAtlas = new TextureAtlas("UI/Options.txt");
    private boolean playerDead;
    public boolean revivedOnce = false;
    public boolean adClicked;
    public GameScreenHelper helper = new GameScreenHelper();


    public GameScreen(MyGdxGame game, int level, World world, OrthogonalTiledMapRenderer mapRenderer
            , Soldier player, String screen, String mode){
        this.mode = mode;
        currentScreen = screen;
        this.myGame = game;
        this.level = level;
        if(mode.equals("Run")){
            pToWHeightRatio = .3f;
            player.setMaxVelocity(75);
        }
        ArrayList<Integer> nums = new ArrayList<>();
        int[][] yo = (int[][]) nums.toArray();
        System.out.println();
        zombieNoise = Gdx.audio.newMusic(Gdx.files.internal("Sounds/ZombieNoise.wav"));
        shopScreen = new ShopScreen(player, level, dataHandler, this);
        collectionScreen = new CollectionScreen(this, dataHandler, "Shop");
        aspectRatio = myGame.getAspectRatio();
        // Setting up Box2D variables
        b2dWorld = world;
        b2dRenderer = new Box2DDebugRenderer();
        // Initializing the OrthographicCamera (cam), and Viewport (gameport)
        cam = new OrthographicCamera();
        cam.setToOrtho(false);
        gameport = new StretchViewport(game_World_Height*9/16*pToWHeightRatio/Soldier.PPM,
                game_World_Height*pToWHeightRatio/Soldier.PPM, cam);
        gameport.apply();
        this.mapRenderer = mapRenderer;
        setPlayer(player, false);
    }

    public void setupOptions() {
        if(blackScreen == null) {
            Texture img = new Texture("Backgrounds/background2.png");
            blackScreen = new Sprite(img);
            blackScreen.setBounds(0, 0, cam.viewportWidth, cam.viewportHeight);
        }
        blackScreen.setCenter(cam.position.x, cam.position.y);
        optionSprites = new ArrayList<>();
        helper.setupOptions(optionSprites, cam, optionsAtlas);
        }

    private void setupTransition(){
        helper.setupTransition(transitionSprites, cam, optionsAtlas, beenTransitioningFor);
    }

    private void setupGameOver() {
        if(blackScreen == null) {
            Texture img = new Texture("Backgrounds/background2.png");
            blackScreen = new Sprite(img);
            blackScreen.setBounds(0, 0, cam.viewportWidth, cam.viewportHeight);
        }
        blackScreen.setCenter(cam.position.x, cam.position.y);

        helper.setupGameOver(gameOverSprites, cam, revivedOnce, optionsAtlas);

    }

    public void setPlayer(Soldier player, boolean revived){
        GameScreen.player = player;
        if(!revived) {
            //Setting up AI variables
            zombieFactory = new ZombieFactory(b2dWorld, this);
            if (mode.equals("Classic") && currentScreen.equals("Game")) {
                zombies.addAll(zombieFactory.createZombiesClassic(level));
                hud = new Hud(player, cam, aspectRatio, false, this);
            } else if (mode.equals("Classic")) {
                hud = new Hud(player, cam, aspectRatio, false, this);
            } else {
                zombies.addAll(zombieFactory.createZombiesRun(0f));
                hud = new Hud(player, cam, aspectRatio, true, this);
            }
        }else {
            hud.setPlayer(player);
            shopScreen.setPlayer(player);
        }
        player.setZombies(zombies);
    }
    @Override
    public void show() {

    }

    private float[] getSpriteRect (Sprite sprite){
        return new float[]{sprite.getX(), sprite.getY(), sprite.getWidth(), sprite.getHeight()};
    }

    public void update(float delta){
        if(adClicked && myGame.adWatchedFully()){
            gameOver = false;
            adClicked = false;
            player.heal();
            dataHandler.saveToFile(player, level);
            setPlayer(dataHandler.getPlayer(b2dWorld), true);
            revivedOnce = true;
        }
        if (allZombiesDead()) {
            transitioning = true;
        }
        if(player.zombiesNearby() && !myGame.mute && currentScreen.equals("Game") && !transitioning
                && !gameOver){
            zombieNoise.play();
        }else {
            zombieNoise.pause();
        }
        if(transitioning){
            setupTransition();
            beenTransitioningFor += delta;
            if(beenTransitioningFor > 5){
                transitioning = false;
                beenTransitioningFor = 0;
                payPlayerForLevel();
                dataHandler.saveToFile(player, level);
                zombieFactory.disposeTextures();
                currentScreen = "Shop";
                myGame.resetBatch();
            }
        }else {
            handleInput(delta);
            if (currentScreen.equals("Game")) {
                b2dWorld.step(1 / 60f, 6, 2);
                GdxAI.getTimepiece().update(delta);
                player.update(delta);
                hud.update(delta);
                if (player.getBeenDeadFor() > 2) {
                    setupGameOver();
                    gameOver = true;
                }
                if (mode == "Run" && hud.TimeToaddZombie()) {
                    zombies.addAll(zombieFactory.createZombiesRun(hud.runTimer / 10f));
                } else {
                    ArrayList<Zombie> toBeRemoved = new ArrayList<>();
                    for (Zombie zombie : zombies) {
                        zombie.update(delta);
                        if (zombie.isdeadFor5()) {
                            toBeRemoved.add(zombie);
                        }
                    }
                    for (Zombie zombie : toBeRemoved) {
                        zombies.remove(zombie);
                    }
                }
            }
            setCamPos();
            cam.update();
            mapRenderer.setView(cam);
        }
    }

    private boolean allZombiesDead() {
        int numberOfZombies = 0;
        for(Zombie zombie: zombies){
            if(!zombie.isDeadFor2()){
                numberOfZombies += 1;
            }
        }
        return numberOfZombies == 0;
    }

    private void setCamPos() {
        if(!player.isDead()){
            cam.position.x = player.body.getPosition().x;
            cam.position.y = player.body.getPosition().y;
            if((cam.position.x + cam.viewportWidth/2) > game_World_Width/Soldier.PPM){
                cam.position.x = game_World_Width/Soldier.PPM - cam.viewportWidth/2;
            }else if((cam.position.x - cam.viewportWidth/2) < 0){
                cam.position.x = cam.viewportWidth/2;
            }
            if((cam.position.y + cam.viewportHeight/2) > game_World_Height/Soldier.PPM){
                cam.position.y = game_World_Height/Soldier.PPM - cam.viewportHeight/2;
            }else if((cam.position.y - cam.viewportHeight/2) < 0){
                cam.position.y = cam.viewportHeight/2;
            }
        }
    }

    private void handleInput(float delta) {
        if(gameOver){
            if(Gdx.input.isTouched()) {
                Vector2 touch = hud.screenToPort(new Vector2(Gdx.input.getX(), Gdx.input.getY()));
                if (MathTools.pointInRect(getSpriteRect(gameOverSprites.get(1)), touch)) {
                    shopScreen.disposeTextures();
                    player.disposeTexture();
                    zombieFactory.disposeTextures();
                    collectionScreen.disposeTextures();
                    disposeTextures();
                    dataHandler.clearFile();
                    myGame.setScreen((new StartScreen(myGame)));
                    myGame.resetBatch();
                }
                else if(MathTools.pointInRect(getSpriteRect(gameOverSprites.get(2)), touch)){
                    if(!revivedOnce) {
                        myGame.showAd();
                        adClicked = true;
                    }
                }
            }
        }else if(currentScreen.equals("Game")) {
            boolean navigating1 = false;
            boolean navigating2 = false;
            if (Gdx.input.isTouched()) {
                if (Gdx.input.isTouched(0)) {
                    navigating1 = hud.checkButtons(new Vector2(Gdx.input.getX(0), Gdx.input.getY(0)),
                            Gdx.input.justTouched(), delta);
                }
                if (Gdx.input.isTouched(1)) {
                    navigating2 = hud.checkButtons(new Vector2(Gdx.input.getX(1), Gdx.input.getY(1)),
                            Gdx.input.justTouched(), delta);
                }
            }
            if (!navigating1 && !navigating2) {
                player.body.setLinearVelocity(0, 0);
            }
        }else if (currentScreen.equals("Options")){
            if(Gdx.input.isTouched()){
                Vector2 touch = hud.screenToPort(new Vector2(Gdx.input.getX(),Gdx.input.getY()));
                if(MathTools.pointInRect(getSpriteRect(optionSprites.get(1)), touch)){
                    currentScreen = "Game";
                }else if(MathTools.pointInRect(getSpriteRect(optionSprites.get(2)), touch)){
                    shopScreen.disposeTextures();
                    player.disposeTexture();
                    zombieFactory.disposeTextures();
                    collectionScreen.disposeTextures();
                    disposeTextures();
                    myGame.setScreen((new StartScreen(myGame)));
                    myGame.resetBatch();
                }else if(MathTools.pointInRect(getSpriteRect(optionSprites.get(3)), touch) &&
                        Gdx.input.justTouched() ) {
                    myGame.mute = !myGame.mute;
                }
                }
            }
        }

    @Override
    public void render(float delta) {
        //Clearing the screen with black
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        if(currentScreen.equals("Game") || currentScreen.equals("Options")) {
            //Rendering:

            //Rendering the map
            mapRenderer.render();

            myGame.batch.setProjectionMatrix(cam.combined);
            myGame.batch.begin();
            for (Zombie zombie : zombies) {
                if(zombie.getTexture() != null) {
                    zombie.draw(myGame.batch);
                    zombie.healthBar.draw(myGame.batch);
                }
            }
            player.draw(myGame.batch);
            hud.draw(myGame.batch, delta);
            renderNoneGamePlay();
            myGame.batch.end();

            // Update logic:
            // Placed here to avoid batch projection after changing screen to Shop.
            update(delta);
        }else if(currentScreen.equals("Shop")){
            shopScreen.render(delta, myGame.batch);
        } else if(currentScreen.equals("Collection")){
            collectionScreen.render(myGame.batch);
        }

    }

    private void renderNoneGamePlay() {
        if(currentScreen.equals("Options") && !transitioning){
            blackScreen.draw(myGame.batch, .7f);
            for (int i = 0; i < optionSprites.size(); i++) {
                if(myGame.mute && i != 4){
                    if(optionSprites.get(i).getTexture() != null){
                        optionSprites.get(i).draw(myGame.batch);
                    }
                }else if(!myGame.mute && i != 5){
                    if(optionSprites.get(i).getTexture() != null){
                        optionSprites.get(i).draw(myGame.batch);
                    }
                }
            }
        }
        if(transitioning){
            for(Sprite sprite: transitionSprites){
                if(sprite.getTexture() != null) {
                    sprite.draw(myGame.batch);
                }
            }
        }
        if(gameOver){
            Label.LabelStyle labelStyle = new com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle(hud.font, Color.WHITE);
            String text;
            if(mode.equals("Run")){
                text = "You  survived  for "+hud.runTimerLabel.getText();
            }else {
                text = "You  survived  for  "+ (level -1) +"  rounds";
            }
            Label label = new Label(text, labelStyle);
            if(mode.equals("Run")) {
                label.setFontScale(.25f);
            }else {
                label.setFontScale(.15f);
            }
            blackScreen.draw(myGame.batch, .7f);
            for(Sprite sprite: gameOverSprites){
                if(sprite.getTexture() != null) {
                    sprite.draw(myGame.batch);
                }
            }
            label.setPosition(cam.position.x - label.getFontScaleX()*label.getWidth()/2,
                    cam.position.y - label.getFontScaleY()*label.getHeight()/2);
            label.draw(myGame.batch, 1);
        }
    }

    @Override
    public void resize(int width, int height) {
        gameport.update(width, height);
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
        mapRenderer.dispose();
        b2dWorld.dispose();
        myGame.batch.dispose();
        player.disposeTexture();
        zombieFactory.disposeTextures();
        hud.disposeTextures();
        shopScreen.dispose();
        zombieNoise.dispose();
        if (player.isDead()) {
            dataHandler.clearFile();
        }
    }

    public void disposeTextures(){
        optionsAtlas.dispose();
        if(blackScreen != null){
            blackScreen.getTexture().dispose();
        }
        zombieNoise.dispose();
        if (player.isDead()) {
            dataHandler.clearFile();
        }
    }

    public ArrayList<Zombie> getZombies() {
        return zombies;
    }

    public void payPlayerForLevel(){
        player.earn(calcLevelPay());
    }

    public int calcLevelPay() {
        int n = 0;
        int copyLvl = level;
        while (copyLvl - 10 > 0){
            copyLvl -= 10;
            n += 1;
        }
        int base = (int) (500*(Math.pow(2, n)));
        return base + (level - 10*n -1)*(base/10);
    }


    public void zombiesaddAll(ArrayList<Zombie> zombiesClassic) {
        zombies.addAll(zombiesClassic);
    }
}
