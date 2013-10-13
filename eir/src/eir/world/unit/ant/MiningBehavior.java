package eir.world.unit.ant;

import eir.world.unit.ai.Task;
import eir.world.unit.ai.TaskBehavior;


/**
 * ant driver when ant is tasked to mine
 * @author Ni
 *
 */
public class MiningBehavior implements TaskBehavior
{
	@Override
	public void update(float delta, Task task, Ant ant)
	{
		ant.angle += delta;
	}
}
