package org.otaibe.at.flight.test.service;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import software.amazon.awssdk.auth.credentials.DefaultCredentialsProvider;
import software.amazon.awssdk.core.async.AsyncRequestBody;
import software.amazon.awssdk.core.async.AsyncResponseTransformer;
import software.amazon.awssdk.http.nio.netty.NettyNioAsyncHttpClient;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3AsyncClient;
import software.amazon.awssdk.services.s3.model.NoSuchKeyException;
import software.amazon.awssdk.utils.StringUtils;

import javax.annotation.PostConstruct;
import javax.enterprise.context.ApplicationScoped;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.CompletionException;

@ApplicationScoped
@Getter
@Setter
@Slf4j
public class AwsService {

    public static final String REGION = "eu-west-1";
    public static final String BUCKET = "s3-stage-otaibea955a0bc5d0f";
    S3AsyncClient s3AsyncClient;

    @PostConstruct
    public void init() {
        s3AsyncClient = S3AsyncClient.builder()
                .httpClientBuilder(NettyNioAsyncHttpClient.builder()
                        .maxConcurrency(100)
                        .maxPendingConnectionAcquires(10_000))
                .credentialsProvider(DefaultCredentialsProvider.create())
                .region(Region.of(REGION))
                .build();
    }

    public Mono<Boolean> write(String key, String text) {
        return Flux.<Boolean>create(fluxSink ->
                getS3AsyncClient().putObject(
                        builder -> builder.key(key).bucket(BUCKET),
                        AsyncRequestBody.fromString(text)
                )
                        .whenComplete((putObjectResponse, throwable) -> {
                            if (putObjectResponse != null) {
                                fluxSink.next(true);
                                fluxSink.complete();
                                return;
                            }
                            fluxSink.error(throwable);
                        })
        )
                .next();
    }

    public Mono<String> read(String key) {
        return Flux.<String>create(fluxSink ->
                getS3AsyncClient().getObject(
                        builder -> builder.key(key).bucket(BUCKET),
                        AsyncResponseTransformer.toBytes()
                )
                        .whenComplete((getObjectResponseResponseBytes, throwable) -> {
                            if (getObjectResponseResponseBytes != null) {
                                if (getObjectResponseResponseBytes.response().contentLength() == 0) {
                                    fluxSink.complete();
                                    return;
                                }
                                String s = StringUtils.fromBytes(
                                        getObjectResponseResponseBytes.asByteArray(), StandardCharsets.UTF_8);
                                fluxSink.next(s);
                                fluxSink.complete();
                                return;
                            }
                            if (CompletionException.class.isAssignableFrom(throwable.getClass())) {
                                Class<? extends Throwable> causeException = ((CompletionException) throwable).getCause().getClass();
                                if (NoSuchKeyException.class.isAssignableFrom(causeException)) {
                                    fluxSink.complete();
                                    return;
                                }
                            }
                            log.error("unable to read object with key: " + key, throwable);
                            fluxSink.error(throwable);
                        })
        )
                .next();
    }

}
