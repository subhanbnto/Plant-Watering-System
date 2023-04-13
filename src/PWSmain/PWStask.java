package PWSmain;

import org.firmata4j.Pin;
import org.firmata4j.ssd1306.SSD1306;
import org.jfree.data.xy.XYSeries;

import javax.swing.*;
import java.io.IOException;
import java.util.Scanner;
import java.util.TimerTask;

public class PWStask extends TimerTask {
    private int x = 0;
    private final SSD1306 myOled;
    private final Pin MoistureSensor;
    private final JLabel label1;
    private final JLabel label2;
    private final Pin Pump;
    private final XYSeries series;
    private final JFrame window;
    private final Pin led;

    public PWStask(SSD1306 myOled, Pin MoistureSensor, JLabel label1, JLabel label2, Pin Pump, XYSeries series, JFrame window, Pin led) {
        this.myOled = myOled;
        this.MoistureSensor = MoistureSensor;
        this.label1 = label1;
        this.label2 = label2;
        this.Pump = Pump;
        this.series = series;
        this.window = window;
        this.led = led;
    }

    @Override
    public void run() {

        myOled.getCanvas().clear();

        int value = (int) MoistureSensor.getValue();
        String MoistureSensorString = String.valueOf(value);

        String MoistureValue1 = String.valueOf(value);

        label1.setText("Moisture Value Is : " + MoistureSensorString + "     ");

        if (value < 560) {

            label2.setText("       " + "Soil is wet!" + "       ");
            myOled.getCanvas().drawString(0, 25, "Wet !");
            myOled.getCanvas().drawString(0, 0, "Pump Off");
            myOled.display();

            try {
                Pump.setValue(0);
                led.setValue(0);
                Thread.sleep(3000);
            } catch (IOException | InterruptedException e) {
                e.printStackTrace();
            }
        } else if (value > 700) {

            label2.setText("       " + "Soil is too dry!" + "       ");
            myOled.getCanvas().drawString(0, 25, "Too Dry !");
            myOled.getCanvas().drawString(0, 0, "Pump ON");
            myOled.display();

            try {
                Pump.setValue(255);
                led.setValue(1);
                Thread.sleep(3000);
            } catch (IOException | InterruptedException e) {
                e.printStackTrace();
            }

        }

        Scanner scanner = new Scanner(MoistureValue1);

        while (scanner.hasNextLine()) {
            try {
                String line = scanner.nextLine();
                int number = Integer.parseInt(line);
                series.add(x++, 5 - number);
                window.repaint();
            } catch (Exception ex) {
            }
        }
        scanner.close();

    }
}
