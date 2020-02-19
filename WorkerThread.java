public class WorkerThread extends Thread {

  ThreadPool pool;
  int threadNum;

  public WorkerThread(ThreadPool pool, int threadNum) {
    this.pool = pool;
    this.threadNum = threadNum;
  }

  @Override
  public void run() {
    String padNum = (threadNum >= 10 ? "" : "0") + threadNum;

    while (true) {
      Job j = pool.consumeJob(threadNum);
      if (j == null)
        break;

      System.out.println("Thread " + padNum + " is handling: " + j.cmd + " command");
      j.doJob();

      try {
        Thread.sleep(1000);
      } catch (Exception e) {
        break;
      }
    }

    pool.removeThread(threadNum);
    System.out.println("Thread " + padNum + " has been killed");
  }

}