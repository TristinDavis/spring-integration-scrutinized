/*
 * Copyright 2017-2019 Ivan Krizsan
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package se.ivankrizsan.springintegration.messagechannels.subscribable;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.integration.channel.FluxMessageChannel;
import org.springframework.integration.support.MessageBuilder;
import org.springframework.messaging.Message;
import se.ivankrizsan.springintegration.messagechannels.helpers.MyReactiveSubscriber;
import se.ivankrizsan.springintegration.shared.SpringIntegrationExamplesConstants;

/**
 * Exercises demonstrating use of Spring Integration flux message channels.
 * Flux message channel is a message channel that uses reactive streams as implemented
 * by the Reactor library.
 *
 * @author Ivan Krizsan
 * @see FluxMessageChannel
 */
public class FluxMessageChannelTests implements SpringIntegrationExamplesConstants {
    /* Constant(s): */
    protected static final Log LOGGER = LogFactory.getLog(FluxMessageChannelTests.class);

    /* Instance variable(s): */


    /**
     * Tests creating a flux message channel and subscribing two
     * subscribers to the channel. A message is then sent to the channel.
     * Expected result: Each subscriber should receive one copy of the
     * message sent.
     */
    @Test
    public void multipleSubscribersTest() {
        final FluxMessageChannel theFluxMessageChannel;
        final Message<String> theInputMessage = MessageBuilder
            .withPayload(GREETING_STRING)
            .build();

        // <editor-fold desc="Answer Section" defaultstate="collapsed">
        /* Create the two subscribers. */
        final MyReactiveSubscriber theFirstSubscriber =
            new MyReactiveSubscriber("First subscriber");
        final MyReactiveSubscriber theSecondSubscriber =
            new MyReactiveSubscriber("Second subscriber");

        /* Create the message channel. */
        theFluxMessageChannel = new FluxMessageChannel();
        theFluxMessageChannel.setComponentName(FLUX_CHANNEL_NAME);

        /* Subscribe two subscribers to the message channel. */
        theFluxMessageChannel.subscribe(theFirstSubscriber);
        theFluxMessageChannel.subscribe(theSecondSubscriber);

        theFluxMessageChannel.send(theInputMessage);
        // </editor-fold>

        /* Verify that both subscribers received one copy each of the message. */
        Assertions.assertEquals(
            1,
            theFirstSubscriber
                .getSubscriberReceivedMessages()
                .size(),
            "A single message should have been received by first subscriber");
        Assertions.assertEquals(
            GREETING_STRING,
            theFirstSubscriber
                .getSubscriberReceivedMessages()
                .get(0)
                .getPayload(),
            "Message should have expected payload");
        Assertions.assertEquals(
            1,
            theSecondSubscriber
                .getSubscriberReceivedMessages()
                .size(),
            "A single message should have been received by second subscriber");
        Assertions.assertEquals(
            GREETING_STRING,
            theSecondSubscriber
                .getSubscriberReceivedMessages()
                .get(0)
                .getPayload(),
            "Message should have expected payload");
    }

    /**
     * Tests creating a flux message channel and subscribing two
     * subscribers to the channel. Two messages are then sent to the channel.
     * Expected result: Each subscriber should receive one copy of each
     * message sent.
     */
    @Test
    public void multipleSubscribersMultipleMessagesTest() {
        final FluxMessageChannel theFluxMessageChannel;
        final Message<String> theFirstInputMessage;
        final Message<String> theSecondInputMessage;

        theFirstInputMessage = MessageBuilder
            .withPayload("1")
            .build();
        theSecondInputMessage = MessageBuilder
            .withPayload("2")
            .build();

        // <editor-fold desc="Answer Section" defaultstate="collapsed">
        /* Create the two subscribers. */
        final MyReactiveSubscriber theFirstSubscriber =
            new MyReactiveSubscriber("First subscriber");
        final MyReactiveSubscriber theSecondSubscriber =
            new MyReactiveSubscriber("Second subscriber");

        /* Create the message channel. */
        theFluxMessageChannel = new FluxMessageChannel();
        theFluxMessageChannel.setComponentName(FLUX_CHANNEL_NAME);

        /* Subscribe two subscribers to the message channel. */
        theFluxMessageChannel.subscribe(theFirstSubscriber);
        theFluxMessageChannel.subscribe(theSecondSubscriber);

        theFluxMessageChannel.send(theFirstInputMessage);
        theFluxMessageChannel.send(theSecondInputMessage);
        // </editor-fold>

        /* Verify that both subscribers received one copy each of the messages. */
        Assertions.assertEquals(
            2,
            theFirstSubscriber
                .getSubscriberReceivedMessages()
                .size(),
            "Two messages should have been received by first subscriber");
        Assertions.assertEquals(
            "1",
            theFirstSubscriber
                .getSubscriberReceivedMessages()
                .get(0)
                .getPayload(),
            "First message should have expected payload");
        Assertions.assertEquals(
            "2",
            theFirstSubscriber
                .getSubscriberReceivedMessages()
                .get(1)
                .getPayload(),
            "Second message should have expected payload");

        Assertions.assertEquals(
            2,
            theSecondSubscriber
                .getSubscriberReceivedMessages()
                .size(),
            "Two messages should have been received by second subscriber");
        Assertions.assertEquals(
            "1",
            theSecondSubscriber
                .getSubscriberReceivedMessages()
                .get(0)
                .getPayload(),
            "First message should have expected payload");
        Assertions.assertEquals(
            "2",
            theSecondSubscriber
                .getSubscriberReceivedMessages()
                .get(1)
                .getPayload(),
            "Second message should have expected payload");
    }

    /**
     * Tests creating a flux message channel and subscribing two
     * subscribers to the channel. One of the subscribers limit the number of messages
     * it wants to receive to one message. Two messages are then sent to the channel.
     * Note that
     * Expected result: Each subscriber should receive one copy of the first
     * message sent.
     */
    @Test
    public void multipleSubscribersMultipleMessagesLimitEventCountTest() {
        final FluxMessageChannel theFluxMessageChannel;
        final Message<String> theFirstInputMessage;
        final Message<String> theSecondInputMessage;

        theFirstInputMessage = MessageBuilder
            .withPayload("1")
            .build();
        theSecondInputMessage = MessageBuilder
            .withPayload("2")
            .build();

        // <editor-fold desc="Answer Section" defaultstate="collapsed">
        /*
         * Create the two subscribers.
         * Note that the second subscriber limits the number of messages it
         * wants to receive to one message.
         */
        final MyReactiveSubscriber theFirstSubscriber =
            new MyReactiveSubscriber("First subscriber");
        final MyReactiveSubscriber theSecondSubscriber =
            new MyReactiveSubscriber("Second subscriber", 1);

        /* Create the message channel. */
        theFluxMessageChannel = new FluxMessageChannel();
        theFluxMessageChannel.setComponentName(FLUX_CHANNEL_NAME);

        /* Subscribe two subscribers to the message channel. */
        theFluxMessageChannel.subscribe(theFirstSubscriber);
        theFluxMessageChannel.subscribe(theSecondSubscriber);

        theFluxMessageChannel.send(theFirstInputMessage);
        theFluxMessageChannel.send(theSecondInputMessage);
        // </editor-fold>

        /* Verify that both subscribers received one copy of the first message. */
        Assertions.assertEquals(
            1,
            theFirstSubscriber
                .getSubscriberReceivedMessages()
                .size(),
            "One message should have been received by first subscriber");
        Assertions.assertEquals(
            "1",
            theFirstSubscriber
                .getSubscriberReceivedMessages()
                .get(0)
                .getPayload(),
            "Message should have expected payload");

        Assertions.assertEquals(
            1,
            theSecondSubscriber
                .getSubscriberReceivedMessages()
                .size(),
            "One message should have been received by second subscriber");
        Assertions.assertEquals(
            "1",
            theSecondSubscriber
                .getSubscriberReceivedMessages()
                .get(0)
                .getPayload(),
            "Message should have expected payload");
    }
}
