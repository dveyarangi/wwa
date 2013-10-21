package eir.world.resource;

/**
 * something that can hold resources
 * @author Nir
 *
 */
public interface IResourceCapacitor
{
	
	/**
	 * extract resource from this capacitor
	 * @param type
	 * @param amount
	 * @return resource
	 */
	public Resource takeResource( Resource.Type type, int amount );
	
	/**
	 * store this resource in the capacitor
	 * @param r
	 * @return may return leftover if capacitor cannot/will not hold the entire resource
	 */
	public Resource storeResource( Resource r );
	
	/**
	 * how much can this capacitor hold of this resource type?
	 * @param type
	 * @return
	 */
	public int getCapacity( Resource.Type type );
	
	/**
	 * set how much of resource this capacitor may hold.
	 * @param type
	 * @param capacity
	 */
	public void setCapacity( Resource.Type type, int capacity );
	
	/**
	 * how much of the resource type does this capacitor hold?
	 * @param type
	 * @return amount between 0 and capacity
	 */
	public int getContents( Resource.Type type );
}
