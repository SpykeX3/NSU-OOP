package ru.nsu.fit.chernikov;

import java.util.HashMap;

public class Broker implements Runnable {

  private Stock stock;
  HashMap<String, Double> goods;

  public Broker(Stock st) {
    stock = st;
    goods = new HashMap<>();
  }

  public void addGoods(String name, double count) {
    double prevCount = goods.getOrDefault(name, 0d);
    goods.put(name, prevCount + count);
  }

  public void removeGoods(String name, double count) {

  }

  public double getBalance() {
    return stock.getBrokerBalance(this);
  }

  public void placeOffer(Offer offer) {
    stock.processDeal(this, offer, false);
  }

  public void placeRequest(Offer offer) {
    stock.processDeal(this, offer, true);
  }

  public boolean checkGoodsForSale(Offer offer) {
    return goods.get(offer.getName()) < offer.getCount();
  }

  public void alertOffer(Offer){

  }
  public void run() {}
}
