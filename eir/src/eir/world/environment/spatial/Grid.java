package eir.world.environment.spatial;

import yarangi.math.FastMath;

public abstract class Grid <E>
{

	/**
	 * buckets array
	 * see {@link #hash(int, int)} for indices here
	 * TODO: hashmap is slow!!!
	 * no duplicates here is important, and depends on correctness of the iteration over the inserted shape
	 */
	protected E [] map;
	/**
	 * number of buckets
	 */
	protected int size;
	/**
	 * dimensions of area this hashmap represents
	 */
	protected float width;
	/**
	 * dimensions of area this hashmap represents
	 */
	protected float height;
	/**
	 * size of single hash cell
	 */
	protected float cellSize;
	/**
	 * 1/cellSize, to speed up some calculations
	 */
	protected float invCellsize;
	/**
	 * cellSize/2
	 */
	protected float halfCellSize;

	protected int gridWidth;

	/**
	 * hash cells amounts
	 */
	protected int halfGridWidth;
	/**
	 * hash cells amounts
	 */
	protected int halfGridHeight;

	@SuppressWarnings("unchecked")
	public Grid(final String name, final int size, final float cellSize, final float width, final float height)
	{
		if(size <= 0) throw new IllegalArgumentException("Size must be positive");
		this.size = size;

		this.width = width;
		this.height = height;

		this.gridWidth = (int) (width / cellSize);
		this.cellSize = cellSize;
		this.invCellsize = 1.f / this.cellSize;
		this.halfGridWidth = (int)(gridWidth/2f);
		this.halfGridHeight = (int)(height/2f/cellSize);
		this.halfCellSize = cellSize/2.f;

		map = createGrid();
	}

	/**
	 * Create array containing the grid
	 */
	protected abstract E [] createGrid();

	/**
	 * Create grid index from x-y array index
	 * @param x
	 * @param y
	 * @return
	 */
	protected int index(final int x, final int y)
	{
		return x + y * gridWidth;
	}

	/**
	 * @return width of the area, covered by this map
	 */
	public final float getHeight() { return height; }

	/**
	 * @return height of the area, covered by this map
	 */
	public final float getWidth() { return width; }

	/**
	 * @return size (height and width) of a single cell
	 */
	public final float getCellSize() { return cellSize; }

	protected final int toGridIndex( final float value )
	{
		return FastMath.round(value * invCellsize);
	}

	public final boolean isInvalidIndex( final int x, final int y )
	{
		return x < -halfGridWidth || x > halfGridWidth || y < -halfGridHeight || y > halfGridHeight;
	}

}