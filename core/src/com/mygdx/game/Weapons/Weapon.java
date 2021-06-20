package com.mygdx.game.Weapons;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public abstract class Weapon {

    protected TextureAtlas walk;
    protected TextureAtlas shoot;
    protected TextureAtlas shoot2;
    protected int damage;
    protected int range;
    protected float ammo;
    protected int level = 0;
    protected int[] upgradePrices;
    protected boolean fullyUpgraded = false;

    private Animation walking;
    private Animation attacking;
    TextureRegion stand;
    private float[] walkingBound;
    private float[] attackingBound;
    private TextureRegion icon;

    protected static TextureAtlas guns = new TextureAtlas("Man/Guns.txt");
    private float offset;


    public Weapon(int damage, int range, int ammo, int[] upgradePrices) {
        this.upgradePrices =upgradePrices;
        this.damage = damage;
        this.range = range;
        this.ammo = ammo;
    }

    public abstract void upgrade();

    public boolean isOutAmmo() {
        return ammo == 0;
    }

    public void addAmmo(int amount) {
        ammo += amount;
    }

    public void shoot(float dt) {
        if (ammo > 0) {
            ammo -= 1;
        }else {
            ammo = 0;
        }
    }

    public boolean isFullyUpgraded() {
        return level >= upgradePrices.length;
    }

    public int getAmmo() {
        return (int) ammo;
    }

    public int getRange() {
        return range;
    }

    public float getDamage(float dt) {
        return damage;
    }

    public Animation getAttackingM() {
        return attacking;
    }

    public Animation getWalkingM() {
        return walking;
    }

    public void setWalking(Animation walking) {
        this.walking = walking;
    }

    public void setAttacking(Animation attackingM) {
        this.attacking = attackingM;
    }

    public TextureRegion getStand() {
        return stand;
    }

    public void setStand(TextureRegion stand) { this.stand = stand;}

    public float[] getAttackingBoundM() {
        return attackingBound;
    }

    public void setAttackingBound(float[] attackingBoundM) {
        this.attackingBound = attackingBoundM;
    }

    public float[] getWalkingBound() {
        return walkingBound;
    }

    public void setWalkingBound(float[] walkingBoundM) {
        this.walkingBound = walkingBoundM;
    }

    public TextureRegion getIcon(){
        return icon;
    }

    public void setIcon(TextureRegion icon) {
        this.icon = icon;
    }

    public int getNextUpgrade() {
        if(!isFullyUpgraded()){
            return upgradePrices[level];
        }
        return 0;
    }

    public int getLevel(){
        return level;
    }

    public void disposeTextures(){
        walk.dispose();
        shoot.dispose();
        if(shoot2 != null){
            shoot2.dispose();
        }
    }

    public float getOffset(){ return offset; }
    
    public void setOffset(float offset){ this.offset = offset; }
}
