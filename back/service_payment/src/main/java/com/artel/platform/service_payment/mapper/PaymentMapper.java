package com.artel.platform.service_payment.mapper;

import com.artel.platform.service_payment.dto.PaymentRateInputDTO;
import com.artel.platform.service_payment.dto.platform.AmountDTO;
import com.artel.platform.service_payment.dto.platform.ConfirmationInputDTO;
import com.artel.platform.service_payment.dto.platform.PaymentOutputDTO;
import com.artel.platform.service_payment.enums.ConfirmationType;
import com.artel.platform.service_payment.enums.Currency;
import com.artel.platform.service_payment.enums.PaymentProcess;
import com.artel.platform.service_payment.enums.PaymentStatus;
import com.artel.platform.service_payment.enums.StatusPayment;
import com.artel.platform.service_payment.model.Payment;
import com.artel.platform.service_payment.property.CommonProperty;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
@RequiredArgsConstructor
public class PaymentMapper {

    private final CommonProperty commonProperty;

    public Payment mapPaymentRateInputToPayment(
            final PaymentRateInputDTO inputDTO,
            final PaymentProcess statusProcess,
            final PaymentStatus statusPayment
            ){
        return Payment.builder()
                .idDevice(inputDTO.idDevice())
                .keyIdempotent(inputDTO.keyIdempotent())
                .nameUser(inputDTO.nameUser())
                .email(inputDTO.email())
                .amount((int)(Double.parseDouble(inputDTO.amount()) * 100))
                .rateId(inputDTO.rateId())
                .currency(Currency.getCurrencyByValue(inputDTO.currency()))
                .description(inputDTO.description())
                .paymentProcess(statusProcess)
                .paymentStatus(statusPayment)
                .dateAdd(LocalDateTime.now())
                .dateUpdate(LocalDateTime.now()).build();
    }

    public PaymentOutputDTO mapPaymentToPaymentOutputDto(final Payment payment){
        return new PaymentOutputDTO(
                getAmountFromModel(payment),
                true,
                getConfirmationFromModel(payment),
                payment.getDescription()
        );
    }

    private AmountDTO getAmountFromModel(final Payment payment){
        return new AmountDTO(
                insert(String.valueOf(payment.getAmount()), "."),
                payment.getCurrency()
        );
    }

    private ConfirmationInputDTO getConfirmationFromModel(final Payment payment) {
        return new ConfirmationInputDTO(
                ConfirmationType.REDIRECT,
                commonProperty.getReturnUrl()
        );
    }

    private String insert(final String number, final String symbol){
        return number.substring(0, number.length()-2) + symbol + number.substring(number.length()-2);
    }

    public PaymentStatus mapStatusPaymentToModel(final StatusPayment statusPayment){
        if (statusPayment == StatusPayment.PENDING) {
            return PaymentStatus.OPEN;
        }
        if (statusPayment == StatusPayment.SUCCEEDED) {
            return PaymentStatus.SUCCEED;
        }
        return PaymentStatus.REFUSED;
    }
}
