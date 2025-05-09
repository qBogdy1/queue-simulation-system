import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.io.IOException;

public class MainFrame extends JFrame {
    private final JTextField clientsField = new JTextField(5);
    private final JTextField queuesField = new JTextField(5);
    private final JTextField timeField = new JTextField(5);
    private final JTextField arrivalMinField = new JTextField(5);
    private final JTextField arrivalMaxField = new JTextField(5);
    private final JTextField serviceMinField = new JTextField(5);
    private final JTextField serviceMaxField = new JTextField(5);
    private final JTextArea logArea = new JTextArea(20, 50);

    public MainFrame() {
        setTitle("Queue Management Simulation");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        JPanel inputPanel = new JPanel(new GridLayout(0, 2));
        inputPanel.add(new JLabel("Number of clients:")); inputPanel.add(clientsField);
        inputPanel.add(new JLabel("Number of queues:")); inputPanel.add(queuesField);
        inputPanel.add(new JLabel("Simulation time:")); inputPanel.add(timeField);
        inputPanel.add(new JLabel("Arrival time min:")); inputPanel.add(arrivalMinField);
        inputPanel.add(new JLabel("Arrival time max:")); inputPanel.add(arrivalMaxField);
        inputPanel.add(new JLabel("Service time min:")); inputPanel.add(serviceMinField);
        inputPanel.add(new JLabel("Service time max:")); inputPanel.add(serviceMaxField);

        JButton startButton = new JButton("Start Simulation");
        startButton.addActionListener(this::startSimulation);

        logArea.setEditable(false);
        add(inputPanel, BorderLayout.NORTH);
        add(new JScrollPane(logArea), BorderLayout.CENTER);
        add(startButton, BorderLayout.SOUTH);

        pack();
        setLocationRelativeTo(null);
        setVisible(true);
    }

    private void startSimulation(ActionEvent e) {
        try {
            int clients = Integer.parseInt(clientsField.getText());
            int queues = Integer.parseInt(queuesField.getText());
            int time = Integer.parseInt(timeField.getText());
            int arrivalMin = Integer.parseInt(arrivalMinField.getText());
            int arrivalMax = Integer.parseInt(arrivalMaxField.getText());
            int serviceMin = Integer.parseInt(serviceMinField.getText());
            int serviceMax = Integer.parseInt(serviceMaxField.getText());

            SimulationManager manager = new SimulationManager(
                    time, clients, queues,
                    arrivalMin, arrivalMax,
                    serviceMin, serviceMax,
                    logArea
            );
            manager.start();
            logArea.setText("Simulation started...\n");

        } catch (NumberFormatException ex) {
            logArea.setText("Please enter valid numbers.\n");
        } catch (IOException ex) {
            ex.printStackTrace();
            logArea.setText("Error starting simulation.\n");
        }
    }
}
