package decorator;

import model.Payment;

public class TaxPaymentDecorator extends PaymentDecorator {
    private static final double TAX_RATE = 0.10;
    private double taxAmount;

    public TaxPaymentDecorator(Payment payment) {
        super(payment);
        this.taxAmount = payment.getAmount() * TAX_RATE;
    }

    @Override
    public double getAmount() {
        return decoratedPayment.getAmount() + taxAmount;
    }

    public double getTotalWithTax() {
        return getAmount();
    }

    public double getTaxAmount() {
        return taxAmount;
    }

    @Override
    public String toString() {
        return String.format("%s | PPN 10%%: Rp%,.0f | Total+Pajak: Rp%,.0f", 
            decoratedPayment.toString(), taxAmount, getTotalWithTax());
    }
}
