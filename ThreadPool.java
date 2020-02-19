import java.util.LinkedList;

class ThreadPool {

  public static final int MAX_JOBS = 50;
  public static final int MAX_THREADS = 40;

  LinkedList<Job> jobs = new LinkedList<>();
  LinkedList<WorkerThread> threads = new LinkedList<>();

  int targetNumThreads = 5;

  public ThreadPool() {
    // Initialize thread list
    for (int i = 0; i < MAX_THREADS; i++)
      threads.add(null);

    // Begin first target group
    for (int i = 0; i < targetNumThreads; i++)
      addThread(i);
  }

  public void stop() {

    System.out.println("-- KILL COMMAND RECEIVED --");

    setTargetPoolSize(0);
    for (int i = 0; i < threads.size(); i++) {
      WorkerThread t = threads.get(i);
      if (t != null) {
        try {
          t.interrupt();
          t.join();
        } catch (Exception e) {
        }
      } else {
      }
    }

    System.out.println("Main thread exiting");
    System.exit(0);
  }

  public synchronized void setTargetPoolSize(int num) {

    // If the pool should be dying, reject other targets
    if (targetNumThreads == 0 || targetNumThreads == num)
      return;

    System.out.println(
        "-- POOL: jobs.size(): " + jobs.size() + ", pool size changed from " + targetNumThreads + " to " + num);

    // Initialize threads that may not exist yet
    targetNumThreads = num;
    for (int i = 0; i < num; i++) {
      addThread(i);
    }

    notifyAll();
  }

  public synchronized Job consumeJob(int threadNum) {

    while (!hasJob()) {

      // If the thread should die, give it a null job
      if (getTargetNumThreads() < (threadNum + 1))
        return null;

      try {
        wait();
      } catch (Exception e) {
        // If the thread is interrupted and should die, give it a null job
        return null;
      }
    }

    return getJob();
  }

  // Synchronized job access
  public synchronized boolean hasJob() {
    return !this.jobs.isEmpty();
  }

  public synchronized Job getJob() {
    Job j = this.jobs.poll();
    notifyAll();
    return j;
  }

  public synchronized boolean addJob(Job j) {
    if (this.jobs.size() >= MAX_JOBS) {
      return false;
    }

    this.jobs.add(j);
    notifyAll();
    return true;
  }

  public synchronized int getJobQueueSize() {
    return this.jobs.size();
  }

  public synchronized boolean maxJobs() {
    return this.jobs.size() >= MAX_JOBS;
  }

  // Synchronized target num threads access
  public synchronized int getTargetNumThreads() {
    return this.targetNumThreads;
  }

  // Synchronized thread access
  public synchronized void addThread(int threadNum) {
    if (this.threads.get(threadNum) == null) {
      WorkerThread t = new WorkerThread(this, threadNum);
      this.threads.set(threadNum, t);
      t.start();
    }
  }

  public synchronized void removeThread(int threadNum) {
    if (this.threads.get(threadNum) != null) {
      this.threads.set(threadNum, null);
    }
  }

}
