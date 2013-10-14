/**
 * 
 */
package eir.world.unit.spider;

import java.util.Deque;
import java.util.LinkedList;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.Vector2;

import eir.resources.GameFactory;
import eir.resources.PolygonalModel;

/**
 * @author dveyarangi
 *
 */
public class Chassis
{
	private Spider spider;
	
	private static final int LEGS = 8;
	
	private Vector2 position;
	
	private Vector2 leftLegJoint;
	private Vector2 rightLegJoint;
	
	private Deque <Leg> legs = new LinkedList <Leg> ();
	
	private Leg lastLeg;
	
	private static float LEG_DISTANCE = 3.5f;
	private static float STEP_DISTANCE = LEGS/2 * LEG_DISTANCE;

	public Chassis (Spider spider, Vector2 position)
	{
		this.spider = spider;
		
		this.position = position;
		leftLegJoint = new Vector2();
		rightLegJoint = new Vector2();
		
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
		position.set( 0,0 );
		for(Leg leg : legs)
		{
			offset.add( Vector2.tmp2.set( leg.getAncleJoint() ).sub( leg.getToeJoint() ) );
			position.add( leg.getAncleJoint() );
		}
		
		position.div( LEGS ).add( offset.div(LEGS).mul( 4 ) );
		leftLegJoint.set(position); 
		rightLegJoint.set(position); 

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
	
	Vector2 getPosition() { return position; }
}
