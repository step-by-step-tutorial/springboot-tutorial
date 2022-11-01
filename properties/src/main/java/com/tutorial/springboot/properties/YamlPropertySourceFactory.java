package com.tutorial.springboot.properties;

import static java.util.Objects.requireNonNull;

import org.springframework.beans.factory.config.YamlPropertiesFactoryBean;
import org.springframework.core.env.PropertiesPropertySource;
import org.springframework.core.env.PropertySource;
import org.springframework.core.io.support.EncodedResource;
import org.springframework.core.io.support.PropertySourceFactory;

public class YamlPropertySourceFactory implements PropertySourceFactory {

  @Override
  public PropertySource<?> createPropertySource(String name, EncodedResource resource) {
    YamlPropertiesFactoryBean factory = new YamlPropertiesFactoryBean();
    factory.setResources(resource.getResource());

    return new PropertiesPropertySource(requireNonNull(resource.getResource().getFilename()), requireNonNull(factory.getObject()));
  }
}
