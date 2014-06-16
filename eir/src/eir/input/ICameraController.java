/**
 *
 */
package eir.input;

import com.badlogic.gdx.graphics.OrthographicCamera;

/**
 * @author dveyarangi
 *
 */
public interface ICameraController
{

	/**
	 * @param b
	 */
	void setUnderUserControl(boolean b);


	public void zoomTo( float x, float y, float amount );

	/**
	 * @param delta
	 */
	void update(float delta);

	/**
	 * @param width
	 * @param height
	 */
	void resize(int width, int height);

	/**
	 * @return
	 */
	OrthographicCamera getCamera();

}
