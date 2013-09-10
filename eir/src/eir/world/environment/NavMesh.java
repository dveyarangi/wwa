/**
 * 
 */
package eir.world.environment;

import java.util.LinkedList;
import java.util.List;

import com.badlogic.gdx.math.Vector2;

/**
 * @author dveyarangi
 *
 */
public class NavMesh
{
	private List <NavNode> nodes;
	
	public NavMesh()
	{
		nodes = new LinkedList <NavNode> ();
	}
	
	public NavNode insertNode(Vector2 point)
	{
		NavNode node = new NavNode(point);
		nodes.add( node );
		return node;
	}
	
	public void linkNodes(NavNode na, NavNode nb)
	{
		na.addNeighbour(nb);
		nb.addNeighbour(na);
	}
}
