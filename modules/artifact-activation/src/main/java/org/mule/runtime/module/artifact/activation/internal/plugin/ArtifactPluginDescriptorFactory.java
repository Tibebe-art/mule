/*
 * Copyright (c) MuleSoft, Inc.  All rights reserved.  http://www.mulesoft.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package org.mule.runtime.module.artifact.activation.internal.plugin;

import static java.util.Optional.empty;

import org.mule.runtime.api.deployment.meta.MuleArtifactLoaderDescriptor;
import org.mule.runtime.api.deployment.meta.MulePluginModel;
import org.mule.runtime.module.artifact.activation.internal.descriptor.AbstractArtifactDescriptorFactory;
import org.mule.runtime.module.artifact.api.descriptor.ArtifactDescriptorValidatorBuilder;
import org.mule.runtime.module.artifact.api.descriptor.ArtifactPluginDescriptor;
import org.mule.runtime.module.artifact.api.descriptor.BundleDependency;
import org.mule.runtime.module.artifact.api.descriptor.BundleDescriptor;
import org.mule.runtime.module.artifact.api.descriptor.ClassLoaderModel;
import org.mule.runtime.module.artifact.api.descriptor.DeployableArtifactDescriptor;
import org.mule.runtime.module.artifact.api.plugin.LoaderDescriber;
import org.mule.tools.api.classloader.model.Artifact;
import org.mule.tools.api.classloader.model.ArtifactCoordinates;

import java.io.File;
import java.util.List;

/**
 * Creates an artifact descriptor for a plugin.
 */
public class ArtifactPluginDescriptorFactory
    extends AbstractArtifactDescriptorFactory<MulePluginModel, ArtifactPluginDescriptor> {

  private final MulePluginModel pluginModel;
  private final BundleDescriptor bundleDescriptor;
  private final DeployableArtifactDescriptor ownerDescriptor;
  private final List<BundleDependency> bundleDependencies;
  private final ArtifactCoordinates pluginArtifactCoordinates;
  private final List<Artifact> pluginDependencies;

  public ArtifactPluginDescriptorFactory(BundleDependency bundleDependency, MulePluginModel pluginModel,
                                         DeployableArtifactDescriptor ownerDescriptor, List<BundleDependency> bundleDependencies,
                                         ArtifactCoordinates pluginArtifactCoordinates, List<Artifact> pluginDependencies,
                                         ArtifactDescriptorValidatorBuilder artifactDescriptorValidatorBuilder) {
    super(new File(bundleDependency.getBundleUri()), artifactDescriptorValidatorBuilder);

    this.pluginModel = pluginModel;
    this.bundleDescriptor = bundleDependency.getDescriptor();
    this.ownerDescriptor = ownerDescriptor;
    this.bundleDependencies = bundleDependencies;
    this.pluginArtifactCoordinates = pluginArtifactCoordinates;
    this.pluginDependencies = pluginDependencies;
  }

  @Override
  protected MulePluginModel createArtifactModel() {
    return pluginModel;
  }

  @Override
  protected void doDescriptorConfig(ArtifactPluginDescriptor descriptor) {
    getArtifactModel().getExtensionModelLoaderDescriptor().ifPresent(extensionModelDescriptor -> {
      final LoaderDescriber loaderDescriber = new LoaderDescriber(extensionModelDescriptor.getId());
      loaderDescriber.addAttributes(extensionModelDescriptor.getAttributes());
      descriptor.setExtensionModelDescriptorProperty(loaderDescriber);
    });

    getArtifactModel().getLicense().ifPresent(descriptor::setLicenseModel);
  }

  @Override
  protected ClassLoaderModel getClassLoaderModel(MuleArtifactLoaderDescriptor muleArtifactLoaderDescriptor) {
    return new PluginClassLoaderConfigurationAssembler(pluginArtifactCoordinates,
                                                       pluginDependencies,
                                                       getArtifactLocation(),
                                                       muleArtifactLoaderDescriptor,
                                                       bundleDependencies, bundleDescriptor, ownerDescriptor)
                                                           .createClassLoaderModel();
  }

  @Override
  protected BundleDescriptor getBundleDescriptor() {
    return bundleDescriptor;
  }

  @Override
  protected ArtifactPluginDescriptor doCreateArtifactDescriptor() {
    return new ArtifactPluginDescriptor(getArtifactModel().getName(), empty());
  }
}
