/**
 * 
 */
package eir.world;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;

import eir.resources.GameFactory;
import eir.resources.PolygonalModel;
import eir.world.environment.nav.NavMesh;

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
	
	private Body body;
	
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
	public void preinit(NavMesh mesh)
	{
		model = GameFactory.loadAsteroidModel( mesh, this, modelId );
		sprite = GameFactory.createSprite( modelId, position, model.getOrigin(), size, size, angle );
	}
	
	public void init(Level level)
	{
		this.body = GameFactory.loadBody(modelId, this);
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
