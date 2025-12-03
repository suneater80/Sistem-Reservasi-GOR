package decorator;

import model.Payment;

// Concrete Decorator: Menambahkan asuransi pada payment
public class InsurancePaymentDecorator extends PaymentDecorator {
    private static final double INSURANCE_FEE = 15000.0; // Biaya asuransi flat

    public InsurancePaymentDecorator(Payment payment) {
        super(payment);
    }

    @Override
    public double getAmount() {
        // Return total including insurance fee for cascading
        return decoratedPayment.getAmount() + INSURANCE_FEE;
    }

    public double getTotalWithInsurance() {
        return getAmount();
    }

    public double getInsuranceFee() {
        return INSURANCE_FEE;
    }

    @Override
    public String toString() {
        return String.format("%s | Asuransi: Rp%,.0f | Total+Asuransi: Rp%,.0f", 
            decoratedPayment.toString(), INSURANCE_FEE, getTotalWithInsurance());
    }
}
