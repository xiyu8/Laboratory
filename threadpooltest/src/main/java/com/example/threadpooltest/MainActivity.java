package com.example.threadpooltest;

import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;

import com.example.threadpooltest.aboutthread.PriorityExecutor;
import com.example.threadpooltest.aboutthread.PriorityRunnable;

import java.util.concurrent.Callable;
import java.util.concurrent.CompletionService;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorCompletionService;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.FutureTask;
import java.util.concurrent.ThreadPoolExecutor;

public class MainActivity extends AppCompatActivity {

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);

  }


  public void testCustermerThreadPool() {
    ExecutorService executorService = new PriorityExecutor(5, false);
    for (int i = 0; i < 20; i++) {
      PriorityRunnable priorityRunnable = new PriorityRunnable(PriorityExecutor.Priority.NORMAL, new Runnable() {
        @Override
        public void run() {
          Log.e("TAG", Thread.currentThread().getName()+"优先级正常");
        }
      });
      if (i % 3 == 1) {
        priorityRunnable = new PriorityRunnable(PriorityExecutor.Priority.HIGH, new Runnable() {
          @Override
          public void run() {
            Log.e("TAG", Thread.currentThread().getName()+"优先级高");
          }
        });
      } else if (i % 5 == 0) {
        priorityRunnable = new PriorityRunnable(PriorityExecutor.Priority.LOW, new Runnable() {
          @Override
          public void run() {
            Log.e("TAG", Thread.currentThread().getName()+"优先级低");
          }
        });
      }
      executorService.execute(priorityRunnable);
    }
  }



  public void testAndroidThreadPool() {
    /**
     * 1、创建线程池
     * 2、新建 Runnable
     * 3、加入到线程池(把Runnable加到缓存的Thread中)，并 执行
     */
    ExecutorService executorService = Executors.newFixedThreadPool(5);

    for (int i = 0; i < 20; i++) {
      Runnable syncRunnable = new Runnable() {
        @Override
        public void run() {
          Log.e("TAG", Thread.currentThread().getName());
        }
      };
      executorService.execute(syncRunnable);
    }

  }


  public void anotherCallableThread() {
//    public interface Runnable {
//      public void run();
//    }
//
//    public interface Callable<V> {
//      V call() throws Exception;
//    }

    Callable<String> callable=new Callable<String>() {
      @Override
      public String call() throws Exception {
        return null;
      }
    };
    Runnable runnable=new Runnable() {
      @Override
      public void run() {

      }
    };
//三、
    Thread thread1 = new Thread(runnable,"aRunnableThread");
    thread1.start();

    //二、
// 调callable线程的方法(executorService+Future)
//    Thread thread2 = new Thread(callable,"aCallableThread");
//    Future<String> future = thread2.start();
    ExecutorService executorService = Executors.newFixedThreadPool(5);
    Future<String> future1=executorService.submit(callable);
    try {
      /**
       * cancel方法用来取消任务，如果取消任务成功则返回true，如果取消任务失败则返回false。参数mayInterruptIfRunning表示是否允许取消正在执行却没有执行完毕的任务，如果设置true，则表示可以取消正在执行过程中的任务。如果任务已经完成，则无论mayInterruptIfRunning为true还是false，此方法肯定返回false，即如果取消已经完成的任务会返回false；如果任务正在执行，若mayInterruptIfRunning设置为true，则返回true，若mayInterruptIfRunning设置为false，则返回false；如果任务还没有执行，则无论mayInterruptIfRunning为true还是false，肯定返回true。
       * isCancelled方法表示任务是否被取消成功，如果在任务正常完成前被取消成功，则返回 true。
       * isDone方法表示任务是否已经完成，若任务完成，则返回true；
       * get()方法用来获取执行结果，这个方法会产生阻塞，会一直等到任务执行完毕才返回；
       * get(long timeout, TimeUnit unit)用来获取执行结果，如果在指定时间内，还没获取到结果，就直接返回null。
       */
      future1.get();  //获取线程执行的结果（此方法会阻塞）
    } catch (ExecutionException e) {
      e.printStackTrace();
    } catch (InterruptedException e) {
      e.printStackTrace();
    }

//一、
// 调callable线程的方法(FutureTask without executorService)
    Callable<String> callable1=new Callable<String>() {
      @Override
      public String call() throws Exception {
        return null;
      }
    };
    FutureTask<String> futureTask = new FutureTask<String>(callable1);
    Thread thread = new Thread(futureTask);
    thread.start();



//执行多个带返回值的任务，并取得多个返回值
    ExecutorService threadPool = Executors.newCachedThreadPool();
    CompletionService<Integer> completionService = new ExecutorCompletionService<Integer>(threadPool);
    for(int i = 1; i < 5; i++) {
      final int taskID = i;
      completionService.submit(new Callable<Integer>() {
        public Integer call() throws Exception {
          return taskID;
        }
      });
    }
    // 可能做一些事情
    for(int i = 1; i < 5; i++) {
      try {
        System.out.println("主线程获取第"+i+"个子线程的结果：" + completionService.take().get());
      } catch (InterruptedException e) {
        e.printStackTrace();
      } catch (ExecutionException e) {
        e.printStackTrace();
      }
    }


  }

}
