/**
 * 
 */
package eir.world.unit.ant;

import java.util.ArrayList;

import com.badlogic.gdx.utils.Pool;

import eir.world.unit.Faction;

/**
 * @author dveyarangi
 *
 */
public class AntFactory
{
	
	public static final int ANT_STICKY = 0;
	public static final int ANT_FLYING = 1;
	
	
	private static ArrayList <Pool <Ant>> antPools = new ArrayList <Pool <Ant>> ();
	
	static {
		antPools.add( ANT_STICKY, new Pool<Ant> () { protected Ant newObject() { return new StickyAnt(); } } );
		antPools.add( ANT_FLYING, new Pool<Ant> () { protected Ant newObject() { return new FlyingAnt(); } } );
	}
	
	public static <A extends Ant, F extends Faction> A getAnt(int type, Faction faction)
	{
		Pool <Ant> pool = antPools.get( type );
		A ant = (A)pool.obtain();
		
		ant.init(faction);
		
		return ant;
	}


	/**
	 * @param ant
	 */
	public static void free(Ant ant)
	{
		Pool <Ant> pool = antPools.get( ant.getType() );
		pool.free( ant );
	}
	
}