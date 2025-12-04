package decorator;

import model.Payment;

public abstract class PaymentDecorator extends Payment {
    protected Payment decoratedPayment;

    protected PaymentDecorator(Payment payment) {
        super(payment.getId(), payment.getAmount(), payment.getMethod());
        this.decoratedPayment = payment;
    }

    @Override
    public abstract String toString();
}
