package com.creasy.pojo;

/**
 * @author laicreasy
 */
public class Loan {

    private int id;
    private String deal_instance;
    private int deal_count;
    private int loan_amount;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDeal_instance() {
        return deal_instance;
    }

    public void setDeal_instance(String deal_instance) {
        this.deal_instance = deal_instance;
    }

    public int getDeal_count() {
        return deal_count;
    }

    public void setDeal_count(int deal_count) {
        this.deal_count = deal_count;
    }

    public int getLoan_amount() {
        return loan_amount;
    }

    public void setLoan_amount(int loan_amount) {
        this.loan_amount = loan_amount;
    }

    @Override
    public String toString() {
        return "Loan{" +
                "id=" + id +
                ", deal_instance='" + deal_instance + '\'' +
                ", deal_count=" + deal_count +
                ", loan_amount=" + loan_amount +
                '}';
    }
}
