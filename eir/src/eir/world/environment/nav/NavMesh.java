/**
 * 
 */
package eir.world.environment.nav;

import gnu.trove.iterator.TIntObjectIterator;
import gnu.trove.map.hash.TIntObjectHashMap;

import java.util.ArrayList;

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
	protected ArrayList<NavNode> nodes;
	
	/**
	 * array of index range (low,high) for each registered asteroid
	 */
	protected ArrayList<int[]> indexRange;
	
	private int[] cur;
	
	/**
	 * i add comment on your thingies too!!
	 */
	protected TIntObjectHashMap<NavEdge> edges;
	
	private static final int MAX_NODES = 1000000;
	
	public NavMesh()
	{
		nodes = new ArrayList<NavNode> ();
		edges = new TIntObjectHashMap <NavEdge> ();
		indexRange = new ArrayList<int[]>();
	}

	/**
	 * Called to calculate the nav mesh
	 */
	public abstract void init();
	
	/**
	 * Called to update mesh on change.
	 */
	public abstract void update();

	/**
	 * Creates iterator over shortest route from to
	 */
	public abstract Route getShortestRoute( NavNode from, NavNode to );

	/**
	 * Count of all nodes in mesh 
	 */
	public int getNodesNum() { return nodes.size(); }
	
	/**
	 * Retrieves node at specified index, with indices in [0, #getNodesNum()]
	 * @param idx
	 * @return
	 */
	public NavNode getNode(int idx) { return nodes.get( idx ); }
	
	/**
	 *  call when start adding nodes belonging to same batch
	 */
	public void beginAsteroid()
	{
		cur = new int[2];
		cur[0] = nodes.size();
	}
	
	/**
	 * call when finished adding  nodes belonging to same batch
	 */
	public void endAsteroid()
	{
		cur[1] = nodes.size()-1;
		indexRange.add(cur);
		cur = null;
	}
	
	public NavNode insertNode(NavNodeDescriptor descriptor, Vector2 point, Vector2 rawPoint)
	{
		if(nodes.size() >= MAX_NODES) // sanity; overflow may break edges mapping
			throw new IllegalStateException("Reached max node capacity.");
		
		if( cur==null )
			throw new IllegalStateException("May not add new nodes outside of asteroid context "
					+ "(must be between beginAsteroid and endAsteroid calls) ");
		
		NavNode node = new NavNode(descriptor, point, rawPoint, nodes.size(), indexRange.size());
		
		nodes.add( node );
		return node;
	}
	
	/** 
	 * Adds edge between specified nodes.
	 * Does not cause nav mesh recalculation, dat is #update()'s task
	 * @param na
	 * @param nb
	 * @param type
	 */
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
	 * Adds edge between specified nodes.
	 * Does not cause nav mesh recalculation, dat is #update()'s task
	 * @param na
	 * @param nb
	 */
	public boolean unlinkNodes(NavNode na, NavNode nb)
	{
		na.removeNeighbour(nb);
		nb.removeNeighbour(na);
		int edgeIdx = getEdgeIdx(na.idx, nb.idx);
		if(edges.contains( edgeIdx ))
		{
			edges.remove( edgeIdx );
			edges.remove( getEdgeIdx(nb.idx, na.idx) );
			return true;
		}
		
		return false;
	}	
	
	public NavEdge getEdge(int node1Idx, int node2Idx)
	{
		return edges.get( getEdgeIdx( node1Idx, node2Idx ) );
	}
	
	public NavEdge getEdge(NavNode na, NavNode nb)
	{
		return getEdge( na.idx, nb.idx );
	}


	protected final int getEdgeIdx(int node1Idx, int node2Idx)
	{
		return node1Idx + (node2Idx+1) * MAX_NODES;
	}
	
	public TIntObjectIterator<NavEdge> getEdgesIterator()
	{
		return edges.iterator();
	}

}
