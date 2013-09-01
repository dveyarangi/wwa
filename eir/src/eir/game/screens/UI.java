/**
 * 
 */
package eir.game.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;

/**
 *  
 * 
 * @author dveyarangi
 */
public class UI
{
	/**
	 * 
	 */
	private Skin skin;
	
	private UI()
	{
        FileHandle skinFile = Gdx.files.internal( "skins/uiskin.json" );
        skin = new Skin( skinFile );
	}
	
	public static final UI instance = new UI();
	
	public static void showTwoChoiceDialog(Stage stage, String text, String yesText, String noText, float ox, float oy)
	{
		Actor dialog = new YesNoDialog(text, yesText, noText);
		dialog.setPosition( ox, oy );
		stage.addActor( dialog );

	}
	
	public static Skin getSkin() 
	{ 
		return instance.skin; 
	}
}
