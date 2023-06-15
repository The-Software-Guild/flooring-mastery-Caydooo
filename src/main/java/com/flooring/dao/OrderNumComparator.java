package com.flooring.dao;

import java.util.Comparator;

public class OrderNumComparator implements Comparator<Integer> {
	@Override
	public int compare(Integer o1, Integer o2) {
		if(o1 < o2) {
			return -1;
		} else if (o1 > o2) {
			return 1;
		} else {
			return 0;
		}
	}

}
