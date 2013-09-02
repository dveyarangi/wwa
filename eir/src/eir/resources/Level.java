package eir.resources;

import java.util.List;

import com.badlogic.gdx.physics.box2d.Body;

import eir.world.Asteroid;

public class Level
{
	private int width;
	private int height;
	private List <Asteroid> asteroids;
	
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
	}

}
