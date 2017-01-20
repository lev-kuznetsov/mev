package edu.dfci.cccb.mev.presets.tools.loader;

import edu.dfci.cccb.mev.annotation.loader.AnnotationLoader;
import edu.dfci.cccb.mev.presets.rest.configuration.PresetsRestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.stereotype.Component;

import java.io.IOException;

import static org.springframework.context.annotation.FilterType.ANNOTATION;

/**
 * Created by antony on 5/25/16.
 */
@Configuration
public class AnnotationLoaderConfiguration {
    @Bean
    public AnnotationLoader getAnnotationLoader() throws IOException {
        return new AnnotationLoader();
    }
}
