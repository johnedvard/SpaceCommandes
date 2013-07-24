package com.rauma.lille.stages;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.rauma.lille.Resource;
import com.rauma.lille.SpaceGame;
import com.rauma.lille.Utils;
import com.rauma.lille.actors.BodyImageActor;

/**
 * This class is responsible for maintaining and drawing actors in the game, and
 * correlate them to the physics engine.
 * 
 * @author frank
 * 
 */
public class ActorStage extends DefaultActorStage {

    private Group actorGroup = new Group();

	public ActorStage(int width, int height, boolean keepAspectRatio) {
		super(width, height, keepAspectRatio);

		initFrame();
		
		// init actors (scene2s) with bodies (box2d)
		// translate events and such from world and bodies to actors in the
		// scene
		// best/easiest example I've seen so far:
		// https://code.google.com/p/codejie/source/browse/#svn%2Fjava%2Fgdx%2FDemoDraft%2Fsrc%2Fcom%2Fjie%2Fandroid%2Fgdx%2Fdemo

	}

	private void initFrame() {
		//create frame
        BodyDef def = new BodyDef();
        def.type = BodyType.StaticBody;
        
        PolygonShape shape = new PolygonShape();
                        
        //bottom
        float width = 64;
        float height = 64;
        float x = 1000;
        float y = 100;
        
        shape.setAsBox(Utils.Screen2World(width / 2), Utils.Screen2World(height / 2));
        def.position.set(Utils.getWorldBoxCenter(Utils.Screen2World(x, y), Utils.Screen2World(width, height)));

        BodyImageActor bottom = new BodyImageActor("bottom", new TextureRegion(Resource.colorTexture, 0, 0, 64, 64), world, def, shape, 1.0f);
        bottom.setWidth(width);
        bottom.setHeight(height);
        
        addActor(bottom);
        
//        //top
//        y = SpaceGame.SKY_Y;
//        def.position.set(Utils.getWorldBoxCenter(Utils.Screen2World(x, y), Utils.Screen2World(width, height)));
//        
//        BodyImageActor top = new BodyImageActor("top", new TextureRegion(Resource.colorTexture, 0, 64 * 3, 64, 64), world, def, shape, 1.0f);
//        top.setWidth(width);
//        top.setHeight(height);
//        
//        frameGroup.addActor(top);
//        
//        //left
//        width = SpaceGame.FRAME_BASE;
//        height = SpaceGame.SKY_Y -  SpaceGame.GROUND_Y + SpaceGame.FRAME_BASE;
//        x = 0;
//        y = SpaceGame.GROUND_Y;
//        
//        shape.setAsBox(Utils.Screen2World(width / 2), Utils.Screen2World(height / 2));      
//        def.position.set(Utils.getWorldBoxCenter(Utils.Screen2World(x, y), Utils.Screen2World(width, height)));
//
//        BodyImageActor left = new BodyImageActor("left", new TextureRegion(Resource.colorTexture, 0, 64, 64, 64), world, def, shape, 1.0f);
//        left.setWidth(width);
//        left.setHeight(height);
//        
//        frameGroup.addActor(left);
//        
//        //right
//        width = SpaceGame.FRAME_BASE;
//        height = SpaceGame.SKY_Y -  SpaceGame.GROUND_Y + SpaceGame.FRAME_BASE;
//        x = SpaceGame.SCREEN_WIDTH - SpaceGame.FRAME_BASE;
//        y = SpaceGame.GROUND_Y;
//        
//        shape.setAsBox(Utils.Screen2World(width / 2), Utils.Screen2World(height / 2));      
//        def.position.set(Utils.getWorldBoxCenter(Utils.Screen2World(x, y), Utils.Screen2World(width, height)));
//
//        BodyImageActor right = new BodyImageActor("right", new TextureRegion(Resource.colorTexture, 0, 64 * 2, 64, 64), world, def, shape, 1.0f);
//        right.setWidth(width);
//        right.setHeight(height);
//        
//        frameGroup.addActor(right);

        shape.dispose();        
        
	}
}
