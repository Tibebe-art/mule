/*
 * Copyright (c) MuleSoft, Inc.  All rights reserved.  http://www.mulesoft.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package org.mule.runtime.module.extension.internal.introspection.enricher;

import org.mule.runtime.api.connection.ConnectionProvider;
import org.mule.runtime.extension.api.introspection.connection.ConnectionProviderModel;
import org.mule.runtime.extension.api.introspection.declaration.DescribingContext;
import org.mule.runtime.extension.api.introspection.declaration.fluent.ConnectionProviderDeclaration;
import org.mule.runtime.extension.api.introspection.declaration.fluent.ExtensionDeclaration;
import org.mule.runtime.extension.api.introspection.declaration.spi.ModelEnricher;
import org.mule.runtime.extension.api.introspection.property.ConnectionHandlingTypeModelProperty;

import java.util.List;


/**
 * Model Enricher for {@link ConnectionProviderModel} which describes
 * the Connection Handling Type for a given {@link ConnectionProvider}.
 * <p>
 * For each {@link ConnectionProviderDeclaration} in the {@code describingContext}
 * a {@link ConnectionHandlingTypeModelProperty} will be added
 *
 * @since 4.0
 */
public final class ConnectionHandlingEnricher implements ModelEnricher
{

    @Override
    public void enrich(DescribingContext describingContext)
    {
        final ExtensionDeclaration declaration = describingContext.getExtensionDeclarer().getDeclaration();
        doEnrich(declaration.getConnectionProviders());
        declaration.getConfigurations().forEach(c -> doEnrich(c.getConnectionProviders()));
    }

    private void doEnrich(List<ConnectionProviderDeclaration> providers)
    {
        providers.forEach(declaration -> declaration.addModelProperty(new ConnectionHandlingTypeModelProperty(declaration.getFactory().newInstance())));
    }
}
