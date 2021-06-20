package com.mygdx.game.AI;

import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.World;

public class Zombie3 extends Zombie {

    public Zombie3(Body body,float boundingRadius, World w) {
        super(body, boundingRadius, w);
        maxLinearSpeed = 20;
        maxAngularAcceleration = 30;
        maxLinearAcceleration = 5000;
        maxAngularSpeed = 300;
        damage = 15;
        reach = 3;
        health = 50;
        maxHealth = 50;
    }
}
