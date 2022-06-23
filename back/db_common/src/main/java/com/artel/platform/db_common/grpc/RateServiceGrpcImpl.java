package com.artel.platform.db_common.grpc;

import com.artel.platform.db_common.mapper.RateGrpcMapper;
import com.artel.platform.db_common.repository.RateRepository;
import com.artel.platform.service_rates.grpc.RateByIdRequest;
import com.artel.platform.service_rates.grpc.RateByIdResponse;
import com.artel.platform.service_rates.grpc.RateDeleteRequest;
import com.artel.platform.service_rates.grpc.RateDeleteResponse;
import com.artel.platform.service_rates.grpc.RateSaveRequest;
import com.artel.platform.service_rates.grpc.RateSaveResponse;
import com.artel.platform.service_rates.grpc.RateUpdateRequest;
import com.artel.platform.service_rates.grpc.RateUpdateResponse;
import com.artel.platform.service_rates.grpc.RatesRequest;
import com.artel.platform.service_rates.grpc.RatesResponse;
import com.artel.platform.service_rates.grpc.ReactorRateServiceGrpc;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.devh.boot.grpc.server.service.GrpcService;
import reactor.core.publisher.Mono;

@GrpcService
@RequiredArgsConstructor
@Slf4j
public class RateServiceGrpcImpl extends ReactorRateServiceGrpc.RateServiceImplBase {

    private final RateRepository repository;
    private final RateGrpcMapper mapper;

    @Override
    public Mono<RatesResponse> getAllRates(Mono<RatesRequest> request) {
        log.info("start method getAllRates");
        return repository.findAllRates()
                         .map(mapper::rateToRateGrpc)
                         .collectList()
                .map(list -> RatesResponse.newBuilder().addAllRate(list).build());
    }

    @Override
    public Mono<RateByIdResponse> getRateById(Mono<RateByIdRequest> request) {
        log.info("start method getRateById");
        return request.map(RateByIdRequest::getIdRate)
                .flatMap(repository::findRateById)
                .map(result -> RateByIdResponse.newBuilder()
                                               .setRate(mapper.rateToRateGrpc(result)).build());
    }

    @Override
    public Mono<RateSaveResponse> saveRate(Mono<RateSaveRequest> request) {
        log.info("start method saveRate");
        return request.map(RateSaveRequest::getRate)
                .flatMap(rate -> repository.save(mapper.rateGrpcToRate(rate, null)))
                      .map(result -> RateSaveResponse.newBuilder().setId(result).build());
    }

    @Override
    public Mono<RateDeleteResponse> deleteRate(Mono<RateDeleteRequest> request) {
        log.info("start method deleteRate");
        return request.flatMap(req -> repository.deleteRateById(req.getIdRate()))
                .map(result -> RateDeleteResponse.newBuilder().setRows(result).build());
    }

    @Override
    public Mono<RateUpdateResponse> updateRateById(Mono<RateUpdateRequest> request) {
        log.info("start method updateRateById");
        return request.flatMap(req ->
                              repository.update(mapper.rateGrpcToRate(req.getRate(), req.getRate().getId())))
                      .map(result -> RateUpdateResponse.newBuilder().setRows(result).build());
    }
}
