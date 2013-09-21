package eir.world.environment.spatial;


public interface ISpatialFilter <K>
{
	public boolean accept(K entity);
}
