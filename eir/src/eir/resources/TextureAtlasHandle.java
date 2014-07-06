package eir.resources;

public class TextureAtlasHandle
{
	private String path;

	TextureAtlasHandle( final String path)
	{
		this.path = path;
	}

	public String getPath() { return path; }

	@Override
	public int hashCode() { return path.hashCode(); }
	@Override
	public boolean equals(final Object object)
	{
		TextureAtlasHandle that = (TextureAtlasHandle) object;
		return path.equals( that.path );
	}

	@Override
	public String toString() { return getPath(); }

}
