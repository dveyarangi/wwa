package eir.world;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

public interface IRenderer
{

	ShapeRenderer getShapeRenderer();

	SpriteBatch getSpriteBatch();

	void addEffect( Effect effect );

}
