/*
 * Copyright (c) MuleSoft, Inc.  All rights reserved.  http://www.mulesoft.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package org.mule.test.integration.client;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import org.mule.compatibility.module.client.MuleClient;
import org.mule.compatibility.transport.jms.JmsConstants;
import org.mule.functional.extensions.CompatibilityFunctionalTestCase;
import org.mule.runtime.api.exception.MuleException;
import org.mule.runtime.core.api.message.InternalMessage;

import java.util.HashMap;
import java.util.Map;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class MuleClientJmsTestCase extends CompatibilityFunctionalTestCase {

  public static final int INTERATIONS = 1;

  private MuleClient client;

  @Override
  protected String getConfigFile() {
    return "org/mule/test/integration/client/test-client-jms-mule-config.xml";
  }

  @Before
  public void before() throws MuleException {
    client = new MuleClient(muleContext);
  }

  @After
  public void after() {
    client.dispose();
  }

  @Test
  public void testClientSend() throws Exception {
    InternalMessage message = client.send(getDispatchUrl(), "Test Client Send message", null);
    assertNotNull(message);
    assertEquals("Received: Test Client Send message", message.getPayload().getValue());
  }

  @Test
  public void testClientMultiSend() throws Exception {
    for (int i = 0; i < INTERATIONS; i++) {
      InternalMessage message = client.send(getDispatchUrl(), "Test Client Send message " + i, null);
      assertNotNull(message);
      assertEquals("Received: Test Client Send message " + i, message.getPayload().getValue());
    }
  }

  @Test
  public void testClientMultiDispatch() throws Exception {
    int i = 0;
    // to init
    client.dispatch(getDispatchUrl(), "Test Client Send message " + i, null);
    long start = System.currentTimeMillis();
    for (i = 0; i < INTERATIONS; i++) {
      client.dispatch(getDispatchUrl(), "Test Client Send message " + i, null);
    }
    long time = System.currentTimeMillis() - start;
    logger.debug(i + " took " + time + "ms to process");
    Thread.sleep(1000);
  }

  @Test
  public void testClientDispatchAndReceiveOnReplyTo() throws Exception {
    Map props = new HashMap();
    props.put(JmsConstants.JMS_REPLY_TO, "replyTo.queue");

    long start = System.currentTimeMillis();
    int i = 0;
    for (i = 0; i < INTERATIONS; i++) {
      logger.debug("Sending message " + i);
      client.dispatch(getDispatchUrl(), "Test Client Dispatch message " + i, props);
    }
    long time = System.currentTimeMillis() - start;
    logger.debug("It took " + time + " ms to send " + i + " messages");

    Thread.sleep(5000);
    start = System.currentTimeMillis();
    for (i = 0; i < INTERATIONS; i++) {
      InternalMessage message = client.request("jms://replyTo.queue", 5000);
      assertNotNull("message should not be null from Reply queue", message);
      logger.debug("Count is " + i);
      logger.debug("ReplyTo Message is: " + getPayloadAsString(message));
      assertTrue(getPayloadAsString(message).startsWith("Received"));
    }
    time = System.currentTimeMillis() - start;
    logger.debug("It took " + time + "ms to receive " + i + " messages");
  }

  public String getDispatchUrl() {
    return "jms://test.queue";
  }
}
