package GUI;

import javax.swing.*;
import java.awt.event.ActionListener;

public class View {
    private JComboBox comboBoxStrategy;
    private JButton buttonSimulate;
    private JTextField textFieldNumberOfClients;
    private JTextField textFieldNumberOfQueues;
    private JTextField textFieldTimeLimit;
    private JTextField textFieldMinArrivalTime;
    private JTextField textFieldMinProcessingTime;
    private JTextField textFieldMaxProcessingTime;
    private JTextField textFieldMaxArrivalTime;
    private JTextArea textAreaSimulation;
    private JPanel MainPanel;
    private JScrollPane scrollPane;
    private JFrame frame;

    public View(){
        scrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
        frame = new JFrame("Queue Simulator");
        frame.setContentPane(MainPanel);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        textAreaSimulation.setEditable(false);
        frame.pack();
        frame.setVisible(true);
    }

    public Object getComboBoxStrategy(){return comboBoxStrategy.getSelectedItem();}
    public String getTextFieldNumberOfClients(){return textFieldNumberOfClients.getText();}
    public String getTextFieldNumberOfQueues(){return textFieldNumberOfQueues.getText();}
    public String getTextFieldTimeLimit(){return textFieldTimeLimit.getText();}
    public String getTextFieldMinArrivalTime(){return textFieldMinArrivalTime.getText();}
    public String getTextFieldMaxArrivalTime(){return textFieldMaxArrivalTime.getText();}
    public String getTextFieldMinProcessingTime(){return textFieldMinProcessingTime.getText();}
    public String getTextFieldMaxProcessingTime(){return textFieldMaxProcessingTime.getText();}
    public void addButtonSimulateActionListener(ActionListener e){buttonSimulate.addActionListener(e);}
    public void appendTextAreaSimulation(String txt){textAreaSimulation.append(txt);}
}
