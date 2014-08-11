/**
 *
 */
package eir.world;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;

import eir.rendering.EntityRenderer;
import eir.rendering.IRenderer;
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

	Sprite sprite;

	private float angle;

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

		model = gameFactory.getPolygonalModel( this, def.getModel() );

		sprite = gameFactory.createSprite(
				def.getTexture(),
				def.getPosition(),
				model.getOrigin(),
				def.getSize(), def.getSize(), def.getAngle() );

		model.addToMesh( level.getEnvironment().getGroundMesh(), this );

	}


	/**
	 * @param batch
	 * @param shapeRenderer
	 */
	public void draw( final IRenderer renderer )
	{
///		angle += 		def.getRotation();

		renderer.getSpriteBatch().draw( sprite,
				def.getPosition().x-sprite.getRegionWidth()/2, def.getPosition().y-sprite.getRegionHeight()/2,
				sprite.getRegionWidth()/2,sprite.getRegionHeight()/2,
				sprite.getRegionWidth(), sprite.getRegionHeight(),
				getSize()/sprite.getRegionWidth(),
				getSize()/sprite.getRegionWidth(), angle);
//		sprite.draw( renderer.getSpriteBatch() );
	}



}
