package com.a1s;

import io.reactivex.Flowable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ConcurrentModificationException;
import java.util.concurrent.Callable;
import java.util.concurrent.ConcurrentHashMap;

public class Processor {
    private static final Logger log = LoggerFactory.getLogger(Processor.class);

    protected ConcurrentHashMap<String, Context> contexts = new ConcurrentHashMap<>();

    public void call(long abonent, String callId) {
        Context ctx = new Context(callId, abonent);

        ctx
            .setFlowable(
                Flowable
                    .just(ctx)
            );


        ctx
            .getFlowable()
            /*.startWith()
            .using(new Callable<Object>() {
                @Override
                public Object call() throws Exception {
                    return null;
                }
            })*/
            .subscribe(
                c -> {
                    log.info("Call id:{} to:{}", ctx.getCallId(), ctx.getAbonent());
                }
            );

    }
}
