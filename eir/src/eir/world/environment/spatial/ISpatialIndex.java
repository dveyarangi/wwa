package eir.world.environment.spatial;





public interface ISpatialIndex<T extends ISpatialObject>
{

	public void add( T t );

	public void update( T t );

	public T remove( T t );

	public ISpatialSensor <T> queryAABB( ISpatialSensor <T> sensor, AABB area );

	public ISpatialSensor <T> queryAABB(ISpatialSensor <T> sensor, float cx, float cy, float rx, float ry);

	public ISpatialSensor <T> queryRadius(ISpatialSensor <T> sensor, float x, float y, float radius);

	public T findClosest( ISpatialFilter <ISpatialObject> filter, float x, float y );

}
