package com.mygdx.game.AI;

import com.badlogic.gdx.ai.steer.Steerable;
import com.badlogic.gdx.ai.steer.SteeringAcceleration;
import com.badlogic.gdx.ai.steer.SteeringBehavior;
import com.badlogic.gdx.ai.utils.Location;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;
import com.mygdx.game.Box2D_Bodies.B2dStaticRectangleObjectGenerator;
import com.mygdx.game.Box2D_Bodies.Soldier;
import com.mygdx.game.MathTools;
import com.mygdx.game.MyGdxGame;
import com.mygdx.game.Screens.GameScreen;

import java.awt.geom.Line2D;
import java.util.ArrayList;
import java.util.Random;

public class Zombie extends Sprite implements Steerable<Vector2> {
    Body body;

    //Animation related variables
    public Soldier.State currentState;
    public Soldier.State previousState;
    private float stateTimer;
    private Animation walking;
    private Animation attacking;
    private Animation dying;


    private TextureRegion stand;
    public Sprite healthBar;
    private float oldAngle;
    private float[] walkingBounds;
    private float[] attackingBounds;
    private float[] dyingBounds;

    public void setWalkingBounds(float[] walkingBounds) {
        this.walkingBounds = walkingBounds;
    }

    //Steering related variables
    private boolean tagged;
    private float boundingRadius;
    protected float maxLinearSpeed, maxLinearAcceleration;
    protected float maxAngularSpeed, maxAngularAcceleration;
    private ArrayList<SteeringBehavior<Vector2>> behaviors = new ArrayList<>();
    private SteeringAcceleration<Vector2> outputAcceleration;
    private boolean isChasing = false;
    private float lastChased = 1;
    private int fieldOfView = 350;
    private float angleOfView = (float) (Math.PI/3);

    World world;

    // Game play related variables
    protected int damage;
    protected int reach;
    protected int health;
    protected int maxHealth;
    private boolean dead = false;
    private float deadFor = 0;
    private boolean deadFor5 = false;
    private Vector2 deathPlace;

    public Zombie(Body body, float boundingRadius, World w){
        // Initializing animation variables
        healthBar = new Sprite(MyGdxGame.uiAtlas.findRegion("Health_Bar"));
        healthBar.setBounds(0, 0, boundingRadius*2, boundingRadius/3);
        currentState = Soldier.State.STANDING;
        previousState = Soldier.State.STANDING;
        stateTimer = 0;

        world = w;

        this.body = body;
        this.boundingRadius =  boundingRadius;
        oldAngle = getOrientation();

        tagged = false;

        outputAcceleration = new SteeringAcceleration<Vector2>(new Vector2());
        this.body.setUserData(this);

        maxLinearSpeed = 35;
        maxAngularAcceleration = 30;
        maxLinearAcceleration = 5000;
        maxAngularSpeed = 300;
        health =100;
        maxHealth = 100;
    }

    private TextureRegion getFrame(float dt) {
        currentState = getState();
        TextureRegion region = null;
        switch (currentState){
            case WALKING:
                region = (TextureRegion) walking.getKeyFrame(stateTimer, true);
                setBounds(0, 0, walkingBounds[0], walkingBounds[1]);
                break;
            case STANDING:
                region = stand;
                setBounds(0, 0, walkingBounds[0], walkingBounds[1]);
                break;
            case DYING:
                region = (TextureRegion) dying.getKeyFrame(stateTimer, false);
                setBounds(0, 0, dyingBounds[0], dyingBounds[1]);
                break;
            case ATTACKING:
                region = (TextureRegion) attacking.getKeyFrame(stateTimer, true);
                setBounds(0, 0, attackingBounds[0], attackingBounds[1]);
                break;
            default:
                throw new IllegalStateException("Unexpected value: " + currentState);
        }
        stateTimer = currentState == previousState? stateTimer + dt: 0;
        previousState = currentState;
        return region;
    }

    protected Soldier.State getState() {
        if(dead){
            return Soldier.State.DYING;
        }
        if(attacking()){
            return Soldier.State.ATTACKING;
        }
        if(body.getLinearVelocity().x != 0 || body.getLinearVelocity().y != 0){
            return Soldier.State.WALKING;
        } else {
            return Soldier.State.STANDING;
        }
    }

    public boolean isDead() {
        return dead;
    }

    public void update (float delta){
        if(!dead) {
            if(chasing()){
                lastChased = 0;
            }
            isChasing = lastChased < .2f;
            lastChased += delta;
            if (!isChasing) {
                behaviors.get(0).calculateSteering(outputAcceleration);
                applySteering(delta, false);
                applySteering(delta, true);
            } else if (attacking()) {
                body.setLinearVelocity(0, 0);
            }else {
                behaviors.get(1).calculateSteering(outputAcceleration);
                applySteering(delta, false);
            }
            if (attacking()) {
                GameScreen.player.damage(damage * delta);
            }
        }
        setRegion(getFrame(delta));
        flip(false, true);
        setOriginCenter();
        healthBar.setBounds(0, 0, (boundingRadius*2)*(getHealth()/maxHealth),boundingRadius/3);
        if(!dead) {
            setCenter(body.getPosition().x,
                    body.getPosition().y);

            healthBar.setCenter(body.getPosition().x,
                    body.getPosition().y - boundingRadius - 5);
            healthBar.setOrigin(healthBar.getWidth()/2, healthBar.getHeight() + boundingRadius + 5);
            if (Math.abs(oldAngle - getOrientation()) > 0.5) {
                rotate((float) Math.toDegrees(getOrientation() - oldAngle));
                healthBar.rotate((float) Math.toDegrees(getOrientation() - oldAngle));
                oldAngle = getOrientation();
            }
        }else {
            setCenter(deathPlace.x, deathPlace.y);
            deadFor += delta;
            if(deadFor > 5){
                deadFor5 = true;
            }
        }
    }

    private float getHealth() { return health;}

    private boolean attacking() {
        Vector2 distance = new Vector2(getPosition().x - GameScreen.player.body.getPosition().x,
                getPosition().y - GameScreen.player.body.getPosition().y);
        if(distance.len() < (GameScreen.playerRadius + boundingRadius + reach) && chasing()){
            return true;
        }
        return false;
    }

    private boolean chasing() {
        if(GameScreen.player.isDead()){
            return false;
        }
        float theta1 = (float) (getOrientation() + Math.PI/2 + angleOfView);
        float theta2 = (float) (getOrientation() + Math.PI/2 - angleOfView);
        float x1 = (float)(fieldOfView*Math.cos(theta1)) + getPosition().x;
        float y1 = (float)(fieldOfView*Math.sin(theta1)) + getPosition().y;
        float x2 = (float)(fieldOfView*Math.cos(theta2)) + getPosition().x;
        float y2 = (float)(fieldOfView*Math.sin(theta2)) + getPosition().y;
            if(MathTools.isInTriangle(body.getPosition(),
                    new Vector2(x1, y1), new Vector2(x2, y2),
                    GameScreen.player.body.getPosition()) &&
                    !isTargetCovered(body.getPosition(), GameScreen.player.body.getPosition(),
                            GameScreen.playerRadius)){
                return true;
        }
        return false;
    }

    private boolean isTargetCovered(Vector2 origin, Vector2 target, float targetRadius) {
        for(Vector2[] rect: B2dStaticRectangleObjectGenerator.rectangles){
            if(covers(rect, origin, target, targetRadius)){
                return true;
            }
        }
        return false;
    }

    // Checks to see if the player is covered by objects from the Zombie's field of view
    private boolean covers(Vector2[] rect, Vector2 origin, Vector2 target , float targetRadius) {
        Vector2[] lineSegToPlayer = new Vector2[]{origin,
                target}; // Represents the line segment from the zombie to the player's center

        // Represents the line segments from the zombie to one of the player's sides
        Vector2[] lineSegToPlayer2 = new Vector2[]{origin,
                MathTools.perpendicularDiametricalPoints(origin,
                        target, targetRadius)[0]};
        Vector2[] lineSegToPlayer3 = new Vector2[]{origin,
                MathTools.perpendicularDiametricalPoints(origin,
                        target, targetRadius)[1]};

        //Checking to see if any of the rectangles side intersect with the above line segments
        Vector2 point1;
        Vector2 point2;
        point1 = new Vector2(rect[0].x, rect[0].y);
        point2 = new Vector2(rect[1].x, rect[1].y);
        Vector2[] side1 = new Vector2[] {point1, point2};
        point1 = new Vector2(rect[2].x, rect[2].y);
        point2 = new Vector2(rect[3].x, rect[3].y);
        Vector2[] side2 = new Vector2[] {point1, point2};
        point1 = new Vector2(rect[2].x, rect[2].y);
        point2 = new Vector2(rect[0].x, rect[0].y);
        Vector2[] side3 = new Vector2[] {point1, point2};
        point1 = new Vector2(rect[3].x, rect[3].y);
        point2 = new Vector2(rect[1].x, rect[1].y);
        Vector2[] side4 = new Vector2[] {point1, point2};
        if(MathTools.intersects(lineSegToPlayer , side1) || MathTools.intersects(lineSegToPlayer , side2)
                || MathTools.intersects(lineSegToPlayer , side3) || MathTools.intersects(lineSegToPlayer , side4)){
            return true;
        }
        if(MathTools.intersects(lineSegToPlayer2 , side1) || MathTools.intersects(lineSegToPlayer2 , side2)
                || MathTools.intersects(lineSegToPlayer2 , side3) || MathTools.intersects(lineSegToPlayer2 , side4)){
            return true;
        }
        if(MathTools.intersects(lineSegToPlayer3 , side1) || MathTools.intersects(lineSegToPlayer3 , side2)
                || MathTools.intersects(lineSegToPlayer3 , side3) || MathTools.intersects(lineSegToPlayer3 , side4)){
            return true;
        }
        return false;
    }

    private void applySteering(float delta, boolean wander) {
        Vector2 linVelocity;
        if(wander){
            linVelocity = calcWanderVelocity();
        }else {
            linVelocity = body.getLinearVelocity().mulAdd(outputAcceleration.linear, delta).limit(this.getMaxLinearSpeed());
        }
        float newOrientation = vectorToAngle(linVelocity);
        body.setTransform(body.getPosition(), newOrientation);
        this.body.setLinearVelocity(linVelocity);
    }

    private Vector2 calcWanderVelocity() {
        Random rd = new Random();
        double theta;
        float x;
        float y;
        if(body.getLinearVelocity().len() < maxLinearSpeed/5){
            theta = (1.01 * rd.nextFloat() * Math.PI/2);
            x = (float) (maxLinearSpeed * Math.cos(theta));
            y = (float) (maxLinearSpeed * Math.sin(theta));
        } else{
            theta = (getOrientation()+ (1.01 * rd.nextFloat() * Math.PI));
            float scale = rd.nextInt(5) + 1;
            x = (float) (maxLinearSpeed * Math.cos(theta) * scale/50) + body.getLinearVelocity().x;
            y = (float) (maxLinearSpeed * Math.sin(theta) * scale/50) + body.getLinearVelocity().y;
        }
        return new Vector2(x, y).limit(maxLinearSpeed);
    }

    @Override
    public Vector2 getLinearVelocity() {
        return body.getLinearVelocity();
    }

    @Override
    public float getAngularVelocity() {
        return body.getAngularVelocity();
    }

    @Override
    public float getBoundingRadius() {
        return boundingRadius;
    }

    @Override
    public boolean isTagged() {
        return tagged;
    }

    @Override
    public void setTagged(boolean tagged) {
        this.tagged = tagged;
    }

    @Override
    public float getZeroLinearSpeedThreshold() {
        return 0;
    }

    @Override
    public void setZeroLinearSpeedThreshold(float value) {

    }

    @Override
    public float getMaxLinearSpeed() {
        return maxLinearSpeed;
    }

    @Override
    public void setMaxLinearSpeed(float maxLinearSpeed) {
        this.maxLinearSpeed = maxLinearSpeed;
    }

    @Override
    public float getMaxLinearAcceleration() {
        return maxLinearAcceleration;
    }

    @Override
    public void setMaxLinearAcceleration(float maxLinearAcceleration) {
        this.maxLinearAcceleration = maxLinearAcceleration;
    }

    @Override
    public float getMaxAngularSpeed() {
        return maxAngularSpeed;
    }

    @Override
    public void setMaxAngularSpeed(float maxAngularSpeed) {
        this.maxAngularSpeed = maxAngularSpeed;
    }

    @Override
    public float getMaxAngularAcceleration() {
        return maxAngularAcceleration;
    }

    @Override
    public void setMaxAngularAcceleration(float maxAngularAcceleration) {
        this.maxAngularAcceleration = maxAngularAcceleration;
    }

    @Override
    public Vector2 getPosition() {
        return body.getPosition();
    }

    @Override
    public float getOrientation() {
        if(body == null){
        }
        return body.getAngle();
    }

    @Override
    public void setOrientation(float orientation) {
    }

    @Override
    public float vectorToAngle(Vector2 vector) {
        return (float)Math.atan2(-vector.x, vector.y);
    }

    @Override
    public Vector2 angleToVector(Vector2 outVector, float angle) {
        outVector.x = - (float)Math.sin(angle);
        outVector.y = (float)Math.cos(angle);
        return outVector;
    }

    @Override
    public Location newLocation() {
        return null;
    }

    public void addBehavior(SteeringBehavior<Vector2> behavior){
        behaviors.add(behavior);
    }

    public Body getBody(){
        return body;
    }

    public void shotAt(float dt) {
        if(!isTargetCovered(GameScreen.player.body.getPosition(), body.getPosition(), boundingRadius)){
            damage(GameScreen.player.getCurrentGun().getDamage(dt));
            if(!attacking()) {
                behaviors.get(1).calculateSteering(outputAcceleration);
                applySteering(dt, false);
            }
        }
    }

    public void damage(float amount) {
        health -=  amount;
        if(health <= 0){
            health = 0;
            die();
        }
    }

    private void die() {
        dead = true;
        deathPlace = new Vector2(body.getPosition());
        world.destroyBody(body);
    }

    public void setAttackingBounds(float[] attackingBounds) {
        this.attackingBounds = attackingBounds;
    }

    public void setWalking(Animation walking) {
        this.walking = walking;
    }

    public void setAttacking(Animation attacking) {
        this.attacking = attacking;
    }

    public void setStand(TextureRegion stand) {
        this.stand = stand;
    }

    public boolean isdeadFor5() { return deadFor5;}

    public boolean isDeadFor2(){ return deadFor > 2;}

    public void setDying(Animation dying) { this.dying = dying;}

    public void setDyingBounds(float[] floats) {
        dyingBounds = floats;
    }
}
