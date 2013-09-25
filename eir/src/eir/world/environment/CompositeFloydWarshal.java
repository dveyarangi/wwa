package eir.world.environment;

import java.util.ArrayList;




/**
 * made out of baby graphs
 * @author Ni
 *
 */
public class CompositeFloydWarshal extends FloydWarshal
{
	
//	TIntObjectHashMap<FloydWarshal> subMaps;
	ArrayList<FloydWarshal> subMaps;
	
	public CompositeFloydWarshal()
	{
		subMaps = new ArrayList<FloydWarshal>();
//		subMaps = new TIntObjectHashMap<FloydWarshal>();
	}
	
	public void addSubNavMesh( FloydWarshal navMesh )
	{
		subMaps.add(navMesh);
	}
	
	@Override
	public void linkNodes(NavNode na, NavNode nb)
	{
		super.linkNodes(na, nb);
	}
}
