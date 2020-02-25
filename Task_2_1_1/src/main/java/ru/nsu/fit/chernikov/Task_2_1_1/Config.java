package ru.nsu.fit.chernikov.Task_2_1_1;

import com.google.gson.GsonBuilder;
import java.io.FileReader;
import java.util.ArrayList;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

/** Utility class for creating Pizzeria using configuration json file. */
public class Config {
  static class CookConfig {
    String name;
    double tpu;

    CookConfig(String _name, int _tpu) {
      name = _name;
      tpu = _tpu;
    }
  }

  static class CourierConfig {
    String name;
    double speed;
    int cap;

    CourierConfig(String _name, double _speed, int _cap) {
      name = _name;
      speed = _speed;
      cap = _cap;
    }
  }

  static class PizzeriaConfig {
    long shiftLenS;
    long delayMS;
    int warehouseCap;
    ArrayList<CookConfig> cookConfigs;
    ArrayList<CourierConfig> courierConfigs;
  }

  /**
   * Create new Pizzeria using configuration file.
   *
   * @param fr FileReader containing json configuration.
   * @return created Pizzeria.
   */
  static Pizzeria parse(FileReader fr) {
    Gson gson = new Gson();
    PizzeriaConfig pc = gson.fromJson(fr, PizzeriaConfig.class);
    ArrayList<Cook> cooks = new ArrayList<>();
    ArrayList<Courier> couriers = new ArrayList<>();
    for (CookConfig cc : pc.cookConfigs) {
      cooks.add(new Cook(cc.name, cc.tpu));
    }
    for (CourierConfig cc : pc.courierConfigs) {
      couriers.add(new Courier(cc.name, cc.speed, cc.cap));
    }
    return new Pizzeria(cooks, couriers, pc.warehouseCap, pc.delayMS, pc.shiftLenS);
  }

  /**
   * Generate example configuration json.
   *
   * @return example json string.
   */
  public static String example() {
    Gson gson = new GsonBuilder().setPrettyPrinting().create();
    PizzeriaConfig pc = new PizzeriaConfig();
    pc.shiftLenS = 10;
    pc.delayMS = 4000;
    pc.warehouseCap = 10;
    pc.cookConfigs = new ArrayList<>();
    pc.courierConfigs = new ArrayList<>();
    pc.cookConfigs.add(new CookConfig("A", 1));
    pc.cookConfigs.add(new CookConfig("B", 3));
    pc.cookConfigs.add(new CookConfig("C", 2));
    pc.courierConfigs.add(new CourierConfig("X", 10, 3));
    pc.courierConfigs.add(new CourierConfig("Y", 20, 2));
    pc.courierConfigs.add(new CourierConfig("Z", 30, 1));
    return gson.toJson(pc);
  }
}
