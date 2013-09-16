/**
 * 
 */
package eir.debug;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
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
	
	private BitmapFont font;
	
	public DebugRenderer(GameFactory factory, Level level, OrthographicCamera camera)
	{
		this.factory = factory;
		debugGrid = new CoordinateGrid( level.getWidth(), level.getHeight(), camera );
		font = factory.loadFont("skins//fonts//default", 0.05f);
	}
	/**
	 * Debug rendering method
	 * @param shape
	 */
	public void draw(SpriteBatch batch, ShapeRenderer shape)
	{
		debugGrid.draw( batch, shape );
		
		drawNavMesh( batch, shape );
		
	}
	
	private void drawNavMesh(SpriteBatch batch, ShapeRenderer shape)
	{
		NavMesh navMesh = factory.getNavMesh();
		NavNode srcNode;
		for(int fidx = 0; fidx < navMesh.getNodesNum(); fidx ++)
		{
			shape.setColor( 0, 1, 0, 1f );
			srcNode = navMesh.getNode( fidx );
			
			shape.begin(ShapeType.FilledCircle);
				shape.filledCircle( srcNode.getPoint().x, srcNode.getPoint().y, 1 );
			shape.end();
		}	
		for(int fidx = 0; fidx < navMesh.getNodesNum(); fidx ++)
		{
			srcNode = navMesh.getNode( fidx );
			batch.begin();
			font.draw( batch, String.valueOf( fidx ), srcNode.getPoint().x+1, srcNode.getPoint().y+1 );
			batch.end();
		}
		for(int fidx = 0; fidx < navMesh.getNodesNum(); fidx ++)
		{
			srcNode = navMesh.getNode( fidx );

			shape.setColor( 0, 1, 0, 0.5f );
			for(NavNode tarNode : srcNode.getNeighbors())
			{
				shape.begin(ShapeType.Line);
					shape.line( srcNode.getPoint().x, srcNode.getPoint().y, tarNode.getPoint().x, tarNode.getPoint().y);
				shape.end();
			}
		}
	}
}
