package eir.world.unit.ant;

import java.util.Iterator;

import yarangi.numbers.RandomUtil;
import eir.world.environment.NavEdge;
import eir.world.environment.NavNode;
import eir.world.unit.ai.Task;
import eir.world.unit.ai.TaskBehavior;

public abstract class TravelingBehavior implements TaskBehavior 
{
	
	public static class TravelToSourceBehavior extends TravelingBehavior
	{
		@Override
		public void update(float delta, Task task, Ant ant)
		{
			travelTo( delta, task, ant, task.getOrder().getSourceNode() );
		}
	}

	public static class TravelToTargetBehavior extends TravelingBehavior
	{
		@Override
		public void update(float delta, Task task, Ant ant)
		{
			travelTo( delta, task, ant, task.getOrder().getTargetNode() );
		}
	}
	
	protected void travelTo( float delta, Task task, Ant ant, NavNode targetNode )
	{
		if(ant.nextNode == null)
		{
			// either we reached next node, or we do not have target
			// pick a random target
			ant.route = ant.mesh.getShortestRoute( ant.currNode, targetNode );
	
			if(!ant.route.hasNext())
			{
				ant.route.recycle();
				ant.route = null;
			}
			else
				ant.nextNode = ant.route.next(); // picking next
			
			if(ant.nextNode == null)
			{
				int randomNeighIdx = RandomUtil.N( ant.currNode.getNeighbors().size() );
				Iterator <NavNode> it = ant.currNode.getNeighbors().iterator();
				int idx = 0;
				while(idx ++ < randomNeighIdx) it.next(); 
				ant.nextNode = it.next();
			}
	
	
			ant.velocity.set( ant.nextNode.getPoint() ).sub( ant.body.getAnchor() ).nor().mul( ant.speed );			
			ant.angle = ant.velocity.angle();
		}
		
		//////////////////////////////////////
		// traversing
		
		NavEdge edge = ant.mesh.getEdge( ant.currNode, ant.nextNode );
		
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
				ant.velocity.set( ant.nextNode.getPoint() ).sub( ant.currNode.getPoint() ).nor().mul( ant.speed );			
				ant.angle = ant.velocity.angle();
				break;
			}
			
			ant.currNode = ant.nextNode;
			
			if( ant.route == null || !ant.route.hasNext() )
			{
				ant.nextNode = null;
				travelDistance = -edge.getLength();
				if( ant.route != null )
				{
					ant.route.recycle();
					ant.route = null;
				}
				break;
			}
			
			ant.nextNode = ant. route.next();
			
			edge = ant.mesh.getEdge( ant.currNode, ant.nextNode );
			if(edge == null)
			{
//				task.cancel();
				ant.nextNode = null;
				return;
			}

		}
		
		if(ant.nextNode != null)
		{
			ant.nodeOffset = edge.getLength()+travelDistance;
			if(ant.nodeOffset < 0) ant.nodeOffset = 0;
				else
			if(ant.nodeOffset > edge.getLength()) 
				ant.nodeOffset = edge.getLength();
		}
		else
		{
			ant.nodeOffset = 0;
//			task.cancel();
		}
		
		ant.body.getAnchor().set( edge.getDirection() ).mul( ant.nodeOffset ).add( ant.currNode.getPoint() );
	}

}
