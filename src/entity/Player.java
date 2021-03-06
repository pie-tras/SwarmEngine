package entity;

import org.joml.*;
import org.lwjgl.glfw.*;

import audio.*;
import effects.*;
import io.*;
import render.*;
import world.*;

public class Player extends Entity{
	public static final int ANIM_IDLE = 0;
	public static final int ANIM_WALK = 1;
	public static final int ANIM_SIZE = 2;
	public final static int WIDTH=16;
	public final static int HEIGHT=16;
	
	private boolean notOnFire = true;
	
	public Player(Transform transform, World world) {
		super(ANIM_SIZE, transform, world, WIDTH, HEIGHT);
		setAnimation(ANIM_IDLE, new Animation(1, 2, "player/idle"));
		setAnimation(ANIM_WALK, new Animation(3, 10, "player/walk"));
		AudioMaster.setListenerData(transform.pos.x, transform.pos.y, 0);
		hasSound = false;
	}
	
	@Override
	public void update(float delta, Window window, Camera camera) {
		Vector2f movement = new Vector2f();
		if(window.getInput().isKeyDown(GLFW.GLFW_KEY_A)) {
			movement.add(-10*delta, 0);
		}
		
		if(window.getInput().isKeyDown(GLFW.GLFW_KEY_W)) {
			movement.add(0, 10*delta);
		}
		if(window.getInput().isKeyDown(GLFW.GLFW_KEY_S)) {
			movement.add(0, -10*delta);
		}
		if(window.getInput().isKeyDown(GLFW.GLFW_KEY_D)) {
			movement.add(10*delta, 0);
		}
		if(window.getInput().isKeyPressed(GLFW.GLFW_KEY_E)) {
			world.kill(this);
		}
		
		if(window.getInput().isKeyPressed(GLFW.GLFW_KEY_SPACE)) {
			Explosion e = new Explosion(world.getParticleShader(), transform, world, 16, new Vector3f(5, 166, 100));
			world.getParticles().add(e);
		}
		
		if(window.getInput().isKeyPressed(GLFW.GLFW_KEY_F) && notOnFire) {
			notOnFire = false;
			Fire f = new Fire(world.getParticleShader(), transform, world, 2, new Vector3f(255, 160, 0));
			world.getParticles().add(f);
		}
	
		move(movement);
		AudioMaster.setListenerData(transform.pos.x, transform.pos.y, 0);
		
		if(movement.x != 0 || movement.y!=0)
			useAnimation(ANIM_WALK);
		else
			useAnimation(ANIM_IDLE);
		
		camera.getPosition().lerp(transform.pos.mul(-world.getScale(), new Vector3f()), 0.05f);
	}
	
}