/**
 * 
 */
package eir.world.unit.spider;

import java.util.Deque;
import java.util.LinkedList;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;

import eir.resources.PolygonalModel;

/**
 * @author dveyarangi
 *
 */
public class Chassis
{
	private Spider spider;
	
	private static final int LEGS = 8;
	
	private Vector2 targetPosition;
	
	private Vector2 leftLegJoint;
	private Vector2 rightLegJoint;
	
	private Deque <Leg> legs = new LinkedList <Leg> ();
	
	private Leg lastLeg;
	
	private static float LEG_DISTANCE = 3.5f;
	private static float STEP_DISTANCE = LEGS/2 * LEG_DISTANCE;

	public Chassis (Spider spider, Vector2 leftLegJoint, Vector2 rightLegJoint)
	{
		this.spider = spider;
		
		this.targetPosition = new Vector2();
		this.leftLegJoint = leftLegJoint;
		this.rightLegJoint = rightLegJoint;
		
//		legs = new Leg[LEGS];
		PolygonalModel model = spider.getAsteroid().getModel();
		
		for(int idx = LEGS/2; idx >= 1; idx --)
		{
			legs.addFirst( new Leg( spider, leftLegJoint, 
					model.getStepSurfaceIndex(
								spider.getSurfaceIdx(), 3+LEG_DISTANCE*idx ), true) );
			
			legs.addFirst( new Leg( spider, rightLegJoint, 
					model.getStepSurfaceIndex(
							spider.getSurfaceIdx(), -3-LEG_DISTANCE*(LEGS/2-idx) ), false) );
		
			
		}

	}
	
	public void update(float delta)
	{
		
		for(Leg leg : legs)
		{
			leg.update( delta );
		}
		
		Vector2 offset = Vector2.tmp.set(0,0);
		
		targetPosition.set( 0,0 );
		for(Leg leg : legs)
		{
			offset.add( Vector2.tmp2.set( leg.getAncleJoint() ).sub( leg.getToeJoint() ).nor() );
			targetPosition.add( leg.getAncleJoint() );
		}
		
		targetPosition.div( LEGS ).add( offset.div(LEGS).mul( 4 ) );

	}
	
	public void draw(SpriteBatch batch, ShapeRenderer shape)
	{
		for(Leg leg : legs)
		{
			leg.draw( batch, shape );
		}
			
	}

	/**
	 * @param walkCW
	 */
	public void step(boolean walkCW)
	{
		if(lastLeg == null || !lastLeg.isStepping())
		{
			float step;
			for(int i = 0; i < 1; i ++) // legs to move at once
			{
				if(walkCW)
				{
					step = -STEP_DISTANCE;
					lastLeg = legs.pollLast();
					legs.addFirst( lastLeg );
				}
				else
				{
					step = STEP_DISTANCE;
					lastLeg = legs.pollFirst();
					legs.addLast( lastLeg );
				}
				lastLeg.startStepTo( 
						spider.getAsteroid().getModel().getStepSurfaceIndex(
									lastLeg.getSurfaceIdx(), step ) );
			}
		}	
	}
	
	Vector2 getPosition() { return targetPosition; }
}
