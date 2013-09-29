/**
 * 
 */
package eir.world.environment;

import eir.world.Asteroid;
import gnu.trove.iterator.TIntObjectIterator;
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
	protected ArrayList<int[]> indexRange;
	
	private int[] cur;
	
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
		indexRange = new ArrayList<int[]>();
	}
	
	public abstract void init();
	
	public int getNodesNum() { return nodes.size(); }
	
	public NavNode getNode(int idx) { return nodes.get( idx ); }
	
	/**
	 *  call when start adding nodes belonging to same batch
	 */
	public void beginAsteroid()
	{
		cur = new int[2];
		cur[0] = nextNodeIndex;
	}
	
	/**
	 * call when finished adding  nodes belonging to same batch
	 */
	public void endAsteroid()
	{
		cur[1] = nextNodeIndex;
		indexRange.add(cur);
		cur = null;
	}
	
	public NavNode insertNode(Asteroid asteroid, int idx, Vector2 point, Vector2 rawPoint)
	{
		if(nodes.size() >= MAX_NODES) // sanity; overflow may break edges mapping
			throw new IllegalStateException("Reached max node capacity.");
		
		NavNode node = new NavNode(asteroid, idx, point, rawPoint, nextNodeIndex++);
		nodes.add( node );
		return node;
	}
	
	public void linkNodes( NavNode na, NavNode nb, NavEdge.Type type )
	{
		na.addNeighbour(nb);
		nb.addNeighbour(na);
		
		int edgeIdx = getEdgeIdx(na.idx, nb.idx);
		if(!edges.contains( edgeIdx ))
		{
			edges.put( edgeIdx, new NavEdge( na, nb, type ) );
			edges.put( getEdgeIdx(nb.idx, na.idx), new NavEdge( nb, na, type ) );
		}
	}

	/**
	 * @param sourceNode
	 * @param targetNode
	 */
	public void unlinkNodes(NavNode na, NavNode nb)
	{
		na.removeNeighbour(nb);
		nb.removeNeighbour(na);
		int edgeIdx = getEdgeIdx(na.idx, nb.idx);
		if(edges.contains( edgeIdx ))
		{
			edges.remove( edgeIdx );
			edges.remove( getEdgeIdx(nb.idx, na.idx) );
		}
		
	}	
	public NavEdge getEdge(int node1Idx, int node2Idx)
	{
		return edges.get( getEdgeIdx( node1Idx, node2Idx ) );
	}
	
	public NavEdge getEdge(NavNode na, NavNode nb)
	{
		return getEdge( na.idx, nb.idx );
	}
	
	public abstract FloydWarshalRoute getShortestRoute( NavNode from, NavNode to );


	protected final int getEdgeIdx(int node1Idx, int node2Idx)
	{
		return node1Idx + (node2Idx+1) * MAX_NODES;
	}
	
	public TIntObjectIterator<NavEdge> getEdgesIterator()
	{
		return edges.iterator();
	}

}
