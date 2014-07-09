package tests.circle;

public interface IMaskingMethod
{
	public int eval(int source, int target);

	public static IMaskingMethod ADD = new IMaskingMethod(){

		@Override
		public int eval( final int source, final int  target )
		{
			int sb = source&0xFF;
			int sg = source>>8&0xFF;
			int sr = source>>16&0xFF;
			int sa = source>>24&0xFF;
			int tb = target&0xFF;
			int tg = target>>8&0xFF;
			int tr = target>>16&0xFF;
			int ta = target>>24&0xFF;

			int ra = Math.min( sa + ta, 0xFF);
			int rr = Math.min( sr + tr, 0xFF);
			int rg = Math.min( sg + tg, 0xFF);
			int rb = Math.min( sb + tb, 0xFF);

			return ColorUtil.toARGB( ra, rr, rg, rb );
		}

	};

	public static IMaskingMethod MULTIPLY = new IMaskingMethod(){

		@Override
		public int eval( final int source, final int  target )
		{
			float sb = (source&0xFF)/255f;
			float sg = (source>>8&0xFF)/255f;
			float sr = (source>>16&0xFF)/255f;
			float sa = (source>>24&0xFF)/255f;
			float tb = (target&0xFF)/255f;
			float tg = (target>>8&0xFF)/255f;
			float tr = (target>>16&0xFF)/255f;
			float ta = (target>>24&0xFF)/255f;
			float outA = sa + ta*(1-sa);
			int ra = (int)(255f*Math.max( ta, sa ));
			int rr = (int)(255f*Math.min( sa*sr + (1-sa)*tr, 1f ));
			int rg = (int)(255f*Math.min( sa*sg + (1-sa)*tg, 1f ));
			int rb = (int)(255f*Math.min( sa*sb + (1-sa)*tb, 1f ));

			return ColorUtil.toARGB( ra, rr, rg, rb );
		}

	};
}
