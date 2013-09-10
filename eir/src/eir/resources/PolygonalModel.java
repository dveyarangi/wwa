/**
 * 
 */
package eir.resources;

import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.Vector2;

import eir.world.environment.NavMesh;
import eir.world.environment.NavNode;

/**
 * @author dveyarangi
 *
 */
public class PolygonalModel
{
	private	Vector2 origin;
	private Vector2 [] vertices;
	private Vector2 [] normals;
	private float [] slopes;
	
	
	private NavNode [] nodes;

	
	/**
	 * @param navMesh 
	 * @param origin 
	 * @param body
	 * @param sprite
	 */
	public PolygonalModel(NavMesh navMesh, Vector2 origin, Vector2 [] rawVertices, float size, float x, float y, float angle)
	{
		super();
		this.origin = origin;
		
		int len = rawVertices.length;
		
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
					.add( x,y )  // transposing
					.cpy();      // tmp ref is not to be saved!
			
			
		}
		
		
		// normals for each poly edge:
		normals = new Vector2 [len];
		// slopes for each poly edge:
		slopes = new float [len];
		
		nodes = new NavNode[len];

		NavNode currNode = navMesh.insertNode( vertices[0] );
		NavNode prevNode;
		for(int idx = 0; idx < len; idx ++)
		{
			Vector2 a = vertices[idx];
			Vector2 b = vertices[(idx+1)%len];
			
			// TODO: checkout about vertical ones
			slopes[idx] = (b.y-a.y)/(b.x-a.x);
			
			normals[idx] = b.tmp().sub( a ).rotate( 90 ).cpy();
			
			nodes[idx] = currNode;
			prevNode = currNode;
			currNode = navMesh.insertNode( b );
			navMesh.linkNodes( currNode, prevNode );
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

}
