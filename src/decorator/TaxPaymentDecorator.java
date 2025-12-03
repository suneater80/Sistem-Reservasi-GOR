package decorator;

import model.Payment;

// Concrete Decorator: Menambahkan pajak pada payment
public class TaxPaymentDecorator extends PaymentDecorator {
    private static final double TAX_RATE = 0.10; // PPN 10%
    private double taxAmount;

    public TaxPaymentDecorator(Payment payment) {
        super(payment);
        // Hitung tax dari amount payment (yang mungkin sudah di-decorate)
        this.taxAmount = payment.getAmount() * TAX_RATE;
    }

    @Override
    public double getAmount() {
        // Return total amount including tax
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
