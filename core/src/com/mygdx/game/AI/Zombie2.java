package com.mygdx.game.AI;

import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.World;

public class Zombie2 extends Zombie {

    public Zombie2(Body body,float boundingRadius, World w) {
        super(body, boundingRadius, w);
        maxLinearSpeed = 12;
        maxAngularAcceleration = 30;
        maxLinearAcceleration = 5000;
        maxAngularSpeed = 300;
        damage = 20;
        reach = 3;
        health = 70;
        maxHealth = 70;
    }
}
