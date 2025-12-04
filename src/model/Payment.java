package model;

public class Payment {
    private String id;
    private double amount;
    private String method;
    private String status;
    private long paymentTimestamp;

    public Payment(String id, double amount, String method) {
        this.id = id;
        this.amount = amount;
        this.method = method;
        this.status = "PAID";
        this.paymentTimestamp = System.currentTimeMillis();
    }
    
    public void refund(double refundAmount) {
        this.status = "REFUNDED";
        this.amount -= refundAmount;
    }

    public String getId() { return id; }
    public double getAmount() { return amount; }
    public String getMethod() { return method; }
    public String getStatus() { return status; }
    public long getPaymentTimestamp() { return paymentTimestamp; }

    @Override
    public String toString() {
        return String.format("Payment ID: %s | Jumlah: %,.0f | Metode: %s | Status: %s", 
            id, amount, method, status);
    }
}