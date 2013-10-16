/**
 * 
 */
package eir.world.environment.parallax;

import java.util.NavigableSet;
import java.util.TreeSet;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import eir.input.GameInputProcessor;

/**
 * @author dveyarangi
 *
 */
public class Background
{
	public static class Layer implements Comparable <Layer>
	{
		private float depth;
		private Texture texture;

		@Override
		public int compareTo(Layer o)
		{
			return Float.compare( depth, o.depth);
		}
	}
	
	
	private NavigableSet<Layer> layers = new TreeSet <Layer> ();
	private GameInputProcessor processor;
	/**
	 * Camera for layers of background.
	 */
	private OrthographicCamera camera = new OrthographicCamera();
	
	public void init(GameInputProcessor processor)
	{
		this.processor = processor;

	}
	
	public void resize(int width, int height)
	{
		camera.setToOrtho( false, width, height );
	}
	
	public void draw(SpriteBatch batch)
	{
		batch.begin();
		for(Layer layer : layers)
		{
			camera.position.x = processor.getCamera().position.x / layer.depth;
			camera.position.y = processor.getCamera().position.y / layer.depth;
			camera.update();
			
			batch.setProjectionMatrix( camera.projection );
			batch.setTransformMatrix( camera.view );
			
			batch.draw( layer.texture, 0 - layer.texture.getWidth()/2, 0 - layer.texture.getHeight()/2 );
		}
		
		batch.end();
	}
}
