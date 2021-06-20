package com.mygdx.game.AI;

import com.badlogic.gdx.Screen;
import com.badlogic.gdx.ai.steer.behaviors.Arrive;
import com.badlogic.gdx.ai.steer.behaviors.RaycastObstacleAvoidance;
import com.badlogic.gdx.ai.steer.utils.rays.CentralRayWithWhiskersConfiguration;
import com.badlogic.gdx.ai.steer.utils.rays.ParallelSideRayConfiguration;
import com.badlogic.gdx.ai.steer.utils.rays.RayConfigurationBase;
import com.badlogic.gdx.ai.steer.utils.rays.SingleRayConfiguration;
import com.badlogic.gdx.ai.utils.RaycastCollisionDetector;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.MassData;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;
import com.mygdx.game.Box2D_Bodies.B2dStaticRectangleObjectGenerator;
import com.mygdx.game.Box2D_Bodies.Soldier;
import com.mygdx.game.Screens.GameScreen;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Random;

public class ZombieFactory {
    private Screen screen;
    private World world;
    private ArrayList<TextureAtlas> atlasesUsed = new ArrayList<>();
    private ArrayList<Zombie> runZombies = new ArrayList<>();

    public ZombieFactory(World b2dWorld, Screen sc) {
        world = b2dWorld;
        screen = sc;
    }

    public ArrayList<Zombie> createZombiesClassic(int level) {
        ArrayList<Zombie> zombies = new ArrayList<>();
        for(int i=0; i < getZombiesBasedOntLevel(level).length; i++) {
            for (int j = 0; j < getZombiesBasedOntLevel(level)[i]; j++) {
                Zombie zombie = createZombie(i + 1);
                addBehaviors(zombie);
                zombies.add(zombie);
            }
        }
        return zombies;
    }

    private void addBehaviors(Zombie zombie) {
        Zombie target = new Zombie(GameScreen.player.body, GameScreen.playerRadius, world);

        Arrive<Vector2> arrive = new Arrive<Vector2>(zombie, target);
        arrive.setTimeToTarget(0.01f);
        arrive.setArrivalTolerance(zombie.getBoundingRadius() + target.getBoundingRadius() + 1);
        arrive.setDecelerationRadius(zombie.getBoundingRadius() + target.getBoundingRadius() + 1);

        @SuppressWarnings("unchecked")

        RayConfigurationBase<Vector2>[] localRayConfigurations = new RayConfigurationBase[]{

                new SingleRayConfiguration<Vector2>(zombie, 100),

                new ParallelSideRayConfiguration<Vector2>(zombie, 100, zombie.getBoundingRadius()),

                new CentralRayWithWhiskersConfiguration<Vector2>(zombie,
                        2.4f*zombie.getBoundingRadius(), zombie.getBoundingRadius(),
                        35 * MathUtils.degreesToRadians)};

        RayConfigurationBase<Vector2>[] rayConfigurations = localRayConfigurations;

        int rayConfigurationIndex = 2;

        RaycastCollisionDetector<Vector2> raycastCollisionDetector = new Box2dRaycastCollisionDetector(world);

        RaycastObstacleAvoidance<Vector2> raycastObstacleAvoidance = new RaycastObstacleAvoidance<Vector2>(zombie,
                rayConfigurations[rayConfigurationIndex],

                raycastCollisionDetector, 40);
        zombie.addBehavior(raycastObstacleAvoidance);
        zombie.addBehavior(arrive);
    }

    private int[] getZombiesBasedOntLevel(int level) {
        int[] zombies = new int[10];
        if(level == 1) {
           zombies[0] = 5;
        }else if(level == 2){
            zombies[0] = 8;
        }else if(level == 3){
            zombies[0] = 7;
            zombies[1] = 3;
        }else if(level == 4){
            zombies[0] = 5;
            zombies[1] = 5;
        }else if(level == 5){
            zombies[0] = 5;
            zombies[1] = 7;
        }else if(level > 5 && level < 40){
            int baseLevel;
            if(level%5 == 0){
                baseLevel = level/5 - 2;
                zombies[baseLevel] = 3;
                zombies[baseLevel+1] = 3;
                zombies[baseLevel+2] = 9;
            }else {
                baseLevel = level/5 - 1;
                if(level%5 == 1){
                    zombies[baseLevel+1] = 7;
                    zombies[baseLevel+2] = 3;
                }else if(level%5 == 2){
                    zombies[baseLevel+1] = 5;
                    zombies[baseLevel+2] = 5;
                }else if(level%5 == 3){
                    zombies[baseLevel+1] = 5;
                    zombies[baseLevel+2] = 7;
                }else{
                    zombies[baseLevel] = 3;
                    zombies[baseLevel+1] = 5;
                    zombies[baseLevel+2] = 7;
                }
            }
        } else if(level > 39){
            zombies = getZombiesBasedOntLevel( level - 10);
            zombies[9] += 1;
        }
        return zombies;
    }

    private boolean inObjects(Vector2 vector) {
        for (Vector2[] rect : B2dStaticRectangleObjectGenerator.rectangles) {
            if (vector.x >= rect[3].x && vector.x <= rect[0].x &&
                    vector.y >= rect[3].y && vector.y <= rect[0].y) {
                return true;
            }
        }
        return false;
    }

    private Vector2 getRandomPosition() {
        Random rd = new Random();
        float x = rd.nextFloat()* GameScreen.game_World_Width/(2* Soldier.PPM);
        float y = rd.nextFloat()* GameScreen.game_World_Height/(2* Soldier.PPM);
        int quadrant = rd.nextInt(3);
        if (quadrant == 0){
            x += GameScreen.game_World_Width/(2* Soldier.PPM);
        }else if (quadrant == 1){
            x += GameScreen.game_World_Width/(2* Soldier.PPM);
            y += GameScreen.game_World_Height/(2* Soldier.PPM);
        }else {
            y += GameScreen.game_World_Height/(2* Soldier.PPM);
        }
        return new Vector2(x, y);
    }

    public Zombie createZombie(int type){
        BodyDef bodyDef = new BodyDef();
        Vector2 randomPosition = getRandomPosition();
        Animation[] animations = getAnimations(type);
        while(inObjects(randomPosition)){
            randomPosition = getRandomPosition();
        }
        bodyDef.position.set(randomPosition);
        bodyDef.type = BodyDef.BodyType.KinematicBody;
        Body body = world.createBody(bodyDef);
        FixtureDef fixtDef = new FixtureDef();
        CircleShape shape = new CircleShape();
        if(type == 10) {
            shape.setRadius(((TextureRegion)animations[0].getKeyFrame(0)).getRegionWidth()/80f);
        }else if(type == 2 || type == 4 || type > 6){
            shape.setRadius(((TextureRegion)animations[0].getKeyFrame(0)).getRegionWidth()/70f);
        }else {
            shape.setRadius(((TextureRegion) animations[0].getKeyFrame(0)).getRegionWidth() / 60f);
        }
        fixtDef.shape = shape;
        body.createFixture(fixtDef);

        Zombie zombie = null;
        if(type == 1) {
            zombie = new Zombie1(body,
                    ((TextureRegion)animations[0].getKeyFrame(0)).getRegionWidth()/60f, world);
        }else if(type == 2) {
            zombie = new Zombie2(body,
                    ((TextureRegion)animations[0].getKeyFrame(0)).getRegionWidth()/70f, world);
        }else if(type == 3) {
            zombie = new Zombie3(body,
                    ((TextureRegion)animations[0].getKeyFrame(0)).getRegionWidth()/60f, world);
        }else if(type == 4) {
            zombie = new Zombie4(body,
                    ((TextureRegion)animations[0].getKeyFrame(0)).getRegionWidth()/70f, world);
        }else if(type == 5) {
            zombie = new Zombie5(body,
                    ((TextureRegion)animations[0].getKeyFrame(0)).getRegionWidth()/60f, world);
        }else if(type == 6) {
            zombie = new Zombie6(body,
                    ((TextureRegion)animations[0].getKeyFrame(0)).getRegionWidth()/60f, world);
        }else if(type == 7) {
            zombie = new Zombie7(body,
                    ((TextureRegion)animations[0].getKeyFrame(0)).getRegionWidth()/70f, world);
        }else if(type == 8) {
            zombie = new Zombie8(body,
                    ((TextureRegion)animations[0].getKeyFrame(0)).getRegionWidth()/70f, world);
        }else if(type == 9) {
            zombie = new Zombie9(body,
                    ((TextureRegion)animations[0].getKeyFrame(0)).getRegionWidth()/70f, world);
        }else if(type == 10) {
            zombie = new Zombie10(body,
                    ((TextureRegion)animations[0].getKeyFrame(0)).getRegionWidth()/80f, world);
        }
        assert zombie != null;
        zombie.setStand((TextureRegion)animations[0].getKeyFrame(0));
        zombie.setWalking(animations[0]);
        zombie.setWalkingBounds(new float[]{((TextureRegion)animations[0].getKeyFrame(0)).getRegionWidth()/30f,
                ((TextureRegion)animations[0].getKeyFrame(0)).getRegionHeight()/30f});
        zombie.setAttacking(animations[1]);
        zombie.setAttackingBounds(new float[]{((TextureRegion)animations[1].getKeyFrame(0)).getRegionWidth()/30f,
                ((TextureRegion)animations[1].getKeyFrame(0)).getRegionHeight()/30f});
        zombie.setDying(animations[2]);
        zombie.setDyingBounds((new float[]{((TextureRegion)animations[2].getKeyFrame(0)).getRegionWidth()/30f,
                ((TextureRegion)animations[2].getKeyFrame(0)).getRegionHeight()/30f}));
        return zombie;
    }

    private Animation[] getAnimations(int type) {
        TextureAtlas zombieWalk = new TextureAtlas("Zombies/Zombie"+type+"/Zombie"+type+"_walk.txt");
        TextureAtlas zombieAttack = new TextureAtlas("Zombies/Zombie"+type+"/Zombie"+type+"_attack.txt");
        TextureAtlas zombieDeath = new TextureAtlas("Zombies/Zombie"+type+"/Zombie"+type+"_death.txt");
        atlasesUsed.add(zombieWalk);
        atlasesUsed.add(zombieAttack);
        atlasesUsed.add(zombieDeath);
        Animation[] animations = new Animation[4];
        Array<TextureRegion> frames = new Array<TextureRegion>();
        if(type > 0 && type < 8){
            //Getting walk animation
            for (int i = 0; i < 9; i++){
                frames.add(zombieWalk.findRegion("Walk", i));
            }
            animations[0] = new Animation<>(0.1f, frames);
            //Getting attack animation
            frames = new Array<TextureRegion>();
            for (int i = 0; i < 9; i++){
                frames.add(zombieAttack.findRegion("Attack", i));
            }
            animations[1] = new Animation<>(0.1f, frames);
            //Getting death animation
            frames = new Array<TextureRegion>();
            for (int i = 0; i < 6; i++){
                frames.add(zombieDeath.findRegion("Death", i));
            }
            animations[2] = new Animation<>(0.1f, frames);
        } else if(type < 11){
            //Getting walk animation
            for (int i = 0; i < 8; i++){
                frames.add(zombieWalk.findRegion("Walk", i));
            }
            animations[0] = new Animation<>(0.1f, frames);
            //Getting attack animation
            frames = new Array<TextureRegion>();
            if(type == 8){
                for (int i = 0; i < 14; i++){
                    frames.add(zombieAttack.findRegion("Attack", i));
                }
            } else if(type == 9){
                for (int i = 0; i < 8; i++){
                    frames.add(zombieAttack.findRegion("Attack", i));
                }
            } else {
                for (int i = 0; i < 16; i++){
                    frames.add(zombieAttack.findRegion("Attack", i));
                }
            }
            animations[1] = new Animation<>(0.1f, frames);
            //Getting death animation
            frames = new Array<TextureRegion>();
            if (type < 10){
                for (int i = 0; i < 10; i++){
                    frames.add(zombieDeath.findRegion("Death", i));
                }
            }else {
                for (int i = 0; i < 16; i++){
                    if(i != 1 && i != 5) {
                        frames.add(zombieDeath.findRegion("Death", i));
                    }
                }
            }
            animations[2] = new Animation<>(0.1f, frames);
        }
        return animations;
    }

    public void disposeTextures(){
        for (TextureAtlas atlas:atlasesUsed){
            atlas.dispose();
        }
    }

    public ArrayList<Zombie> createZombiesRun(float time) {
        ArrayList<Zombie> zombies = new ArrayList<>();
        Zombie zombie;
        if(time == 0){
            for(int i =0; i<10; i++){
                zombie = createZombie(3);
                addBehaviors(zombie);
                zombies.add(zombie);
            }
            setupRunZombies();
            return zombies;
        }
        else if(time <= 160){
            zombies.add(runZombies.get((int) (time - 10)/10));
            return zombies;
        }
        zombie = createZombie(3);
        addBehaviors(zombie);
        zombies.add(zombie);
        return zombies;
    }

    private void setupRunZombies() {
        Zombie zombie;
        for(int i = 0; i <15 ;i++){
            zombie = createZombie(3);
            addBehaviors(zombie);
            runZombies.add(zombie);
        }
    }
}
