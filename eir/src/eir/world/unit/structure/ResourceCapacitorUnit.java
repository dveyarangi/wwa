package eir.world.unit.structure;

import java.util.HashMap;

import eir.world.resource.IResourceCapacitor;
import eir.world.resource.Resource;
import eir.world.resource.Resource.Type;
import eir.world.unit.Unit;
import gnu.trove.map.hash.TObjectIntHashMap;


/**
 * a unit that can hold resources
 * @author Nir
 *
 */
public abstract class ResourceCapacitorUnit extends Unit implements IResourceCapacitor
{
	/**
	 * resource map
	 */
	HashMap<Resource.Type, Resource> rmap;
	
	/**
	 * capacity map
	 */
	TObjectIntHashMap<Resource.Type> cmap;
	
	
	public ResourceCapacitorUnit()
	{
		rmap = new HashMap<Resource.Type, Resource>();
		cmap = new TObjectIntHashMap<Resource.Type>();
		
		rmap.put(Type.OOZE, Resource.createResource(Type.OOZE, 0));
		rmap.put(Type.BOOZE, Resource.createResource(Type.BOOZE, 0));
		
		cmap.put(Type.OOZE, 0);
		cmap.put(Type.BOOZE, 0);
	}
	
	
	@Override
	public Resource takeResource(Type type, int amount)
	{
		return rmap.get(type).split(amount);
	}

	@Override
	public Resource storeResource(Resource r)
	{
		Resource storage = rmap.get(r.getType());
		int cap = cmap.get(r.getType());
		
		if( storage.getAmount() + r.getAmount() > cmap.get(r.getType()))
		{
			r.transter(storage, cap);
			return r;
		}
		else
		{
			storage.merge(r);
			return null;
		}
	}

	@Override
	public int getCapacity(Type type)
	{
		return cmap.get(type);
	}

	@Override
	public void setCapacity(Type type, int capacity)
	{
		if( capacity < 0 )
			throw new IllegalArgumentException("capacity may not be negative");
		
		cmap.put(type, capacity);
	}

	@Override
	public int getContents(Type type)
	{
		return rmap.get(type).getAmount();
	}
}
