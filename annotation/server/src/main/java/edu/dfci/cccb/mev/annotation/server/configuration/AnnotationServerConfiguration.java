package edu.dfci.cccb.mev.annotation.server.configuration;

import static org.springframework.context.annotation.FilterType.ANNOTATION;

import java.util.List;

import javax.annotation.PostConstruct;
import javax.inject.Inject;

import lombok.ToString;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ComponentScan.Filter;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import com.google.refine.ProjectManager;
import com.google.refine.ProjectManagerFactory;
import com.google.refine.SessionWorkspaceDir;
import com.google.refine.io.FileProjectManager;

import edu.dfci.cccb.mev.heatmap.domain.Workspace;
import edu.dfci.cccb.mev.heatmap.server.resolvers.WorkspaceHeatmapMethodArgumentResolver;

@Configuration
@ComponentScan (value = "edu.dfci.cccb.mev.annotation",
                includeFilters = @Filter (type = ANNOTATION, value = {
                                                                      Controller.class,
                                                                      ControllerAdvice.class,
                                                                      RestController.class }))
@ToString
public class AnnotationServerConfiguration extends WebMvcConfigurerAdapter {
	
	private @Inject Workspace workspace;
	private @Inject ProjectManager sessionProjectManager;
	
	@Bean
	@Scope(value ="session", proxyMode = ScopedProxyMode.TARGET_CLASS)
	public ProjectManager sessionProjectManager () {
		FileProjectManager projectManager = new FileProjectManager();
		projectManager.setWorkspaceDir(new SessionWorkspaceDir());
		return projectManager;
	}
	
	@PostConstruct
	public void setProjectmanagerSingleton () {
		ProjectManager.setSingleton(sessionProjectManager);
	}
	
	/*
	@Bean
	public ProjectManagerFactory projectManagerFactory(){
		return new ProjectManagerFactory(){
			
		};
	}*/
	
	/*
	<bean id="myWorkspaceDir" class="com.google.refine.SessionWorkspaceDir" scope="session" lazy-init="true">
		<aop:scoped-proxy proxy-target-class="true"/>
	</bean>
	
	<bean id="mySessionProjectManager" class="com.google.refine.io.FileProjectManager" scope="session">
		<property name="workspaceDir" ref="myWorkspaceDir"  />		
		<aop:scoped-proxy proxy-target-class="true" />
	</bean>
	
	<bean id="myProjectManagerFactory" class="com.google.refine.ProjectManagerFactory" >				
		<property name="projectManager" ref="mySessionProjectManager"></property>		
	</bean>
	 */
	
	@Override
		public void addArgumentResolvers(
				List<HandlerMethodArgumentResolver> argumentResolvers) {
			argumentResolvers.add(new WorkspaceHeatmapMethodArgumentResolver(workspace));
		}
}
