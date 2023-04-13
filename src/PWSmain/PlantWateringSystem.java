package PWSmain;

import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import org.firmata4j.I2CDevice;
import org.firmata4j.IODevice;
import org.firmata4j.Pin;
import org.firmata4j.firmata.FirmataDevice;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.Date;
import java.util.Scanner;
import java.util.Timer;
import java.util.TimerTask;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Pattern;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import jssc.SerialNativeInterface;
import jssc.SerialPortList;
import org.firmata4j.ssd1306.SSD1306;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;


public class PlantWateringSystem {

    private static final JFrame INITIALIZATION_FRAME = new JFrame();

    public static void main(String[] args) throws IOException {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | UnsupportedLookAndFeelException ex) {
            Logger.getLogger(PlantWateringSystem.class.getName()).log(Level.SEVERE, "Cannot load system look and feel.", ex);
        }

        String port = requestPort();
        final IODevice device = new FirmataDevice(port);
        showInitializationMessage();
        device.start();
        try {
            device.ensureInitializationIsDone();
        } catch (InterruptedException e) {
            JOptionPane.showMessageDialog(INITIALIZATION_FRAME, e.getMessage(), "Connection error", JOptionPane.ERROR_MESSAGE);
            System.exit(1);
        }
        hideInitializationWindow();
        SwingUtilities.invokeLater(new Runnable() {

            @Override
            public void run() {

                Pin MoistureSensor = device.getPin(16);

                Pin Pump = device.getPin(2);
                try {
                    Pump.setMode(Pin.Mode.OUTPUT);
                } catch (IOException e) {
                    e.printStackTrace();
                }

                Pin led = device.getPin(4);
                try {
                    led.setMode(Pin.Mode.OUTPUT);
                } catch (IOException e) {
                    e.printStackTrace();
                }

                Pin buzzer = device.getPin(5);
                try {
                    buzzer.setMode(Pin.Mode.OUTPUT);
                } catch (IOException e) {
                    e.printStackTrace();
                }

                I2CDevice I2CDevice = null;
                try {
                    I2CDevice = device.getI2CDevice((byte) 0x3C);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                SSD1306 Oled = new SSD1306(I2CDevice, SSD1306.Size.SSD1306_128_64);
                Oled.init();
                Oled.getCanvas().setTextsize(2);


                JFrame window = new JFrame();
                window.setTitle("Minor Project");
                window.setSize(1000, 600);
                window.setLayout(new BorderLayout());
                window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);




                JLabel label1 = new JLabel();
                label1.setLocation(550, 250);
                label1.setFont(new Font("Sans Serif", Font.ITALIC, 15));



                JLabel label2 = new JLabel();
                label2.setLocation(550, 400);
                label2.setFont(new Font("Sans Serif", Font.ITALIC, 15));


                JPanel panel = new JPanel();

                JPanel panel1 = new JPanel();
                panel1.add(label1, BorderLayout.CENTER);

                JPanel panel2 = new JPanel();
                panel2.add(label2, BorderLayout.CENTER);

                panel.add(panel1, BorderLayout.NORTH);
                panel.add(panel2, BorderLayout.NORTH);

                window.add(panel, BorderLayout.NORTH);


                XYSeries series = new XYSeries("Sensors Readings");
                XYSeriesCollection dataset = new XYSeriesCollection(series);
                JFreeChart chart = ChartFactory.createXYLineChart("Sensors Readings", "Time (seconds)", " Sensor Values", dataset);
                window.add(new ChartPanel(chart), BorderLayout.CENTER);



                Timer timer = new Timer();
                TimerTask myTask = new PWStask(Oled, MoistureSensor, label1, label2, Pump, series, window, led);
                timer.schedule(myTask, new Date(), 1000);




                window.addWindowListener(new WindowAdapter() {
                    @Override
                    public void windowClosing(WindowEvent e) {
                        try {
                            device.stop();
                        } catch (IOException ex) {
                            throw new RuntimeException(ex);
                        }
                        super.windowClosing(e);
                    }
                });
                window.setVisible(true);
            }

        });

    }


    @SuppressWarnings("unchecked")

    private static String requestPort() {
        JComboBox<String> portNameSelector = new JComboBox<>();
        portNameSelector.setModel(new DefaultComboBoxModel<String>());
        String[] portNames;
        if (SerialNativeInterface.getOsType() == SerialNativeInterface.OS_MAC_OS_X) {

            portNames = SerialPortList.getPortNames("/dev/", Pattern.compile("cu\\..*"));
        } else {
            portNames = SerialPortList.getPortNames();
        }
        for (String portName : portNames) {
            portNameSelector.addItem(portName);
        }
        if (portNameSelector.getItemCount() == 0) {
            JOptionPane.showMessageDialog(null, "Cannot find any serial port", "Error", JOptionPane.ERROR_MESSAGE);
            System.exit(1);
        }
        JPanel panel = new JPanel();
        panel.setLayout(new GridBagLayout());
        panel.add(new JLabel("Port "));
        panel.add(portNameSelector);
        if (JOptionPane.showConfirmDialog(null, panel, "Select the port", JOptionPane.OK_CANCEL_OPTION) == JOptionPane.OK_OPTION) {
            return portNameSelector.getSelectedItem().toString();
        } else {
            System.exit(0);
        }
        return "";
    }

    private static void showInitializationMessage() {
        try {
            SwingUtilities.invokeAndWait(new Runnable() {
                @Override
                public void run() {
                    JFrame frame = INITIALIZATION_FRAME;
                    frame.setUndecorated(true);
                    JLabel label = new JLabel("Connecting to device");
                    label.setHorizontalAlignment(JLabel.CENTER);
                    frame.add(label);
                    frame.pack();
                    frame.setSize(frame.getWidth() + 40, frame.getHeight() + 40);
                    Dimension dimension = Toolkit.getDefaultToolkit().getScreenSize();
                    int x = (int) ((dimension.getWidth() - frame.getWidth()) / 2);
                    int y = (int) ((dimension.getHeight() - frame.getHeight()) / 2);
                    frame.setLocation(x, y);
                    frame.setVisible(true);
                }
            });
        } catch (InterruptedException | InvocationTargetException ex) {
            throw new RuntimeException(ex);
        }
    }

    private static void hideInitializationWindow() {
        try {
            SwingUtilities.invokeAndWait(new Runnable() {
                @Override
                public void run() {
                    INITIALIZATION_FRAME.setVisible(false);
                    INITIALIZATION_FRAME.dispose();
                }
            });
        } catch (InterruptedException | InvocationTargetException ex) {
            throw new RuntimeException(ex);
        }
    }
}


