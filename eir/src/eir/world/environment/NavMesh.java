/**
 * 
 */
package eir.world.environment;

import java.util.ArrayList;
import java.util.List;

import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.Vector2;

/**
 * @author dveyarangi
 *
 */
public class NavMesh
{
	protected List <NavNode> nodes;
	private int nextNodeIndex = 0;	
	
	public NavMesh()
	{
		nodes = new ArrayList<NavNode> ();
	}
	
	public NavNode insertNode(Vector2 point)
	{
		NavNode node = new NavNode(point, nextNodeIndex++);
		nodes.add( node );
		return node;
	}
	
	public void linkNodes(NavNode na, NavNode nb)
	{
		na.addNeighbour(nb);
		nb.addNeighbour(na);
	}
	
	
	/**
	 * Debug rendering method
	 * @param shape
	 */
	public void draw(ShapeRenderer shape)
	{
		shape.setColor( 0, 1, 0, 0.5f );
		for(NavNode srcNode : nodes)
		{
			shape.begin(ShapeType.Circle);
				shape.circle( srcNode.getPoint().x, srcNode.getPoint().y, 1 );
			shape.end();
			
			for(NavNode tarNode : srcNode.getNeighbors())
			{
				shape.begin(ShapeType.Line);
					shape.line( srcNode.getPoint().x, srcNode.getPoint().y, tarNode.getPoint().x, tarNode.getPoint().y);
				shape.end();
			}
		}
		
	}
}
