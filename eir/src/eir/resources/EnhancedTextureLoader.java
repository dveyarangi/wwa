package eir.resources;

import mandala.Mandala;
import yarangi.image.ColorUtil;
import yarangi.image.Mask;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.assets.loaders.FileHandleResolver;
import com.badlogic.gdx.assets.loaders.TextureLoader;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;

public class EnhancedTextureLoader extends TextureLoader
{
	private Pixmap generatedPixels;

	public EnhancedTextureLoader(final FileHandleResolver resolver)
	{
		super( resolver );
	}
	@Override
	public void loadAsync (final AssetManager manager, final String fileName, final TextureParameter parameter)
	{
		FileHandle handle = Gdx.files.internal( fileName );
		if(handle.exists())
		{
			super.loadAsync( manager, fileName, parameter );
			return;
		}

		// generating texture buffer:
		Mask mandala = Mandala.generateMandala( fileName );

		// preparing background:
		String backgroundFile = "anima\\transparent_" + mandala.size() + "x" + mandala.size() + ".png";
		generatedPixels = new Pixmap( Gdx.files.internal( backgroundFile ) );

		// copying buffer to background:
		for(int mx = 0; mx < mandala.size(); mx ++) for(int my = 0; my < mandala.size(); my ++)
		{
			int source = mandala.at( mx, my );
			int b = source&0xFF;
			int g = source>>8&0xFF;
			int r = source>>16&0xFF;
			int a = source>>24&0xFF;
			generatedPixels.drawPixel( mx, my, ColorUtil.toRGBA8888( a, r, g, b )  );
		}
	}

	@Override
	public Texture loadSync (final AssetManager manager, final String fileName, final TextureParameter parameter)
	{
		FileHandle handle = Gdx.files.internal( fileName );
		if(handle.exists())
		{
			return super.loadSync( manager, fileName, parameter );
		}

		return new Texture( generatedPixels );
	}
}
