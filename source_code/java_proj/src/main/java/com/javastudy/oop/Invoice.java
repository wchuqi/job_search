package com.javastudy.oop;

/**
 * 知识点：接口实现
 */
public class Invoice implements Payable {
    private final double amount;
    private boolean paid = false;

    public Invoice(double amount) {
        this.amount = amount;
    }

    @Override
    public double amount() {
        return amount;
    }

    @Override
    public void pay() {
        this.paid = true;
    }

    public boolean isPaid() {
        return paid;
    }
}
