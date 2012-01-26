package org.lilian.search.evo;

import static org.junit.Assert.*;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.List;

import javax.imageio.ImageIO;

import org.junit.Test;
import org.lilian.data.real.AffineMap;
import org.lilian.data.real.Draw;
import org.lilian.data.real.Point;
import org.lilian.data.real.fractal.IFS;
import org.lilian.data.real.fractal.IFSTarget;
import org.lilian.data.real.fractal.IFSs;
import org.lilian.search.Builder;
import org.lilian.util.Functions;
import org.lilian.util.Series;

public class ESTest
{
	private static final int TARGET_SIZE = 10000;
	private static final int POP_SIZE = 100;	
	private static final double VAR = 0.01;
	private static final int SAMPLE_SIZE = 150;
	private static final int GENERATIONS = 1000;
	
	@Test
	public void testIFS()
	{
		String name = "madrigal";
		File dir = new File("/home/peter/Documents/PhD/output/es/" + name + "/");
		dir.mkdirs();
		
		IFS<AffineMap> targetModel = IFSs.sierpinski();
		List<Point> targetSet = targetModel.generator().generate(TARGET_SIZE);
		Target<IFS<AffineMap>> target = new IFSTarget<AffineMap>(SAMPLE_SIZE, targetSet); 
		
		Builder<IFS<AffineMap>> builder = IFS.builder(3, AffineMap.builder(2));
		List<List<Double>> initial = ES.initial(POP_SIZE, builder.numParameters(), VAR);
		
		ES<IFS<AffineMap>> es = new ES<IFS<AffineMap>>(
				builder, target, initial, 
				2, initial.size()*2, 0, 
				ES.CrossoverMode.UNIFORM);
		
		Functions.tic();		
		for(int i : Series.series(GENERATIONS))
		{
			es.breed();

			if(i%(GENERATIONS/10) == 0)
			{
				write(es.best().instance(), dir, String.format("out%04d", i));
				System.out.println("generation " + i + ": " + Functions.toc() + " seconds.");
				Functions.tic();				
			}	
		}
	}
	
	private void write(IFS<AffineMap> ifs, File dir, String name)
	{
		double[] xrange = new double[]{-2.1333, 2.1333};
		double[] yrange = new double[]{-1.2, 1.2};
		
		BufferedImage image = Draw.draw(ifs.generator(), 10000000, xrange, yrange, 1920/4, 1080/4, true);
		try
		{
			ImageIO.write(image, "PNG", new File(dir, name + ".png") );
		} catch (IOException e)
		{
			e.printStackTrace();
		}
	}	

}