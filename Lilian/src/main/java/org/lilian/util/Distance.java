package org.lilian.util;

import java.io.Serializable;

public interface Distance<T> extends Serializable
{
	public double distance(T a, T b); 
}
