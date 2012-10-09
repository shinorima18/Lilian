package org.lilian.util.graphs;

import static org.junit.Assert.*;

import java.util.Collection;

import org.junit.Test;
import org.lilian.util.graphs.algorithms.CostFunctions;
import org.lilian.util.graphs.algorithms.GraphMDL;
import org.lilian.util.graphs.algorithms.InexactCost;
import org.lilian.util.graphs.algorithms.Subdue;

public class SubdueTest
{

	@Test
	public void test()
	{
		BaseGraph<String> in = 
				// Graphs.jbc();
				// Graphs.random(10, 0.5);
				Graphs.ba(30, 3, 1);
		
		
		InexactCost<String> costFunction = CostFunctions.uniform();
		Subdue<String, BaseGraph<String>.Node> sub = 
				new Subdue<String, BaseGraph<String>.Node>(
						in, costFunction, 0.0);
		
		Collection<Subdue<String, BaseGraph<String>.Node>.Substructure> subs =
				sub.search(4, 10, 10, -1);
		
		System.out.println(GraphMDL.mdl(in));
		for(Subdue<String, BaseGraph<String>.Node>.Substructure structure : subs)
			System.out.println(structure);
	}

}
