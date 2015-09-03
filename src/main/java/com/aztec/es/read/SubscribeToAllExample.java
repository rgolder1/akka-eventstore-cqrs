package com.aztec.es.read;

import akka.actor.ActorSystem;
import com.aztec.es.connection.EventStoreConnector;
import eventstore.IndexedEvent;
import eventstore.SubscriptionObserver;
import eventstore.j.EsConnection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Closeable;
import java.io.IOException;

import static com.aztec.es.DemoConstants.SYSTEM_NAME;

/**
 * Subscribe to all streams.
 *
 * @author robertgolder
 */
public class SubscribeToAllExample {
    private static final Logger LOGGER = LoggerFactory.getLogger(SubscribeToAllExample.class);

    private Closeable subscriptionObserver;
    private ActorSystem system;

    public void subscribeToAll() {
        system = ActorSystem.create(SYSTEM_NAME);
        final EsConnection connection = EventStoreConnector.getESConnection(system);
        subscriptionObserver = connection.subscribeToAll(new SubscriptionObserver<IndexedEvent>() {
            @Override
            public void onLiveProcessingStart(Closeable subscription) {
                LOGGER.info("Live processing started to subscription [{}]", subscription);
            }

            @Override
            public void onEvent(IndexedEvent event, Closeable subscription) {
                LOGGER.info("Event received [{}]", event);
                LOGGER.info("Event eventId [{}] eventType [{}] metadata [{}] data [{}] streamId [{}].",
                        event.event().data().eventId(),
                        event.event().data().eventType(),
                        event.event().data().metadata(),
                        event.event().data().data(),
                        event.event().streamId().streamId());
            }

            @Override
            public void onError(Throwable e) {
                LOGGER.error("Error received [{}]", e.getMessage());
            }

            @Override
            public void onClose() {
                LOGGER.info("Subscription closed.");
            }
        }, false, null);
    }

    public void closeSubscription() {
        LOGGER.info("Closing subscription.");
        try {
            subscriptionObserver.close();
            system.shutdown();
        } catch (IOException e) {
            LOGGER.error(e.getMessage());
            throw new RuntimeException(e);
        }
    }
}