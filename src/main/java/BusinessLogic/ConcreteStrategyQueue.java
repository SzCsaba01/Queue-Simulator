package BusinessLogic;

import Model.Server;
import Model.Task;

import java.util.List;

public class ConcreteStrategyQueue implements Strategy {
    @Override
    public void addTask(List<Server> servers, Task t) {
        int min = 9999;
        int i = 0;
        int nr;
        int cnt = 0;
        while(i <servers.size()){
            nr = 0;
            for(Task task : servers.get(i).getTasks()){
                nr++;
            }
            if(nr < min) {
                cnt = i;
                min = nr;
            }
            i++;
        }
        t.setFinishTime(t.getArrivalTime() + t.getServiceTime() + servers.get(cnt).getWaitingPeriod().get());
        servers.get(cnt).addTask(t);
    }
}
