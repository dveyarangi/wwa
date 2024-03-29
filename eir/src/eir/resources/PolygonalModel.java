/**
 *
 */
package eir.resources;

import java.util.List;

import com.badlogic.gdx.math.Vector2;

import eir.world.Asteroid;
import eir.world.environment.nav.FloydWarshal;
import eir.world.environment.nav.NavEdge;
import eir.world.environment.nav.NavNodeDescriptor;
import eir.world.environment.nav.SurfaceNavNode;

/**
 * @author dveyarangi
 *
 */
public class PolygonalModel
{
	/**
	 * number of vertices
	 */
	private final int len;

	/**
	 * polygon origin point
	 */
	private final	Vector2 origin;

	/**
	 * scaled, rotated and transposed vertices
	 */
	private final Vector2 [] vertices;

	/**
	 * normals of polygon edges
	 */
	private final Vector2 [] normals;

	/**
	 * directions of polygon edges
	 */

	private final Vector2 [] directions;

	/**
	 * slopes of polygon edges
	 */
	private final float [] slopes;

	/**
	 * lengths of polygon edges
	 */
	private final float [] lengths;

	/**
	 * navigation nodes corresponding to polygon vertices
	 */
	private final SurfaceNavNode [] nodes;

	private float maxSurfaceIdx;


	/**
	 * @param navMesh
	 * @param origin
	 * @param polygons
	 * @param body
	 * @param sprite
	 */
	public PolygonalModel(final FloydWarshal navMesh, final Asteroid asteroid, final Vector2 origin, final Vector2 [] rawVertices, final List<List<Vector2>> polygons)
	{
		super();
		this.origin = origin;

		len = rawVertices.length;

		// preparing stuff for faster nav calculation:
		this.vertices = new Vector2[len];

		// transforming the polygon to real coordinates:
		// the provided vertices array is in (0,1) range
		for(int idx = 0; idx < len; idx ++)
		{
			vertices[idx] = rawVertices[idx].tmp()
					.sub( origin.x, origin.y ) // making relative to origin
					.rotate( asteroid.getAngle() ) // rotating
					.mul( asteroid.getSize() ) // scaling
					.add( asteroid.getPosition() )  // transposing
					.cpy();      // tmp ref is not to be saved!
		}

		// normals for each poly edge:
		normals = new Vector2 [len];
		// directions for each poly edge:
		directions = new Vector2 [len];
		// slopes for each poly edge:
		slopes = new float [len];
		// lengths of poly edges:
		lengths = new float [len];

		nodes = new SurfaceNavNode[len];

		navMesh.beginAsteroid();
		SurfaceNavNode currNode = navMesh.insertNode( new NavNodeDescriptor(asteroid, 0), vertices[0] /*, rawVertices[0]*/ );
		int startingIdx = currNode.getIndex();
		SurfaceNavNode prevNode;
		for(int idx = 0; idx < len; idx ++)
		{
			int nidx = (idx+1)%len;
			Vector2 a = vertices[idx];
			Vector2 b = vertices[nidx];

			directions[idx] = new Vector2( b.x-a.x, b.y-a.y ).nor();

			// TODO: checkout about vertical ones
			slopes[idx] = directions[idx].y / directions[idx].x;

			normals[idx] = b.tmp().sub( a ).nor().rotate( 90 ).cpy();

			lengths[idx] = (float)Math.hypot( b.x-a.x, b.y-a.y );

			nodes[idx] = currNode;
			prevNode = currNode;

			if(nidx != 0)
			{
				currNode = navMesh.insertNode( new NavNodeDescriptor(asteroid, nidx),  b /*, rawVertices[nidx]*/ );
			} else
			{
				currNode = navMesh.getNode(startingIdx);
			}
			navMesh.linkNodes( currNode, prevNode, NavEdge.Type.LAND );

			maxSurfaceIdx ++;
		}
		navMesh.endAsteroid();
	}

	/**
	 * @param navNodeIdx
	 * @return
	 */
	public SurfaceNavNode getNavNode(final int navNodeIdx)
	{
		return nodes[ navNodeIdx >= 0 ? navNodeIdx % getSize() : navNodeIdx+getSize() ];
	}

	public int getNavNodesCount() { return nodes.length; }

	public Vector2 getOrigin()
	{
		return origin;
	}

	public Vector2 [] getVertices() { return vertices; }

	/**
	 * Returns a new index, that is resulting from stepping the specified stepLen from the initial index
	 * @param surfaceIdx
	 * @param stepLen
	 * @return
	 */
	public float getStepSurfaceIndex(final float surfaceIdx, float stepLen)
	{
		int dir = stepLen > 0 ? 1 : -1;
		int idx = (int) surfaceIdx % len;

		// offset scaled by edge length:
		float scaledOffset = ( surfaceIdx - idx ) * lengths[idx];

		// rounding to int surface idx in step direction:
		stepLen -= dir > 0 ? lengths[idx] - scaledOffset : -scaledOffset;
		int nidx = idx;

		// adding edges lenghts until we are past the stepLen:
		// loop is required since step may skip several poly vertices:
		while(dir*stepLen > 0)
		{
			nidx += dir;
			if(nidx >= len)
			{
				nidx = 0;
			} else if(nidx < 0)
			{
				nidx = len-1;
			}

			stepLen -= dir*lengths[nidx];
		}

		float newSurfaceIdx = (dir > 0 ? lengths[nidx] + stepLen : stepLen) / lengths[nidx] + nidx;
		if(newSurfaceIdx < 0)
		{
			newSurfaceIdx += len;
		}
		if(newSurfaceIdx >= len)
		{
			newSurfaceIdx -= len;
		}

		return newSurfaceIdx;
	}
	/**
	 * retrieves a point on the surface of the polygon, where fraction specifies
	 * offset from nearest vertex
	 * @param surfaceIdx
	 * @return
	 */
	public void getSurfacePoint(final float surfaceIdx, final Vector2 result)
	{
		int idx = getIdx(surfaceIdx);

		// surface idx offset from the floor vertex:
		float offset = surfaceIdx - idx;

		// offset scaled by edge length:
		float scaledOffset = offset * lengths[idx];

		Vector2 dir = directions[idx];

		result.set(dir).mul( scaledOffset ).add( vertices[idx] );
	}

	/**
	 * @param surfaceIdx
	 * @return
	 */
	public float getSlope(final float surfaceIdx)
	{
		return slopes[getIdx(surfaceIdx)];
	}

	/**
	 * @param surfaceIdx
	 * @return Normalized normal
	 */
	public Vector2 getNormal(final float surfaceIdx)
	{
		return normals[getIdx(surfaceIdx)];
	}

	private float normalSurfaceIdx(final float surfaceIdx)
	{
		return surfaceIdx > 0 ? surfaceIdx : maxSurfaceIdx+surfaceIdx;
	}


	private final int getIdx(final float surfaceIdx)
	{
		return (int)normalSurfaceIdx(surfaceIdx) % len;

	}

	/**
	 * @return
	 */
	public int getSize()
	{
		return vertices.length;
	}
}
