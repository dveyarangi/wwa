/**
 * 
 */
package eir.world.unit;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.Manifold;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.physics.box2d.WorldManifold;
import com.badlogic.gdx.physics.box2d.joints.RevoluteJoint;
import com.badlogic.gdx.physics.box2d.joints.RevoluteJointDef;

import eir.resources.GameFactory;
import eir.world.Asteroid;
import eir.world.environment.NavNode;

/**
 * @author dveyarangi
 *
 */
public class Spider
{
	private static final float size = 10;
	
	private Vector2 position;
	
	/**
	 * currently traversed asteroid, maybe null in transition
	 */
	private Asteroid asteroid;
	
	private float speed = 20f;

	
	private float surfaceIdx;
	
	private Sprite sprite;
	
	
	public Spider(GameFactory factory, Asteroid asteroid, float x, float y)
	{
		this.asteroid = asteroid;
		position = new Vector2();
		asteroid.getModel().getSurfacePoint( surfaceIdx, position );
		
		sprite = new Sprite(factory.loadTexture( "models/spider_placeholder.png" ));
		sprite.setOrigin( sprite.getWidth()/2, sprite.getHeight()/2 );
		sprite.setScale( size / sprite.getWidth() );
		
	}
	
	public void update(float delta)
	{
		surfaceIdx = asteroid.getModel().getStepSurfaceIndex( surfaceIdx, delta*speed );
		asteroid.getModel().getSurfacePoint( surfaceIdx, position );
	}

	/**
	 * Debug rendering method
	 * @param shape
	 */
	public void draw(SpriteBatch batch)
	{
		sprite.setPosition( position.x-sprite.getOriginX(), position.y-sprite.getOriginY() );
		sprite.setRotation( asteroid.getModel().getNormal(surfaceIdx).angle() + 90 );
		sprite.draw( batch );
	}
}
