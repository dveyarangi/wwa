package eir.world.unit.behaviors;


public class IPulseDef
{

	private final float pulseDuration;
	private final float pulseDecay;
	private final float pulseStrength;

	public IPulseDef(final float pulseDuration, final float pulseStrength, final float pulseDecay)
	{
		this.pulseDuration = pulseDuration;
		this.pulseStrength = pulseStrength;
		this.pulseDecay = pulseDecay;
	}

	public float getPulseDuration() { return pulseDuration; }

	public float getPulseDecay() { return pulseDecay; }

	public float getPulseStrength() { return pulseStrength; }

}
