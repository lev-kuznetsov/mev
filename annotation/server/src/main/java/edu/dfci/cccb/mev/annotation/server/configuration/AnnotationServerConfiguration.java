package edu.dfci.cccb.mev.annotation.server.configuration;

import static org.springframework.context.annotation.FilterType.ANNOTATION;

import java.io.IOException;
import java.sql.SQLException;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import javax.inject.Named;

import lombok.ToString;

import org.springframework.context.annotation.*;
import org.springframework.context.annotation.ComponentScan.Filter;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import com.google.refine.ProjectManager;
import com.google.refine.SessionWorkspaceDir;
import com.google.refine.io.FileProjectManager;

import edu.dfci.cccb.mev.annotation.domain.probe.contract.ProbeAnnotationPlatforms;
import edu.dfci.cccb.mev.dataset.domain.contract.Workspace;
import edu.dfci.cccb.mev.presets.contract.PresetDatasetBuilder;
import edu.dfci.cccb.mev.presets.contract.PresetDimensionBuilder;

@Configuration
@ComponentScan (value = "edu.dfci.cccb.mev.annotation",
                includeFilters = @Filter (type = ANNOTATION, value = {
                                                                      Controller.class,
                                                                      ControllerAdvice.class,
                                                                      RestController.class }))
@Import({AnnotationProjectManagerConfiguration.class})
@ToString
public class AnnotationServerConfiguration extends WebMvcConfigurerAdapter {
}
