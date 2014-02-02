/**
 * 
 */
package eir.world.unit.wildlings;

import eir.world.unit.UnitsFactory.UnitFactory;
import eir.world.unit.ai.TaskStage;

/**
 * @author dveyarangi
 *
 */
public class BirdyFactory extends UnitFactory <Birdy>
{

	public BirdyFactory()
	{
		behaviors.put( TaskStage.GUARD, new BirdyGuardingBehavior() );
		behaviors.put( TaskStage.ATTACK, new BirdyAttackingBehavior() );
	}
	
	protected Birdy createEmpty() { return new Birdy(); }

	protected Class<Birdy> getUnitClass() { return Birdy.class; }
	
	
}