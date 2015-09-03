package com.aztec.es.write;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Props;
import com.aztec.es.connection.EventStoreConnector;
import eventstore.EventData;
import eventstore.WriteEvents;
import eventstore.j.EventDataBuilder;
import eventstore.j.WriteEventsBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.UUID;

import static com.aztec.es.DemoConstants.SYSTEM_NAME;

/**
 * Write events to stream.
 *
 * @author robertgolder
 */
public class WriteEventExample {
    private static final Logger LOGGER = LoggerFactory.getLogger(WriteEventExample.class);

    public void writeEvent(final String streamName, final UUID eventId, final String eventName, final String eventData, final String eventMetaData) {
        LOGGER.info("Writing event - stream name [{}] UUID [{}] name [{}] data [{}] metadata [{}].", streamName, eventId, eventName, eventData, eventMetaData);

        final ActorSystem system = ActorSystem.create(SYSTEM_NAME);
        final ActorRef connection = EventStoreConnector.getActorRefConnection(system);
        final ActorRef writeResult = system.actorOf(Props.create(WriteResult.class));

        final EventData event = new EventDataBuilder(eventName)
                .eventId(eventId)
//                .data(eventData)
//                .metadata(eventMetaData)
                .jsonData(eventData)
                .jsonMetadata(eventMetaData)
                .build();

        final WriteEvents writeEvents = new WriteEventsBuilder(streamName)
                .addEvent(event)
                .expectAnyVersion()
                .build();

        connection.tell(writeEvents, writeResult);

        LOGGER.info("Written events.");
    }
}