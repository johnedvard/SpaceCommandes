package com.rauma.lille;

import java.util.ArrayList;
import java.util.List;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.Shape;
import com.badlogic.gdx.physics.box2d.World;
import com.rauma.lille.actors.Bullet;

public class BulletFactory {

	private static final int BULLET_LIMIT = 200;
	private static List<Bullet> freeBullets = new ArrayList<Bullet>();
	private static List<Bullet> busyBullets = new ArrayList<Bullet>();
	
	private static BodyDef def = new BodyDef();
	private static Shape circle = new CircleShape();
	private static float density = 0.001f;
	private static TextureRegion texture = new TextureRegion(new Texture(
			Gdx.files.internal("data/touchKnob.png")), 0, 0, 64, 64);
		
	public static Bullet getBullet() {
		if (freeBullets.size() == 0) {
			if(busyBullets.size() < BULLET_LIMIT) {
				create(1);
			} else {
				return null;
			}
		}
		Bullet bullet = freeBullets.remove(0);
		if (bullet != null) {
			bullet.getBody().setActive(true);
			busyBullets.add(bullet);
		}
		
		return bullet;
	}
	
	public static Bullet createBullet() {

		def.position.x = Utils.Screen2World(MathUtils.random(SpaceGame.SCREEN_WIDTH)*1000);
		def.position.y = Utils.Screen2World(MathUtils.random(SpaceGame.SCREEN_HEIGHT)*1000);
		return new Bullet("bullet", texture, world, def, circle, density);
	}

	/**
	 * Create bullet and add it to the free pool
	 * 
	 * @param numBullets
	 */
	private static void create(int numBullets) {
		for (int j = 0; j < numBullets; j++) {
			freeBullets.add(createBullet());
		}
	}

	private static World world;

	public static void init(World world) {
		BulletFactory.world = world;
		
		def.type = BodyType.DynamicBody;
		def.active = false;
		
		circle.setRadius(Utils.Screen2World(1f));
		
		create(BULLET_LIMIT);
	}

	public static void release(Bullet bullet) {
		busyBullets.remove(bullet);
		freeBullets.add(bullet);
	}
	
	public static void deactivateFreeBullets() {
		for (Bullet bullet : freeBullets) {
			bullet.getBody().setActive(false);
		}
	}
}
