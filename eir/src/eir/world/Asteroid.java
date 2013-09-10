/**
 * 
 */
package eir.world;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;

import eir.resources.GameFactory;
import eir.resources.PolygonalModel;

/**
 * @author dveyarangi
 *
 */
public class Asteroid
{
	/**
	 * Asteroid id for whatever we will need tihs
	 */
	private String name;
	
	/**
	 * Position of asteroid
	 */
	private Vector2 position;
	
	/**
	 * Angle, huh
	 */
	private float angle;
	
	/**
	 * Size
	 */
	private float size;
	
	/**
	 * Asteroid poly + texture model id (such models may potentialy be reused)
	 */
	private String modelId;
	
	/**
	 * Asteroid sprite overlay
	 */
	private Sprite sprite;
	
	/**
	 * Underlying polygon model with navigation and ambulation helpers.
	 */
	private PolygonalModel model;
	
	public Asteroid()
	{

	}

	public PolygonalModel getModel() { return model; }

	public String getName() { return name; }

	public float getAngle()	{ return angle;	}
	public Vector2 getPosition() { return position;	}
	public float getSize() { return size; }
	
	/**
	 * This is required to initialize sprite and model
	 * @param factory
	 */
	public void init(GameFactory factory)
	{
		model = factory.loadAsteroidModel( this, modelId );
		sprite = factory.createSprite( modelId, position, model.getOrigin(), size, size, angle );
	}
	
	/**
	 * @param batch
	 * @param shapeRenderer 
	 */
	public void draw( SpriteBatch batch )
	{
		sprite.draw( batch );
	}


}
