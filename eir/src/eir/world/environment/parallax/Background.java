/**
 *
 */
package eir.world.environment.parallax;

import java.util.NavigableSet;
import java.util.TreeSet;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;

import eir.input.GameInputProcessor;
import eir.resources.GameFactory;
import eir.resources.TextureHandle;

/**
 * @author dveyarangi
 *
 */
public class Background
{
	public static class Layer implements Comparable <Layer>
	{
		private float depth;
		private TextureHandle texture;
		private Texture txr;
		private Vector2 scroll;
		private boolean tiling;

		public Layer(final float depth, final TextureHandle handle, final Vector2 scroll, final boolean tiling)
		{
			this.depth = depth;
			this.texture = handle;
			this.scroll = scroll;
			this.tiling = tiling;
		}

		@Override
		public int compareTo(final Layer o)
		{
			return Float.compare( o.depth, depth);
		}
	}
	public Background() {}
	public Background(final NavigableSet <Layer> layers)
	{
		this.layers = layers;
	}

	private NavigableSet<Layer> layers = new TreeSet <Layer> ();
	private GameInputProcessor processor;
	/**
	 * Camera for layers of background.
	 */
	private OrthographicCamera camera = new OrthographicCamera();

	private float time = 0;

	public void init( final GameFactory gameFactory, final GameInputProcessor processor)
	{
		this.processor = processor;

		for(Layer layer : layers)
		{
			layer.txr = gameFactory.getTexture( layer.texture );
		}

	}

	public void resize(final int width, final int height)
	{
		camera.setToOrtho( false, width, height );
	}


	public void update(final float delta)
	{
		time += delta;
	}


	public void draw(final SpriteBatch batch)
	{
		batch.begin();
		for(Layer layer : layers)
		{

			camera.position.x = processor.getCamera().position.x / layer.depth;
			camera.position.y = processor.getCamera().position.y / layer.depth;
//			camera.zoom = processor.getCamera().zoom / layer.depth;
			camera.update();

			batch.setProjectionMatrix( camera.projection );
			batch.setTransformMatrix( camera.view );

			int width = layer.txr.getWidth();
			int height = layer.txr.getHeight();

			float xOffset = -width/2, yOffset = -height/2;
			if(layer.scroll != null)
			{
				xOffset = layer.scroll.x * time - width/2;
				yOffset = layer.scroll.y * time - height/2;
			}

			int minXIdx = 0, maxXIdx = 1, minYIdx = 0, maxYIdx = 1;

			if(layer.tiling)
			{
				float screenMinX = camera.position.x - camera.viewportWidth/2*camera.zoom;
				float screenMinY = camera.position.y - camera.viewportHeight/2*camera.zoom;
					// higher right screen corner in world coordinates
				float screenMaxX = camera.position.x + camera.viewportWidth/2*camera.zoom;
				float screenMaxY = camera.position.y + camera.viewportHeight/2*camera.zoom;
				minXIdx = (int) Math.ceil( (screenMinX - xOffset) / width ) -1;
				maxXIdx = (int) Math.ceil( (screenMaxX - xOffset) / width );
				minYIdx = (int) Math.ceil( (screenMinY - yOffset) / height ) -1;
				maxYIdx = (int) Math.ceil( (screenMaxY - yOffset) / height );
			}

			for(int xIdx = minXIdx; xIdx < maxXIdx; xIdx ++)
			{
				for(int yIdx = minYIdx; yIdx < maxYIdx; yIdx ++)
				{
					batch.draw( layer.txr, xIdx*width + xOffset, yIdx*height + yOffset  );
				}
			}
		}

		batch.end();
	}
}
