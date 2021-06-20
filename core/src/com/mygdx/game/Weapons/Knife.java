package com.mygdx.game.Weapons;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.Array;
import com.mygdx.game.Box2D_Bodies.Soldier;

public class Knife extends Weapon {
    private float attackAnimationDuration = 0.8f;
    public Knife() {
        super(40, 50, 0, new int[]{2000, 5000});
        Array<TextureRegion> frames;
        if (Soldier.sex.equals("M")) {
            shoot = new TextureAtlas("Man/Man_Attack_Knife1.txt");
            shoot2 = new TextureAtlas("Man/Man_Attack_Knife2.txt");
            walk = new TextureAtlas("Man/Man_Walk_Knife.txt");
            frames = new Array<TextureRegion>();
            for (int i = 0; i < 8; i++) {
                if (i < 6) {
                    frames.add(shoot.findRegion("Knife", i));
                } else {
                    frames.add(shoot2.findRegion("Knife", i));
                }
            }
            setAttacking(new Animation<>(0.15f, frames));
            setAttackingBound(new float[]{738 / 30f, 553 / 30f});
        } else {
            shoot = new TextureAtlas("Woman/Woman_Attack_Knife.txt");
            walk = new TextureAtlas("Woman/Woman_Walk_Knife.txt");
            frames = new Array<TextureRegion>();
            for (int i = 0; i < 8; i++) {
                frames.add(shoot.findRegion("Knife", i));
            }
            setAttacking(new Animation<>(0.15f, frames));
            setAttackingBound(new float[]{738 / 30f, 553 / 30f});
        }
        frames = new Array<TextureRegion>();
        for (int i = 0; i < 6; i++) {
            frames.add(walk.findRegion("Walk_knife", i));
        }
        setWalking(new Animation<>(0.15f, frames));
        setWalkingBound(new float[]{542 / 30f, 402 / 30f});
        setStand(walk.findRegion("Walk_knife", 0));
        setIcon(Weapon.guns.findRegion("Knife"));
    }

    @Override
    public void upgrade() {
        if(!isFullyUpgraded()) {
            if (level == 0) {
                damage = 60;
            }else {
                damage = 100;
            }
            level += 1;

        }
    }

    public float getAttackAnimationDuration() {
        return attackAnimationDuration;
    }

    @Override
    public boolean isOutAmmo() {
        return false;
    }

    @Override
    public void shoot(float dt) {
    }
}
