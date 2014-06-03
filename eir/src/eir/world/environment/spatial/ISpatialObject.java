package eir.world.environment.spatial;

/**
 * Interface for object with AABB for indexing in {@link SpatialHashMap}.
 */
public interface ISpatialObject
{

	/**
	 * Object volume.
	 * @return
	 */
	public AABB getArea();

	/**
	 * Unique (per spatial hashmap) object id for faster hashing
	 * @return
	 */
	public int getId();

	public boolean isAlive();
}
