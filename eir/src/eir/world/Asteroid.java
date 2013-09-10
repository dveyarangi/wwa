/**
 * 
 */
package eir.world;

import java.util.List;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import eir.resources.GameFactory;
import eir.resources.PolygonalModel;
import eir.world.environment.NavNode;

/**
 * @author dveyarangi
 *
 */
public class Asteroid
{
	private String name;
	
	private float x;
	private float y;
	private float z;
	
	private float a;
	
	private float size;
	
	private String modelId;
	
	private Sprite sprite;
	
	private PolygonalModel model;
	
	public Asteroid()
	{

	}

	/**
	 * @return
	 */
	public PolygonalModel getModel()
	{
		return model;
	}

	public String getName() { return name; }

	public float getAngle()	{ return a;	}
	public float getX()	{ return x;	}
	public float getY()	{ return y;	}
	public float getSize() { return size; }
	
	public void init(GameFactory factory)
	{
		sprite = factory.createSprite(modelId, x, y, size, size, a);
		model = factory.loadAsteroidModel( this, modelId );
	}
	
	/**
	 * @param batch
	 */
	public void draw(SpriteBatch batch)
	{
		sprite.draw( batch );
	}

}
