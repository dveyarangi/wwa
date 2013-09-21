package eir.world.environment.spatial;

/**
 * Interface for object with shape.
 */
public interface ISpatialObject 
{

	/**
	 * Object volume.
	 * @return
	 */
	public AABB getArea();

	public int getId();
}
