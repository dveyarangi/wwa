/**
 * 
 */


import com.badlogic.gdx.tools.imagepacker.TexturePacker2;

/**
 * Packs textures into TextureAtlas-readable format
 * 
 * @author dveyarangi
 *
 */
public class TexturePacker
{
	  public static void main (String[] args) throws Exception {
	         TexturePacker2.
	         process("anima\\explosion01", // source dir
	        		 "anima\\explosion01\\res",  // target dir
	        		 "explosion04" // atlas name
	        		 );
	 }
}
