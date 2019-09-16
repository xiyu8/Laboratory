package com.example.threadpooltest.aboutthread;



/**
 * 带有优先级的Runnable类型
 */
public class PriorityRunnable implements Runnable {
  public final PriorityExecutor.Priority priority;//任务优先级
  private final Runnable runnable;//任务真正执行者
  /*package*/ long SEQ;//任务唯一标示

  public PriorityRunnable(PriorityExecutor.Priority priority, Runnable runnable) {
    this.priority = priority == null ? PriorityExecutor.Priority.NORMAL : priority;
    this.runnable = runnable;
  }

  @Override
  public final void run() {
    this.runnable.run();
  }
}
