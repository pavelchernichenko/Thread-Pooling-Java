import java.io.PrintWriter;
import java.util.Timer;
import java.util.TimerTask;

public class ThreadManager extends Thread {

  private ThreadPool pool = new ThreadPool();

  public static final int V = 2;
  public static final int T1 = 10;
  public static final int T2 = 20;

  @Override
  public void run() {
    System.out.println("Thread Manager is runnning.");

    new Timer().schedule(new TimerTask() {
      @Override
      public void run() {
        setPoolSize();
      }
    }, 0, V * 1000);
  }

  public boolean addJob(String input, PrintWriter output) {

    if (input == null || output == null || pool.maxJobs() || pool.getTargetNumThreads() == 0)
      return false;

    if (input.trim().isEmpty()) {
      output.println("Invalid command");
      return true;
    }
    
    if (input.trim().equals("KILL")) {
      pool.stop();
      output.println("Killing server");
      return false;
    }
    
    Job j = new Job(input.trim(), output);
    boolean successful = pool.addJob(j);
    
    if (!successful) {
      output.println("Server busy try again later.");
      return false;
    }

    return true;
  }

  private synchronized void setPoolSize() {
    int newTargetSize;

    if (pool.getJobQueueSize() <= T1)
      newTargetSize = 5;
    else if (pool.getJobQueueSize() <= T2)
      newTargetSize = 10;
    else
      newTargetSize = 20;

    pool.setTargetPoolSize(newTargetSize);
  }

}