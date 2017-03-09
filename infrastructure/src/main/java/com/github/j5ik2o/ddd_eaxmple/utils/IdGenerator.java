package com.github.j5ik2o.ddd_eaxmple.utils;

import java.util.concurrent.atomic.AtomicLong;

public final class IdGenerator {

   private static AtomicLong currentId = new AtomicLong();

   public static Long generateId() {
      return currentId.incrementAndGet();
   }

}
