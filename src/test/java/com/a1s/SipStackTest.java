package com.a1s;


import io.reactivex.Flowable;
import io.reactivex.Observable;
import io.reactivex.subjects.PublishSubject;
import org.junit.Test;
import org.reactivestreams.Publisher;
import org.reactivestreams.Subscriber;

import java.util.concurrent.TimeUnit;


public class SipStackTest {
    @Test
    public void testAll() throws InterruptedException {
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

    /*@Test
    public void testAll() throws InterruptedException {
        Processor processor = new Processor();

        final String callId = "123456";

        processor.call(79111234567l, callId);

        /*processor.response(callId, 100); // trying
        processor.response(callId, 180); // session in progress
        processor.response(callId, 183); // ringing

        TimeUnit.MILLISECONDS.sleep(250);

        processor.response(callId, 200); // 200 - ok
        processor.response(callId, 487); // 487 - request terminated

    }*/


}
