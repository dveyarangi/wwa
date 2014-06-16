package eir.world.unit.cannons;

import com.badlogic.gdx.math.Vector2;

import eir.world.environment.spatial.ISpatialObject;
import eir.world.unit.UnitBehavior;
import eir.world.unit.ai.Task;

public class LinearTargetingBehavior implements UnitBehavior<Cannon>
{

	@Override
	public void update( final float delta, final Task task, final Cannon unit )
	{

		ISpatialObject target = unit.getTarget();

		if(target == null)
			return;

		Vector2 targetDir = target.getArea().getAnchor().tmp().sub( unit.getArea().getAnchor() ).nor();

		unit.getWeapon().fire( unit, targetDir );
	}

}
