package eir.world.environment.sensors;

import eir.world.environment.nav.AirNavNode;
import eir.world.environment.spatial.ISpatialFilter;
import eir.world.environment.spatial.ISpatialObject;

public interface ISensingFilter extends ISpatialFilter<ISpatialObject>
{
	public static ISensingFilter AIR_NODES_FILTER = new ISensingFilter() {
		@Override
		public boolean accept( final ISpatialObject entity )
		{
			return entity instanceof AirNavNode;
		}
	};
}
