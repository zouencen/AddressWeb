package com.usts.lib;

import java.util.Comparator;

import com.usts.lib.model.Tuple;
public class TupleComparator implements Comparator<Tuple>{
	public int compare(Tuple one, Tuple another) {
         Float val1 = (Float) one.getSecond();
         Float val2 = (Float) another.getSecond();
         return val2.compareTo(val1);  //由大到小排序
    }
}