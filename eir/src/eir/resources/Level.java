package eir.resources;

import java.util.List;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;

import eir.debug.Debug;
import eir.world.Asteroid;
import eir.world.Web;
import eir.world.environment.NavNode;

public class Level
{
	private String name;

	
	private int width;
	private int height;
	
	private List <Asteroid> asteroids;
	
	private List <Web> webs;
	
	private Texture backgroundTexture;
	
	private float initialZoom;
	private int initialNodeIdx;
	private NavNode initialNode;
	
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

		// nav mesh initiated after this point
		////////////////////////////////////////////////////
		
		Debug.startTiming("navmesh");
		factory.getNavMesh().init();
		Debug.stopTiming("navmesh");
		
		initialNode = factory.getNavMesh().getNode( initialNodeIdx );
		

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

	/**
	 * @return
	 */
	public float getInitialZoom() { return initialZoom; }
	public Vector2 getInitialPoint() { return initialNode.getPoint(); }
	
	private void log(String message)
	{
		Gdx.app.log( name, message);
	}

}
