/**
 * 
 */
package eir.world.environment.parallax;

import java.util.Comparator;
import java.util.NavigableSet;
import java.util.SortedSet;
import java.util.TreeSet;

import com.badlogic.gdx.graphics.Texture;

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
	
	
	public NavigableSet<Layer> layers = new TreeSet <Layer> ();
}
