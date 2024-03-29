package dev.example.payment.workflow;

import dev.example.common.activities.PaymentActivities;
import dev.example.common.model.OrderDTO;
import dev.example.common.model.Status;
import dev.example.payment.entity.OperationCode;
import dev.example.payment.entity.Payment;
import dev.example.payment.service.PaymentService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@AllArgsConstructor
public class PaymentActivitiesImpl implements PaymentActivities {
    private final PaymentService paymentService;
    @Override
    public void debitPayment(OrderDTO orderDTO) {
        log.info("Debiting payment");
        paymentService.savePayment(mapToEntityCompletion(orderDTO));
    }

    private Payment mapToEntityCompletion(OrderDTO orderDTO){
        final Payment payment=new Payment();
        payment.setStatus(Status.COMPLETED);
        payment.setOrderId(orderDTO.getOrderId());
        payment.setPrice(orderDTO.getPrice()* orderDTO.getQuantity());
        payment.setOperationCode(OperationCode.DEBIT);
        return payment;
    }
    @Override
    public void reversePayment(OrderDTO orderDTO) {
        log.info("Reversing payment");
        final var dbPayments= paymentService.getPaymentsByOrderId(orderDTO.getOrderId());
        dbPayments.stream()
                .forEach(payment -> {
                    payment.setOperationCode(OperationCode.CREDIT);
                    payment.setStatus(Status.REVERSED);
                });
        paymentService.saveAllAndFlush(dbPayments);
    }
}
