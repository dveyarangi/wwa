package eir.rendering;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

import eir.resources.AnimationHandle;
import eir.world.Effect;

public interface IRenderer
{

	ShapeRenderer getShapeRenderer();

	SpriteBatch getSpriteBatch();

	void addEffect( Effect effect );

	public Animation getAnimation( final AnimationHandle handle );

}
