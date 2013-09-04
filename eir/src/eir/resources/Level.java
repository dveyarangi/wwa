package eir.resources;

import java.util.List;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.physics.box2d.Body;

import eir.world.Asteroid;

public class Level
{
	private String name;
	
	private int width;
	private int height;
	private List <Asteroid> asteroids;
	
	private Texture backgroundTexture;
	
	/**
	 * @return
	 */
	public List <Asteroid> getAsteroids()
	{
		return asteroids;
	}

	/**
	 * 
	 */
	public void init()
	{
		for(Asteroid asteroid : asteroids)
		{
			
			Body body = asteroid.getModel().getBody();
			body.setTransform( asteroid.getX(), asteroid.getY(), asteroid.getAngle() );
		}
		
		backgroundTexture = new Texture(Gdx.files.internal("data/levels/" + name + ".png"));
	}

	/**
	 * @return
	 */
	public Texture getBackgroundTexture()
	{
		return backgroundTexture;
	}

	/**
	 * @return
	 */
	public float getHeight() { return height; }
	/**
	 * @return
	 */
	public float getWidth() { return width; }

}
