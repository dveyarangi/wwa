package eir.world.unit.cannons;

import java.util.List;

import yarangi.numbers.RandomUtil;
import eir.world.environment.spatial.ISpatialObject;
import eir.world.unit.Unit;
import eir.world.unit.structure.Spawner;

public abstract class TargetingModule
{


	public abstract ISpatialObject pickTarget(List <ISpatialObject> units);


	public static final TargetingModule CLOSEST_TARGETER(final Unit owner)
	{
		return new TargetingModule()
		{

			@Override
			public ISpatialObject pickTarget( final List <ISpatialObject> units )
			{

				ISpatialObject target = null;

				float smallestDst = Float.MAX_VALUE;
				for(ISpatialObject unit : units)
				{
					float dst = unit.getArea().getAnchor().dst2( owner.getArea().getAnchor() );
					if( dst < smallestDst && !(unit instanceof Spawner))
					{
						smallestDst = dst;

						target = unit;
					}
				}
				return target;
			}
		};
	}

	public static final TargetingModule RANDOM_TARGETER(final Unit owner)
	{
		return new TargetingModule()
		{

			@Override
			public ISpatialObject pickTarget( final List <ISpatialObject> units )
			{
				ISpatialObject target = null;

				if(units.size() > 0)
				{
					int targetIdx = RandomUtil.N( units.size() );
					target = units.get( targetIdx );
				}
				else
				{
					target = null;
				}

				return target;
			}
		};
	}
}
