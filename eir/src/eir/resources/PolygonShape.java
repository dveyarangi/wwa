package eir.resources;

import com.badlogic.gdx.math.Vector2;

public class PolygonShape
{
	private Vector2 [] vertices;

	private Vector2 origin;

	public PolygonShape (final Vector2 [] vertices, final Vector2 origin)
	{
		this.vertices = vertices;
		this.origin = origin;
	}

	public Vector2[] getVertices() { return vertices; }

	public Vector2 getOrigin() { return origin; }


	public static String toCircularName(final float radius, final int segments)
	{
		return "R:" + radius + "-S:" + segments;
	}

	public static PolygonShape generateCircleModel(final String paramString)
	{
		String [] parts = paramString.split( "-" );
		float radius = Float.parseFloat( parts[0].split( ":" )[1] );
		int segments = Integer.parseInt( parts[1].split( ":" )[1] );

		return generateCircleModel( segments );
	}

	public static PolygonShape generateCircleModel( final int segments)
	{
		 final Vector2 [] rawVertices = new Vector2[segments];

		 float angularStep = (float)(Math.PI * 2f / segments);

		 for(int idx = 0; idx < segments; idx ++)
		 {
			 rawVertices[idx] = new Vector2(
					 (float)( 0.5 * Math.cos( angularStep * idx ) ),
					 (float)( 0.5 * Math.sin( angularStep * idx ) )
					 );
		 }

		 PolygonShape shape = new PolygonShape(rawVertices, new Vector2(0,0));

		 return shape;
	}
}
