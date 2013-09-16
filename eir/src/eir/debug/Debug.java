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
public class Debug
{
	
	public static Debug debug;
	
	
	private GameFactory factory;

	private CoordinateGrid debugGrid;
	
	private BitmapFont font;
	
	private boolean drawCoordinateGrid = false;
	private boolean drawNavMesh = true;
	
	public static void init(GameFactory factory, Level level, OrthographicCamera camera)
	{
		debug = new Debug( factory, level, camera );
	}
	
	private Debug(GameFactory factory, Level level, OrthographicCamera camera)
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
		
		if(drawCoordinateGrid)
			debugGrid.draw( batch, shape );
		
		if(drawNavMesh)
			drawNavMesh( batch, shape );
		
	}
	
	private void drawNavMesh(SpriteBatch batch, ShapeRenderer shape)
	{
		
		NavMesh navMesh = factory.getNavMesh();
		NavNode srcNode;
		
		shape.begin(ShapeType.FilledCircle);
		for(int fidx = 0; fidx < navMesh.getNodesNum(); fidx ++)
		{
			shape.setColor( 0, 1, 0, 1f );
			srcNode = navMesh.getNode( fidx );
			
				shape.filledCircle( srcNode.getPoint().x, srcNode.getPoint().y, 1 );
		}	
		shape.end();
		
		batch.begin();
		for(int fidx = 0; fidx < navMesh.getNodesNum(); fidx ++)
		{
			srcNode = navMesh.getNode( fidx );
			font.draw( batch, String.valueOf( fidx ), srcNode.getPoint().x+1, srcNode.getPoint().y+1 );
		}
		batch.end();
		
		shape.begin(ShapeType.Line);
		for(int fidx = 0; fidx < navMesh.getNodesNum(); fidx ++)
		{
			srcNode = navMesh.getNode( fidx );

			shape.setColor( 0, 1, 0, 0.5f );
			for(NavNode tarNode : srcNode.getNeighbors())
			{
				shape.line( srcNode.getPoint().x, srcNode.getPoint().y, tarNode.getPoint().x, tarNode.getPoint().y);
			}
		}
		shape.end();
	}
	
	public static void toggleNavMesh() { debug.drawNavMesh =! debug.drawNavMesh; }
	public static void toggleCoordinateGrid() { debug.drawCoordinateGrid =! debug.drawCoordinateGrid; }
}
