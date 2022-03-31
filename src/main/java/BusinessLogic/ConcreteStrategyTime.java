package BusinessLogic;

import Model.Server;
import Model.Task;

import java.util.List;

public class ConcreteStrategyTime implements Strategy {
    @Override
    public void addTask(List<Server> servers, Task t) {
            int min = servers.get(0).getWaitingPeriod().get();
            int i = 0;
            int cnt = 0;
            while(i < servers.size()){
                if(servers.get(i).getWaitingPeriod().get() < min){
                    min = servers.get(i).getWaitingPeriod().get();
                    cnt = i;
                }
                i++;
            }
            t.setFinishTime(t.getArrivalTime() + t.getServiceTime() + servers.get(cnt).getWaitingPeriod().get());
            servers.get(cnt).addTask(t);
    }
}
