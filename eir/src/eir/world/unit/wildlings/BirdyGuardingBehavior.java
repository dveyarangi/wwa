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
public class BirdyGuardingBehavior implements UnitBehavior <Birdy>
{

	@Override
	public void update(float delta, Task task, Birdy unit)
	{
		if(unit.timeToPulse <= 0)
		{
			if(++ unit.quantum > 3)
			{
				task.nextStage();
				unit.quantum = 0;
				return;
			}
			
			unit.timeToPulse = unit.pulseLength;
			
			Vector2 target = task.getOrder().getTargetNode().getPoint();
			
			unit.velocity.set( target );
			unit.velocity.x += RandomUtil.R( 50 ) - 25;
			unit.velocity.y += RandomUtil.R( 50 ) - 25;
			
			unit.velocity.sub( unit.getBody().getAnchor() ).nor().mul( unit.pulseStreght );
			
			unit.angle = unit.velocity.angle();
		}
		
		unit.getArea().getAnchor().add( unit.velocity.tmp().mul( delta ) );
		
		unit.velocity.mul( unit.pulseDecay );
		
		unit.timeToPulse -= delta;
	}

}
