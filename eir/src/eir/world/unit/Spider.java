/**
 * 
 */
package eir.world.unit;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.Manifold;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.physics.box2d.WorldManifold;
import com.badlogic.gdx.physics.box2d.joints.RevoluteJoint;
import com.badlogic.gdx.physics.box2d.joints.RevoluteJointDef;

import eir.world.Asteroid;

/**
 * @author dveyarangi
 *
 */
public class Spider
{
	private static final float size = 5;
	
	private Body chassis;
	private Body leftWheel;
	private Body rightWheel;
	
	private Vector2 anchor;
	
	private Vector2 lastLeftContact;
	private Vector2 lastRightContact;
	
	private static final float GRAVITY = 5000;
	
	/**
	 * currently traversed asteroid, maybe null in transition
	 */
	private Asteroid asteroid;
	
	/**
	 * This force applies when there is no contact with asteroid surface
	 */
	private Vector2 gravityForce;
	private Vector2 stickyForce;
	
	private static final float STICK_DURATION = 1f;
	private float stickTimeRemaining = 0;
	
	public Spider(World world, float x, float y)
	{
		anchor = new Vector2(x, y);
		gravityForce = new Vector2( 0, 0 );
//		stickyForce;
		
        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyType.DynamicBody;
        bodyDef.position.set(x, y);

        // chassis
        PolygonShape chassisShape = new PolygonShape();
        chassisShape.set(new float[] {
        	   -size , -size/4, 
        		size , -size/4, 
        		size ,  size/4, 
        	   -size , size/4}); // counterclockwise order

        FixtureDef chassisFixtureDef = new FixtureDef();
        chassisFixtureDef.shape = chassisShape;
        chassisFixtureDef.density = 0.1f;

        chassis = world.createBody(bodyDef);
        chassis.createFixture(chassisFixtureDef);

        // left wheel
        CircleShape wheelShape = new CircleShape();
        wheelShape.setRadius( size*0.9f );
        
        FixtureDef wheelFixtureDef = new FixtureDef();
        wheelFixtureDef.shape = wheelShape;
        wheelFixtureDef.density = 0.1f;
        wheelFixtureDef.friction = 1f;

        
        bodyDef.position.set(x-size/2, y);
        leftWheel = world.createBody(bodyDef);
        leftWheel.createFixture(wheelFixtureDef);

        // right wheel
        bodyDef.position.set(x+size/2, y);
        rightWheel = world.createBody(bodyDef);
        rightWheel.createFixture(wheelFixtureDef);

        // left axis
        RevoluteJointDef axisDef = new RevoluteJointDef();
        axisDef.bodyA = chassis;
        axisDef.bodyB = leftWheel;
        axisDef.localAnchorA.set(-size*0.99f, 0);
        axisDef.localAnchorB.set(0, 0);
//        axisDef.frequencyHz = chassisFixtureDef.density;
 //       axisDef.localAxisA.set(Vector2.Zero);
 //        axisDef.dampingRatio = 0;
        RevoluteJoint leftAxis = (RevoluteJoint) world.createJoint(axisDef);

        // right axis
        axisDef.bodyB = rightWheel;
        axisDef.localAnchorA.x *= -1;
        
        axisDef.maxMotorTorque = 10000;
        axisDef.collideConnected = false;
        axisDef.enableMotor = true;
        axisDef.motorSpeed = 10;


        RevoluteJoint rightAxis = (RevoluteJoint) world.createJoint(axisDef);
        
        // this listener with adjust the sticking force
        // to make spider stick to the surface of asteroid
        world.setContactListener( new AsteroidContactListener() );
	}
	
	public void setAsteroid(Asteroid asteroid)
	{
		this.asteroid = asteroid;
	}
	
	public void update(float delta)
	{
		if(asteroid != null)
		{
			Vector2 force;
			if(stickyForce == null)
			{
				force = gravityForce.set( 
						asteroid.getModel().getBody().getPosition().x - chassis.getPosition().x, 
						asteroid.getModel().getBody().getPosition().y - chassis.getPosition().y )
						.nor().mul( GRAVITY );
//				System.out.println("using gravity : " + force);
			}
			else
			{
				force = stickyForce;
				if(stickTimeRemaining <= 0)
					stickyForce = null;
				stickTimeRemaining -= delta;
//				System.out.println("using sticky : " + force);
			}
//			System.out.println("position: " + chassis.getPosition());
			chassis.applyForce( force, chassis.getPosition() );
		}
		
//		System.out.println(stickyForce);
	}
	
	private class AsteroidContactListener implements ContactListener
	{
		
		public AsteroidContactListener()
		{
		}

		@Override
		public void beginContact(Contact contact)
		{
		}

		@Override
		public void endContact(Contact contact)
		{
			// TODO Auto-generated method stub
			
		}

		@Override
		public void preSolve(Contact contact, Manifold oldManifold)
		{
			// TODO Auto-generated method stub
			
		}

		@Override
		public void postSolve(Contact contact, ContactImpulse impulse)
		{
			if(contact.getFixtureA().getBody() != asteroid.getModel().getBody()
			&& contact.getFixtureB().getBody() != asteroid.getModel().getBody())
				return;
			
			WorldManifold worldManifold = contact.getWorldManifold();
//			contact.getWorldManifold().get

			Vector2 contactNormal = worldManifold.getNormal().nor();
			// print the contact point and the normal
//			System.out.println( "contact: " + worldManifold.getPoints()[0].x + "," + worldManifold.getPoints()[0].y );
//			System.out.println( worldManifold.getNormal().x + "," + worldManifold.getNormal().y );
			stickyForce = new Vector2(contactNormal.x, contactNormal.y).mul( -GRAVITY );
			stickTimeRemaining = STICK_DURATION;
		}
	}
}
