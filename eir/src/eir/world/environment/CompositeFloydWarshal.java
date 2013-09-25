package eir.world.environment;

import gnu.trove.map.hash.TIntObjectHashMap;



/**
 * made out of baby graphs
 * @author Ni
 *
 */
public class CompositeFloydWarshal extends FloydWarshal
{
	
	TIntObjectHashMap<FloydWarshal> subMaps;
	
	public CompositeFloydWarshal()
	{
		subMaps = new TIntObjectHashMap<FloydWarshal>();
	}
	
	public void addSubNavMesh( FloydWarshal navMesh )
	{
		
	}
	
	
}
