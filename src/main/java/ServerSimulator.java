import BusinessLogic.SelectionPolicy;
import BusinessLogic.SimulationManager;
import GUI.View;

import java.io.*;
import java.util.Formatter;

public class ServerSimulator {
    public static void main(String[] args) {
        SimulationManager simulation = new SimulationManager(new View(),"out.txt");
    }
}
