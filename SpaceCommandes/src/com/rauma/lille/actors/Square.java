package com.rauma.lille.actors;

import java.util.Arrays;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;

public class Square extends Shape {

		public Square(World world) {
			super(world);
			setName("Square");

			// First we create a body definition
			BodyDef bodyDef = new BodyDef();
			// We set our body to dynamic, for something like ground which doesnt move we would set it to StaticBody
			bodyDef.type = BodyType.DynamicBody;
			// Set our body's starting position in the world
			bodyDef.position.set(150, 300);

			// Create our body in the world using our body definition
			Body body = world.createBody(bodyDef);

			// Create a circle shape and set its radius to 6
			PolygonShape square = new PolygonShape();
			square.setAsBox(3, 3);
			// Create a fixture definition to apply our shape to
			FixtureDef fixtureDef = new FixtureDef();
			fixtureDef.shape = square;
			fixtureDef.density = 0.5f; 
			fixtureDef.friction = 0.4f;
			fixtureDef.restitution = 0.0f; // Make it bounce a little bit

			fixture = body.createFixture(fixtureDef);
			
			// Remember to dispose of any shapes after you're done with them!
			// BodyDef and FixtureDef don't need disposing, but shapes do.
			square.dispose();
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
			renderer.polygon(vertices);
			renderer.end();

			batch.begin();
		}

	}