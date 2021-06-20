package com.mygdx.game.Weapons;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.Array;
import com.mygdx.game.Box2D_Bodies.Soldier;

public class FlameThrower extends Weapon {
    public static int price = 3000;

    public FlameThrower(int ammo) {

        super(150, 100, ammo, new int[]{4000, 5000, 6000, 7000, 8000});
        Array<TextureRegion> frames;
        if(Soldier.sex.equals("M")) {
            shoot = new TextureAtlas("Man/Man_Shoot_Flamethrower1.txt");
            shoot2 = new TextureAtlas("Man/Man_Shoot_Flamethrower2.txt");
            walk = new TextureAtlas("Man/Man_Walk_Flamethrower.txt");
            frames = new Array<TextureRegion>();
            for (int i = 0; i < 9; i++) {
                if (i < 5) {
                    frames.add(shoot.findRegion("FlameThrower", i));
                } else {
                    frames.add(shoot2.findRegion("FlameThrower", i));
                }
            }
            setAttacking(new Animation<>(0.15f, frames));
            setAttackingBound(new float[]{394 / 30f, 1102 / 30f});
            frames = new Array<TextureRegion>();
            for (int i = 0; i < 6; i++) {
                frames.add(walk.findRegion("Walk_firethrower", i));
            }
//            setWalking(new Animation<>(0.15f, frames));
//            setWalkingBound(new float[]{354 / 30f, 526 / 30f});
//            setStand(walk.findRegion("Walk_firethrower", 0));
        }else {
            shoot = new TextureAtlas("Woman/Woman_Shoot_Flamethrower.txt");
            walk = new TextureAtlas("Woman/Woman_Walk_Flamethrower.txt");
            frames = new Array<TextureRegion>();
            for (int i = 0; i < 9; i++) {
                frames.add(shoot.findRegion("FlameThrower", i));
            }
            setAttacking(new Animation<>(0.15f, frames));
        }
            setAttackingBound(new float[]{394 / 30f, 1102 / 30f});
            frames = new Array<TextureRegion>();
            for (int i = 0; i < 6; i++) {
                frames.add(walk.findRegion("Walk_firethrower", i));
            }
            setWalking(new Animation<>(0.15f, frames));
            setWalkingBound(new float[]{354 / 30f, 526 / 30f});
            setStand(walk.findRegion("Walk_firethrower", 0));
        setIcon(Weapon.guns.findRegion("Flamethrower"));
    }

    @Override
    public void upgrade() {
        if(!isFullyUpgraded()) {
            damage += 14;
            level += 1;
        }
    }

    @Override
    public void shoot(float dt) {
        if (ammo > 0) {
            ammo -= dt*5;
        }else {
            ammo = 0;
        }
    }

    @Override
    public float getDamage(float dt){
        return super.getDamage(dt)*dt;
    }
}
