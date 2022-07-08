package com.artel.platform.service_payment.validation;

import com.artel.platform.service_payment.dto.PaymentRateInputDTO;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

public class PaymentRateInputValidation implements Validator {


    @Override
    public boolean supports(Class<?> clazz) {
        return PaymentRateInputDTO.class.isAssignableFrom(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        ValidationUtils.rejectIfEmptyOrWhitespace(
                errors, "idDevice", "field.required");
        ValidationUtils.rejectIfEmptyOrWhitespace(
                errors, "keyIdempotent", "field.required");
        ValidationUtils.rejectIfEmptyOrWhitespace(
                errors, "nameUser", "field.required");
        ValidationUtils.rejectIfEmptyOrWhitespace(
                errors, "email", "field.required");
        ValidationUtils.rejectIfEmptyOrWhitespace(
                errors, "rateId", "field.required");
        ValidationUtils.rejectIfEmptyOrWhitespace(
                errors, "amount", "field.required");
        ValidationUtils.rejectIfEmptyOrWhitespace(
                errors, "currency", "field.required");
        ValidationUtils.rejectIfEmptyOrWhitespace(
                errors, "description", "field.required");
        ValidationUtils.rejectIfEmptyOrWhitespace(
                errors, "nameRate", "field.required");
    }
}
