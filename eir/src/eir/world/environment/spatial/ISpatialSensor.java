package eir.world.environment.spatial;

/**
 * Callback class for {@link SpatialHashMap} query methods
 * @author dveyarangi
 *
 * @param <O>
 */
public interface ISpatialSensor <O> 
{
	/**
	 * Called on object detection.
	 * @param object
	 * @return true, if the query should stop after this object.
	 */
	public boolean objectFound(O object);
	
	/**
	 * Resets sensor collections; called by {@link SpatialHashMap} at start of each query.
	 */
	public void clear();
}
