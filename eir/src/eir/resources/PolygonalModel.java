/**
 * 
 */
package eir.resources;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;

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
	 * @param origin 
	 * @param body
	 * @param sprite
	 */
	public PolygonalModel(Vector2 origin, Vector2 [] rawVertices, float size, float x, float y, float angle)
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

		for(int idx = 0; idx < len; idx ++)
		{
			Vector2 a = vertices[idx];
			Vector2 b = vertices[(idx+1)%len];
			
			// TODO: checkout about vertical ones
			slopes[idx] = (b.y-a.y)/(b.x-a.x);
			
			normals[idx] = b.tmp().sub( a ).rotate( 90 ).cpy();
		}
	}
	
	public Vector2 getOrigin()
	{
		return origin;
	}
	
	/**
	 * Debug rendering method
	 * @param shape
	 */
	public void draw(ShapeRenderer shape)
	{
		shape.setColor( 1, 1, 1, 1 );
		for(int idx = 0; idx < vertices.length; idx ++)
		{
			Vector2 a = vertices[idx];
			Vector2 b = vertices[(idx+1)%vertices.length];
			shape.begin(ShapeType.Line);
				shape.line( a.x, a.y, b.x, b.y );
			shape.end();
			
			shape.begin(ShapeType.Circle);
				shape.circle( a.x, a.y, 1 );
			shape.end();
		}
		
	}
}
