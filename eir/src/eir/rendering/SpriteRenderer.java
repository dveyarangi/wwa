package eir.rendering;

import com.badlogic.gdx.graphics.g2d.Sprite;

public class SpriteRenderer <E> implements EntityRenderer <E>
{

	private Sprite sprite;


	public SpriteRenderer(final Sprite sprite)
	{
		this.sprite = sprite;
	}

	@Override
	public void draw( final IRenderer renderer, final E entity )
	{
		sprite.draw( renderer.getSpriteBatch() );
	}

}
