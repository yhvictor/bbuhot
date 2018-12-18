package com.bbuhot.server.util;

import com.google.common.util.concurrent.ListeningExecutorService;
import com.google.common.util.concurrent.MoreExecutors;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicInteger;
import javax.annotation.Nonnull;

public class BbuhotThreadPool {

  public static final ListeningExecutorService limitedThreadPool =
      MoreExecutors.listeningDecorator(
          Executors.newFixedThreadPool(10, new BbuhotThreadFactory("bbuhot-limited")));
  public static final ListeningExecutorService workerThreadPool =
      MoreExecutors.listeningDecorator(
          Executors.newCachedThreadPool(new BbuhotThreadFactory("bbuhot-worker")));

  private BbuhotThreadPool() {}

  private static class BbuhotThreadFactory implements ThreadFactory {

    private static final AtomicInteger poolNumber = new AtomicInteger(1);
    private final ThreadGroup group;
    private final AtomicInteger threadNumber = new AtomicInteger(1);
    private final String namePrefix;

    BbuhotThreadFactory(String namePrefix) {
      SecurityManager s = System.getSecurityManager();
      group = (s != null) ? s.getThreadGroup() : Thread.currentThread().getThreadGroup();

      this.namePrefix = namePrefix;
    }

    public Thread newThread(@Nonnull Runnable r) {
      Thread t = new Thread(group, r, namePrefix + threadNumber.getAndIncrement(), 0);
      if (t.isDaemon()) {
        t.setDaemon(false);
      }
      if (t.getPriority() != Thread.NORM_PRIORITY) {
        t.setPriority(Thread.NORM_PRIORITY);
      }
      return t;
    }
  }
}
