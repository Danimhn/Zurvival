package com.mygdx.game.Weapons;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.Array;
import com.mygdx.game.Box2D_Bodies.Soldier;

import java.util.ArrayList;

public class HandGun extends Weapon {

    private float attackAnimationDuration = 0.5f;


    public HandGun() {

        super(21, 400, 30, new int[]{500, 1000, 1500, 2000, 2500});
        if(Soldier.sex.equals("M")) {
            shoot = new TextureAtlas("Man/Man_Shoot_Gun.txt");
            walk = new TextureAtlas("Man/Man_Walk_Gun.txt");
            setOffset(3.2F);
        }else {
            shoot = new TextureAtlas("Woman/Woman_Shoot_Gun.txt");
            walk = new TextureAtlas("Woman/Woman_Walk_Gun.txt");
            setOffset(3.1F);
        }
        Array<TextureRegion> frames = new Array<TextureRegion>();
        for (int i = 0; i < 5; i++){
            frames.add(shoot.findRegion("Gun_Shot", i));
        }
        setAttacking(new Animation<>(attackAnimationDuration/5f, frames));
        setAttackingBound(new float[]{368/30f, 638/30f});
        frames = new Array<TextureRegion>();
        for (int i = 0; i < 6; i++){
            frames.add(walk.findRegion("Walk_gun", i));
        }
        setWalking(new Animation<>(0.15f, frames));
        setWalkingBound(new float[]{332/30f, 413/30f});
        setStand(walk.findRegion("Walk_gun", 0));
        setIcon(Weapon.guns.findRegion("HandGun"));
    }

    public void setAmmo(int amount){
        if(amount >= 0){
            ammo = amount;
        }
    }

    @Override
    public void upgrade() {
        if(!isFullyUpgraded()) {
            damage += 5;
            level += 1;
        }
    }

    public float getAttackAnimationDuration() {
        return attackAnimationDuration;
    }
}
