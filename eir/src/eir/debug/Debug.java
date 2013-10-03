/**
 * 
 */
package eir.debug;

import java.util.HashMap;
import java.util.Map;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.FPSLogger;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.Vector2;

import eir.input.GameInputProcessor;
import eir.resources.GameFactory;
import eir.world.Level;
import eir.world.environment.NavEdge;
import eir.world.environment.NavMesh;
import eir.world.environment.NavNode;
import gnu.trove.iterator.TIntObjectIterator;

/**
 * @author dveyarangi
 *
 */
public class Debug
{
	
	public static final String TAG = "debug";
	
	public static Debug debug;
	
	public Level level;

	private CoordinateGrid debugGrid;
	
	private SpatialHashMapLook spatialGrid;
	
	private BitmapFont font;
	
	public boolean drawCoordinateGrid = false;
	public boolean drawNavMesh = false;
	public boolean drawSpatialHashmap = false;
	
	private static Map <String, Long> timings = new HashMap <String, Long> ();

	private int frameCount = 0;
	/**
	 * measures delta time average
	 */
	private static final int SAMPLES = 60;
	private float [] deltas = new float [SAMPLES]; 
	private float deltaPeak = 0;
	private boolean isFirstBatch = true;
    
    private FPSLogger fpsLogger = new FPSLogger();
	
	public static void init( Level level, GameInputProcessor inputController )
	{
		debug = new Debug(  level, inputController  );
	}
	
	private Debug( Level level, GameInputProcessor inputController)
	{
		this.level = level;
		debugGrid = new CoordinateGrid( 
				level.getWidth(), level.getHeight(), 
				inputController.getCamera() );
		
		spatialGrid = new SpatialHashMapLook( level.getSpatialIndex() );
		
		font = GameFactory.loadFont("skins//fonts//default", 0.05f);
	}
	
	public void update(float delta)
	{
		int sampleIdx = (frameCount ++ % SAMPLES);
		deltas[sampleIdx] = delta;
		if(sampleIdx >= SAMPLES-1)
		{
			float maxDelta = Float.MIN_VALUE;
			for(int idx = 0; idx < SAMPLES; idx ++)
			{
				float sample = deltas[idx];
				
				if(isFirstBatch)
					continue;
				
				if(sample > maxDelta)
				{
					maxDelta = sample;
				}
				
				if(sample > deltaPeak)
				{
					deltaPeak = sample;
//					log("New delta maximum: " + deltaPeak);
				}
			}
//			log("Average delta time: " + deltaSum / SAMPLES);
			isFirstBatch = false;
		}
		deltaPeak -= 0.00001f;
		if(!isFirstBatch && delta > deltaPeak * 0.5)
		{
//			log("Delta peak: " + delta);
//			deltaPeak *= 2;
		}
	}
	
	/**
	 * Debug rendering method
	 * @param shape
	 */
	public void draw(SpriteBatch batch, ShapeRenderer shape)
	{
		// a libgdx helper class that logs the current FPS each second
		//fpsLogger.log();
		
		if(drawCoordinateGrid)
			debugGrid.draw( batch, shape );
		
		if(drawSpatialHashmap)
			spatialGrid.draw( shape );
		
		if(drawNavMesh)
			drawNavMesh( batch, shape );
		
	}
	
	private void drawNavMesh(SpriteBatch batch, ShapeRenderer shape)
	{
		
		NavMesh navMesh = level.getNavMesh();
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
		shape.setColor( 0, 1, 0, 0.5f );
		
		TIntObjectIterator<NavEdge> i = navMesh.getEdgesIterator();
		i.advance();
		while( i.hasNext() )
		{
			NavEdge e = i.value();
			Vector2 p1 = e.getNode1().getPoint();
			Vector2 p2 = e.getNode2().getPoint();
			shape.line(p1.x, p1.y, p2.x, p2.y);
			i.advance();
		}
//		for(int fidx = 0; fidx < navMesh.getNodesNum(); fidx ++)
//		{
//			srcNode = navMesh.getNode( fidx );
//
//			shape.setColor( 0, 1, 0, 0.5f );
//			for(NavNode tarNode : srcNode.getNeighbors())
//			{
//				shape.line( srcNode.getPoint().x, srcNode.getPoint().y, tarNode.getPoint().x, tarNode.getPoint().y);
//			}
//		}
		shape.end();
	}
	
	public static void toggleNavMesh() { debug.drawNavMesh =! debug.drawNavMesh; }
	public static void toggleCoordinateGrid() { debug.drawCoordinateGrid =! debug.drawCoordinateGrid; }
	public static void toggleSpatialGrid() { debug.drawSpatialHashmap =! debug.drawSpatialHashmap; }


	public static void startTiming(String processName)
	{
		log("Starting timer for " + processName);
		timings.put( processName, System.currentTimeMillis() );
	}
	public static void stopTiming(String processName)
	{
		long duration = System.currentTimeMillis() - timings.get( processName );
		log(processName + " finished in " + duration + "ms");
	}
		
	private static void log(String message)
	{
		Gdx.app.log( TAG, message);
	}
}
