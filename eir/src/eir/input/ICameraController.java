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

	/**
	 * @param i
	 * @param j
	 * @param k
	 */
	void injectLinearImpulse(float i, float j, float k);

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
