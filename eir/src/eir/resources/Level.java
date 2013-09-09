package eir.resources;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.physics.box2d.Body;

import eir.world.Asteroid;
import eir.world.environment.Web;

public class Level
{
	private String name;
	
	private int width;
	private int height;
	
	private List <Asteroid> asteroids;
	
	private List <Web> webs;
	
	private Texture backgroundTexture;
	
	/**
	 * @return
	 */
	public Collection <Asteroid> getAsteroids()
	{
		return asteroids;
	}

	/**
	 * @return
	 */
	public List <Web> getWebs()
	{
		return webs;
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
		
		for( Web web : webs )
		{
			web.init();
		}
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
