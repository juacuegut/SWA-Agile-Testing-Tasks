package edu.upc.talent.swqa.util;

import java.util.List;
import java.util.Objects;
import java.util.Set;

public final class Utils {
  public static boolean eq(Object o1, Object o2) {
    return Objects.equals(o1, o2);
  }

  public static <A> List<A> join(List<A> list1, List<A> list2) {
    var res = new java.util.ArrayList<A>();
    res.addAll(list1);
    res.addAll(list2);
    return res;
  }

  public static <A> Set<A> union(Set<A> set1, Set<A> set2) {
    var res = new java.util.HashSet<A>();
    res.addAll(set1);
    res.addAll(set2);
    return res;
  }

}

