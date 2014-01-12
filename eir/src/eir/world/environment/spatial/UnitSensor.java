package eir.world.environment.spatial;

import eir.world.unit.Unit;

public class UnitSensor implements ISpatialSensor <Unit> 
{

	@Override
	public boolean objectFound(Unit object) {
		return false;
	}

	@Override
	public void clear() {
		// TODO Auto-generated method stub
		
	}


}
