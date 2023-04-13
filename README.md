# Plant-Watering-System

## Overview

Arduino, an open-source electronics platform, enables users to execute hardware operations and programming easily. Arduino boards can interpret inputs from various sensors, such as light sensors, and produce corresponding outputs like activating a motor or illuminating an LED. This project is an automated plant watering system that leverages Java and the Firmata library for communication between the Arduino device and Java. The system continuously monitors soil moisture levels using sensors and manages a water pump to maintain ideal moisture conditions. Additionally, a Java GUI is included to display moisture readings in an easily interpretable graph.

## Background

In many countries, including Canada, people often have backyard gardens and plants but find it challenging to maintain them due to their busy lives. The Auto-Watering Plant System was created to assist individuals in managing their plants' watering needs by automatically measuring soil moisture levels and providing water as necessary. This solution aims to alleviate the burden of plant maintenance and ensure that plants receive proper care.

## Technical Specifications

- The moisture sensor reads the soil's moisture level in the plant pot.
- When the soil is dry, Java sends a signal to pin D2 connected to the Mosfet, activating the water pump. Simultaneously, a signal is sent to pin D4, causing the LED to blink.
- A Java GUI features two labels: one for moisture values and another for soil status. A live graph on the GUI displays moisture values (y-axis) ranging from 550 to 700 and updates every second (x-axis).
- The system automatically pauses the water pump when the soil becomes sufficiently wet.
- An OLED screen displays the status of the sensor and pump, along with the current moisture value.
- The system can perform multiple reading and pump activation cycles.

## Components

- Grove Beginner Kit: Includes LED, D2 (connected to Mosfet), A2 (connected to Moisture Sensor), and OLED.
- Mosfet: Functions as a switch that receives signals from the Arduino and supplies voltage from the battery to run the pump.
- Water Pump: A 5V pump connected to the Mosfet, responsible for supplying water to the plant.
- Battery: A 9V battery connected to the Mosfet to provide voltage for the water pump.
- Silicone Pipe: Connects the water pump to the plant pot for water supply.
- Plant Pot: Contains soil and the plant, suitable for both indoor and outdoor environments.

## Implementation

The hardware setup includes the Grove Board, Mosfet, battery, moisture sensor, water pump, and plant pot. The software implementation involves Java programming, integrating the Firmata library for communication with the Arduino device, as well as additional libraries for creating the GUI and live graph. The system uses a timer task approach to collect moisture values every second and control the pump accordingly based on the moisture level.

## Testing and Learning Outcomes

The system was thoroughly tested with dry soil, and the code was debugged using Java's debugging features and try/catch statements to handle exceptions. The project demonstrates the successful integration of Java, Arduino, and various APIs to create an automated plant watering system. It highlights the versatility and power of Java as an object-oriented programming language when combined with computer engineering concepts.

## Conclusion

The Auto-Watering Plant System effectively prevents plant dehydration and promotes healthy growth by automatically maintaining optimal soil moisture levels. The system is built on Arduino Grove boards, sensors, pumps, and Mosfets, controlled through Java programming. This solution aims to simplify plant care, allowing individuals with busy schedules to easily maintain their gardens. The system is customizable and adaptable, offering a flexible and robust technology for plant maintenance.
