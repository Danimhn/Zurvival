package com.mygdx.game.Box2D_Bodies;

import com.badlogic.gdx.maps.MapLayer;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;

import java.util.ArrayList;

public class B2dStaticRectangleObjectGenerator {
        World world;
        public static ArrayList<Vector2[]> rectangles = new ArrayList<>();

        public B2dStaticRectangleObjectGenerator(World w){
            this.world = w;
        }

        public void createObjects(MapLayer layer) {
            Body body;
            for(MapObject object: layer.getObjects().getByType(RectangleMapObject.class)) {
                BodyDef bodyDef = new BodyDef();
                Rectangle rect = ((RectangleMapObject) object).getRectangle();
                addRect(rect);
                bodyDef.position.set((rect.getX() + rect.getWidth()/2)/Soldier.PPM, (rect.getY() + rect.getHeight()/2)/Soldier.PPM);
                bodyDef.type = BodyDef.BodyType.StaticBody;
                body = world.createBody(bodyDef);

                FixtureDef fixtdef = new FixtureDef();
                PolygonShape shape = new PolygonShape();
                shape.setAsBox(rect.getWidth()/2/Soldier.PPM, rect.getHeight()/2/Soldier.PPM);
                fixtdef.shape = shape;
                body.createFixture(fixtdef);
            }
        }

        private void addRect(Rectangle rect) {
            Vector2 v1 = new Vector2((rect.getX() + rect.getWidth()) / Soldier.PPM,
                    (rect.getY() + rect.getHeight()) / Soldier.PPM);
            Vector2 v2 = new Vector2((rect.getX() + rect.getWidth()) / Soldier.PPM,
                    rect.getY() / Soldier.PPM);
            Vector2 v3 = new Vector2(rect.getX() / Soldier.PPM,
                    (rect.getY() + rect.getHeight()) / Soldier.PPM);
            Vector2 v4 = new Vector2(rect.getX() / Soldier.PPM,
                    rect.getY() / Soldier.PPM);
            rectangles.add(new Vector2[]{v1, v2, v3, v4});
        }
    }

