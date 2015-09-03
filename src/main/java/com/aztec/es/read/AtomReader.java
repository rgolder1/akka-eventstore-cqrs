package com.aztec.es.read;

import org.apache.abdera.Abdera;
import org.apache.abdera.model.Document;
import org.apache.abdera.model.Entry;
import org.apache.abdera.model.Feed;
import org.apache.abdera.parser.stax.FOMEntry;
import org.apache.abdera.protocol.Response.ResponseType;
import org.apache.abdera.protocol.client.AbderaClient;
import org.apache.abdera.protocol.client.ClientResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static com.aztec.es.DemoConstants.FEED_BASE_URL;

/**
 * Atom feed reader.
 *
 * @author robertgolder
 */
public class AtomReader {
    private static final Logger LOGGER = LoggerFactory.getLogger(AtomReader.class);

    public void read(final String feedName) {
        LOGGER.info("Reading stream [{}].", FEED_BASE_URL+feedName);
        Abdera abdera = new Abdera();
        AbderaClient client = new AbderaClient(abdera);
        ClientResponse resp = client.get(FEED_BASE_URL+feedName);
        if (resp.getType() == ResponseType.SUCCESS) {
            Document<Feed> doc = resp.getDocument();
            Feed feed = doc.getRoot();
            LOGGER.info("Feed [{}].", feed);
            LOGGER.info("Feed title [{}].", feed.getTitle());
            int recordNumber=1;
            for (Entry entry : feed.getEntries()) {
                LOGGER.info("Feed entry [{}] [{}]", recordNumber, entry);
                LOGGER.info("Feed entry [{}] id [{}]", recordNumber, entry.getId());
                LOGGER.info("Feed entry [{}] title [{}]", recordNumber, entry.getTitle());
                LOGGER.info("Feed entry [{}] links [{}]", recordNumber, entry.getLinks());
                LOGGER.info("Feed entry [{}] summary [{}]", recordNumber, entry.getSummary());
                LOGGER.info("Feed entry [{}] author [{}]", recordNumber, entry.getAuthor());

                followLink(client, entry.getLinks().get(0).getHref().toString(), recordNumber);
                ++recordNumber;
            }
        } else {
            LOGGER.error("Response was [{}].", resp.getType());
            throw new RuntimeException("Response was ["+ resp.getType() + "].");
        }
    }

    private void followLink(final AbderaClient client, final String url, final Integer recordNumber) {
        LOGGER.info("Following link for record {} [{}].", recordNumber, url);
        ClientResponse resp = client.get(url);
        if (resp.getType() == ResponseType.SUCCESS) {
            Document<FOMEntry> doc = resp.getDocument();
            FOMEntry entry = doc.getRoot();
            LOGGER.info("Entry detail [{}] [{}].", recordNumber, entry);
            LOGGER.info("Entry detail [{}] title [{}].", recordNumber, entry.getTitle());
            LOGGER.info("Entry detail [{}] author name [{}].", recordNumber, entry.getAuthor().getName());
            LOGGER.info("Entry detail [{}] summary [{}].", recordNumber, entry.getSummary());
        } else {
            LOGGER.error("Response was [{}].", resp.getType());
            throw new RuntimeException("Response was ["+ resp.getType() + "].");
        }
    }
}
