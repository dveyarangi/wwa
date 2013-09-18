/**
 * 
 */
package eir.world.environment;

import java.util.ArrayList;
import java.util.List;

import com.badlogic.gdx.math.Vector2;

/**
 * @author dveyarangi
 *
 */
public abstract class NavMesh
{
	protected List <NavNode> nodes;
	private int nextNodeIndex = 0;	
	
	public NavMesh()
	{
		nodes = new ArrayList<NavNode> ();
	}
	
	public abstract void init();
	
	public int getNodesNum() { return nodes.size(); }
	
	public NavNode getNode(int idx) { return nodes.get( idx ); }
	
	public NavNode insertNode(Vector2 point, Vector2 rawPoint)
	{
		NavNode node = new NavNode(point, rawPoint, nextNodeIndex++);
		nodes.add( node );
		return node;
	}
	
	public void linkNodes(NavNode na, NavNode nb)
	{
		na.addNeighbour(nb);
		nb.addNeighbour(na);
	}
	
	public abstract Route getShortestRoute( NavNode from, NavNode to );

}
