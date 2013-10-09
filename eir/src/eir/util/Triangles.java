/**
 * 
 */
package eir.util;

import com.badlogic.gdx.math.Vector2;

/**
 * @author dveyarangi
 *
 */
public class Triangles
{
	/**
	 * Calculates the missing third vertex of triangle, one which is located clockwise from a->b vector.
	 * 
	 * @param a vertex of triangle
	 * @param A length of edge opposite to vertex a
	 * @param b vertex of triangle
	 * @param B length of edge opposite to vertex b
	 * @param target result
	 * @return target
	 */
	public static Vector2 calcFromVertices(Vector2 a, float A, Vector2 b, float B, Vector2 target, boolean cw)
	{
		// vector from a to b
		Vector2 c = Vector2.tmp.set( b ).sub( a );
		float C = c.len();
		
		// normalizing
		c.div( C );
		Vector2 cr = cw ? Vector2.tmp2.set( -c.y, c.x ) : Vector2.tmp2.set( c.y, -c.x );
		
		// length of projection of B on c
		float proj = (B*B + C*C - A*A) / (2*C);
		
		target.set( a )
			.add( c.mul( proj ) )
			.add( cr.mul( (float)Math.sqrt( B*B - proj*proj ) ) );
		
		return target;
	}
	
	public static void main(String ... args)
	{
		Vector2 target = new Vector2();
		calcFromVertices( new Vector2(0, 0), 4.24f, new Vector2(6, 0), 4.24f, target, true );
		System.out.println(target);
	}
}
