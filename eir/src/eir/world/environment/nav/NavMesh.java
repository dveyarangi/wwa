/**
 *
 */
package eir.world.environment.nav;

import gnu.trove.iterator.TIntObjectIterator;
import gnu.trove.map.hash.TIntObjectHashMap;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import yarangi.math.IVector2D;
import yarangi.math.Vector2D;

import com.badlogic.gdx.math.Vector2;

/**
 * @author dveyarangi
 *
 */
public abstract class NavMesh <N extends NavNode>
{
	/**
	 * List of all participating nodes
	 */
	protected ArrayList <N> nodes;

	/**
	 * i add comment on your thingies too!!
	 */
	protected TIntObjectHashMap<NavEdge <N>> edges;

	private static final int MAX_NODES = 1000000;

	public NavMesh()
	{
		nodes = new ArrayList <N> ();
		edges = new TIntObjectHashMap <NavEdge <N>> ();
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
	public abstract Route <N> getShortestRoute( N from, N to );

	/**
	 * Count of all nodes in mesh
	 */
	public int getNodesNum() { return nodes.size(); }

	/**
	 * Retrieves node at specified index, with indices in [0, #getNodesNum()]
	 * @param idx
	 * @return
	 */
	public N getNode(final int idx) { return nodes.get( idx ); }

	public N insertNode(final NavNodeDescriptor descriptor, final Vector2 point)
	{
		if(nodes.size() >= MAX_NODES) // sanity; overflow may break edges mapping
			throw new IllegalStateException("Reached max node capacity.");

		N node = createNavNode(descriptor, point, nodes.size());

		nodes.add( node );
		return node;
	}

	protected abstract N createNavNode(NavNodeDescriptor descriptor, Vector2 point, int nodeIdx);

	/**
	 * Adds edge between specified nodes.
	 * Does not cause nav mesh recalculation, dat is #update()'s task
	 * @param na
	 * @param nb
	 * @param type
	 */
	public void linkNodes( final N na, final N nb, final NavEdge.Type type )
	{
		na.addNeighbour(nb);
		nb.addNeighbour(na);

		int edgeIdx = getEdgeIdx(na.idx, nb.idx);
		if(!edges.contains( edgeIdx ))
		{
			edges.put( edgeIdx, new NavEdge <N> ( na, nb, type ) );
			edges.put( getEdgeIdx(nb.idx, na.idx), new NavEdge <N> ( nb, na, type ) );
		}
	}

	/**
	 * Adds edge between specified nodes.
	 * Does not cause nav mesh recalculation, dat is #update()'s task
	 * @param na
	 * @param nb
	 */
	public boolean unlinkNodes(final N na, final N nb)
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

	public NavEdge <N> getEdge(final int node1Idx, final int node2Idx)
	{
		return edges.get( getEdgeIdx( node1Idx, node2Idx ) );
	}

	public NavEdge <N> getEdge(final N na, final N nb)
	{
		return getEdge( na.idx, nb.idx );
	}


	protected final int getEdgeIdx(final int node1Idx, final int node2Idx)
	{
		return node1Idx + (node2Idx+1) * MAX_NODES;
	}

	public TIntObjectIterator<NavEdge<N>> getEdgesIterator()
	{
		return edges.iterator();
	}

	public List <IVector2D> asVectors()
	{
		List <IVector2D> vectors = new LinkedList <IVector2D> ();

		for(int nidx = 0; nidx < getNodesNum(); nidx ++)
		{
			vectors.add(Vector2D.R(getNode(nidx).getPoint().x, getNode(nidx).getPoint().y));
		}

		return vectors;
	}

}
