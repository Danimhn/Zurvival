package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.physics.box2d.World;
import com.mygdx.game.Box2D_Bodies.Soldier;
import com.mygdx.game.Weapons.FlameThrower;
import com.mygdx.game.Weapons.HandGun;
import com.mygdx.game.Weapons.Knife;
import com.mygdx.game.Weapons.Riffle;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;

public class DataHandler<obj> {
    private JSONObject jfile;
    private FileHandle file;

    public DataHandler() {
        file = Gdx.files.local("data.json");
        JSONParser parser = new JSONParser();

        try {
            Object obj = parser.parse(file.readString());
            jfile = (JSONObject) obj;
        } catch (Exception e) {
            createFile();
        }
        if(jfile == null){
            createFile();
        }

    }

    private void createFile() {
        jfile = new JSONObject();
        jfile.put("HighestLevel", 0);
        clearFile();
    }

    public void clearFile() {
        jfile.put("Sex", null);
        jfile.put("Weapons", null);
        jfile.put("Health", null);
        jfile.put("Armor", null);
        jfile.put("Level", null);
        jfile.put("Saved", false);
        jfile.put("HighestLevel", getHighestLevel());
        file.writeString(jfile.toJSONString(), false);
    }

    public Soldier getPlayer(World b2dWorld){
        Soldier player;
        if(isSaved()){
            player = new Soldier(b2dWorld, (String) jfile.get("Sex"));
            JSONArray weapons = (JSONArray) jfile.get("Weapons");
            // Objects in the arrays represents in order: HandGun, Knife, Riffle, FlameThrower
            JSONObject handGun = (JSONObject)weapons.get(0);
            JSONObject knife = (JSONObject)weapons.get(1);
            JSONObject riffle = (JSONObject)weapons.get(2);
            JSONObject flameThrower = (JSONObject)weapons.get(3);
            int level = getIntVal(handGun.get("Level"));
            for (int i = 0; i < level; i++){
                player.getWeapon(HandGun.class).upgrade();
            }
            ((HandGun)player.getWeapon(HandGun.class)).setAmmo(getIntVal(handGun.get("Ammo")));
            level = getIntVal(knife.get("Level"));
            for (int i = 0; i < level; i++){
                player.getWeapon(Knife.class).upgrade();
            }
            if((boolean)riffle.get("Purchased")){
                level = getIntVal(riffle.get("Level"));
                player.addWeapon(new Riffle(getIntVal(riffle.get("Ammo"))), level);
            }
            if((boolean) flameThrower.get("Purchased")){
                level = getIntVal(flameThrower.get("Level"));
                player.addWeapon(new FlameThrower(getIntVal(flameThrower.get("Ammo"))), level);
            }
            int health = getIntVal(jfile.get("Health"));
            player.setHealth(health);
            int armor = getIntVal(jfile.get("Armor"));
            while (armor > 0){
                player.upgradeArmor();
                armor -= 20;
            }
            player.earn(getIntVal(jfile.get("Money")));
            return player;
        }
        return new Soldier(b2dWorld, "M");
    }

    private int getIntVal(Object obj) {
        if(obj instanceof Integer){
            return  (int) obj;
        }else{
            return ((Long) obj).intValue();
        }
    }

    public boolean isSaved(){ return (boolean) jfile.get("Saved");}


    public int getlevel() {
        return ((Long)jfile.get("Level")).intValue();
    }

    public int getHighestLevel() {
        if(jfile.get("HighestLevel") instanceof Integer ){
            return (int) jfile.get("HighestLevel");
        }
        return ((Long)jfile.get("HighestLevel")).intValue();
    }

    public void saveToFile(Soldier player, int level){
        jfile.put("Sex", player.getSex());
        jfile.put("Weapons", weaponsToJson(player));
        jfile.put("Health", player.getHealth());
        jfile.put("Armor", player.getArmor());
        jfile.put("Level", level);
        jfile.put("Money", player.getMoney());
        jfile.put("Saved", true);
        if (level > getHighestLevel()){
            jfile.put("HighestLevel", level);
        }else {
            jfile.put("HighestLevel", getHighestLevel());
        }
        file.writeString(jfile.toJSONString(), false);


    }

    private JSONArray weaponsToJson(Soldier player) {
        JSONArray weapons = new JSONArray();
        JSONObject weapon = new JSONObject();
        weapon.put("Level", player.getWeapon(HandGun.class).getLevel());
        weapon.put("Ammo", player.getWeapon(HandGun.class).getAmmo());
        weapons.add(weapon);
        weapon = new JSONObject();
        weapon.put("Level", player.getWeapon(Knife.class).getLevel());
        weapons.add(weapon);
        weapon = new JSONObject();
        if(player.hasWeapon(Riffle.class)) {
            weapon.put("Purchased", true);
            weapon.put("Level", player.getWeapon(Riffle.class).getLevel());
            weapon.put("Ammo", player.getWeapon(Riffle.class).getAmmo());
        }else {
            weapon.put("Purchased", false);
        }
        weapons.add(weapon);
        weapon = new JSONObject();
        if(player.hasWeapon(FlameThrower.class)) {
            weapon.put("Purchased", true);
            weapon.put("Level", player.getWeapon(FlameThrower.class).getLevel());
            weapon.put("Ammo", player.getWeapon(FlameThrower.class).getAmmo());
        }else {
            weapon.put("Purchased", false);
        }
        weapons.add(weapon);
        return weapons;
    }
}
