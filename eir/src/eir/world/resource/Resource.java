package eir.world.resource;

import com.badlogic.gdx.utils.Pool;
import com.badlogic.gdx.utils.Pool.Poolable;

/**
 * a resource instance
 */
public class Resource implements Poolable
{
	static Pool<Resource> pool = new Pool<Resource> ()
	{
		@Override
		protected Resource newObject()
		{
			return new Resource();
		}
	};
	
	public enum Type 
	{ 
		OOZE, 
		BOOZE
	};
	
	
	public static Resource createResource( Type type, int amount )
	{
		if( amount<0 )
			throw new IllegalArgumentException("resource amount cannot be negative");
		
		Resource r = pool.obtain();
		r.amount = amount;
		r.type = type;
		return r;
	}
	
	
	
	/////////////////////////////////////////////////////
	// members 
	
	private Type type;
	private int amount;
	
	/////////////////////////////////////////////////////
	// methods 
	
	@Override
	public void reset()
	{
		amount = 0;
		type = null;
	}
	
	/**
	 * recycle this instance
	 */
	public void recycle()
	{
		pool.free(this);
	}
	
	
	/**
	 * split this resource in two <br>
	 * will transfer as much as possible to the new object ( minimum between <b>amount</b> and <b>this.amount</b> )
	 * @param amount how much will the new instance hold?
	 * @return a resource object containing <b>amount</b>. the called object will decrease in the same amount.
	 */
	public Resource split( int amount )
	{
		Resource r = pool.obtain();
		this.transter(r, amount);
		
		return r;
	}
	
	/**
	 * transfer <b>amount</b> of this resource to Resource <b>r</b> <br>
	 * <b>this</b> and <b>r</b> must be of the same type. <br>
	 * will transfer as much as possible ( minimum between <b>amount</b> and <b>this.amount</b> )
	 * @param r Resource instance to transfer to
	 * @param amount how much of this resource to transfer
	 */
	public void transter( Resource r, int amount )
	{
		if( amount<0 )
			throw new IllegalArgumentException("cannot transfer negative amount");
		
		if( r.type != this.type )
			throw new IllegalArgumentException("two resources must be of the same type to transfer");
		
		if( this.amount < amount )
			amount = this.amount;
		
		r.amount = amount;
		this.amount -= amount;
	}
	
	/**
	 * merge <b>r</b> with <b>this</b>. <br>
	 * <b>r</b> will be recycled.
	 * @param r
	 */
	public void merge( Resource r )
	{
		r.transter(this, r.amount);
		r.recycle();
	}
	
	/////////////////////////////////////////////////////
	// getters, setters
	public Type getType()
	{
		return type;
	}

//	public void setType(Type type)
//	{
//		this.type = type;
//	}

	public int getAmount()
	{
		return amount;
	}

//	public void setAmount(int amount)
//	{
//		if( amount<0 )
//			throw new IllegalArgumentException("resource amount cannot be negative");
//		
//		this.amount = amount;
//	}
}
