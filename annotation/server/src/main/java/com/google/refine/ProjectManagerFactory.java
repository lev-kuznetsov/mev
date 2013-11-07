package com.google.refine;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Service;


@Service
public class ProjectManagerFactory implements InitializingBean{
    
	private ProjectManager projectManager;
    public ProjectManager getProjectManager() {
		return projectManager;
	}
	public void setProjectManager(ProjectManager pm) {
		projectManager = pm;
	}
	public ProjectManagerFactory(){}
	
	private static ProjectManagerFactory instance;

    @Override
    public void afterPropertiesSet() throws Exception {
        instance = this;
    }
    public static ProjectManagerFactory getInstance() {
        return instance;
    }

	
}
