package com.rauma.lille.actors;

import java.util.Arrays;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;

public class Ground extends Shape {

	public Ground(World world, Stage stage) {
		super(world);
		// Create our body definition
		BodyDef groundBodyDef =new BodyDef();  
		// Set its world position
		groundBodyDef.position.set(new Vector2(0, 10));  

		// Create a body from the defintion and add it to the world
		Body groundBody = world.createBody(groundBodyDef);  

		// Create a polygon shape
		PolygonShape groundBox = new PolygonShape();  
		// Set the polygon shape as a box which is twice the size of our view port and 20 high
		// (setAsBox takes half-width and half-height as arguments)
		float viewportWidth = stage.getCamera().viewportWidth;
		groundBox.setAsBox(viewportWidth, 10.0f);
		// Create a fixture from our polygon shape and add it to our ground body  
		fixture = groundBody.createFixture(groundBox, 0.0f);
		// Clean up after ourselves
		groundBox.dispose();
	}
	
	@Override
	public void draw(SpriteBatch batch, float parentAlpha) {
		batch.end();

		renderer.setProjectionMatrix(batch.getProjectionMatrix());
		renderer.setTransformMatrix(batch.getTransformMatrix());
		renderer.translate(getX(), getY(), 0);

		renderer.begin(ShapeType.Line);
		PolygonShape ps = (PolygonShape) fixture.getShape();
		
		float[] vertices = new float[ps.getVertexCount()*2];
		Vector2 vertex = new Vector2();
		for (int i = 0; i < ps.getVertexCount(); i++) {
			ps.getVertex(i, vertex);
			vertices[i*2] = vertex.x;
			vertices[i*2+1] = vertex.y;
		}
		renderer.polygon(vertices);;
		renderer.end();

		batch.begin();
	}
}
