/**
 * 
 */
package eir.world.unit.wildlings;

import yarangi.numbers.RandomUtil;

import com.badlogic.gdx.math.Vector2;

import eir.world.unit.UnitBehavior;
import eir.world.unit.ai.Task;

/**
 * @author dveyarangi
 *
 */
public class BirdyAttackingBehavior implements UnitBehavior <Birdy>
{

	@Override
	public void update(float delta, Task task, Birdy unit)
	{
		if(unit.timeToPulse <= 0)
		{
			if(++ unit.quantum > 5)
			{
				task.nextStage();
				unit.quantum = 0;
				return;
			}
			
			unit.timeToPulse = unit.pulseLength/4;
			
			Vector2 target = task.getOrder().getUnit().getBody().getAnchor();
			
			
			unit.velocity.set( target ).sub( unit.getBody().getAnchor() );
			
			float dist = unit.velocity.len();
			unit.velocity.x += RandomUtil.R( dist ) - dist/2;
			unit.velocity.y += RandomUtil.R( dist ) - dist/2;
			
			unit.velocity.div(dist).mul( unit.pulseStreght );
			
			unit.angle = unit.velocity.angle();
		}
		
		unit.getArea().getAnchor().add( unit.velocity.tmp().mul( delta ) );
		
		unit.velocity.mul( unit.pulseDecay );
		
		unit.timeToPulse -= delta;
	}

}
