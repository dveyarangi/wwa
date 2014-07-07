package eir.resources;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.google.common.base.Preconditions;

public class TextureHandle
{
	private String path;

	public TextureHandle( final String path )
	{
		this.path = Preconditions.checkNotNull( path );
	}
	public String getPath() { return path; }

	@Override
	public int hashCode() { return path.hashCode(); }
	@Override
	public boolean equals(final Object object)
	{
		TextureHandle that = (TextureHandle) object;
		return path.equals( that.path );
	}

	@Override
	public String toString() { return getPath(); }

	public Texture load()
	{
		return new Texture(Gdx.files.internal( getPath() ) + ".png");
	}
}
