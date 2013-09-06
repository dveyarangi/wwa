/**
 * 
 */
package eir.world.unit;

import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.PolygonShape;

/**
 * @author dveyarangi
 *
 */
public class Spider
{
	private static final float size = 10;
	
	public Spider(float x, float y)
	{
/*        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyType.DynamicBody;
        bodyDef.position.set(x, y);

        // chassis
        PolygonShape chassisShape = new PolygonShape();
        chassisShape.set(new float[] {
        	   -size / 2, -size / 2, 
        		size / 2, -size / 2, 
        		size / 2,  size / 2, 
        	   -size / 2 , size / 2}); // counterclockwise order

        chassisFixtureDef.shape = chassisShape;

        chassis = world.createBody(bodyDef);
        chassis.createFixture(chassisFixtureDef);

        // left wheel
        CircleShape wheelShape = new CircleShape();
        wheelShape.setRadius(height / 3.5f);

        wheelFixtureDef.shape = wheelShape;

        leftWheel = world.createBody(bodyDef);
        leftWheel.createFixture(wheelFixtureDef);

        // right wheel
        rightWheel = world.createBody(bodyDef);
        rightWheel.createFixture(wheelFixtureDef);

        // left axis
        WheelJointDef axisDef = new WheelJointDef();
        axisDef.bodyA = chassis;
        axisDef.bodyB = leftWheel;
        axisDef.localAnchorA.set(-width / 2 * .75f + wheelShape.getRadius(), -height / 2 * 1.25f);
        axisDef.frequencyHz = chassisFixtureDef.density;
        axisDef.localAxisA.set(Vector2.Y);
        axisDef.maxMotorTorque = chassisFixtureDef.density * 10;
        leftAxis = (WheelJoint) world.createJoint(axisDef);

        // right axis
        axisDef.bodyB = rightWheel;
        axisDef.localAnchorA.x *= -1;

        rightAxis = (WheelJoint) world.createJoint(axisDef);*/
	}
}
