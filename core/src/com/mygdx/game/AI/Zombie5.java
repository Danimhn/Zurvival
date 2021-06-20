package com.mygdx.game.AI;

import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.World;

public class Zombie5 extends Zombie {

    public Zombie5(Body body,float boundingRadius, World w) {
        super(body, boundingRadius, w);
        maxLinearSpeed = 14;
        maxAngularAcceleration = 30;
        maxLinearAcceleration = 5000;
        maxAngularSpeed = 300;
        damage = 25;
        reach = 3;
        health = 100;
        maxHealth = 100;
    }
}
