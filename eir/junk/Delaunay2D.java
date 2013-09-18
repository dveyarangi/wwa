package yarangi.math.triangulation;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import yarangi.math.Geometry;
import yarangi.math.IVector2D;
import yarangi.math.LexicographicComparator;
import yarangi.math.Pair;
import yarangi.math.Vector2D;

/**
 * Divide and conquer implementation of Delaunay triangulation.
 * <a href="http://www.personal.psu.edu/faculty/c/x/cxc11/AERSP560/DELAUNEY/13_Two_algorithms_Delauney.pdf">
 */
public class Delaunay2D implements ITriangulator
{

	private static class BilistElement 
	{
		private IVector2D element;
		private BilistElement next;
		private BilistElement prev;

		private ArrayList <BilistElement> adjacency; 

		public BilistElement(IVector2D element) 
		{
			this.element = element;
			this.adjacency = new ArrayList <BilistElement> ();
		}
		private int findInsertIdx(BilistElement element)
		{
			int idx = 0;
			while(idx < adjacency.size() )
			{
				if(cwFrom(this.element, adjacency.get( idx ).element, element.element)
				&& ccwFrom(this.element, adjacency.get( (idx+1) % adjacency.size() ).element, element.element))
					return idx+1;
				idx ++;
			}

			if(adjacency.size() > 0 && ccwFrom(this.element, adjacency.get( 0 ).element, element.element))
				return 0;
			return adjacency.size();

		}

		public void insert(BilistElement element)
		{

			adjacency.add(findInsertIdx(element), element);
//			element.adjacency.add( element );
		}

		public void remove(BilistElement element)
		{
			adjacency.remove( element );
		}

		public BilistElement PRED(BilistElement v)
		{
			int idx = adjacency.indexOf( v );
			if(idx == -1)
				idx = findInsertIdx( v );
			else
				idx = (idx + 1);
			return adjacency.get( (idx) % adjacency.size());
		}

		public BilistElement SUCC(BilistElement v)
		{
			int idx = adjacency.indexOf( v );
			if(idx == -1)
				idx = findInsertIdx( v );
			return adjacency.get( (idx-1+adjacency.size()) % adjacency.size() );
		}

		@Override
		public String toString() { return element.toString(); }

		@Override
		public boolean equals(Object object)
		{
			if(this == object)
				return true;
			if(object == null)
				return false;
			if(!(object instanceof BilistElement))
				return false;

			BilistElement that = (BilistElement) object;
			return that.element.equals( this.element );
		}

		@Override
		public int hashCode()
		{
			return element.hashCode();
		}
	}

	private static class Tangent extends Pair <BilistElement>
	{

		public Tangent(BilistElement first, BilistElement second)
		{
			super( first, second );
		}

	}
	private static void INSERT(BilistElement v1, BilistElement v2)
	{
		v1.insert( v2 );
		v2.insert( v1 );
	}

	private static void REMOVE(BilistElement v1, BilistElement v2)
	{
		v1.remove( v2 );
		v2.remove( v1 );
	}

	private static BilistElement SUCC(BilistElement v1, BilistElement v2)
	{
		return v1.SUCC( v2 );
	}

	private static BilistElement PRED(BilistElement v1, BilistElement v2)
	{
		return v1.PRED( v2 );
	}


	@Override
	public TriangleStore triangulate(List <IVector2D> points)
	{
		if(points.size() <= 2)
			return null;
		// converting to array list, as it is faster to split it:
		ArrayList <IVector2D> nodes = new ArrayList <IVector2D> (points.size());

		for(Iterator <IVector2D> it = points.iterator(); it.hasNext(); )
			nodes.add( it.next() );


		Collections.sort( nodes, new LexicographicComparator() );

		TriangleStore store = new TriangleStore();

		triangulate(nodes, store);

		return store;
	}

	/**
	 * This method first binary decomposes the sorted points list to primitive hulls (triangles or lines).
	 * 
	 * @param points
	 * @param min
	 * @param result
	 * @return
	 */
	private Tangent triangulate(List <IVector2D> points, TriangleStore store)
	{
		// STOP condition: hull combined of two points:
		if(points.size() == 2)
		{

			// just linking the two points bidirectionally:
			BilistElement leftmost  = new BilistElement( points.get( 0 ) );
			BilistElement rightmost = new BilistElement( points.get( 1 ) );

			leftmost.next = rightmost;
			leftmost.prev = rightmost;
			rightmost.prev = leftmost;
			rightmost.next = leftmost;

			INSERT(leftmost, rightmost);

			// no triangle is added to store

			return new Tangent (leftmost, rightmost);
		}

		// STOP condition: triangle hull:
		if(points.size() == 3)
		{

			// testing if the middle point is cw or ccw to the leftmost-rightmost tangent:

			BilistElement leftmost   = new BilistElement( points.get( 0 ) );
			BilistElement middlemost = new BilistElement( points.get( 1 ) );
			BilistElement rightmost  = new BilistElement( points.get( 2 ) );

			// checking crossproduct
			boolean midAbove = ccwFrom( leftmost.element, rightmost.element, middlemost.element );

			// ordering clockwise:
			if(midAbove)
			{
				leftmost  .next = middlemost; leftmost  .prev = rightmost;
				rightmost .next = leftmost;   rightmost .prev = middlemost;
				middlemost.next = rightmost;  middlemost.prev = leftmost; 

				store.addTriangle( leftmost.element, rightmost.element, middlemost.element );
			}
			else
			{
				leftmost  .next = rightmost;   leftmost  .prev = middlemost;
				rightmost .next = middlemost;  rightmost .prev = leftmost;
				middlemost.next = leftmost;    middlemost.prev = rightmost;
				store.addTriangle( leftmost.element, middlemost.element, rightmost.element );
			}

			INSERT(leftmost, middlemost);
			INSERT(rightmost, middlemost);
			INSERT(leftmost, rightmost);

			return new Tangent (leftmost, rightmost);
		}

		//////////////////////////////////////////////////////////
		// collecting sub-triangulations:
		//////////////////////////////////////////////////////////

		Tangent leftPair  = triangulate( points.subList( 0, points.size()/2 ),             store );
		Tangent rightPair = triangulate( points.subList( points.size()/2, points.size() ), store );

		//////////////////////////////////////////////////////////
		// merging convex hulls
		//////////////////////////////////////////////////////////


		//////////////////////////////////////////////////////////
		// looking for upper common tangent
		Tangent UT = getUpperCommonTangent( leftPair, rightPair );

		//////////////////////////////////////////////////////////
		// looking for lower common tangent
		Tangent BT = getLowerCommonTangent( leftPair, rightPair );

		// completing hull merge:
		UT.first().next = UT.second();
		UT.second().prev = UT.first();

		BT.first().prev = BT.second();
		BT.second().next = BT.first();


		boolean A, B;

		BilistElement L = BT.first();
		BilistElement R = BT.second();

//		INSERT(L, R);

		while(L != UT.first() || R != UT.second())
		{
			A = B = false;
			INSERT(L, R);
			BilistElement R1 = PRED(R, L);
			if(ccwFrom( L.element, R.element, R1.element ))
			{
				BilistElement R2 = PRED(R, R1);
				while(L != R2 && !Geometry.pointInCircumCirle( R2.element, L.element, R1.element, R.element))
				{
					REMOVE( R, R1 );
					store.removeTriangles( R.element, R1.element );
					R1 = R2;
					R2 = PRED(R, R1);
				}
			}
			else
				A = true;

			BilistElement L1 = SUCC(L, R);
			if(cwFrom(R.element, L.element, L1.element))
			{
				BilistElement L2 = SUCC(L, L1);
				while(R != L2 && !Geometry.pointInCircumCirle(  L2.element, L.element,  L1.element, R.element))
				{
					REMOVE( L, L1 );
					store.removeTriangles( L.element, L1.element );
					L1 = L2;
					L2 = SUCC( L, L1 );
				}
			}
			else 
				B = true;

			if(A)
			{
				store.addTriangle( L.element, L1.element, R.element );
				L = L1;
			}
			else
			if(B)
			{
				store.addTriangle( L.element, R1.element, R.element );
				R = R1;
			}
			else
			if(Geometry.pointInCircumCirle( L1.element, L.element, R1.element, R.element))
			{
				store.addTriangle( L.element, R1.element, R.element );
				R = R1;
			}
			else
			{
				store.addTriangle( L.element, L1.element, R.element );
				L = L1;
			}
		}

		INSERT(R, L);


		return new Tangent( leftPair.first(), rightPair.second() );

	}

	/**
	 * Given two 
	 * @param leftPair
	 * @param rightPair
	 * @return
	 */
	private Tangent getUpperCommonTangent(Tangent leftHullMaxima, Tangent rightHullMaxima)
	{
		BilistElement upperRight = rightHullMaxima.first();
		BilistElement upperLeft = leftHullMaxima.second();

		//////////////////////////////////////////////////////////
		// looking for upper common tangent of the convex hulls of the sub-triangulations:
		// to do this, we are starting from the  edge formed by the hulls' x-closest vertices
		// then step by step ascending on the both hulls 
		// (CCW on the left one and CW on the right one, forming a candidate tangent.
		// If the candidate is "higher" than the previous one, replacing the upper common tanged
		// vertices to the new ones:
		boolean leftAscended, rightAscended;
		do
		{
			leftAscended = rightAscended = false;
			if( cwFrom(upperRight.element, upperLeft.element, upperLeft.prev.element) )
			{
				upperLeft = upperLeft.prev;
				leftAscended = true;
			}

			if( ccwFrom(upperLeft.element, upperRight.element, upperRight.next.element) )
			{
				upperRight = upperRight.next;
				rightAscended = true;
			}
		}
		while( rightAscended || leftAscended);

		return new Tangent (upperLeft, upperRight);
	}

	private Tangent getLowerCommonTangent(Tangent leftHullMaxima, Tangent rightHullMaxima)
	{
		BilistElement lowerRight = rightHullMaxima.first();
		BilistElement lowerLeft = leftHullMaxima.second();

		boolean leftDescended, rightDescended;
		do
		{
			leftDescended = rightDescended = false;
			if( ccwFrom(lowerRight.element, lowerLeft.element, lowerLeft.next.element) )
			{
				lowerLeft = lowerLeft.next;
				leftDescended = true;
			}

			if( cwFrom(lowerLeft.element, lowerRight.element, lowerRight.prev.element) )
			{
				lowerRight = lowerRight.prev;
				rightDescended = true;
			}
		}
		while( rightDescended || leftDescended);

		return new Tangent (lowerLeft, lowerRight);
	}


	protected void mergeHull()
	{

	}

	/**
	 * checks either the (p - a) vector is clockwise-rotated in relation to (b - a) vector
	 * @param a
	 * @param b
	 * @param p
	 * @return
	 */
	public static boolean ccwFrom(IVector2D a, IVector2D b, IVector2D p)
	{
		return Vector2D.crossZComponent( b.x() - a.x(), b.y() - a.y(), p.x() - a.x(), p.y() - a.y() ) > 0;
	}
	public static boolean cwFrom(IVector2D a, IVector2D b, IVector2D p)
	{
		return Vector2D.crossZComponent( b.x() - a.x(), b.y() - a.y(), p.x() - a.x(), p.y() - a.y() ) < 0;
	}	
	public static void main(String ... args)
	{
		Delaunay2D triang = new Delaunay2D();

		List <IVector2D> points = new ArrayList <IVector2D> ();

		points.add( Vector2D.R(-3,0) );
		points.add( Vector2D.R(-2,2) );
		points.add( Vector2D.R(-1, -2) );
		points.add( Vector2D.R(1,2) );
		points.add( Vector2D.R(3,0) );
		points.add( Vector2D.R(-2,0.5) );
		points.add( Vector2D.R(1,-0.5) );

		Collections.sort( points, new LexicographicComparator() );

		TriangleStore store = new TriangleStore();


		Tangent hull = triang.triangulate( points, store );
		System.out.println(hull);

/*		BilistElement element = new BilistElement( Vector2D.R(-1.0, -2.0) );
		element.insert( new BilistElement( Vector2D.R(1.0, -0.5) ) );
		element.insert( new BilistElement( Vector2D.R(3.0, 0.0) ) );
		element.insert( new BilistElement( Vector2D.R(1.0, 2.0) ) );
		element.insert( new BilistElement( Vector2D.R(-3.0, 0.0) ) );*/
	}
}