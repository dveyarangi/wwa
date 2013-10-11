/**
 * 
 */
package eir.world.unit.spider;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.Vector2;

import eir.util.ParametricLine;
import eir.util.Triangles;
import eir.world.Asteroid;

/**
 * @author dveyarangi
 *
 */
public class Leg
{
	
	private Spider spider;
	
	private Vector2 bodyJoint;
	
	private float kneeLength;
	private Vector2 kneeJoint = new Vector2();
	
	private float ancleLength;
	private Vector2 ancleJoint = new Vector2();
	
	
	private float toeLength;
	private Vector2 toeJoint = new Vector2();
	
	private float lastSurfaceIdx;
	private float targetSurfaceIdx;

	
	private float stepTime = 0;
	
	private boolean isStepping = false;

	private float stepIdx = 0;
	
	private ParametricLine toeTrajectory = new ParametricLine();
	private ParametricLine ancleTrajectory = new ParametricLine();
	
	private static final float STEP_SPEED = 10f;
	
	private boolean isLeft;
	
//	private float toeLength;
//	private Vector2 toeJoint = new Vector2();
	
	public Leg(Spider spider, Vector2 bodyJoint, float surfaceIdx, boolean isLeft)
	{
		this.spider = spider;
		
		// dat joint is controlled from outside by spider body movement
		this.bodyJoint = bodyJoint;
		
		this.kneeLength = 10;
		this.ancleLength = 10;
		this.toeLength = 1;
		
		this.isLeft = isLeft;
		
		Asteroid asteroid = spider.getAsteroid();
		
		asteroid.getModel().getSurfacePoint( surfaceIdx, toeJoint );
		Vector2 normal = asteroid.getModel().getNormal( surfaceIdx ).tmp();
		
		ancleJoint.set( normal.mul( -toeLength ).add( toeJoint ) );
		
		this.lastSurfaceIdx = surfaceIdx;
	}
	
	public void startStepTo(float surfaceIdx)
	{
		if(isStepping)
			throw new IllegalStateException("Already is stepping process.");
		
		isStepping = true;
		
		Asteroid asteroid = spider.getAsteroid();
	
		Vector2 stepTarget = Vector2.tmp;
		
		// acquiring target
		asteroid.getModel().getSurfacePoint( surfaceIdx, stepTarget );
		
		targetSurfaceIdx = surfaceIdx;
		
		stepIdx = 0;
		
		toeTrajectory.set( toeJoint, stepTarget );

		Vector2 normal = Vector2.tmp2.set( asteroid.getModel().getNormal( surfaceIdx ) );
		ancleTrajectory.set( ancleJoint, normal.mul( -toeLength ).add( stepTarget ) );

	}
	
	public void update(float delta)
	{
		if(isStepping)
		{
			stepIdx += delta * STEP_SPEED;
			if(stepIdx >= 1)
			{
				stepIdx = 1;
				isStepping = false;
				lastSurfaceIdx = targetSurfaceIdx;
				targetSurfaceIdx = 0;
			}
			toeTrajectory.at( toeJoint, stepIdx );
			ancleTrajectory.at( ancleJoint, stepIdx);
		}
		
		// adjusting knee position:
		Triangles.calcFromVertices( ancleJoint, kneeLength, bodyJoint, ancleLength, kneeJoint, isLeft );
	}
	
	
	public void draw(SpriteBatch batch, ShapeRenderer shape)
	{
		
		shape.begin( ShapeType.Line );
		
		shape.setColor( 1.0f, 0.0f, 0.0f, 1.0f );
			shape.line( bodyJoint.x, bodyJoint.y, kneeJoint.x, kneeJoint.y );
			shape.line( kneeJoint.x, kneeJoint.y, ancleJoint.x, ancleJoint.y );
			shape.line( ancleJoint.x, ancleJoint.y, toeJoint.x, toeJoint.y );
			
		shape.end();
	}

	/**
	 * @return
	 */
	public boolean isStepping()
	{
		return isStepping;
	}

	/**
	 * @return
	 */
	public float getSurfaceIdx()
	{
		return lastSurfaceIdx;
	}

	/**
	 * @return
	 */
	public Vector2 getAncleJoint()
	{
		return ancleJoint;
	}

	/**
	 * @return
	 */
	public Vector2 getToeJoint()
	{
		return toeJoint;
	}
}