package BusinessLogic;

import Model.Server;
import Model.Task;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Scheduler {
    private List<Server> servers;
    private int NrServers;
    private int TasksPerServer;
    private Strategy strategy;

    public Scheduler(int NrServers, int NrTasksPerServer){
        this.NrServers = NrServers;
        this.TasksPerServer = NrTasksPerServer;
        servers = Collections.synchronizedList(new ArrayList<Server>());
        for(int i = 0; i < NrServers; i++){
            servers.add(new Server(NrTasksPerServer, i));
        }
    }

    public void changeStrategy(SelectionPolicy policy){
        if(policy == SelectionPolicy.SHORTEST_QUEUE){
            strategy = new ConcreteStrategyQueue();
        }
        if(policy == SelectionPolicy.SHORTEST_TIME){
            strategy = new ConcreteStrategyTime();
        }
    }

    public void dispatchTask(Task t){
        strategy.addTask(servers, t);
    }
    public List<Server> getServers(){
        return servers;
    }

    public int getNrServers() {
        return NrServers;
    }

    public void setNrNoServers(int maxNoServers) {
        this.NrServers = maxNoServers;
    }

    public int getNrTasksPerServer() {
        return TasksPerServer;
    }

    public void setNrTasksPerServer(int maxTasksPerServer) {
        this.TasksPerServer = maxTasksPerServer;
    }

    public Strategy getStrategy() {
        return strategy;
    }

    public void setStrategy(Strategy strategy) {
        this.strategy = strategy;
    }
}
