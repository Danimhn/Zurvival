package com.mygdx.game.Screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.mygdx.game.Box2D_Bodies.Soldier;

import java.util.ArrayList;
import java.util.Arrays;

public class GameScreenHelper {

    public void setupOptions(ArrayList<Sprite> optionSprites,
                             OrthographicCamera cam, TextureAtlas optionsAtlas) {
        Sprite base = new Sprite(optionsAtlas.findRegion("Base"));
        base.setBounds(0, 0, cam.viewportWidth / 1.5f, cam.viewportWidth / 1.2f);
        base.setCenter(cam.position.x, cam.position.y);
        Sprite mute = new Sprite(optionsAtlas.findRegion("MuteIcon"));
        mute.setBounds(0, 0, base.getWidth() / 4f, base.getWidth() / 4f);
        mute.setCenter(cam.position.x, cam.position.y - base.getHeight() / 2 + mute.getHeight());
        Sprite unmute = new Sprite(optionsAtlas.findRegion("UnmuteIcon"));
        unmute.setBounds(0, 0, base.getWidth() / 4f, base.getWidth() / 4f);
        unmute.setCenter(cam.position.x, cam.position.y - base.getHeight() / 2 + mute.getHeight());
        Sprite whiteBase = new Sprite(optionsAtlas.findRegion("WhiteBase"));
        whiteBase.setBounds(0, 0, base.getWidth() / 3.5f, base.getWidth() / 3.5f);
        whiteBase.setCenter(cam.position.x, cam.position.y - base.getHeight() / 2 + mute.getHeight());
        Sprite resumeB = new Sprite(optionsAtlas.findRegion("Button"));
        resumeB.setBounds(0, 0, cam.viewportWidth / 1.7f, base.getHeight() / 5.4f);
        resumeB.setCenter(cam.position.x, cam.position.y + base.getHeight() / 2f - 5 * resumeB.getHeight() / 4f);
        Sprite mainMenuB = new Sprite(optionsAtlas.findRegion("Button"));
        mainMenuB.setBounds(0, 0, cam.viewportWidth / 1.7f, base.getHeight() / 5.4f);
        mainMenuB.setCenter(cam.position.x, resumeB.getY() - 3 * mainMenuB.getHeight() / 4f);
        Sprite resume = new Sprite(optionsAtlas.findRegion("Resume"));
        resume.setBounds(0, 0, cam.viewportWidth / 4f, base.getHeight() / 7.8f);
        resume.setCenter(cam.position.x, cam.position.y + base.getHeight() / 2f - 5 * resumeB.getHeight() / 4f);
        Sprite mainMenu = new Sprite(optionsAtlas.findRegion("Main_Menu"));
        mainMenu.setBounds(0, 0, cam.viewportWidth / 3.5f, base.getHeight() / 7.8f);
        mainMenu.setCenter(cam.position.x, resumeB.getY() - 3 * mainMenuB.getHeight() / 4f);
        optionSprites.addAll(Arrays.asList(base, resumeB, mainMenuB, whiteBase, unmute, mute,
                resume, mainMenu));
        Sprite circle;
        for (int i = 0; i < 4; i++) {
            circle = new Sprite(optionsAtlas.findRegion("Circle"));
            circle.setBounds(0, 0, cam.viewportWidth / 7.5f, cam.viewportWidth / 7.5f);
            if (i == 0) {
                circle.setCenter(base.getX(), base.getY());
            } else if (i == 1) {
                circle.setCenter(base.getX() + base.getWidth(), base.getY());
            } else if (i == 2) {
                circle.setCenter(base.getX(), base.getY() + base.getHeight());
            } else {
                circle.setCenter(base.getX() + base.getWidth(), base.getY() + base.getHeight());
            }
            optionSprites.add(circle);
        }
    }

    public void setupTransition(ArrayList<Sprite> transitionSprites, OrthographicCamera cam,
                                TextureAtlas optionsAtlas, float beenTransitioningFor) {
        Sprite blackBar = new Sprite(optionsAtlas.findRegion("BlackBar"));
        blackBar.setBounds(0, 0, cam.viewportWidth, cam.viewportHeight/10f);
        Sprite cleared = new Sprite(optionsAtlas.findRegion("Round Cleared"));
        cleared.setBounds(0, 0, cam.viewportWidth/2f, cam.viewportHeight/11f);
        if(beenTransitioningFor < 2) {
            blackBar.setCenter(cam.position.x - 4*cam.viewportWidth + 2*beenTransitioningFor * cam.viewportWidth,
                    cam.position.y);
            cleared.setCenter(cam.position.x - 4*cam.viewportWidth + 2*beenTransitioningFor * cam.viewportWidth,
                    cam.position.y);
        }else {
            blackBar.setCenter(cam.position.x, cam.position.y);
            cleared.setCenter(cam.position.x, cam.position.y);
        }
        transitionSprites.addAll(Arrays.asList(blackBar, cleared));
    }

    public void setupGameOver(ArrayList<Sprite> gameOverSprites,
                              OrthographicCamera cam, boolean revivedOnce, TextureAtlas optionsAtlas) {
        Sprite base = new Sprite(optionsAtlas.findRegion("Base"));
        base.setBounds(0, 0, cam.viewportWidth/1.5f, cam.viewportWidth/1.2f);
        base.setCenter(cam.position.x, cam.position.y);
        Sprite okB = new Sprite(optionsAtlas.findRegion("Button"));
        okB.setBounds(0, 0, cam.viewportWidth/1.7f, base.getHeight()/5.4f);
        okB.setCenter(cam.position.x, cam.position.y - base.getHeight()/2f + okB.getHeight());
        Sprite reviveB = new Sprite(optionsAtlas.findRegion("Button"));
        reviveB.setBounds(0, 0, cam.viewportWidth/1.7f, base.getHeight()/5.4f);
        reviveB.setCenter(cam.position.x, okB.getY() + okB.getHeight() + 3*reviveB.getHeight()/4f);
        Sprite ok = new Sprite(optionsAtlas.findRegion("Ok"));
        ok.setBounds(0, 0, base.getHeight()/7.8f, base.getHeight()/7.8f);
        ok.setCenter(cam.position.x, cam.position.y - base.getHeight()/2f + okB.getHeight());
        Sprite revive = new Sprite(optionsAtlas.findRegion("Revive"));
        revive.setBounds(0, 0, cam.viewportWidth/5.3f, base.getHeight()/10.4f);
        revive.setCenter(cam.position.x - 0.25f*reviveB.getWidth() + revive.getWidth()/2,
                okB.getY() + okB.getHeight() + 3*reviveB.getHeight()/4f);
        Sprite ad = new Sprite(optionsAtlas.findRegion("AdIcon"));
        ad.setBounds(0, 0, base.getHeight()/7.8f, base.getHeight()/7.8f);
        ad.setCenter(cam.position.x + 0.24f*reviveB.getWidth() - ad.getWidth()/2,
                okB.getY() + okB.getHeight() + 3*reviveB.getHeight()/4f);
        Sprite gameOver = new Sprite(optionsAtlas.findRegion("Game Over"));
        gameOver.setBounds(0, 0, cam.viewportWidth/2f, base.getHeight()/5f);
        gameOver.setCenter(cam.position.x, cam.position.y + base.getHeight()/2f - gameOver.getHeight());
        gameOverSprites.addAll(Arrays.asList(base, okB, reviveB, revive, ok, ad, gameOver));
        Sprite circle;
        for(int i = 0; i<4; i++){
            circle = new Sprite(optionsAtlas.findRegion("Circle"));
            circle.setBounds(0, 0, cam.viewportWidth/7.5f, cam.viewportWidth/7.5f);
            if(i == 0){
                circle.setCenter(base.getX(), base.getY());
            }else if(i == 1){
                circle.setCenter(base.getX() + base.getWidth(), base.getY());
            }else if(i == 2){
                circle.setCenter(base.getX(), base.getY() + base.getHeight());
            }else {
                circle.setCenter(base.getX() + base.getWidth(), base.getY() + base.getHeight());
            }
            gameOverSprites.add(circle);
        }
        if(revivedOnce){
            reviveB.setAlpha(0.5f);
            revive.setAlpha(0.5f);
            ad.setAlpha(0.5f);
        }
    }

    public void setUpHudPosition(Sprite gunSlot, Sprite gunSlotL, Sprite gunSlotR, Sprite healthBar,
                                 Sprite healthBarR, Sprite healthBarL, Sprite shooting,
                                 Sprite options, Sprite navigate, OrthographicCamera cam, Soldier player) {
        gunSlot.setCenter(cam.position.x,
                cam.position.y - cam.viewportHeight/2 + gunSlot.getHeight()/2 + 5);
        gunSlotL.setCenter(cam.position.x - gunSlot.getWidth()/2 - gunSlotL.getWidth()/2,
                cam.position.y - cam.viewportHeight/2 + gunSlot.getHeight()/2 + 5);
        gunSlotR.setCenter(cam.position.x + gunSlot.getWidth()/2 + gunSlotL.getWidth()/2,
                cam.position.y - cam.viewportHeight/2 + gunSlot.getHeight()/2 + 5);
        healthBar.setBounds(0, 0, 4.64f*cam.viewportHeight/27f*(player.getHealth()/100f), 1.44f*cam.viewportHeight/40f);
        healthBar.setCenter(cam.position.x,
                cam.position.y + cam.viewportHeight/2 - healthBar.getHeight()/2 - 5);
        healthBarL.setCenter(cam.position.x - healthBar.getWidth()/2 - healthBarL.getWidth()/2,
                cam.position.y + cam.viewportHeight/2 - healthBar.getHeight()/2 - 5);
        healthBarR.setCenter(cam.position.x + healthBar.getWidth()/2 + healthBarR.getWidth()/2,
                cam.position.y + cam.viewportHeight/2 - healthBar.getHeight()/2 - 5);
        shooting.setCenter(cam.position.x - ((cam.viewportWidth/2 -
                gunSlot.getWidth()/2 - gunSlotL.getWidth())/2 + gunSlot.getWidth()/2 + gunSlotL.getWidth()), cam.position.y -
                cam.viewportHeight/2 + gunSlot.getHeight() + shooting.getHeight()/2 +5);
        options.setCenter(cam.position.x + (cam.viewportWidth/4f + healthBar.getWidth()/4f +
                healthBarR.getWidth()/2f), cam.position.y + cam.viewportHeight/2 - options.getHeight()/2 - 5);
        navigate.setCenter(cam.position.x + ((cam.viewportWidth/2 -
                gunSlot.getWidth()/2 - gunSlotR.getWidth())/2 + gunSlot.getWidth()/2 + gunSlotR.getWidth() - cam.viewportWidth/20), cam.position.y -
                cam.viewportHeight/2 + gunSlot.getHeight() + navigate.getHeight()/2 + cam.viewportWidth/5);
    }

    public void setUpBloodyHands(ArrayList<Sprite> bloodyHands, TextureAtlas items, Sprite shop) {
        Sprite bloodyHand;
        float handHeight = .55f*(Gdx.graphics.getHeight()/2f - shop.getHeight()/2);
        float handWidth = handHeight*.80f;
        bloodyHand = new Sprite(items.findRegion("BloodyHandR"));
        bloodyHand.setBounds(0, 0, handWidth, handHeight);
        bloodyHand.setCenter(1.3f*handWidth, Gdx.graphics.getHeight() - 3*handHeight/5);
        bloodyHands.add(bloodyHand);
        bloodyHand = new Sprite(items.findRegion("BloodyHandL"));
        bloodyHand.setBounds(0, 0, handWidth, handHeight);
        bloodyHand.setCenter(2*handWidth/3f, Gdx.graphics.getHeight() - 1.2f*handHeight);
        bloodyHands.add(bloodyHand);
        bloodyHand = new Sprite(items.findRegion("BloodyHandL"));
        bloodyHand.setBounds(0, 0, handWidth, handHeight);
        bloodyHand.setCenter(Gdx.graphics.getWidth() - 3*handWidth/4f, Gdx.graphics.getHeight() - handHeight/2f);
        bloodyHands.add(bloodyHand);
        bloodyHand = new Sprite(items.findRegion("BloodyHandR"));
        bloodyHand.setBounds(0, 0, handWidth, handHeight);
        bloodyHand.setCenter(Gdx.graphics.getWidth() - 5*handWidth/4f, Gdx.graphics.getHeight() - 4*handHeight/3f);
        bloodyHands.add(bloodyHand);
    }

    public void setUpButtons(TextureAtlas items, Sprite shop, ArrayList<Sprite> buttons) {
        Sprite nextButton = new Sprite(items.findRegion("Button2"));
        nextButton.setBounds(0, 0, 0.8f*Gdx.graphics.getWidth(), 0.15f*shop.getHeight());
        nextButton.setCenter(Gdx.graphics.getWidth()/2f, (Gdx.graphics.getHeight()/2f - shop.getHeight()/2)/3f);
        Sprite adButton = new Sprite(items.findRegion("Button2"));
        adButton.setBounds(0, 0, 0.6f*Gdx.graphics.getWidth(), 0.12f*shop.getHeight());
        adButton.setCenter(Gdx.graphics.getWidth()/2f, Gdx.graphics.getHeight()/2f -
                shop.getHeight()/2 - 3* adButton.getHeight()/4);
        Sprite button1 = new Sprite(items.findRegion("Button"));
        button1.setBounds(0, 0, 0.14f*Gdx.graphics.getWidth(), 0.05f*shop.getHeight());
        button1.setCenter(0.404f*Gdx.graphics.getWidth(), Gdx.graphics.getHeight()/2f + 0.014f*shop.getHeight());
        Sprite button2 = new Sprite(items.findRegion("Button"));
        button2.setBounds(0, 0, 0.14f*Gdx.graphics.getWidth(), 0.05f*shop.getHeight());
        button2.setCenter(0.56f*Gdx.graphics.getWidth(), Gdx.graphics.getHeight()/2f + 0.014f*shop.getHeight());
        Sprite button3 = new Sprite(items.findRegion("Button"));
        button3.setBounds(0, 0, 0.14f*Gdx.graphics.getWidth(), 0.05f*shop.getHeight());
        button3.setCenter(0.72f*Gdx.graphics.getWidth(), Gdx.graphics.getHeight()/2f + 0.014f*shop.getHeight());
        Sprite button4 = new Sprite(items.findRegion("Button"));
        button4.setBounds(0, 0, 0.14f*Gdx.graphics.getWidth(), 0.05f*shop.getHeight());
        button4.setCenter(0.404f*Gdx.graphics.getWidth(), Gdx.graphics.getHeight()/2f - 0.271f*shop.getHeight());
        Sprite button5 = new Sprite(items.findRegion("Button"));
        button5.setBounds(0, 0, 0.14f*Gdx.graphics.getWidth(), 0.05f*shop.getHeight());
        button5.setCenter(0.56f*Gdx.graphics.getWidth(), Gdx.graphics.getHeight()/2f - 0.271f*shop.getHeight());
        Sprite button6 = new Sprite(items.findRegion("Button"));
        button6.setBounds(0, 0, 0.14f*Gdx.graphics.getWidth(), 0.05f*shop.getHeight());
        button6.setCenter(0.72f*Gdx.graphics.getWidth(), Gdx.graphics.getHeight()/2f - 0.271f*shop.getHeight());
        Sprite bottomBar = new Sprite(items.findRegion("Button"));
        bottomBar.setBounds(0, 0, 0.545f*Gdx.graphics.getWidth(), 0.074f*shop.getHeight());
        bottomBar.setCenter(0.491f*Gdx.graphics.getWidth(), Gdx.graphics.getHeight()/2f - 0.425f*shop.getHeight());
        buttons.addAll(Arrays.asList(nextButton, adButton, button1, button2, button3, button4, button5,
                button6, bottomBar));
    }

    public void setUpSprites(TextureAtlas items, TextureAtlas guns, Sprite shop,
                             ArrayList<Sprite> gunSprites, ArrayList<Sprite> ammoSprites) {
        Sprite money = new Sprite(items.findRegion("Money"));
        money.setBounds(0, 0, 0.06f * Gdx.graphics.getWidth(), 0.06f * Gdx.graphics.getWidth());
        money.setCenter(0.303f * Gdx.graphics.getWidth(), Gdx.graphics.getHeight() / 2f - 0.425f * shop.getHeight());
        Sprite health = new Sprite(items.findRegion("HealthPack"));
        health.setBounds(0, 0, 0.06f * Gdx.graphics.getWidth(), 0.06f * Gdx.graphics.getWidth());
        health.setCenter(0.55f * Gdx.graphics.getWidth(), Gdx.graphics.getHeight() / 2f - 0.425f * shop.getHeight());
        Sprite ad = new Sprite(items.findRegion("AdIcon"));
        ad.setBounds(0, 0, 0.09f * shop.getHeight(), 0.09f * shop.getHeight());
        ad.setCenter(0.575f * Gdx.graphics.getWidth() + ad.getWidth() / 2,
                Gdx.graphics.getHeight() / 2f -
                        shop.getHeight() / 2 - 0.09f * shop.getHeight());
        //Setting up guns tab Sprites
        Sprite handGun = new Sprite(guns.findRegion("HandGun"));
        handGun.setBounds(0, 0, 0.12f * Gdx.graphics.getWidth(), 0.12f * Gdx.graphics.getWidth() / 2);
        handGun.setCenter(0.404f * Gdx.graphics.getWidth(), Gdx.graphics.getHeight() / 2f + 0.143f * shop.getHeight());
        Sprite knife = new Sprite(guns.findRegion("Knife"));
        knife.setBounds(0, 0, 0.12f * Gdx.graphics.getWidth(), 0.12f * Gdx.graphics.getWidth());
        knife.setCenter(0.56f * Gdx.graphics.getWidth(), Gdx.graphics.getHeight() / 2f + 0.143f * shop.getHeight());
        Sprite riffle = new Sprite(guns.findRegion("Riffle"));
        riffle.setBounds(0, 0, 0.12f * Gdx.graphics.getWidth(), 0.12f * Gdx.graphics.getWidth() / 3);
        riffle.setCenter(0.72f * Gdx.graphics.getWidth(), Gdx.graphics.getHeight() / 2f + 0.143f * shop.getHeight());
        Sprite flamethrower = new Sprite(guns.findRegion("Flamethrower"));
        flamethrower.setBounds(0, 0, 0.12f * Gdx.graphics.getWidth(), 0.12f * Gdx.graphics.getWidth() / 3);
        flamethrower.setCenter(0.404f * Gdx.graphics.getWidth(), Gdx.graphics.getHeight() / 2f - 0.129f * shop.getHeight());
        Sprite healthPack = new Sprite(items.findRegion("HealthPack"));
        healthPack.setBounds(0, 0, 0.11f * Gdx.graphics.getWidth(), 0.11f * Gdx.graphics.getWidth());
        healthPack.setCenter(0.56f * Gdx.graphics.getWidth(), Gdx.graphics.getHeight() / 2f - 0.129f * shop.getHeight());
        Sprite armor = new Sprite(items.findRegion("Armor"));
        armor.setBounds(0, 0, 0.11f * Gdx.graphics.getWidth(), 0.11f * Gdx.graphics.getWidth());
        armor.setCenter(0.72f * Gdx.graphics.getWidth(), Gdx.graphics.getHeight() / 2f - 0.129f * shop.getHeight());
        gunSprites.addAll(Arrays.asList(handGun, knife, riffle, flamethrower, healthPack, armor,
                money, health, ad));

        //Setting up ammo tab Sprites
        Sprite handGunMag = new Sprite(items.findRegion("HandGunMag"));
        handGunMag.setBounds(0, 0, 0.12f * Gdx.graphics.getWidth(), 0.12f * Gdx.graphics.getWidth() / 2);
        handGunMag.setCenter(0.404f * Gdx.graphics.getWidth(), Gdx.graphics.getHeight() / 2f + 0.143f * shop.getHeight());
        Sprite riffleMag = new Sprite(items.findRegion("RiffleMag"));
        riffleMag.setBounds(0, 0, 0.12f * Gdx.graphics.getWidth(), 0.06f * Gdx.graphics.getWidth());
        riffleMag.setCenter(0.56f * Gdx.graphics.getWidth(), Gdx.graphics.getHeight() / 2f + 0.143f * shop.getHeight());
        Sprite tank = new Sprite(items.findRegion("Tank"));
        tank.setBounds(0, 0, 0.12f * Gdx.graphics.getWidth(), 0.12f * Gdx.graphics.getWidth());
        tank.setCenter(0.72f * Gdx.graphics.getWidth(), Gdx.graphics.getHeight() / 2f + 0.143f * shop.getHeight());
        ammoSprites.addAll(Arrays.asList(handGunMag, riffleMag, tank, money, health, ad));
    }
}
