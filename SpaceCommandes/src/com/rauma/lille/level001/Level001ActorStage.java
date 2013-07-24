package com.rauma.lille.level001;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.rauma.lille.Resource;
import com.rauma.lille.Utils;
import com.rauma.lille.actors.BodyImageActor;
import com.rauma.lille.stages.DefaultActorStage;


/**
 * @author frank
 *
 */
public class Level001ActorStage extends DefaultActorStage {
	public Level001ActorStage(int width, int height, boolean keepAspectRatio) {
		super(width, height, keepAspectRatio);
		
		initLevel();
	}

	private void initLevel() {
		//bottom
		float width = 64;
		float height = 64;
		float x = 100;
		float y = 100;
        
		BodyDef def = new BodyDef();
		def.type = BodyType.StaticBody;
		def.position.set(Utils.getWorldBoxCenter(Utils.Screen2World(x, y), Utils.Screen2World(width, height)));

		PolygonShape shape = new PolygonShape();
        shape.setAsBox(Utils.Screen2World(width / 2), Utils.Screen2World(height / 2));
		
//		Image image = new Image(new TextureRegionDrawable(new TextureRegion(Resource.colorTexture, 0, 0, 64, 64)));
//        addActor(image);
        
        BodyImageActor bottom = new BodyImageActor("bottom", new TextureRegion(Resource.colorTexture, 0, 0, 64, 64), world, def, shape, 1.0f);
        bottom.setWidth(width);
        bottom.setHeight(height);
        addActor(bottom);
        
        shape.dispose();
	}
}
