/*
 * Copyright (c) MuleSoft, Inc.  All rights reserved.  http://www.mulesoft.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package org.mule.compatibility.core.api.endpoint;

import org.mule.compatibility.core.config.i18n.TransportCoreMessages;
import org.mule.runtime.api.i18n.I18nMessage;

/**
 * <code>EndpointNotFoundException</code> is thrown when an endpoint name or protocol is specified but a matching endpoint is not
 * registered with the Mule server
 * 
 * @deprecated Transport infrastructure is deprecated.
 */
@Deprecated
public class EndpointNotFoundException extends EndpointException {

  /**
   * Serial version
   */
  private static final long serialVersionUID = 790450139906970837L;

  public EndpointNotFoundException(String endpoint) {
    super(TransportCoreMessages.endpointNotFound(endpoint));
  }

  /**
   * @param message the exception message
   */
  public EndpointNotFoundException(I18nMessage message) {
    super(message);
  }

  /**
   * @param message the exception message
   * @param cause the exception that cause this exception to be thrown
   */
  public EndpointNotFoundException(I18nMessage message, Throwable cause) {
    super(message, cause);
  }

  public EndpointNotFoundException(Throwable cause) {
    super(cause);
  }
}
