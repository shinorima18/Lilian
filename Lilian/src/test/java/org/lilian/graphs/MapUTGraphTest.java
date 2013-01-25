package org.lilian.graphs;

import static org.junit.Assert.*;

import org.junit.Test;

public class MapUTGraphTest
{

	@Test
	public void testMapUTGraph()
	{
		UTGraph<String, Double> graph = new MapUTGraph<String, Double>();
	}

	@Test
	public void testToString()
	{
		UTGraph<String, Double> graph = new MapUTGraph<String, Double>();
		
		UTNode<String, Double> a = graph.add("a"),
		                       b = graph.add("b"),
		                       c = graph.add("c");
	
		a.connect(b, 0.5);
		
		System.out.println(graph);
	}

	@Test
	public void starTest()
	{
		UTGraph<String, Double> graph = new MapUTGraph<String, Double>();
		
		UTNode<String, Double> a = graph.add("a"),
		                       b = graph.add("b"),
		                       c = graph.add("c"),
		                       d = graph.add("d"),
		                       e = graph.add("e");
	
		b.connect(a, 0.5);
		c.connect(a, 0.5);
		d.connect(a, 0.5);
		e.connect(a, 0.5);
		
		System.out.println(graph);
		
		a.disconnect(b);
		
		System.out.println(graph);
		
		a.remove();
		
		System.out.println(graph);	
	}
	
	@Test
	public void testRemove()
	{
		UTGraph<String, Double> graph = new MapUTGraph<String, Double>();
		
		UTNode<String, Double> a = graph.add("a"),
		                       b = graph.add("b"),
		                       c = graph.add("c"),
		                       d = graph.add("d"),
		                       e = graph.add("e");
	
		b.connect(a, 0.5);
		c.connect(a, 0.5);
		d.connect(a, 0.5);
		e.connect(a, 0.5);
		
		System.out.println(graph.numLinks() + " " + graph.size());
		
		assertEquals(4, graph.numLinks());
		assertEquals(5, graph.size());
		
		a.remove();
		
		assertEquals(0, graph.numLinks());
		assertEquals(4, graph.size());
	}
	
	@Test
	public void testConnected()
	{
		DTGraph<String, Double> graph = new MapDTGraph<String, Double>();
		
		DTNode<String, Double> a = graph.add("a"),
		                       b = graph.add("b"),
		                       c = graph.add("c");

	
		a.connect(b, 0.5);
		
		assertTrue(a.connected(b));
		assertFalse(a.connected(a));
		assertFalse(b.connected(a));
		assertFalse(a.connected(c));
		assertFalse(c.connected(a));
		assertFalse(b.connected(c));
		assertFalse(c.connected(b));
	}
	
}
