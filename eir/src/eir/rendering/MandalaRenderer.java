package eir.rendering;

import yarangi.numbers.RandomUtil;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;

import eir.world.Asteroid;

public class MandalaRenderer implements EntityRenderer <Asteroid>
{

	Color color;

	public MandalaRenderer()
	{
		color = new Color(RandomUtil.R( 0.5f ) + 0.5f,
						  RandomUtil.R( 0.5f ) + 0.5f,
						  RandomUtil.R( 0.5f ) + 0.5f,
				          1);
	}

	@Override
	public void draw( final IRenderer renderer, final Asteroid entity )
	{
		ShapeRenderer shaper = renderer.getShapeRenderer();

		shaper.begin( ShapeType.FilledCircle );

		shaper.filledCircle( entity.getPosition().x, entity.getPosition().y, entity.getSize() );

		shaper.end();


	}

}
