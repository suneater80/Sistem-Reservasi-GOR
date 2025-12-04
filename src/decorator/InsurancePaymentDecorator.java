package decorator;

import model.Payment;

public class InsurancePaymentDecorator extends PaymentDecorator {
    private static final double INSURANCE_FEE = 15000.0;

    public InsurancePaymentDecorator(Payment payment) {
        super(payment);
    }

    @Override
    public double getAmount() {
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
