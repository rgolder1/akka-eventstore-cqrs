package com.aztec.es.read;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Props;
import com.aztec.es.connection.EventStoreConnector;
import eventstore.ReadEvent;
import eventstore.j.ReadEventBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static com.aztec.es.DemoConstants.SYSTEM_NAME;

/**
 * Read events from stream.
 *
 * @author robertgolder
 */
public class ReadEventExample {
    private static final Logger LOGGER = LoggerFactory.getLogger(ReadEventExample.class);

    public void readEvent(final String streamName, final Integer eventNumber) {
        LOGGER.info("Reading event - stream name [{}].", streamName);

        final ActorSystem system = ActorSystem.create(SYSTEM_NAME);
        final ActorRef connection = EventStoreConnector.getActorRefConnection(system);
        final ActorRef readResult = system.actorOf(Props.create(ReadResult.class));

        final ReadEvent readEvent = new ReadEventBuilder(streamName)
                .number(eventNumber)
                .resolveLinkTos(false)
                .requireMaster(true)
                .build();

        connection.tell(readEvent, readResult);
        LOGGER.info("Read event.");
    }
}