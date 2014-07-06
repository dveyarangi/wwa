/**
 *
 */
package eir.world;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;

import eir.rendering.EntityRenderer;
import eir.rendering.IRenderer;
import eir.rendering.SpriteRenderer;
import eir.resources.GameFactory;
import eir.resources.PolygonalModel;
import eir.resources.levels.AsteroidDef;

/**
 * @author dveyarangi
 *
 */
public class Asteroid
{

	private AsteroidDef def;

	/**
	 * Asteroid sprite overlay
	 */
	private EntityRenderer <Asteroid> renderer;

	/**
	 * Underlying polygon model with navigation and ambulation helpers.
	 */
	private PolygonalModel model;

	public Asteroid( )
	{
	}

	public PolygonalModel getModel() { return model; }

	public String getName() { return def.getName(); }

	public float getAngle()	{ return def.getAngle();	}
	public Vector2 getPosition() { return def.getPosition();	}
	public float getSize() { return def.getSize(); }

	public void init(final GameFactory gameFactory, final Level level, final AsteroidDef def)
	{
		this.def = def;

		model = GameFactory.loadAsteroidModel( this, def.getModelId() );

		Sprite sprite = gameFactory.createSprite(
				def.getTexture(),
				def.getPosition(),
				model.getOrigin(),
				def.getSize(), def.getSize(), def.getAngle() );

		this.renderer = new SpriteRenderer <Asteroid>( sprite );

		model.addToMesh( level.getEnvironment().getGroundMesh(), this );
	}


	/**
	 * @param batch
	 * @param shapeRenderer
	 */
	public void draw( final IRenderer renderer )
	{
		this.renderer.draw( renderer, this );
	}



}
