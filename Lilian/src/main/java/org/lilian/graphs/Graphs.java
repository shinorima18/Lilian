package org.lilian.graphs;

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
	
//	public <L, N extends Node<L, N>> List<L> label(List<N> nodes)
//	{
//		// TODO
//		return null;
//	}
//
//	public <L, N extends Node<L, N>> Collection<L> labelCollection(Collection<N> nodes)
//	{
//		// TODO
//		return null;
//	}
//	
//	public <L, N extends Node<L, N>> Set<L> labelSet(Set<N> nodes)
//	{
//		// TODO
//		return null;
//	}
//	
//	public <L, N extends Node<L, N>> Iterable<L> labelIterable(Iterable<N> nodes)
//	{
//		// TODO
//		return null;
//	}
//	
//	/**
//	 * Finds the given path of labels in the graph. 
//	 * 
//	 * Example use:
//	 * <code>
//	 * Walks.find(graph, Arrays.asList("a", "b", "c", "d", "e"));
//	 * </code>
//	 * 
//	 * @param <T>
//	 * @return
//	 */
//	public static <L, N extends Node<L, N>> Set<Walk<L, N>> find(Iterable<L> track, Graph<L, ?> graph)
//	{
//		// TODO
//		return null;
//	}	
//	
//	public static <L, N extends Node<L, N>> Set<L> labels(Graph<L, N> graph)
//	{
//		Set<L> labels = new LinkedHashSet<L>();
//		
//		for(N node : graph)
//			labels.add(node.label());
//		
//		return labels;
//	}
//	
//	/**
//	 * Returns a graph with the same structure and labels as that in the 
//	 * argument, but with the nodes in a different order. Ie. this method 
//	 * returns a random isomorphism of the argument.
//	 * 
//	 * @param graph
//	 * @return
//	 */
//	public static <L, N extends Node<L, N>> BaseGraph<L> shuffle(Graph<L, N> graph)
//	{
//		List<Integer> shuffle = Series.series(graph.size());
//		Collections.shuffle(shuffle);
//		
//		List<N> nodes = new ArrayList<N>(graph);
//		List<BaseGraph<L>.Node> outNodes = new ArrayList<BaseGraph<L>.Node>(graph.size());
//		
//		BaseGraph<L> out = new BaseGraph<L>(); 
//		for(int i : shuffle)
//		{
//			BaseGraph<L>.Node newNode = out.addNode(nodes.get(i).label());
//			outNodes.add(newNode);
//		}
//		
//		for(int i : Series.series(graph.size()))
//			for(int j : Series.series(i, graph.size()))
//			{
//				if(nodes.get(shuffle.get(i)).connected(nodes.get(shuffle.get(j))))
//					outNodes.get(i).connect(outNodes.get(j));
//			}
//		
//		return out;
//	}
//	
//		
	/**
	 * Returns a fully connected UT graph of the given size, with the given label
	 * on all nodes (and null for all tags)
	 * 
	 * @param size
	 * @param label
	 * @return
	 */
	public static UTGraph<String, String> k(int size, String label)
	{
		UTGraph<String, String> graph = new MapUTGraph<String, String>();
		
		for(int i : Series.series(size))
		{
			UTNode<String, String> node = graph.add(label);
			
			for(int j : Series.series(graph.size() - 1 ))
				node.connect(graph.nodes().get(j));
		}
		
		return graph;
	}
	
	public static UTGraph<String, String> line(int n, String label)
	{
		UTGraph<String, String> graph = new MapUTGraph<String, String>();

		if(n == 0)
			return graph;
			
		UTNode<String, String> last = graph.add("x"), next;
		for(int i : series(n-1))
		{
			next = graph.add("x");
			last.connect(next);
			last = next;
		}
		
		return graph;
	}
	
	/**
	 * Returns a graph with n nodes, arranged in a star topology (ie. one node 
	 * is connected to all others, and all others are connected only to that node).
	 * 
	 * 
	 * @param n
	 * @param label
	 * @return
	 */
	public static UTGraph<String, String> star(int n, String label)
	{
		UTGraph<String, String> graph = new MapUTGraph<String, String>();
			
		UTNode<String, String> center = graph.add(label);
		for(int i : Series.series(n))
			center.connect(graph.add(label));
		
		return graph;
	}
	
	public static UTGraph<String, String> ladder(int n, String label)
	{
		UTGraph<String, String> graph = new MapUTGraph<String, String>();

		if(n == 0)
			return graph;
			
		Node<String> 	lastLeft = graph.add(label),
						lastRight = graph.add(label),				
						nextLeft, nextRight;
		lastLeft.connect(lastRight);
		
		for(int i : series(n-1))
		{
			nextRight = graph.add("x");
			nextLeft  = graph.add("x");
			
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
	public static UTGraph<String, String> jbc()
	{
		UTGraph<String, String> graph = new MapUTGraph<String, String>();

		// * triangle 1
		UTNode<String, String> t1a = graph.add("a"),
		                       t1b = graph.add("b"),
		                       t1c = graph.add("c");
		t1a.connect(t1b);
		t1b.connect(t1c);
		t1c.connect(t1a);
		
		// * triangle 2
		UTNode<String, String> t2a = graph.add("a"),
                               t2b = graph.add("b"),
                               t2d = graph.add("d");
		
		t2a.connect(t2b);
		t2b.connect(t2d);
		t2d.connect(t2a);

		// * triangle 3
		UTNode<String, String> t3a = graph.add("a"),
                               t3b = graph.add("b"),
                               t3e = graph.add("e");
		
		t3a.connect(t3b);
		t3b.connect(t3e);
		t3e.connect(t3a);	
		
		// * triangle 4
		UTNode<String, String> t4a = graph.add("a"),
                               t4b = graph.add("b"),
                               t4f = graph.add("f");

		t4a.connect(t4b);
		t4b.connect(t4f);
		t4f.connect(t4a);	
		
		// * square 1
		UTNode<String, String> s1x = graph.add("x"),
                               s1y = graph.add("y"),
                               s1z = graph.add("z"),
                               s1q = graph.add("q");

		s1x.connect(s1y);
		s1y.connect(s1q);
		s1q.connect(s1z);	
		s1z.connect(s1x);	
			
		// * square 2
		UTNode<String, String> s2x = graph.add("x"),
                               s2y = graph.add("y"),
                               s2z = graph.add("z"),
                               s2q = graph.add("q");

		s2x.connect(s2y);
		s2y.connect(s2q);
		s2q.connect(s2z);	
		s2z.connect(s2x);	
		
		// * square 3
		UTNode<String, String> s3x = graph.add("x"),                   
                               s3y = graph.add("y"),                   
                               s3z = graph.add("z"),                   
                               s3q = graph.add("q");                   
                                                        
		s3x.connect(s3y);                                              
		s3y.connect(s3q);                                                  
		s3q.connect(s3z);	                                               
		s3z.connect(s3x);	                                               

		// * square 4
		UTNode<String, String> s4x = graph.add("x"),                   
                               s4y = graph.add("y"),                   
                               s4z = graph.add("z"),                   
                               s4q = graph.add("q");                   
                                             
		s4x.connect(s4y);                                              
		s4y.connect(s4q);                                              
		s4q.connect(s4z);	                                           
		s4z.connect(s4x);			
		
		// rest
		UTNode<String, String> k = graph.add("k"),                   
		                       r = graph.add("r"); 
		
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
	

	public static UTGraph<String, String> single(String label)
	{
		UTGraph<String, String> graph = new MapUTGraph<String, String>();
		graph.add(label);
		
		return graph;
		
	}

	public static List<Integer> degrees(Graph<?> graph)
	{
		List<Integer> degrees = new ArrayList<Integer>(graph.size());
		
		for(Node<?> node : graph.nodes())
			degrees.add(node.neighbors().size());
		
		return degrees;
	}
	
	/**
	 * Adds one graph to another.
	 * 
	 * @param graph
	 * @param addition
	 */
	public static <L, T> void add(UTGraph<L, T> graph, UTGraph<L, T> addition)
	{
		List<UTNode<L, T>> nodesAdded = new ArrayList<UTNode<L,T>>(addition.size());
		
		// * Add all nodes
		for(UTNode<L, T> aNode : addition.nodes())
		{
			UTNode<L, T> gNode = graph.add(aNode.label());
			nodesAdded.add(gNode);
		}
		
		// * Add all links
		for(UTLink<L, T> link : addition.links())
		{
			// System.out.println('.');
			int i = link.first().index(), j = link.second().index();
			
			nodesAdded.get(i).connect(nodesAdded.get(j), link.tag());
		}
	}

}
