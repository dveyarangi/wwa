package yarangi.math.triangulation;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

import yarangi.math.Edge;
import yarangi.math.IVector2D;
import yarangi.math.UnorderedPair;

public class TriangleStore
{
	private HashMap	<Edge, IVector2D> edgeToPoint;

	private Set <UnorderedPair<IVector2D>> links;

	public TriangleStore()
	{
		this.edgeToPoint = new HashMap <Edge, IVector2D> ();
		links = new HashSet <UnorderedPair<IVector2D>> ();
	}

	/**
	 * Add a triangle entry, CW
	 * @param a
	 * @param b
	 * @param c
	 */
	public void addTriangle(IVector2D a, IVector2D b, IVector2D c)
	{
		links.add( new UnorderedPair <IVector2D> (a, b) );
		links.add( new UnorderedPair <IVector2D> (b, c) );
		links.add( new UnorderedPair <IVector2D> (c, a) );
//		edgeToPoint.put( new Edge(a, b), c ); 
//		edgeToPoint.put( new Edge(b, c), a );
//		edgeToPoint.put( new Edge(c, a), b );
	}

	/**
	 * Remove a triangle entry, CW
	 * @param a
	 * @param b
	 * @param c
	 */
	public void removeTriangles(IVector2D a, IVector2D b)
	{
		links.remove( new UnorderedPair<IVector2D>( a, b ) );

/*		Edge edge1 = new Edge(a, b);
		Edge edge2 = new Edge(b ,a);
		
		edgeToPoint.remove( edge1 );
		IVector2D c1 = edgeToPoint.get( edge1 );
		if(c1 != null)
		{
			edgeToPoint.remove( new Edge(b, c1) ); 
			edgeToPoint.remove( new Edge(c1, a) );
		}
		
		edgeToPoint.remove( edge2 );
		IVector2D c2 = edgeToPoint.get( edge2 );
		if(c2 != null)
		{
			edgeToPoint.remove( new Edge(a, c2) ); 
			edgeToPoint.remove( new Edge(c2, b) );
		}*/
	}


	public IVector2D getCCW(IVector2D a, IVector2D b)
	{
		return edgeToPoint.get( new Edge(a, b) );
	}

	public Set <Edge> getEdges() { return edgeToPoint.keySet(); }

	public Set <UnorderedPair<IVector2D>> getLinks() { return links; }
}