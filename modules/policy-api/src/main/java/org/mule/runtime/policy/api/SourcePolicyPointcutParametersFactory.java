/*
 * Copyright (c) MuleSoft, Inc.  All rights reserved.  http://www.mulesoft.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package org.mule.runtime.policy.api;

import org.mule.runtime.api.component.ComponentIdentifier;
import org.mule.runtime.api.component.location.ComponentLocation;
import org.mule.runtime.api.meta.AnnotatedObject;
import org.mule.runtime.api.metadata.TypedValue;

/**
 * Factory for creating {@link PolicyPointcutParameters} for an specific source.
 *
 * @since 4.0
 */
public interface SourcePolicyPointcutParametersFactory {

  /**
   * @return true if this factory can create {@link PolicyPointcutParameters} for the source identifier, false otherwise.
   */
  boolean supportsSourceIdentifier(ComponentIdentifier sourceIdentifier);

  /**
   * Creates an specific {@link PolicyPointcutParameters} for a particular source operation by {@code sourceIdentifier}.
   *
   * @param source the source where the policy is being applied.
   * @param attributes the attributes from the message generated by the message source
   * @return a {@link PolicyPointcutParameters} with custom parameters associated to the {@code sourceIdentifier}
   */
  <T> PolicyPointcutParameters createPolicyPointcutParameters(AnnotatedObject source, TypedValue<T> attributes);

}
