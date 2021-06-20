package com.mygdx.game.AI;

import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.World;
import com.mygdx.game.Box2D_Bodies.Soldier;

public class Zombie10 extends Zombie {

        public Zombie10(Body body,float boundingRadius, World w) {
                super(body, boundingRadius, w);
                maxLinearSpeed = 12;
                maxAngularAcceleration = 30;
                maxLinearAcceleration = 5000;
                maxAngularSpeed = 300;
                damage = 50;
                reach = 3;
                health = 1000;
                maxHealth = 1000;
        }
}
