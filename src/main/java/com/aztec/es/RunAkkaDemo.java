package com.aztec.es;


import com.aztec.es.read.AtomReader;
import com.aztec.es.read.ReadEventExample;
import com.aztec.es.read.SubscribeToAllExample;
import com.aztec.es.write.WriteEventExample;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.UUID;
import java.util.concurrent.TimeUnit;

import static com.aztec.es.DemoConstants.DEFAULT_EVENT_STREAM_PREFIX;

/**
 * Demo for Akka and EventStore.
 *
 * @author robertgolder
 */
public class RunAkkaDemo {
    private static final Logger LOGGER = LoggerFactory.getLogger(RunAkkaDemo.class);

    public static void main(String args[]) throws Exception {
        LOGGER.info("Starting RunAkkaDemo.");

        // Set up some UUIDs.
        final String streamUuid = UUID.randomUUID().toString();
        final UUID createdEventUuid = UUID.randomUUID();
        final UUID completedEventUuid = UUID.randomUUID();
        LOGGER.info("Using stream [{}] createdEventUuid [{}] completedEventUuid [{}].", DEFAULT_EVENT_STREAM_PREFIX + streamUuid, createdEventUuid, completedEventUuid);

        // Subscribe to all streams.
        SubscribeToAllExample subscribeToAllExample = new SubscribeToAllExample();
        subscribeToAllExample.subscribeToAll();

        // Write the events to a specific stream.
        WriteEventExample writeEventExample = new WriteEventExample();
        writeEventExample.writeEvent(DEFAULT_EVENT_STREAM_PREFIX + streamUuid, createdEventUuid, "CreatedEvent", "Created Event Data", "Aztec Created Event");
        TimeUnit.MILLISECONDS.sleep(100);
        writeEventExample.writeEvent(DEFAULT_EVENT_STREAM_PREFIX + streamUuid, completedEventUuid, "CompletedEvent", "Completed Event Data", "AztecF Completed Event");
        TimeUnit.MILLISECONDS.sleep(100);

        // Read the events from a specific stream.
        ReadEventExample readEventExample = new ReadEventExample();
        readEventExample.readEvent(DEFAULT_EVENT_STREAM_PREFIX + streamUuid, 0);
        readEventExample.readEvent(DEFAULT_EVENT_STREAM_PREFIX + streamUuid, 1);

        TimeUnit.MILLISECONDS.sleep(500);

        // Read the ATOM feed from a specific stream
        AtomReader reader = new AtomReader();
        reader.read(DEFAULT_EVENT_STREAM_PREFIX + streamUuid);

        TimeUnit.MILLISECONDS.sleep(500);
        subscribeToAllExample.closeSubscription();

        LOGGER.info("Completed RunAkkaDemo.");
    }
}
