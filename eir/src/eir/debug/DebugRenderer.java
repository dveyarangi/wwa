/**
 * 
 */
package eir.debug;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;

import eir.resources.GameFactory;
import eir.resources.Level;
import eir.world.environment.NavMesh;
import eir.world.environment.NavNode;

/**
 * @author dveyarangi
 *
 */
public class DebugRenderer
{
	
	private GameFactory factory;

	private CoordinateGrid debugGrid;
	
	public DebugRenderer(GameFactory factory, Level level, OrthographicCamera camera)
	{
		this.factory = factory;
		debugGrid = new CoordinateGrid( level.getWidth(), level.getHeight(), camera );
	}
	/**
	 * Debug rendering method
	 * @param shape
	 */
	public void draw(ShapeRenderer shape)
	{
		debugGrid.draw( shape );
		
		drawNavMesh(shape);
		
	}
	
	private void drawNavMesh(ShapeRenderer shape)
	{
		NavMesh navMesh = factory.getNavMesh();
		shape.setColor( 0, 1, 0, 0.5f );
		for(int fidx = 0; fidx < navMesh.getNodesNum(); fidx ++)
		{
			NavNode srcNode = navMesh.getNode( fidx );
			
			shape.begin(ShapeType.Circle);
				shape.circle( srcNode.getPoint().x, srcNode.getPoint().y, 1 );
			shape.end();
			
			for(NavNode tarNode : srcNode.getNeighbors())
			{
				shape.begin(ShapeType.Line);
					shape.line( srcNode.getPoint().x, srcNode.getPoint().y, tarNode.getPoint().x, tarNode.getPoint().y);
				shape.end();
			}
		}
	}
}
