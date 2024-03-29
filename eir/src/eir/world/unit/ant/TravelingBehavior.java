package eir.world.unit.ant;

import eir.world.environment.nav.NavEdge;
import eir.world.environment.nav.SurfaceNavNode;
import eir.world.unit.UnitBehavior;
import eir.world.unit.ai.Task;

public abstract class TravelingBehavior implements UnitBehavior <Ant>
{

	public static class TravelToSourceBehavior extends TravelingBehavior
	{
		@Override
		public void update(final float delta, final Task task, final Ant ant)
		{
			travelTo( delta, task, ant, (SurfaceNavNode) task.getOrder().getSource() );
		}
	}

	public static class TravelToTargetBehavior extends TravelingBehavior
	{
		@Override
		public void update(final float delta, final Task task, final Ant ant)
		{
			travelTo( delta, task, ant, (SurfaceNavNode) task.getOrder().getTarget() );
		}
	}

	protected void travelTo( final float delta, final Task task, final Ant ant, final SurfaceNavNode targetNode )
	{
		if(ant.nextNode == null)
		{
			// either we reached next node, or we do not have target
			if( ant.route != null)
			{
				ant.route.recycle();
			}

			ant.route = ant.mesh.getShortestRoute(ant.anchor, targetNode);

			if(!ant.route.hasNext())
			{
				ant.route.recycle();
				task.nextStage();
				ant.route = null;
				return;
			}
			else {
				ant.nextNode = ant.route.next(); // picking next
			}

			ant.velocity.set( ant.nextNode.getPoint() ).sub( ant.getBody().getAnchor() ).nor().mul( ant.getMaxSpeed() );
			ant.angle = ant.velocity.angle();
		}

		//////////////////////////////////////
		// traversing

		NavEdge edge = ant.mesh.getEdge( ant.anchor, ant.nextNode );

		float travelDistance = ant.speed * delta + // the real travel distance
				ant.nodeOffset;

		if(edge == null)
		{
			ant.nextNode = null;
			return;
		}

		while(travelDistance > 0)
		{
			travelDistance -= edge.getLength();
			if(travelDistance < 0)
			{
				ant.velocity.set( ant.nextNode.getPoint() ).sub( ant.anchor.getPoint() ).nor().mul( ant.speed );
				ant.angle = ant.velocity.angle();
				break;
			}

			ant.anchor = ant.nextNode;

			if( ant.route == null || !ant.route.hasNext() )
			{
				ant.nextNode = null;
				travelDistance = -edge.getLength();
				if( ant.route != null )
				{
					task.nextStage();
					ant.route.recycle();
					ant.route = null;
				}
				break;
			}

			ant.nextNode = ant.route.next();


			edge = ant.mesh.getEdge( ant.anchor, ant.nextNode );
			if(edge == null)
			{
				task.setCanceled();
				ant.nextNode = null;
				return;
			}

		}

		if(ant.nextNode != null)
		{
			ant.nodeOffset = edge.getLength()+travelDistance;
			if(ant.nodeOffset < 0)
			{
				ant.nodeOffset = 0;
			} else
			if(ant.nodeOffset > edge.getLength())
			{
				ant.nodeOffset = edge.getLength();
			}
		}
		else
		{
			ant.nodeOffset = 0;
//			task.cancel();
		}

		ant.getBody().getAnchor().set(
				edge.getDirection() ).mul( ant.nodeOffset ).add( ant.anchor.getPoint() );
	}


}
