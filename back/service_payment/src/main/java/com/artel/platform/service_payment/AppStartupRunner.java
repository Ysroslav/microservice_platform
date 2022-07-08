package com.artel.platform.service_payment;

import com.artel.platform.service_payment.model.Payment;
import com.artel.platform.service_payment.property.CommonProperty;
import com.artel.platform.service_payment.repository.MongoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class AppStartupRunner implements ApplicationRunner {

    private final MongoRepository mongoRepository;
    private final CommonProperty commonProperty;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        mongoRepository.createCollectionIfNotExist(Payment.class).then(
                mongoRepository.createIndex(Payment.class, commonProperty.getFieldIndex())
        ).subscribe();
    }
}
