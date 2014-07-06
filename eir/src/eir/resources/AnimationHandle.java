package eir.resources;

public class AnimationHandle
{
	private TextureAtlasHandle atlas;
	private final String regionName;

	public AnimationHandle(final TextureAtlasHandle atlas, final String regionName)
	{
		this.atlas = atlas;
		this.regionName = regionName;
	}

	public TextureAtlasHandle getAtlas() { return atlas; }
	public String getRegionName() { return regionName; }

	@Override
	public int hashCode() { return atlas.hashCode() ^ regionName.hashCode(); }
	@Override
	public boolean equals(final Object object)
	{
		AnimationHandle that = (AnimationHandle) object;
		return regionName.equals( that.regionName )
			&& atlas.equals( that.atlas );
	}

	@Override
	public String toString() { return atlas + ":" + regionName; }
}
