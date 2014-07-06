package eir.rendering;


public interface EntityRenderer <E>
{
	public void draw( IRenderer renderer, E entity );
}
