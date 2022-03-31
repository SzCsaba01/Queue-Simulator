package BusinessLogic;

import GUI.View;
import Model.Server;
import Model.Task;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.*;
import java.util.Collections;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.Iterator;

public class SimulationManager implements Runnable{
    private AtomicInteger timeLimit = new AtomicInteger();
    private int maxProcessingTime;
    private int minProcessingTime;
    private int numberOfServers;
    private int numberOfClients;
    private int minArrivalTime;
    private int maxArrivalTime;
    private SelectionPolicy selectionPolicy;
    private float avgWaitingTime;

    private File txt;
    private Scheduler scheduler;
    private List<Task> Tasks = new ArrayList<Task>();
    private PrintWriter printWriter;
    private View view;

    public SimulationManager(View view, String out){
        this.view = view;
        try {
            printWriter= new PrintWriter(out);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        view.addButtonSimulateActionListener(new ButtonSimulateActionListener());
    }

    private void startThread(){
        Thread t = new Thread(this);
        t.start();
    }


    public void generateNRandomTasks(){
            Random rand = new Random();
            for(int i = 0; i < numberOfClients; i++){
                this.Tasks.add(new Task(i+1, minArrivalTime + rand.nextInt(maxArrivalTime-minArrivalTime),
                        minProcessingTime+ rand.nextInt(maxProcessingTime-minProcessingTime)));
            }
            Collections.sort(Tasks);
    }

    public float getAvgWaitingTime() {
        return avgWaitingTime;
    }

    public void setAvgWaitingTime(float avgWaitingTime) {
        this.avgWaitingTime = avgWaitingTime;
    }

    class ButtonSimulateActionListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e){
            try{
                numberOfClients = Integer.parseInt(view.getTextFieldNumberOfClients());
                numberOfServers = Integer.parseInt(view.getTextFieldNumberOfQueues());
                timeLimit.set(Integer.parseInt(view.getTextFieldTimeLimit()));
                minArrivalTime = Integer.parseInt(view.getTextFieldMinArrivalTime());
                maxArrivalTime = Integer.parseInt(view.getTextFieldMaxArrivalTime());
                minProcessingTime = Integer.parseInt(view.getTextFieldMinProcessingTime());
                maxProcessingTime = Integer.parseInt(view.getTextFieldMaxProcessingTime());
                if(String.valueOf(view.getComboBoxStrategy()).equals("Time Strategy"))
                    selectionPolicy = SelectionPolicy.SHORTEST_TIME;
                else
                    selectionPolicy = SelectionPolicy.SHORTEST_QUEUE;
            }catch (NumberFormatException exception){
                exception.printStackTrace();
            }
            scheduler = new Scheduler(numberOfServers,numberOfClients);
            System.out.println("Number of clients:" + getNumberOfClients() + "\nNumber of Queues:" +
                   getNumberOfServers() + "\nTime Limit:" + getTimeLimit() + "\nMin Arrival Time:" +
                   getMinArrivalTime() + "\nMax Arrival Time:" + getMaxArrivalTime() + "\nMin Processing Time:" +
                   getMinProcessingTime() + "\nMax Processing Time:" + getMaxProcessingTime());
            System.out.println("\n" + "Generated Tasks:\n");

            generateNRandomTasks();
            for (Task task : getGeneratedTasks()) {
                System.out.println("ID:" + task.getID() + "\n" + "Service time:" + task.getServiceTime() + "\n" +
                        "Arrival Time:" + task.getArrivalTime() + "\n"
                        + "Finish Time:" + task.getFinishTime() + "\n");
            }

            scheduler.changeStrategy(getSelectionPolicy());
            startThread();
        }
    }

    @Override
    public synchronized void run() {
        AtomicInteger currentTime = new AtomicInteger();
        view.appendTextAreaSimulation("Starting Simulation...\n");
        System.out.println("Starting Simulation...\n");
        while (currentTime.get() < timeLimit.get()) {
            Iterator<Task> iterator = Tasks.iterator();
            printWriter.println("Time:" + currentTime.get());
            view.appendTextAreaSimulation("Time:" + currentTime.get() + "\n");
            System.out.println("Time:" + currentTime.get());
            int i = 0;
            printWriter.print("Waiting Clients:");
            view.appendTextAreaSimulation("Waiting Clients:");
            System.out.print("Waiting Clients:");
            while(iterator.hasNext()){
               Task t = iterator.next();
               if(t.getArrivalTime() <= currentTime.get()){
                   scheduler.dispatchTask(t);
                   iterator.remove();
               }
               else{
                   printWriter.print(t + " ");
                   view.appendTextAreaSimulation(t + " ");
                   System.out.print(t + " ");
               }
            }
            while(i < scheduler.getNrServers()){
                Thread t = new Thread(scheduler.getServers().get(i));
                t.start();
                i++;
            }

            try{
                Thread.sleep(1000);
            }catch (InterruptedException e) {
                e.printStackTrace();
            }
            printWriter.println();
            view.appendTextAreaSimulation("\n");
            System.out.println();
            printQueues();
            printWriter.println();
            view.appendTextAreaSimulation("\n");
            System.out.println();
            currentTime.set(currentTime.get()+1);
            if(Tasks.isEmpty()){
                int cnt = 0;
                for(Server server : scheduler.getServers()){
                    if(server.getTasks().size() == 0)
                        cnt++;
                }
                if(cnt == scheduler.getNrServers())
                    break;
            }
        }
        for(Server server : scheduler.getServers()){
            avgWaitingTime += server.getTotalTime();
        }
        avgWaitingTime = (float) avgWaitingTime/ numberOfClients;
        System.out.println("Average Waiting Time = " + avgWaitingTime);
        view.appendTextAreaSimulation("Average Waiting Time = " + avgWaitingTime);
        printWriter.println("Average Waiting Time = " + avgWaitingTime);
        printWriter.close();
    }

    public void printQueues(){
        int i = 0;
        while(i < scheduler.getNrServers()){
            if(scheduler.getServers().get(i).getTasks().size() == 0){
                printWriter.println("Queue" + i + " is closed");
                view.appendTextAreaSimulation("Queue" + i + " is closed\n");
                System.out.println("Queue" + i +" is closed");
            }
            else{
                printWriter.print("Queue" + i + ":");
                view.appendTextAreaSimulation("Queue" + i + ":");
                System.out.print("Queue" + i +":");
                for(Task task: scheduler.getServers().get(i).getTasks()) {
                    printWriter.print(task + " ");
                    view.appendTextAreaSimulation(task + " ");
                    System.out.print(task + " ");
                }
                printWriter.println();
                view.appendTextAreaSimulation("\n");
                System.out.println();
                }
            i++;
        }
    }

    public int getTimeLimit() {
        return timeLimit.get();
    }

    public void setTimeLimit(int timeLimit) {
        this.timeLimit.set(this.timeLimit.get() + timeLimit);
    }

    public int getMaxProcessingTime() {
        return maxProcessingTime;
    }

    public void setMaxProcessingTime(int maxProcessingTime) {
        this.maxProcessingTime = maxProcessingTime;
    }

    public int getMinProcessingTime() {
        return minProcessingTime;
    }

    public void setMinProcessingTime(int minProcessingTime) {
        this.minProcessingTime = minProcessingTime;
    }

    public int getNumberOfClients() {
        return numberOfClients;
    }

    public void setNumberOfClients(int numberOfClients) {
        this.numberOfClients = numberOfClients;
    }

    public SelectionPolicy getSelectionPolicy() {
        return selectionPolicy;
    }

    public void setSelectionPolicy(SelectionPolicy selectionPolicy) {
        this.selectionPolicy = selectionPolicy;
    }

    public Scheduler getScheduler() {
        return scheduler;
    }

    public void setScheduler(Scheduler scheduler) {
        this.scheduler = scheduler;
    }

    public List<Task> getGeneratedTasks() {
        return Tasks;
    }

    public void setGeneratedTasks(List<Task> generatedTasks) {
        this.Tasks = generatedTasks;
    }

    public int getNumberOfServers() {
        return numberOfServers;
    }

    public void setNumberOfServers(int numberOfServers) {
        this.numberOfServers = numberOfServers;
    }

    public int getMinArrivalTime() {
        return minArrivalTime;
    }

    public void setMinArrivalTime(int minArrivalTime) {
        this.minArrivalTime = minArrivalTime;
    }

    public int getMaxArrivalTime() {
        return maxArrivalTime;
    }

    public void setMaxArrivalTime(int maxArrivalTime) {
        this.maxArrivalTime = maxArrivalTime;
    }


}
