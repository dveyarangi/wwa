/**
 *
 */
package eir.world.unit.ant;

import eir.resources.GameFactory;
import eir.world.unit.UnitsFactory.UnitFactory;
import eir.world.unit.ai.TaskStage;

/**
 * @author dveyarangi
 *
 */
public class AntFactory extends UnitFactory <Ant>
{

	public AntFactory( final GameFactory gameFactory )
	{
		behaviors.put( TaskStage.TRAVEL_TO_SOURCE, new TravelingBehavior.TravelToSourceBehavior() );
		behaviors.put( TaskStage.TRAVEL_TO_TARGET, new TravelingBehavior.TravelToTargetBehavior() );
		behaviors.put( TaskStage.MINING, new MiningBehavior() );

	}

	@Override
	protected Ant createEmpty() { return new Ant(); }



}
