
package com.weatherprediction;

import javax.swing.*;
import java.awt.*;

public class WeatherApp extends JFrame {

    private JTextField cityField;
    private JButton searchButton;
    private JTextArea resultArea;
    private WeatherService weatherService;

    public WeatherApp() {
        weatherService = new WeatherService();

        setTitle("Weather Prediction System");
        setSize(650, 500);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        
        getContentPane().setBackground(
        new java.awt.Color(230, 240, 250));

setResizable(false);

        JLabel titleLabel = new JLabel(
                "Weather Prediction System",
                SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 22));
        
        titleLabel.setForeground(
        new java.awt.Color(25, 75, 130));

titleLabel.setBorder(
        BorderFactory.createEmptyBorder(15, 5, 15, 5));

        JLabel cityLabel = new JLabel("Enter City Name:");
        cityField = new JTextField(18);

        searchButton = new JButton("Check Weather");
        JButton forecastButton = new JButton("5-Day Forecast");
        
        searchButton.setBackground(
        new java.awt.Color(40, 110, 180));

searchButton.setForeground(java.awt.Color.WHITE);

searchButton.setFocusPainted(false);

        resultArea = new JTextArea(10, 30);
        resultArea.setEditable(false);
        resultArea.setFont(new Font("Arial", Font.PLAIN, 16));
        resultArea.setText("Enter a city name to check its weather.");

        JPanel inputPanel = new JPanel();
        inputPanel.setBackground(new java.awt.Color(225, 240, 255));
        inputPanel.add(cityLabel);
        inputPanel.add(cityField);
        inputPanel.add(searchButton);
        inputPanel.add(forecastButton);
        
        forecastButton.addActionListener(e -> {
    String city = cityField.getText().trim();

    if (city.isEmpty()) {
        resultArea.setText("Please enter a city name.");
        return;
    }

    resultArea.setText("Loading 5-day forecast...");

    new SwingWorker<String, Void>() {
        @Override
        protected String doInBackground() throws Exception {
            return weatherService.getForecast(city);
        }

        @Override
        protected void done() {
            try {
                resultArea.setText(get());
            } catch (Exception ex) {
                ex.printStackTrace();
                resultArea.setText("Error: " + ex.getMessage());
            }
        }
    }.execute();
});

        setLayout(new BorderLayout(10, 10));
        add(titleLabel, BorderLayout.NORTH);
        add(inputPanel, BorderLayout.CENTER);
        add(new JScrollPane(resultArea), BorderLayout.SOUTH);

        searchButton.addActionListener(e -> checkWeather());
    }

    private void checkWeather() {
        String city = cityField.getText().trim();

        if (city.isEmpty()) {
            JOptionPane.showMessageDialog(
                    this, "Please enter a city name.");
            return;
        }

        searchButton.setEnabled(false);
        resultArea.setText("Fetching weather data...");

        SwingWorker<String, Void> worker =
                new SwingWorker<>() {

            @Override
            protected String doInBackground() throws Exception {
                return weatherService.getWeather(city);
            }

            @Override
            protected void done() {
                try {
                    resultArea.setText(get());
                } catch (Exception ex) {
    ex.printStackTrace();

    resultArea.setText(
            "Unable to fetch weather data.\n"
            + "Please check the city name and internet connection.\n"
            + "Error: " + ex.getMessage());
} finally {
    searchButton.setEnabled(true);
}
            }
        };

        worker.execute();
    }

   public static void main(String[] args) {
    SwingUtilities.invokeLater(() -> {
        WeatherApp app = new WeatherApp();
        app.setVisible(true);
    });
   }
}
   
