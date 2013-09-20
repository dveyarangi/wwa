/**
 * 
 */
package eir.world.environment;

import gnu.trove.map.hash.TIntObjectHashMap;

import java.util.ArrayList;
import java.util.List;

import com.badlogic.gdx.math.Vector2;

/**
 * @author dveyarangi
 *
 */
public abstract class NavMesh
{
	/**
	 * List of all participating nodes
	 */
	protected List <NavNode> nodes;
	
	protected TIntObjectHashMap<NavEdge> edges;
	
	private static final int MAX_NODES = 1000000;
	/** 
	 * Node index incremental 
	 */
	private int nextNodeIndex = 0;	
	
	public NavMesh()
	{
		nodes = new ArrayList<NavNode> ();
		edges = new TIntObjectHashMap <NavEdge> ();
	}
	
	public abstract void init();
	
	public int getNodesNum() { return nodes.size(); }
	
	public NavNode getNode(int idx) { return nodes.get( idx ); }
	
	public NavNode insertNode(Vector2 point, Vector2 rawPoint)
	{
		if(nodes.size() >= MAX_NODES) // sanity; overflow may break edges mapping
			throw new IllegalStateException("Reached max node capacity.");
		
		NavNode node = new NavNode(point, rawPoint, nextNodeIndex++);
		nodes.add( node );
		return node;
	}
	
	public void linkNodes(NavNode na, NavNode nb)
	{
		na.addNeighbour(nb);
		nb.addNeighbour(na);
		
		int edgeIdx = getEdgeIdx(na.index, nb.index);
		if(!edges.contains( edgeIdx ))
		{
			edges.put( edgeIdx, new NavEdge( na, nb ) );
			edges.put( getEdgeIdx(nb.index, na.index), new NavEdge( nb, na ) );
		}
	}
	
	public NavEdge getEdge(int node1Idx, int node2Idx)
	{
		int edgeIdx = getEdgeIdx( node1Idx, node2Idx );
/*		if(!edges.contains( edgeIdx ))
		{
			throw new IllegalArgumentException("No edge between node idxs " + node1Idx + " and " + node2Idx );
		}*/
		return edges.get( getEdgeIdx( node1Idx, node2Idx ) );
	}
	public NavEdge getEdge(NavNode na, NavNode nb)
	{
		return getEdge( na.index, nb.index );
	}
	
	public abstract Route getShortestRoute( NavNode from, NavNode to );


	protected final int getEdgeIdx(int node1Idx, int node2Idx)
	{
		return node1Idx + (node2Idx+1) * MAX_NODES;
	}
}
