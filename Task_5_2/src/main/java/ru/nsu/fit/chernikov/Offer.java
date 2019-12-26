package ru.nsu.fit.chernikov;

public class Offer {
  private String name;
  private Double price;
  private Double count;

  /**
   * Create new offer for buying or selling.
   *
   * @param _name name of the goods
   * @param _price price per unit
   * @param _count count of goods
   */
  public Offer(String _name, Double _price, Double _count) {
    name = _name;
    price = _price;
    count = _count;
  }

  public String getName() {
    return name;
  }

  public Double getPrice() {
    return price;
  }

  public Double getCount() {
    return count;
  }
}
