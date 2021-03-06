package org.lilian.util.graphs.old;

import static org.lilian.util.Series.series;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import org.lilian.Global;
import org.lilian.util.Series;
import org.lilian.util.graphs.old.algorithms.BAGenerator;

public class Graphs
{
	
	public <L, N extends Node<L, N>> List<L> label(List<N> nodes)
	{
		// TODO
		return null;
	}

	public <L, N extends Node<L, N>> Collection<L> labelCollection(Collection<N> nodes)
	{
		// TODO
		return null;
	}
	
	public <L, N extends Node<L, N>> Set<L> labelSet(Set<N> nodes)
	{
		// TODO
		return null;
	}
	
	public <L, N extends Node<L, N>> Iterable<L> labelIterable(Iterable<N> nodes)
	{
		// TODO
		return null;
	}
	
	/**
	 * Finds the given path of labels in the graph. 
	 * 
	 * Example use:
	 * <code>
	 * Walks.find(graph, Arrays.asList("a", "b", "c", "d", "e"));
	 * </code>
	 * 
	 * @param <T>
	 * @return
	 */
	public static <L, N extends Node<L, N>> Set<Walk<L, N>> find(Iterable<L> track, Graph<L, ?> graph)
	{
		// TODO
		return null;
	}	
	
	public static <L, N extends Node<L, N>> Set<L> labels(Graph<L, N> graph)
	{
		Set<L> labels = new LinkedHashSet<L>();
		
		for(N node : graph)
			labels.add(node.label());
		
		return labels;
	}
	
	/**
	 * Returns a graph with the same structure and labels as that in the 
	 * argument, but with the nodes in a different order. Ie. this method 
	 * returns a random isomorphism of the argument.
	 * 
	 * @param graph
	 * @return
	 */
	public static <L, N extends Node<L, N>> BaseGraph<L> shuffle(Graph<L, N> graph)
	{
		List<Integer> shuffle = Series.series(graph.size());
		Collections.shuffle(shuffle);
		
		List<N> nodes = new ArrayList<N>(graph);
		List<BaseGraph<L>.Node> outNodes = new ArrayList<BaseGraph<L>.Node>(graph.size());
		
		BaseGraph<L> out = new BaseGraph<L>(); 
		for(int i : shuffle)
		{
			BaseGraph<L>.Node newNode = out.addNode(nodes.get(i).label());
			outNodes.add(newNode);
		}
		
		for(int i : Series.series(graph.size()))
			for(int j : Series.series(i, graph.size()))
			{
				if(nodes.get(shuffle.get(i)).connected(nodes.get(shuffle.get(j))))
					outNodes.get(i).connect(outNodes.get(j));
			}
		
		return out;
	}
	
		
	public static BaseGraph<String> k2()
	{
		BaseGraph<String> graph = new BaseGraph<String>();
		
		BaseGraph<String>.Node n1 = graph.addNode("a");
		BaseGraph<String>.Node n2 = graph.addNode("b");
		
		n1.connect(n2);
		
		return graph;
	}
	
	public static BaseGraph<String> k3()
	{
		BaseGraph<String> graph = new BaseGraph<String>();
		
		BaseGraph<String>.Node n1 = graph.addNode("a");
		BaseGraph<String>.Node n2 = graph.addNode("b");
		BaseGraph<String>.Node n3 = graph.addNode("c");
		
		n1.connect(n2);
		n2.connect(n3);
		n3.connect(n1);
		
		return graph;
	}	
	
	public static BaseGraph<String> line(int n)
	{
		BaseGraph<String> graph = new BaseGraph<String>();

		if(n == 0)
			return graph;
			
		BaseGraph<String>.Node last = graph.addNode("x"), next;
		for(int i : series(n-1))
		{
			next = graph.addNode("x");
			last.connect(next);
			last = next;
		}
		
		return graph;
	}
	
	public static BaseGraph<String> star(int n)
	{
		BaseGraph<String> graph = new BaseGraph<String>();
			
		BaseGraph<String>.Node center = graph.addNode("x");
		for(int i : Series.series(n))
			center.connect(graph.addNode("x"));
		
		return graph;
	}
	
	public static BaseGraph<String> ladder(int n)
	{
		BaseGraph<String> graph = new BaseGraph<String>();

		if(n == 0)
			return graph;
			
		BaseGraph<String>.Node lastLeft = graph.addNode("x"),
		                       lastRight = graph.addNode("x"),				
		                       nextLeft, nextRight;
		lastLeft.connect(lastRight);
		
		for(int i : series(n-1))
		{
			nextRight = graph.addNode("x");
			nextLeft  = graph.addNode("x");
			
			nextLeft.connect(nextRight);
			
			nextRight.connect(lastRight);
			nextLeft.connect(lastLeft);
			
			lastLeft = nextLeft;
			lastRight = nextRight;
		}
		
		return graph;
	}
	
	/**
	 * A graph based on the example graph in the paper by Jonyer, Holder and Cook
	 * 
	 * @return
	 */
	public static BaseGraph<String> jbc()
	{
		BaseGraph<String> graph = new BaseGraph<String>();

		// * triangle 1
		BaseGraph<String>.Node t1a = graph.addNode("a"),
		                       t1b = graph.addNode("b"),
		                       t1c = graph.addNode("c");
		t1a.connect(t1b);
		t1b.connect(t1c);
		t1c.connect(t1a);
		
		// * triangle 2
		BaseGraph<String>.Node t2a = graph.addNode("a"),
                t2b = graph.addNode("b"),
                t2d = graph.addNode("d");
		
		t2a.connect(t2b);
		t2b.connect(t2d);
		t2d.connect(t2a);

		// * triangle 3
		BaseGraph<String>.Node t3a = graph.addNode("a"),
                               t3b = graph.addNode("b"),
                               t3e = graph.addNode("e");
		
		t3a.connect(t3b);
		t3b.connect(t3e);
		t3e.connect(t3a);	
		
		// * triangle 4
		BaseGraph<String>.Node t4a = graph.addNode("a"),
                               t4b = graph.addNode("b"),
                               t4f = graph.addNode("f");

		t4a.connect(t4b);
		t4b.connect(t4f);
		t4f.connect(t4a);	
		
		// * square 1
		BaseGraph<String>.Node s1x = graph.addNode("x"),
                               s1y = graph.addNode("y"),
                               s1z = graph.addNode("z"),
                               s1q = graph.addNode("q");

		s1x.connect(s1y);
		s1y.connect(s1q);
		s1q.connect(s1z);	
		s1z.connect(s1x);	
			
		// * square 2
		BaseGraph<String>.Node s2x = graph.addNode("x"),
                               s2y = graph.addNode("y"),
                               s2z = graph.addNode("z"),
                               s2q = graph.addNode("q");

		s2x.connect(s2y);
		s2y.connect(s2q);
		s2q.connect(s2z);	
		s2z.connect(s2x);	
		
		// * square 3
		BaseGraph<String>.Node s3x = graph.addNode("x"),                   
                               s3y = graph.addNode("y"),                   
                               s3z = graph.addNode("z"),                   
                               s3q = graph.addNode("q");                   
                                                            
		s3x.connect(s3y);                                                  
		s3y.connect(s3q);                                                  
		s3q.connect(s3z);	                                               
		s3z.connect(s3x);	                                               

		// * square 4
		BaseGraph<String>.Node s4x = graph.addNode("x"),                   
                               s4y = graph.addNode("y"),                   
                               s4z = graph.addNode("z"),                   
                               s4q = graph.addNode("q");                   
                                             
		s4x.connect(s4y);                                                  
		s4y.connect(s4q);                                                  
		s4q.connect(s4z);	                                               
		s4z.connect(s4x);			
		
		// rest
		BaseGraph<String>.Node k = graph.addNode("k"),                   
		                       r = graph.addNode("r"); 
		
		t1a.connect(t2a);
		t2a.connect(t3a);
		t3a.connect(t4a);
		
		t1b.connect(s1y);
		t2b.connect(s4y);
		
		t2d.connect(k);
		k.connect(r);
		
		s1y.connect(s2x);
		s2y.connect(r);
		s3x.connect(r);
		s3y.connect(s4x);
		
		return graph;
	}
	
	public static BaseGraph<String> random(int n, double prob)
	{
		BaseGraph<String> graph = new BaseGraph<String>();
		List<BaseGraph<String>.Node> nodes = new ArrayList<BaseGraph<String>.Node>(n);

		for(int i : series(n))
			nodes.add(graph.addNode("x"));
		
		for(int i : series(n))
			for(int j : series(i+1, n))
				if(Global.random.nextDouble() < prob)
					nodes.get(i).connect(nodes.get(j));
		
		return graph;
	}
	
	public static BaseGraph<String> ba(int n, int initial, int attach)
	{
		BAGenerator gen = new BAGenerator(initial, attach);
		
		gen.iterate(n - initial);
		
		return gen.graph();
	}

	public static BaseGraph<String> single(String label)
	{
		BaseGraph<String> graph = new BaseGraph<String>();
		graph.addNode(label);
		
		return graph;
		
	}

	public static <L, N extends Node<L, N>> List<Integer> degrees(Graph<L, N> graph)
	{
		List<Integer> degrees = new ArrayList<Integer>(graph.size());
		
		for(N node : graph)
			degrees.add(node.neighbours().size());
		
		return degrees;
	}
	
	
}
