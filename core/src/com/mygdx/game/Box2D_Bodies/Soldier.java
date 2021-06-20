package com.mygdx.game.Box2D_Bodies;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;
import com.mygdx.game.AI.Zombie;
import com.mygdx.game.MathTools;
import com.mygdx.game.Screens.GameScreen;
import com.mygdx.game.Weapons.FlameThrower;
import com.mygdx.game.Weapons.HandGun;
import com.mygdx.game.Weapons.Knife;
import com.mygdx.game.Weapons.Riffle;
import com.mygdx.game.Weapons.Weapon;

import java.util.ArrayList;


public class Soldier extends Sprite {


    // Variables related to animation
    public enum State {WALKING, STANDING, ATTACKING, DYING}
    public static String sex;
    public State currentState;
    public State previousState;
    private float stateTimer;
    private TextureAtlas death;
    private Animation dying;
    private TextureRegion stand;
    private float nextRotation = 0;
    // Although the picture points down, the sprite is rotated 180 degrees when created in
    // GameScreen, thus the angle here is set to 90
    private float oldAngle;
    public static int xDirection = 1; // 1 if moving to the right or directly up, -1 if left or down.

    // Variables related to Box2d
    World world;
    public Body body;
    public static float PPM = 4f;

    // Game play related variables
    private float maxVelocity = 50;
    private int armor = 0;
    private float health;
    private int money;
    private Weapon currentGun;
    private float beenAttackingFor = 0;
    private boolean haveShotRecently = false;
    private boolean dead = false;
    private float beenDeadFor = 0;
    private Vector2 deathPlace;
    private ArrayList<Weapon> weapons = new ArrayList<>();
    // Current weapon index:
    private int cWI;
    private float lastShotHandGun = 10;
    private float lastAttckedKnife = 10;
    ArrayList<Zombie> zombies;
    public boolean needToDrawArrow = false;
    public float lastSeenZombie;

    public Soldier(World w, String sex){
        Soldier.sex = sex;
        money = 0;
        // Getting sprite
        if(sex.equals("M")) {
            death = new TextureAtlas("Man/Man_Death.txt");
        }else{
            death = new TextureAtlas("Woman/Woman_Death.txt");
        }
        currentGun = new HandGun();
        stand = currentGun.getStand();
        setRegion(stand);
        setBounds(0, 0, 35.4f/3, 58.6f/3);

        // Initializing animation variables
        currentState = State.STANDING;
        previousState = State.STANDING;
        stateTimer = 0;
        Array<TextureRegion> frames = new Array<TextureRegion>();
        if(sex.equals("M")) {
            for (int i = 0; i < 6; i++) {
                frames.add(death.findRegion("death_000" + i + "_Man"));
            }
        }else {
            for (int i = 6; i < 12; i++) {
                if(i < 10) {
                    frames.add(death.findRegion("death_000" + i + "_Girl"));
                }else {
                    frames.add(death.findRegion("death_00" + i + "_Girl"));
                }
            }
        }
        dying = new Animation(0.1f, frames);

        world = w;
        defineSoldier();
        setOriginCenter();
        float centerX = getOriginX();
        float centerY = getOriginY();
        setOrigin(centerX, centerY - 2.5f);

        health = 100;
        oldAngle = body.getAngle();
        cWI = 0;
        weapons.add(currentGun);
        weapons.add(new Knife());
    }

    public void update(float dt){
        if (isDead()){
            beenDeadFor += dt;
        }
        setRegion(getFrame(dt));
        flip(false, true);
        setOriginCenter();
        if(currentGun instanceof Knife && getState() == State.ATTACKING) {
            setOrigin(GameScreen.playerRadius + 8f, GameScreen.playerRadius);
        }else if(currentGun instanceof Knife){
            setOrigin(GameScreen.playerRadius + 2.5f , GameScreen.playerRadius);
        }else if(getState() == State.ATTACKING) {
            setOrigin(GameScreen.playerRadius , GameScreen.playerRadius);
        }else {
            setOrigin(GameScreen.playerRadius - 1.5f, GameScreen.playerRadius);
        }
        if(!isDead()) {
            updateLastSeen(dt);
            lastShotHandGun += dt;
            lastAttckedKnife += dt;
            if(currentGun instanceof Knife && getState() == State.ATTACKING){
                setPosition(body.getPosition().x - GameScreen.playerRadius - 8,
                        body.getPosition().y - GameScreen.playerRadius);
            }else if(currentGun instanceof Knife) {
                setPosition(body.getPosition().x - GameScreen.playerRadius - 2.5f,
                        body.getPosition().y - GameScreen.playerRadius);
            } else if(getState() == State.ATTACKING) {
                setPosition(body.getPosition().x - GameScreen.playerRadius,
                        body.getPosition().y - GameScreen.playerRadius);
            }else {
                setPosition(body.getPosition().x - GameScreen.playerRadius + 1.5f,
                        body.getPosition().y - GameScreen.playerRadius);
            }
            rotate((float) Math.toDegrees(body.getAngle() - oldAngle));
            oldAngle = body.getAngle();
        } else {
            setPosition(deathPlace.x, deathPlace.y);
        }
    }

    private void updateLastSeen(float dt) {
        if(zombiesNearby()){
            lastSeenZombie = 0;
        }else {
            lastSeenZombie += dt;
        }
        needToDrawArrow = lastSeenZombie > 10;
    }

    public boolean zombiesNearby() {
        boolean zombiesNearby = false;
        for(Zombie zombie:zombies){
            if(MathTools.subtractVector2D(zombie.getPosition(), body.getPosition()).len() < 100 &&
                    !zombie.isDead()){
                zombiesNearby = true;
            }
        }
        return zombiesNearby;
    }

    public void arrowDrawn(){
        lastSeenZombie = 0;
    }

    private TextureRegion getFrame(float dt) {
        currentState = getState();
        TextureRegion region = null;
        switch (currentState){
            case WALKING:
                region = (TextureRegion) currentGun.getWalkingM().getKeyFrame(stateTimer, true);
                setBounds(0, 0, currentGun.getWalkingBound()[0], currentGun.getWalkingBound()[1]);
                break;
            case STANDING:
                region = currentGun.getStand();
                setBounds(0, 0, currentGun.getWalkingBound()[0], currentGun.getWalkingBound()[1]);
                break;
            case DYING:
                region = (TextureRegion) dying.getKeyFrame(stateTimer, false);
                if(stateTimer <= 0.3) {
                    setBounds(0, 0,  33.2f / 3, 37.8f / 3);
                }else {
                    setBounds(0, 0, 54.2f/2, 23.1f/2);
                }
                break;
            case ATTACKING:
                region = (TextureRegion) currentGun.getAttackingM().getKeyFrame(stateTimer, true);
                setBounds(0, 0, currentGun.getAttackingBoundM()[0], currentGun.getAttackingBoundM()[1]);
                beenAttackingFor += dt;
                break;
            default:
                throw new IllegalStateException("Unexpected value: " + currentState);
        }
        stateTimer = currentState == previousState? stateTimer + dt: 0;
        previousState = currentState;
        return region;
    }

    private State getState() {
        if (isDead()) {
            return State.DYING;
        }
        if(beenAttackingFor <= 0.5 && haveShotRecently){
            return State.ATTACKING;
        }else{
            beenAttackingFor = 0;
            haveShotRecently = false;
        }
        if(body.getLinearVelocity().x != 0 || body.getLinearVelocity().y != 0){
            return State.WALKING;
        } else {
            return State.STANDING;
        }
    }

    private void defineSoldier() {
        BodyDef bodyDef = new BodyDef();
        bodyDef.position.set(50, 100);
        bodyDef.type = BodyDef.BodyType.DynamicBody;
        body = world.createBody(bodyDef);

        FixtureDef fixtdef = new FixtureDef();
        CircleShape shape = new CircleShape();
        shape.setRadius(GameScreen.playerRadius);
        fixtdef.shape = shape;
        body.createFixture(fixtdef);
    }

    public void shoot(float dt) {
        // Making sure the player can't rapid fire the hand gun,
        // and has to wait for the animation for be before shooting the handgun again.
        HandGun gun = (HandGun) getWeapon(HandGun.class);
        Knife knife = (Knife) getWeapon(Knife.class);
        boolean handGunAndCantShoot = currentGun instanceof HandGun && lastShotHandGun < gun.getAttackAnimationDuration();
        boolean knifeCantAttack = currentGun instanceof Knife && lastAttckedKnife < knife.getAttackAnimationDuration();
        if(!isDead() && !currentGun.isOutAmmo() && !handGunAndCantShoot && !knifeCantAttack) {
            beenAttackingFor = 0;
            haveShotRecently = true;
            currentGun.shoot(dt);
            if(currentGun instanceof  HandGun){
                lastShotHandGun = 0;
            }else if(currentGun instanceof Knife){
                lastAttckedKnife = 0;
            }
            // Line segment from player's i the direction of facing.
            Vector2[] lineSeg = new Vector2[]{body.getPosition(),
                    new Vector2((float) Math.cos(body.getAngle() + Math.PI / 2) + body.getPosition().x,
                            (float) Math.sin(body.getAngle() + Math.PI / 2) + body.getPosition().y)};
            // Line segment from the barrel to as far as the weapon can reach
            Vector2 faceDirection = MathTools.subtractVector2D(lineSeg[1], lineSeg[0]);
            Vector2 barrelPoint = MathTools.addVector2D(body.getPosition(), MathTools.scalarMultiplication(currentGun.getOffset(),
                    MathTools.unitVector2D(MathTools.getPerpendicularVectors2D(faceDirection)[1])));
            lineSeg = new Vector2[]{body.getPosition(),
                    new Vector2((float) Math.cos(body.getAngle() + Math.PI / 2)* currentGun.getRange() + barrelPoint.x,
                            (float) Math.sin(body.getAngle() + Math.PI / 2)* currentGun.getRange() + barrelPoint.y)};

            ArrayList<Zombie> zombiesInWay = new ArrayList<>();
            for (Zombie zombie : zombies) {
                if(!zombie.isDead()) {
                    if(currentGun instanceof Knife){
                        float reach = (zombie.getBoundingRadius() + GameScreen.playerRadius + 5);
                        lineSeg = new Vector2[]{body.getPosition(),
                                new Vector2((float) Math.cos(body.getAngle() + Math.PI / 2) * reach + body.getPosition().x,
                                        (float) Math.sin(body.getAngle() + Math.PI / 2) * reach + body.getPosition().y)};
                        if (MathTools.intersects(lineSeg,
                                MathTools.perpendicularDiametricalPoints(body.getPosition(),
                                        zombie.getPosition(), 2*zombie.getBoundingRadius()))) {
                            zombiesInWay.add(zombie);
                        }
                    }else {
                        if (MathTools.intersects(lineSeg,
                                MathTools.perpendicularDiametricalPoints(body.getPosition(),
                                        zombie.getPosition(), zombie.getBoundingRadius() + 5))) {
                            zombiesInWay.add(zombie);
                        }
                    }
                }
            }
            if (!(currentGun instanceof FlameThrower) && zombiesInWay.size() != 0) {
                Vector2 distance;
                Zombie currentZombie = zombiesInWay.get(0);
                for (Zombie zombie : zombiesInWay) {
                    if (MathTools.subtractVector2D(body.getPosition(), zombie.getPosition()).len() >
                            MathTools.subtractVector2D(body.getPosition(), currentZombie.getPosition()).len()) {
                        currentZombie = zombie;
                    }
                }
                currentZombie.shotAt(dt);
            } else if (currentGun instanceof FlameThrower && zombiesInWay.size() != 0) {
                for (Zombie zombie : zombiesInWay) {
                    zombie.shotAt(dt);
                }
            }
        }
    }

    public void damage(float amount) {
        health -=  (amount*100)/(100 + armor);
        if(health < 0){
            health = 0;
            die();
        }
    }

    private void die() {
        dead = true;
        deathPlace = new Vector2(body.getPosition());
        world.destroyBody(body);
    }

    public void switchWeaponL() {
        if(weapons.size() >= 3){
            cWI -= 1;
            if(cWI < 0){
                cWI = weapons.size() - 1;
            }
        }
        currentGun = weapons.get(cWI);
    }

    public void switchWeaponR() {
        if(weapons.size() >= 2){
            cWI += 1;
            if(cWI >= weapons.size()){
                cWI = 0;
            }
        }
        currentGun = weapons.get(cWI);
    }

    public Sprite[] getGunSlot(){
        int lWI = cWI - 1;
        if(lWI < 0){
            lWI = weapons.size() -1;
        }
        int rWI = cWI + 1;
        if(rWI > weapons.size() -1){
            rWI = 0;
        }
        if(weapons.size() == 0){
            return new Sprite[]{null, null, null};
        }else if(weapons.size() == 1){
            return new Sprite[]{null, new Sprite(currentGun.getIcon()), null};
        }else if(weapons.size() == 2){
            return new Sprite[]{null, new Sprite(currentGun.getIcon()),
                    new Sprite(weapons.get(rWI).getIcon())};
        }
        return new Sprite[]{new Sprite(weapons.get(lWI).getIcon()),
                new Sprite(currentGun.getIcon()), new Sprite(weapons.get(rWI).getIcon())};
    }

    public void earn(int amount){
        if(amount > 0){
            money += amount;
        }
    }

    public void pay(int amount){
        if(canAfford(amount)){
            money -= amount;
        }
    }

    public boolean canAfford(int amount) {
        return amount > 0 && amount <= money;
    }

    public int getHealth(){ return (int)health;}

    public Weapon getCurrentGun() { return currentGun;}

    public float getmaxVelocity() {return maxVelocity;}

    public boolean isDead() { return dead;}

    public boolean hasWeapon(Class weapon) {
        for(Weapon wpn : weapons){
            if (wpn.getClass() == weapon){
                return true;
            }
        }
        return false;
    }

    public Weapon getWeapon(Class weapon) {
        if(hasWeapon(weapon)){
            for(Weapon wpn : weapons){
                if (wpn.getClass() == weapon){
                    return wpn;
                }
            }
        }
        return null;
    }

    public void addWeapon(Weapon weapon, int level){
        if(!hasWeapon(weapon.getClass())){
            for(int i = 0; i<level; i++){
                weapon.upgrade();
            }
            weapons.add(weapon);
        }
    }

    public void upgradeWeapon(Class weapon){
        if(hasWeapon(weapon.getClass())){
            getWeapon(weapon).upgrade();
        }
    }

    public void addAmmo(Class weapon, int amount){
        if(hasWeapon(weapon.getClass())){
            getWeapon(weapon).addAmmo(amount);
        }
    }

    public void heal() {
        health = 100;
    }

    public int getMoney() {
        return money;
    }

    public void setHealth(int health) {
        this.health = health;
    }

    public void upgradeArmor(){
        if(armor < 100){
            pay(getNextArmorPrice());
            armor += 20;
        }
    }

    public boolean armorFullyUpgraded(){
        return armor == 100;
    }

    public int getNextArmorPrice(){
        return (armor + 20)*30/2;
    }

    public int getArmor(){
        return armor;
    }

    public void setZombies(ArrayList<Zombie> zombies) {
        this.zombies = zombies;
    }

    public String  getSex() {
        return sex;
    }

    public void disposeTexture() {
        stand.getTexture().dispose();
        for(Weapon weapon:weapons){
            weapon.disposeTextures();
        }
        death.dispose();
    }

    public void setMaxVelocity(float maxVelocity) {
        this.maxVelocity = maxVelocity;
    }

    public float getBeenDeadFor(){ return beenDeadFor;}

}

