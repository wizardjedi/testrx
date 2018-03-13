package com.a1s;


import io.reactivex.Observable;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.subjects.PublishSubject;
import org.junit.Test;
import org.reactivestreams.Publisher;
import org.reactivestreams.Subscriber;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;


public class SipStackTest {
    private static final Logger log = LoggerFactory.getLogger(SipStackTest.class);

    @Test
    public void testAll2() throws InterruptedException {
        final PublishSubject<Long> subject = PublishSubject.create();

        subject
            .doOnNext(number->System.out.println("First" + number))
            .flatMap(
                number->{
                    System.out.println("FLat map" + number);

                    return Observable.fromPublisher(new Publisher<Object>() {
                        @Override
                        public void subscribe(Subscriber<? super Object> subscriber) {
                            subscriber.onNext(number + 2000);
                        }
                    });
                })
            .subscribe(number->System.out.println("Subscribe" + number));

        subject.onNext(1l);
        subject.onNext(2l);
        subject.onNext(3l);
        subject.onNext(4l);
    }

    @Test
    public void testAll() throws InterruptedException {
        Processor processor = new Processor();

        final String callId = "123456";

        Observable callFlow = processor.call(79111234567l, callId);

        CountDownLatch latch = new CountDownLatch(1);

        callFlow.subscribe(new DisposableObserver() {
            @Override
            public void onNext(Object o) {
                log.info("Object", o);
            }

            @Override
            public void onError(Throwable throwable) {
                log.error("Error", throwable);

                latch.countDown();
            }

            @Override
            public void onComplete() {
                log.info("Completed");

                latch.countDown();
            }
        });

        processor.response(callId, 100); // trying
        processor.response(callId, 180); // session in progress
        processor.response(callId, 183); // ringing

        TimeUnit.MILLISECONDS.sleep(700);

        processor.response(callId, 200); // 200 - ok
        processor.response(callId, 487); // 487 - request terminated

        latch.await();
    }
}
