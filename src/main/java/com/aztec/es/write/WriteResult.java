package com.aztec.es.write;

import akka.actor.Status;
import akka.actor.UntypedActor;
import eventstore.EsException;
import eventstore.WriteEventsCompleted;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * A write result.
 *
 * @author robertgolder
 */
public class WriteResult extends UntypedActor {
    private static final Logger LOGGER = LoggerFactory.getLogger(WriteResult.class);

    public void onReceive(Object message) throws Exception {
        LOGGER.info("Message received.");
        if (message instanceof WriteEventsCompleted) {
            final WriteEventsCompleted completed = (WriteEventsCompleted) message;
            LOGGER.info("Completed [{}].", completed);
        } else if (message instanceof Status.Failure) {
            final Status.Failure failure = ((Status.Failure) message);
            final EsException exception = (EsException) failure.cause();
            LOGGER.error("Failed status.  Exception [{}].", exception);
        } else {
            LOGGER.warn("Unknown message received [{}].", message);
            unhandled(message);
        }

        context().system().shutdown();
    }
}
