package com.example.threadpooltest.aboutthread.a;

public interface Executor {
  void execute(Runnable command);//执行已提交的 Runnable 任务对象。此接口提供一种将任务提交与每个任务将如何运行的机制（包括线程使用的细节、调度等）分离开来的方法
}