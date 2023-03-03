package edu.upc.talent.swqa.functions;

import java.util.Objects;

public final class Utils {
  public static boolean eq(Object o1, Object o2) {
    return Objects.equals(o1, o2);
  }

  public static void failOnException(Consumer0 f) {
    try {
      f.apply();
    } catch (Exception ex) {
      throw new RuntimeException(ex);
    }
  }

  public static <T> T failOnException(Function0<T> f) {
    try {
      return f.apply();
    } catch (Exception ex) {
      throw new RuntimeException(ex);
    }
  }
}

