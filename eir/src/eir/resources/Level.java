package eir.resources;

import java.util.Collection;
import java.util.List;

import com.badlogic.gdx.graphics.Texture;

import eir.world.Asteroid;
import eir.world.Web;

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
	public List <Asteroid> getAsteroids()
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
	 * @param factory  
	 * 
	 */
	public void init( GameFactory factory )
	{
		for(Asteroid asteroid : asteroids)
		{
			asteroid.init( factory );
		}
		
		for( Web web : webs )
		{
			web.init( factory );
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
