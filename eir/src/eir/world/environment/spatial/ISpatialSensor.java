package eir.world.environment.spatial;

public interface ISpatialSensor <O> 
{
	/**
	 * Called on object detection.
	 * @param chunk
	 * @param object
	 * @return true, if the query should stop after this object.
	 */
	public boolean objectFound(O object);
	
	/**
	 * Resets sensor collections; called by {@link ISpatialIndex} at start of each query.
	 */
	public void clear();
}
