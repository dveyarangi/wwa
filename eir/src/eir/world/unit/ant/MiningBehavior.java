package eir.world.unit.ant;

import eir.world.unit.UnitBehavior;
import eir.world.unit.ai.Task;


/**
 * ant driver when ant is tasked to mine
 * @author Ni
 *
 */
public class MiningBehavior implements UnitBehavior <Ant>
{
	@Override
	public void update(float delta, Task task, Ant ant)
	{
		ant.angle += delta;
	}
}
