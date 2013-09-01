/**
 * 
 */
package eir.game.screens;


import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.Align;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;

/**
 * @author dveyarangi
 *
 */
class YesNoDialog extends Actor
{
	private Table dialogTable;
	
	YesNoDialog(String message, String yesText, String noText)
	{
		dialogTable = new Table(UI.getSkin());
		
		dialogTable.add(new Label( message, UI.getSkin() )).colspan( 2 ).row();
		
		
		TextButton yesButton = new TextButton( yesText, UI.getSkin() );
		dialogTable.align( Align.left ).add(yesButton);
		
		TextButton noButton = new TextButton( noText, UI.getSkin() );
		dialogTable.align( Align.right ).add(noButton);
		
		yesButton.addListener( 
				new ClickListener() {
					public void clicked (InputEvent event, float x, float y) {
				}
		});
	}
	
	public void draw (SpriteBatch batch, float parentAlpha) {
		dialogTable.draw( batch, parentAlpha );
	}
	
	public void setPosition( float ox, float oy )
	{
		dialogTable.setPosition( ox, oy );
	}
}
