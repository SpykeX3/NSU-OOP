/*
 * This Java source file was generated by the Gradle 'init' task.
 */
package ru.nsu.fit.chernikov;

import java.util.ArrayList;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Semaphore;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class Stock {

  private class BrokerRecord {
    public Double balance;
    public ReadWriteLock balanceLock;

    public BrokerRecord(Double _balance) {
      balance = _balance;
      balanceLock = new ReentrantReadWriteLock();
    }
  }

  public enum OfferStatus {
    FINISHED,
    ACCEPTED,
    REJECTED
  }

  private int brokersNum;
  private double stockFee;

  private Semaphore semaphore;
  private ArrayList<Broker> brokers;
  private ConcurrentHashMap<Broker, BrokerRecord> balance;
  private ConcurrentHashMap<Broker, ConcurrentHashMap<String, Offer>> sellOffers;
  private ConcurrentHashMap<Broker, ConcurrentHashMap<String, Offer>> buyRequests;

  public Stock(double fee) {
    brokersNum = 0;
    stockFee = fee;
    semaphore = new Semaphore(0);
    balance = new ConcurrentHashMap<>();
    sellOffers = new ConcurrentHashMap<>();
    buyRequests = new ConcurrentHashMap<>();
  }

  public void addBroker(Broker newBroker, Double _balance) {
    brokers.add(newBroker);
    balance.put(newBroker, new BrokerRecord(_balance));
    sellOffers.put(newBroker, new ConcurrentHashMap<>());
    buyRequests.put(newBroker, new ConcurrentHashMap<>());
    semaphore.release();
  }

  public double getBrokerBalance(Broker br) {
    return balance.get(br).balance;
  }

  public synchronized Stock.OfferStatus processDeal(Broker br, Offer newOffer, boolean request) {
    if (request) {
      double payment = newOffer.getCount() * newOffer.getPrice();
      if (getBrokerBalance(br) < payment + stockFee) {
        return OfferStatus.REJECTED;
      }
      for (Map.Entry<Broker, ConcurrentHashMap<String, Offer>> account : sellOffers.entrySet()) {
        if (account.getKey() == br) {
          continue;
        }
        Offer offered = account.getValue().get(newOffer.getName());
        if (offered != null) {
          if (offered.getCount() > newOffer.getCount()
              && offered.getPrice() <= newOffer.getPrice()) {
            Lock bLock = balance.get(br).balanceLock.writeLock();
            Lock sLock = balance.get(account.getKey()).balanceLock.writeLock();
            sLock.lock();
            bLock.lock();
            double buyerBrokerBalance = balance.get(br).balance;
            double sellerBrokerBalance = balance.get(account.getKey()).balance;
            if (buyerBrokerBalance - stockFee - newOffer.getCount() * newOffer.getPrice() < 0) {
              bLock.unlock();
              sLock.unlock();
              return OfferStatus.REJECTED;
            }
            sellerBrokerBalance += payment;
            buyerBrokerBalance -= payment + stockFee;
            br.addGoods(newOffer.getName(), newOffer.getCount());
            sellOffers.get(account.getKey()).put(offered.getName(),new Offer(offered.getName(),offered.getPrice(),offered.getCount()-newOffer.getCount()));

          }
        }
      }
    }
  }

  public static void main(String[] args) {}
}
