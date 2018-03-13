package com.a1s;

import io.reactivex.*;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;
import io.reactivex.subjects.PublishSubject;
import io.reactivex.subscribers.DisposableSubscriber;
import org.reactivestreams.Publisher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Time;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class App {
    public static final Logger logger  = LoggerFactory.getLogger(App.class);

    public static void main(String ... args) throws InterruptedException {
        logger.info("Started");

        final String[] cgpns = new String[]{"79111234567"};

        final Flowable<Long> longFlowable =
            Flowable
                .rangeLong(79110000000L, 2000);


        final PublishSubject<Long> calls = PublishSubject.<Long>create();

        calls
            .toFlowable(BackpressureStrategy.DROP)
            .delay(500,TimeUnit.MILLISECONDS, Schedulers.from(Executors.newFixedThreadPool(10)))
            .observeOn(Schedulers.from(Executors.newFixedThreadPool(10)))
            .subscribe(
                new DisposableSubscriber<Long>() {

                    @Override
                    public void onNext(Long aLong) {
                        logger.info("Call:{}", aLong);

                        try {
                            TimeUnit.SECONDS.sleep(1);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onError(Throwable throwable) {
                        logger.error("calls onError",throwable);
                    }

                    @Override
                    public void onComplete() {
                        logger.info("calls Completed");
                    }
                }
            );

        longFlowable
            .observeOn(Schedulers.from(Executors.newFixedThreadPool(10)))
            .subscribe(new DisposableSubscriber<Long>() {
                public void onNext(final Long aLong) {
                    logger.info("Value:{}", aLong);

                    calls.onNext(aLong);
                }

                public void onError(Throwable throwable) {
                    logger.error("onError",throwable);

                    calls.onError(throwable);
                }

                public void onComplete() {
                    logger.info("Completed");

                    calls.onComplete();
                }
            });

        //Thread.currentThread().join();
    }

    //public static long
}
