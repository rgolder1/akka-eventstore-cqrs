package com.aztec.es.connection;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import eventstore.Settings;
import eventstore.j.EsConnection;
import eventstore.j.EsConnectionFactory;
import eventstore.j.SettingsBuilder;
import eventstore.tcp.ConnectionActor;

import java.net.InetSocketAddress;

import static com.aztec.es.DemoConstants.*;

/**
 * Get a connection to the EventStore.
 *
 * @author robertgolder
 */
public class EventStoreConnector {

    public static ActorRef getActorRefConnection(final ActorSystem system) {
        final Settings settings = new SettingsBuilder()
                .address(new InetSocketAddress(HOST_URL, PORT))
                .defaultCredentials(USERNAME, PASSWORD)
                .build();
        final ActorRef connection = system.actorOf(ConnectionActor.getProps(settings));
        return connection;
    }

    public static EsConnection getESConnection(final ActorSystem system) {
        final Settings settings = new SettingsBuilder()
                .address(new InetSocketAddress(HOST_URL, PORT))
                .defaultCredentials(USERNAME, PASSWORD)
                .build();
        final EsConnection connection = EsConnectionFactory.create(system, settings);
        return connection;
    }
}
