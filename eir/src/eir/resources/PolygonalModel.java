/**
 * 
 */
package eir.resources;

import com.badlogic.gdx.math.Vector2;

import eir.world.environment.NavMesh;
import eir.world.environment.NavNode;

/**
 * @author dveyarangi
 *
 */
public class PolygonalModel
{
	/**
	 * number of vertices
	 */
	private int len;
	
	/**
	 * polygon origin point
	 */
	private	Vector2 origin;
	
	/**
	 * scaled, rotated and transposed vertices
	 */
	private Vector2 [] vertices;
	
	/**
	 * normals of polygon edges
	 */
	private Vector2 [] normals;
	
	/**
	 * directions of polygon edges
	 */
	
	private Vector2 [] directions;
	
	/**
	 * slopes of polygon edges
	 */
	private float [] slopes;
	
	/**
	 * lengths of polygon edges
	 */
	private float [] lengths;
	
	/**
	 * navigation nodes corresponding to polygon vertices
	 */
	private NavNode [] nodes;
	
	private float maxSurfaceIdx;

	
	/**
	 * @param navMesh 
	 * @param origin 
	 * @param body
	 * @param sprite
	 */
	public PolygonalModel(NavMesh navMesh, Vector2 origin, Vector2 [] rawVertices, float size, Vector2 position, float angle)
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
					.rotate( angle ) // rotating
					.mul( size ) // scaling
					.add( position )  // transposing
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
		
		nodes = new NavNode[len];

		NavNode currNode = navMesh.insertNode( vertices[0] );
		NavNode prevNode;
		for(int idx = 0; idx < len; idx ++)
		{
			Vector2 a = vertices[idx];
			Vector2 b = vertices[(idx+1)%len];
			
			directions[idx] = new Vector2( b.x-a.x, b.y-a.y ).nor();
			
			// TODO: checkout about vertical ones
			slopes[idx] = directions[idx].y / directions[idx].x;
			
			normals[idx] = b.tmp().sub( a ).rotate( 90 ).cpy();
			
			lengths[idx] = (float)Math.hypot( b.x-a.x, b.y-a.y );
			
			nodes[idx] = currNode;
			prevNode = currNode;
			currNode = navMesh.insertNode( b );
			navMesh.linkNodes( currNode, prevNode );
			
			maxSurfaceIdx ++;
		}
	}
	
	/**
	 * @param navNodeIdx
	 * @return
	 */
	public NavNode getNavNode(int navNodeIdx)
	{
		return nodes[navNodeIdx];
	}

	
	public Vector2 getOrigin()
	{
		return origin;
	}
	
	/**
	 * Returns a new index, that is resulting from stepping the specified stepLen from the initial index
	 * @param surfaceIdx
	 * @param stepLen
	 * @return
	 */
	public float getStepSurfaceIndex(float surfaceIdx, float stepLen)
	{
		int dir = stepLen > 0 ? 1 : -1;
		int idx = ((int) surfaceIdx) % len;
		
		// offset scaled by edge length:
		float scaledOffset = (normalSurfaceIdx( surfaceIdx ) - idx) * lengths[idx];
		
		// adding edges lenghts until we are past the stepLen:
		stepLen -= lengths[idx] - scaledOffset;
		int nidx = idx;
		
		// loop is required since step may skip several poly vertices:
		while(stepLen > 0) 
		{
			nidx += dir;
			if(nidx >= len) nidx = 0;
			else if(nidx < 0) nidx = len-1;
			
			stepLen -= lengths[nidx];
		}
		
		return (lengths[nidx] + stepLen) / lengths[nidx] + nidx;
	}
	/**
	 * retrieves a point on the surface of the polygon, where fraction specifies
	 * offset from nearest vertex
	 * @param surfaceIdx
	 * @return
	 */
	public void getSurfacePoint(float surfaceIdx, Vector2 result)
	{
		int idx = getIdx(surfaceIdx);
		
		// surface idx offset from the floor vertex:
		float offset = normalSurfaceIdx(surfaceIdx) - idx;
		
		// offset scaled by edge length:
		float scaledOffset = offset * lengths[idx];
		
		Vector2 dir = directions[idx];
		
		result.set(dir).mul( scaledOffset ).add( vertices[idx] ).cpy();
	}

	/**
	 * @param surfaceIdx
	 * @return
	 */
	public float getSlope(float surfaceIdx)
	{
		return slopes[getIdx(surfaceIdx)];
	}

	/**
	 * @param surfaceIdx
	 * @return
	 */
	public Vector2 getNormal(float surfaceIdx)
	{
		return normals[getIdx(surfaceIdx)];
	}

	private float normalSurfaceIdx(float surfaceIdx)
	{
		return (surfaceIdx > 0 ? surfaceIdx : (maxSurfaceIdx+surfaceIdx));
	}
	
	
	private final int getIdx(float surfaceIdx)
	{
		return ((int)normalSurfaceIdx(surfaceIdx)) % len;

	}
}
