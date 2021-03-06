package org.lilian.data.real;

import static java.lang.Math.cos;
import static java.lang.Math.sin;
import static org.lilian.util.Series.series;

import java.util.List;

import org.apache.commons.math.linear.ArrayRealVector;
import org.apache.commons.math.linear.RealVector;
import org.lilian.Global;
import org.lilian.util.Series;

public class Generators
{

	public static Generator<Integer> uniform(int lower, int upper)
	{
		return new UniformGenerator(lower, upper);
	}
	
	private static class UniformGenerator extends AbstractGenerator<Integer>
	{
		private int lower, upper;

		public UniformGenerator(int lower, int upper)
		{
			this.lower = lower;
			this.upper = upper;
		}
		
		@Override
		public Integer generate()
		{	
			return Global.random.nextInt(upper - lower) + lower;
		}
	}
	
	public static Generator<Double> logisticMap(double r)
	{
		return new LogisticMapGenerator(r);
	}
	
	public static Generator<Boolean> logisticMapBinary(double r)
	{
		return new LogisticMapBinaryGenerator(r);
	}
	
	private static class LogisticMapGenerator extends AbstractGenerator<Double>
	{
		private double x = Global.random.nextDouble();
		private double r;
		
		public LogisticMapGenerator(double  r)
		{
			this.r = r;
			
			for(int i : series(50))
				generate();
		}
		
		@Override
		public Double generate()
		{
			x = x * r * (1.0 - x);
			return x;
		}
	}
	
	private static class LogisticMapBinaryGenerator extends AbstractGenerator<Boolean>
	{
		private LogisticMapGenerator master;
		
		public LogisticMapBinaryGenerator(double  r)
		{
			master = new LogisticMapGenerator(r);
		}
		
		@Override
		public Boolean generate()
		{
			return master.generate() < 0.5;
		}
	}
	
	public static Generator<Integer> exponential(int lower)
	{
		return new ExpoGenerator(lower);
	}
	
	private static class ExpoGenerator extends AbstractGenerator<Integer>
	{
		private int lower;
		
		public ExpoGenerator(int lower)
		{
			this.lower = lower;
		}

		@Override
		public Integer generate()
		{
			int i = 0;
			
			while(Global.random.nextBoolean())
				i++;
			
			return i + lower;
		}
	}
	
	public static Generator<Point> henon()
	{
		return fromMap(Maps.henon(), new Point(2));
	}

	public static Generator<Point> logistic()
	{
		double eoc = 3.0 + ((1+Math.sqrt(6)) - 3) * (1/(1 - (1/4.669201609102990671853203821578)));
		return fromMap(Maps.logistic(eoc), new Point(0.5));
	}
	
	public static Generator<Point> ikeda()
	{
		return new Ikeda();
	}
	
	private static class Ikeda extends AbstractGenerator<Point>
	{
		private static final int PRE = 50;
		public double x, y, u;
		
		public Ikeda()
		{
			this(0.9);
		}
		
		public Ikeda(double u)
		{
			this.u = u;
			
			generate(PRE);
		}
		
		@Override
		public Point generate()
		{
			double t = 0.4 - 6.0 / (1.0 + x*x + y*y),
			       xx = 1 + u * (x * cos(t) - y * sin(t)),
			       yy = u * (x * sin(t) + y * cos(t));
			
			x = xx;
			y = yy;
			
			return new Point(x, y);
		}
		
	}	
	
	public static Generator<Point> rossler()
	{
		return rossler(0.2, 0.2, 5.7, 0.001, 200);
	}
	
	public static Generator<Point> rossler(double a, double b, double c, double h, int skip)
	{
		Generator<Point> rossler = new RK4(new RosslerDerivative(a, b, c), h, new ArrayRealVector(3), skip);
		
		rossler.generate(1000000);
		
		return rossler;
	}
	
	private static class RosslerDerivative implements RK4.Derivative
	{
		double a, b, c;
		
		public RosslerDerivative(double a, double b, double c)
		{
			this.a = a;
			this.b = b;
			this.c = c;
		}
		
		@Override
		public RealVector derivative(RealVector in, double t)
		{
			double x = in.getEntry(0),
			       y = in.getEntry(1),
			       z = in.getEntry(2);
			
			return new ArrayRealVector(new double[]{
				- y - z,
				x + a * y,
				b + z * (x - c)
			}, 0, 3);
		}
	}
	
	public static Generator<Point> lorenz()
	{
		return lorenz(10.0, 28.0, 8.0/3.0, 0.001);
	}
	
	public static Generator<Point> lorenz(double s, double r, double b, double h)
	{
		Generator<Point> lorenz = new RK4(new LorenzDerivative(s, r, b), h, 
				new ArrayRealVector(new double[]{0.1, 0.1, 0.1}, 0, 3));
		
		lorenz.generate(1000000);
		
		return lorenz;
	}
	
	private static class LorenzDerivative implements RK4.Derivative
	{
		double s, r, b;
		
		public LorenzDerivative(double s, double r, double b)
		{
			this.s = s;
			this.r = r;
			this.b = b;
		}
		
		@Override
		public RealVector derivative(RealVector in, double t)
		{
			double x = in.getEntry(0),
			       y = in.getEntry(1),
			       z = in.getEntry(2);
			
			return new ArrayRealVector(new double[]{
				s * (y - x),
				x * (r - z) - y,
				x * y - b * z
			}, 0, 3);
		}
	}	
	
	public static Generator<Point> mapped(Generator<Point> master, Map map)
	{
		return new MappedGenerator(map, master);
	}
	
	private static class MappedGenerator implements Generator<Point>
	{
		private Map map;
		private Generator<Point> master;

		public MappedGenerator(Map map, Generator<Point> master)
		{
			this.map = map;
			this.master = master;
		}

		@Override
		public Point generate()
		{
			Point p = master.generate();
			return map.map(p);
		}

		@Override
		public List<Point> generate(int n)
		{
			List<Point> p = master.generate(n);
			return map.map(p);
		}
	}
	
	public static Generator<Point> fromMap(Map map, Point initial)
	{
		return new MapGenerator(map, initial);
	}
	
	private static class MapGenerator extends AbstractGenerator<Point>
	{
		private Map map;
		private Point initial;
		private Point p; 
		
		public MapGenerator(Map map, Point initial)
		{
			this.map = map;
			p = initial;
			
			for(int i : series(100))
				generate();
		}

		@Override
		public Point generate()
		{
			p = map.map(p);
			return p;
		}
		
		
	}
	
}
