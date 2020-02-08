package build.dream.thc.tasks;

import build.dream.thc.Application;
import build.dream.thc.utils.SensorUtils;
import build.dream.thc.utils.ThreadUtils;

public class MonitorTask implements Runnable {
    private boolean proceed;

    public MonitorTask() {
        this.proceed = true;
    }

    @Override
    public void run() {
        while (proceed) {
            double temperature = SensorUtils.obtainTemperature();
            double humidity = SensorUtils.obtainHumidity();
            if (temperature > Application.temperatureAlarmThreshold || humidity > Application.humidityAlarmThreshold) {

            }

            ThreadUtils.sleepSafe(5000);
        }
    }

    public void start() {
        new Thread(this).start();
    }

    public void stop() {
        this.proceed = false;
    }
}
