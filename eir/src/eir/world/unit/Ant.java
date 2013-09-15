/**
 * 
 */
package eir.world.unit;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Pool;
import com.badlogic.gdx.utils.Pool.Poolable;

import eir.resources.GameFactory;
import eir.world.environment.NavNode;

/**
 * @author dveyarangi
 *
 */
public class Ant implements Poolable
{
	private static Pool<Ant> pool = new Pool<Ant> () {

		@Override
		protected Ant newObject()
		{
			return new Ant();
		}
		
	};
	
	public static Ant getAnt(GameFactory factory, NavNode node)
	{
		Ant ant = pool.obtain();
		if(ant.position == null)
			ant.position = new Vector2();
		
		ant.node = node;
		ant.position.set( node.getPoint().x, node.getPoint().y );
		
		if(ant.animation == null)
			ant.animation = factory.loadAnimation( "anima//ant//ant.atlas", "blob" );

		return ant;
	}
	
	public static void freeAnt(Ant ant)
	{
		pool.free( ant );
	}
	
	private Vector2 position;
	
	private NavNode node;
	
	private Animation animation;
	private float stateTime;
	
	private float size = 5;
	private float angle = 0;
	
	private Ant()
	{
		stateTime = 0;
	}

	@Override
	public void reset()
	{
		
	}
	
	public void draw(float delta, SpriteBatch batch)
	{
		TextureRegion region = animation.getKeyFrame( stateTime, true );
		batch.draw( region, 
				position.x-region.getRegionWidth()/2, position.y-region.getRegionHeight()/2,
				region.getRegionWidth()/2,region.getRegionHeight()/2, 
				region.getRegionWidth(), region.getRegionHeight(), 
				size/region.getRegionWidth(), 
				size/region.getRegionWidth(), angle);
		stateTime += delta;
	}
	
}
