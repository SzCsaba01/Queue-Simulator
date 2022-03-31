package Model;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.atomic.AtomicInteger;

public class Server implements Runnable{
    private BlockingQueue<Task> tasks;
    private AtomicInteger waitingPeriod;
    private int nrServer;
    private int size;
    private int totalTime;

    public Server(int size, int nrServer){
        this.size = size;
        this.nrServer = nrServer;
        tasks = new ArrayBlockingQueue<Task>(this.size);
        waitingPeriod = new AtomicInteger();
    }

    public void addTask(Task task){
        try{
            tasks.put(task);
        }catch (Exception e){
            e.printStackTrace();
        }
        waitingPeriod.set(waitingPeriod.get() + task.getServiceTime());
    }

    @Override
    public void run() {
        try {
            if (getTasks().size() != 0) {
                getTasks().element().setServiceTime(getTasks().element().getServiceTime() - 1);
                waitingPeriod.getAndDecrement();
                if (getTasks().element().getServiceTime() == 0) {
                    totalTime += getTasks().element().getFinishTime() - getTasks().element().getArrivalTime();
                    tasks.take();
                }
                Thread.sleep(1000);
            }
        }catch (InterruptedException e){
            e.printStackTrace();
        }
    }

    public BlockingQueue<Task> getTasks(){
        return tasks;
    }

    public AtomicInteger getWaitingPeriod(){
        return waitingPeriod;
    }

    public void setWaitingPeriod(AtomicInteger waitingPeriod) {
        this.waitingPeriod = waitingPeriod;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public int getNrServer() {
        return nrServer;
    }

    public void setNrServer(int nrServer) {
        this.nrServer = nrServer;
    }

    public int getTotalTime() {
        return totalTime;
    }

    public void setTotalTime(int totalTime) {
        this.totalTime = totalTime;
    }
}
