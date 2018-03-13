package com.a1s;

import io.reactivex.BackpressureStrategy;
import io.reactivex.Observable;
import io.reactivex.subjects.PublishSubject;
import io.reactivex.subscribers.DisposableSubscriber;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

public class Processor {
    private static final Logger log = LoggerFactory.getLogger(Processor.class);

    protected ConcurrentHashMap<String, Context> contexts = new ConcurrentHashMap<>();

    public Observable call(long abonent, String callId) {
        final Context ctx = new Context(callId, abonent);

        log.info("Call id:{} abonent:{}", callId, abonent);

        PublishSubject<Long> subject = PublishSubject.<Long>create();

        PublishSubject mainSubject = PublishSubject.<Long>create();

        ctx.setMain(mainSubject);

        subject
            .toFlowable(BackpressureStrategy.DROP)
            .filter(number-> number == 183)
            .timeout(500, TimeUnit.MILLISECONDS)
            .delay(200, TimeUnit.MILLISECONDS)
            .subscribe(new DisposableSubscriber<Long>() {
                @Override
                public void onNext(Long aLong) {
                    log.info("1 onnext {}", aLong);

                    PublishSubject<Long> newSubject = PublishSubject.<Long>create();

                    ctx.getMain().onNext(Boolean.TRUE);

                    cancel();

                    newSubject
                        .toFlowable(BackpressureStrategy.DROP)
                        .filter(number-> number == 487)
                        .timeout(500, TimeUnit.MILLISECONDS)
                        .subscribe(new DisposableSubscriber<Long>() {
                            @Override
                            public void onNext(Long aLong) {
                                log.info("2 onnext {}", aLong);

                                cancel();

                                mainSubject.onComplete();
                            }

                            @Override
                            public void onError(Throwable throwable) {
                                log.info("2 error", throwable);

                                mainSubject.onError(throwable);
                            }

                            @Override
                            public void onComplete() {
                                log.info("2 complete");

                                mainSubject.onComplete();
                            }
                        });

                    ctx.setSubject(newSubject);

                    newSubject.onNext(0l);
                }

                @Override
                public void onError(Throwable throwable) {
                    log.info("1 error", throwable);

                    mainSubject.onError(throwable);
                }

                @Override
                public void onComplete() {
                    log.info("1 complete");
                }
            });

        ctx.setSubject(subject);

        contexts.put(callId, ctx);

        return ctx.getMain();
    }


    public void response(String callId, int respCode) {
        final Context context = contexts.get(callId);

        context.getSubject().onNext((long)respCode);
    }
}
