package eir.util;

import com.badlogic.gdx.math.Vector2;

public class BrokenLine implements ParametricCurve
{
	private final Vector2 [] points;

	private float [] lengths;

	private Vector2 [] tangents;

	private float totalLength;


	public BrokenLine(final Vector2 ... ps)
	{
		points = ps;

		totalLength = 0;
		lengths = new float [ps.length-1];
		tangents = new Vector2 [ps.length-1];
		for(int idx = 0; idx < ps.length-1; idx ++)
		{
			Vector2 p1 = points[idx];
			Vector2 p2 = points[idx+1];

			float dx = p1.x-p2.x;
			float dy = p1.y-p2.y;

			lengths[idx] = (float)Math.sqrt( dx*dx + dy*dy );

			totalLength += lengths[idx];

			tangents[idx] = new Vector2(dx, dy).nor();
		}
	}


	@Override
	public Vector2 at(final Vector2 target, final float t)
	{
		float accLength = 0;
		float dt = 0;
		int idx = 0;
		while (dt < t)
		{
			dt = (accLength+lengths[idx]) / totalLength;
			if(dt >= t)
				break;

			accLength += lengths[idx];
			idx ++;
		}

		float ft = lengths[idx] / totalLength;

		return target.set( points[idx]).add( tangents[idx].tmp().mul( (t - dt)*totalLength ) );
	}

}
