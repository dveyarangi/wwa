package eir.world.unit.cannons;

import eir.world.unit.Unit;
import eir.world.unit.UnitBehavior;
import eir.world.unit.ai.Task;

public class SimpleTargetingBehavior implements UnitBehavior<Cannon>
{

	@Override
	public void update( final float delta, final Task task, final Cannon unit )
	{

		Unit target = unit.getTarget();
//		System.out.println("Cannon " + unit + " is targeting " + target);

		if(target == null)
			return;

		unit.getWeapon().fire( target.getArea().getAnchor() );

		unit.angle = target.getArea().getAnchor().tmp().sub( unit.getArea().getAnchor() ).angle();
	}

}
