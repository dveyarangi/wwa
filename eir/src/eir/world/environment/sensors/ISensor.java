package eir.world.environment.sensors;

import java.util.List;

import eir.world.environment.spatial.ISpatialObject;

public interface ISensor
{
	public List <ISpatialObject> sense( ISensingFilter filter );
}
