package com.mygdx.game.Weapons;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.Array;
import com.mygdx.game.Box2D_Bodies.Soldier;

public class Riffle extends Weapon {

    public static int price = 1000;
    private float automationTimer;

    public Riffle(int ammo) {
        super(13, 400, ammo, new int[]{1500, 2000, 2500, 3000, 3500});
        Array<TextureRegion> frames;
        if(Soldier.sex.equals("M")) {
            shoot = new TextureAtlas("Man/Man_Shoot_riffle1.txt");
            shoot2 = new TextureAtlas("Man/Man_Shoot_riffle2.txt");
            walk = new TextureAtlas("Man/Man_walk_riffle.txt");
            setOffset(2.57f);
            frames = new Array<>();
            for (int i = 0; i < 9; i++) {
                if (i < 5) {
                    frames.add(shoot.findRegion("Riffle", i));
                } else {
                    frames.add(shoot2.findRegion("Riffle", i));
                }
            }
            setAttacking(new Animation<>(0.15f, frames));
        }else {
            frames = new Array<TextureRegion>();
            shoot = new TextureAtlas("Woman/Woman_Shoot_riffle.txt");
            walk = new TextureAtlas("Woman/Woman_Walk_riffle.txt");
            setOffset(3.02f);
            for (int i = 0; i < 9; i++) {
                frames.add(shoot.findRegion("Riffle", i));
            }
            setAttacking(new Animation<>(0.15f, frames));
        }
        setAttackingBound(new float[]{393/30f, 1082/30f});
        frames = new Array<TextureRegion>();
        for (int i = 0; i < 6; i++){
            frames.add(walk.findRegion("Walk_riffle", i));
        }
        setWalking(new Animation<>(0.15f, frames));
        setWalkingBound(new float[]{354/30f, 586/30f});
        setStand(walk.findRegion("Walk_riffle", 0));
        setIcon(Weapon.guns.findRegion("Riffle"));
    }

    @Override
    public void upgrade() {
        if(!isFullyUpgraded()) {
            damage += 3;
            level += 1;
        }
    }

    @Override
    public void shoot(float dt) {
        if (ammo > 0) {
            if (dt == -1){
                ammo -= 1;
            }else {
                ammo -= dt * 3;
            }
        }else {
            ammo = 0;
        }
    }

    @Override
    public float getDamage(float dt) {
        if(dt == -1 || automationTimer >= .33f){
            automationTimer = 0;
            return damage;
        }
        automationTimer += dt;
        return 0;
    }
}
