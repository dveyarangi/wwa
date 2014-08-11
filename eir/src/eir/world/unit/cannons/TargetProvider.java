package eir.world.unit.cannons;

import java.util.List;

import yarangi.numbers.RandomUtil;
import eir.world.environment.spatial.ISpatialObject;
import eir.world.unit.Unit;

public abstract class TargetProvider
{


	public abstract ISpatialObject pickTarget(List <ISpatialObject> units);


	public static final TargetProvider CLOSEST_TARGETER(final Unit owner)
	{
		return new TargetProvider()
		{

			@Override
			public ISpatialObject pickTarget( final List <ISpatialObject> units )
			{

				ISpatialObject target = null;

				float smallestDst = Float.MAX_VALUE;
				for(ISpatialObject unit : units)
				{
					float dst = unit.getArea().getAnchor().dst2( owner.getArea().getAnchor() );
					if( dst < smallestDst )
					{
						smallestDst = dst;

						target = unit;
					}
				}
				return target;
			}
		};
	}
	public static final TargetProvider FAREST_TARGETER(final Unit owner)
	{
		return new TargetProvider()
		{

			@Override
			public ISpatialObject pickTarget( final List <ISpatialObject> units )
			{

				ISpatialObject target = null;

				float biggestDst = Float.MIN_VALUE;
				for(ISpatialObject unit : units)
				{
					float dst = unit.getArea().getAnchor().dst2( owner.getArea().getAnchor() );
					if( dst > biggestDst )
					{
						biggestDst = dst;

						target = unit;
					}
				}
				return target;
			}
		};
	}

	public static final TargetProvider RANDOM_TARGETER(final Unit owner)
	{
		return new TargetProvider()
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
