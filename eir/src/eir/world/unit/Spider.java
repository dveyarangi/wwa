/**
 * 
 */
package eir.world.unit;

import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
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

import eir.world.Asteroid;
import eir.world.environment.NavNode;

/**
 * @author dveyarangi
 *
 */
public class Spider
{
	private static final float size = 5;
	
	private Vector2 position;
	
	/**
	 * currently traversed asteroid, maybe null in transition
	 */
	private Asteroid asteroid;
	
	private float speed = 10f;
	
	private float surfaceIdx;
	
	
	public Spider(Asteroid asteroid, float x, float y)
	{
		this.asteroid = asteroid;
		position = asteroid.getModel().getSurfacePoint( surfaceIdx );
	}
	
	public void update(float delta)
	{
		surfaceIdx = asteroid.getModel().getStepSurfaceIndex( surfaceIdx, delta*speed );
		position = asteroid.getModel().getSurfacePoint( surfaceIdx );
	}

	/**
	 * Debug rendering method
	 * @param shape
	 */
	public void draw(ShapeRenderer shape)
	{
		shape.setColor( 1, 1, 1, 1 );
		shape.begin(ShapeType.Circle);
			shape.circle( position.x, position.y, 5 );
		shape.end();
			
		
	}
}
