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


}
