package eir.world.environment;

import com.badlogic.gdx.math.Vector2;

import eir.world.environment.spatial.Grid;

public class WindGrid extends Grid <Vector2>
{

	public WindGrid(final String name, final int size, final float cellSize, final float width, final float height)
	{
		super( name, size, cellSize, width, height );
	}

	@Override
	public Vector2[] createGrid()
	{
		Vector2 [] map = new Vector2 [size];
		for(int idx = 0; idx < size; idx ++)
		{
			map[idx] = new Vector2(0,0);
		}
		return map;
	}


	public Vector2 getWind(final float x, final float y, final Vector2 result)
	{
		int xidx = toGridIndex( x ) + halfGridWidth;
		int yidx = toGridIndex( y ) + halfGridHeight;

//		to
//		float fX = map[xidx][yidx] * x -;
		return null;
	}

	@Override
	protected int index( final int x, final int y )
	{
		// TODO Auto-generated method stub
		return 0;
	}
}
