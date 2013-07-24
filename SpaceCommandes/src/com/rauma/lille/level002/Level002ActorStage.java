package com.rauma.lille.level002;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.rauma.lille.Resource;
import com.rauma.lille.SpaceGame;
import com.rauma.lille.Utils;
import com.rauma.lille.actors.BodyImageActor;
import com.rauma.lille.stages.DefaultActorStage;

/**
 * This class is responsible for maintaining and drawing actors in the game, and
 * correlate them to the physics engine.
 * 
 * @author frank
 * 
 */
public class Level002ActorStage extends DefaultActorStage {

	public Level002ActorStage(int width, int height, boolean keepAspectRatio) {
		super(width, height, keepAspectRatio);
		initFrame();
	}

	
	private void initFrame() {
		//create frame
        BodyDef def = new BodyDef();
        def.type = BodyType.StaticBody;
        
        PolygonShape shape = new PolygonShape();
                        
        //bottom
        float width = SpaceGame.SCREEN_WIDTH;
        float height = 10;
        float x = 0;
        float y = 0;
        
        //Bottom
        shape.setAsBox(Utils.Screen2World(width / 2), Utils.Screen2World(height / 2));
        def.position.set(Utils.getWorldBoxCenter(Utils.Screen2World(x, y), Utils.Screen2World(width, height)));

        BodyImageActor bottom = new BodyImageActor("bottom", new TextureRegion(Resource.colorTexture, 0, 0, 64, 64), world, def, shape, 1.0f);
        bottom.setWidth(width);
        bottom.setHeight(height);
        addActor(bottom);
        
        //top
        y= SpaceGame.SCREEN_HEIGHT;
        x = 0;
        def.position.set(Utils.getWorldBoxCenter(Utils.Screen2World(x, y), Utils.Screen2World(width, height)));
        BodyImageActor top = new BodyImageActor("top", new TextureRegion(Resource.colorTexture, 0, 64 * 3, 64, 64), world, def, shape, 1.0f);
        top.setWidth(width);
        top.setHeight(height);
        addActor(top);
        
        //left
        width = 10;
        height = SpaceGame.SCREEN_HEIGHT;
        x = 0;
        y = 0;
        
        shape.setAsBox(Utils.Screen2World(width / 2), Utils.Screen2World(height / 2));      
        def.position.set(Utils.getWorldBoxCenter(Utils.Screen2World(x, y), Utils.Screen2World(width, height)));
        BodyImageActor left = new BodyImageActor("left", new TextureRegion(Resource.colorTexture, 0, 64, 64, 64), world, def, shape, 1.0f);
        left.setWidth(width);
        left.setHeight(height);
        
        addActor(left);
        
        //right
        width = 10;
        height = SpaceGame.SCREEN_HEIGHT;
        x = SpaceGame.SCREEN_WIDTH - width;
        y = 0;
        
        shape.setAsBox(Utils.Screen2World(width / 2), Utils.Screen2World(height / 2));      
        def.position.set(Utils.getWorldBoxCenter(Utils.Screen2World(x, y), Utils.Screen2World(width, height)));

        BodyImageActor right = new BodyImageActor("right", new TextureRegion(Resource.colorTexture, 0, 64 * 2, 64, 64), world, def, shape, 1.0f);
        right.setWidth(width);
        right.setHeight(height);
        addActor(right);

        shape.dispose();  
        
        
        x = 200;
        y = 50;
        width = 62;
        height = 62;
     // Create our body in the world using our body definition
        def.position.set(Utils.getWorldBoxCenter(Utils.Screen2World(x, y), Utils.Screen2World(width, height)));
        def.type = BodyType.DynamicBody;
        
     // Create a circle shape and set its radius to 6
        CircleShape circle = new CircleShape();
        circle.setRadius(Utils.Screen2World(height/2));
        // Create a fixture definition to apply our shape to
        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = circle;
        fixtureDef.density = 0.9f; 
        fixtureDef.friction = 0.4f;
        fixtureDef.restitution = .7f; // Make it bounce a little bit

        BodyImageActor myCircle = new BodyImageActor("circle", new TextureRegion(Resource.ballTexture, 0, 0, 64, 64), world, def, fixtureDef);
        myCircle.setWidth(width);
        myCircle.setHeight(height);
        myCircle.setOrigin(width/2, height/2);
        addActor(myCircle);
        myCircle.applyForce(Utils.Screen2World(400,0), new Vector2(0,0));
        
        circle.dispose();
	}
}
