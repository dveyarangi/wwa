package tests.circle;

public class Crayon
{

	private static float COMPRESSION_LIMIT = 0.1f;
	private static float NICENESS = 1f;

	private void adjustCrayonHeight(
			final int px, final int py, // crayon location
			final Mask crayon,
			final Mask paper,
			final Mask layer,
			final float force
			)
	{
		// crayon min height
		float HCrayonMin = crayon.getMin();

		// layer max height at the crayon application location
		float Hmax = Float.MIN_VALUE;

		// layer min height at the crayon application location
		float Hmin = Float.MAX_VALUE;
		for(int mx = 0; mx < crayon.size(); mx ++)
		{
			for(int my = 0; my < crayon.size(); my ++)
			{
				float Hpaper = paper.at( mx+px, my+py );
				if(Hmin < Hpaper)
				{
					Hmin = Hpaper;
				}
				if(Hmax > Hpaper)
				{
					Hmax = Hpaper;
				}
			}
		}

		float dHPaper = Hmax - Hmin;
		float Havg;
		float FAvg = 0;
		while( dHPaper > COMPRESSION_LIMIT )
		{
			dHPaper = Hmax - Hmin;
			Havg = dHPaper / 2;
			FAvg = 0;

			for(int mx = 0; mx < crayon.size(); mx ++)
			{
				for(int my = 0; my < crayon.size(); my ++)
				{
					int lx = mx+px;
					int ly = my+py;
					float Hpaper = paper.at(lx,ly);
					float Hwax = layer.at(lx,ly);
					float Hcrayon = crayon.at(lx,ly);

					float dH = Hpaper + Hwax - (Hcrayon - HCrayonMin + Havg);
					if(dH > 0)
					{
						FAvg += NICENESS * dH;
					}
				}
			}

			if(force < FAvg)
			{
				Hmin = Havg;
			} else
			{
				Hmax = Havg;
			}
		}

		Havg = (Hmax - Hmin) / 2;
		for(int mx = 0; mx < crayon.size(); mx ++)
		{
			for(int my = 0; my < crayon.size(); my ++)
			{
				int lx = mx+px;
				int ly = my+py;
				float crayonCompression = crayon.at(lx,ly) - HCrayonMin + Havg;
				crayon.put( lx, ly, crayonCompression );
			}
		}
	}

	private void addNewWax(
			final int px, final int py, // crayon location
			final float vx, final float vy,
			final Mask crayon,
			final Mask paper,
			final Mask layer,
			final float force
			)
	{
		float vnx = vx / Math.max( vx, vy );
		float vny = vy / Math.max( vx, vy );

		for(int mx = 0; mx < crayon.size(); mx ++)
		{
			for(int my = 0; my < crayon.size(); my ++)
			{
				int lx = mx+px;
				int ly = my+py;


			}
		}
	}
}
