package edu.dfci.cccb.mev.annotation.server.configuration;

import java.util.ArrayList;
import java.util.Collection;
import java.util.UUID;

import javax.annotation.PostConstruct;
import javax.inject.Inject;

import lombok.ToString;
import lombok.extern.log4j.Log4j;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.AutowireCapableBeanFactory;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.config.ConstructorArgumentValues;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.BeanDefinitionRegistryPostProcessor;
import org.springframework.beans.factory.support.RootBeanDefinition;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Scope;

@Configuration
@ToString
@Log4j
public class MyTest implements BeanDefinitionRegistryPostProcessor{

  @Bean 
  @Scope("prototype") 
  public MyBeanClass defaultMyBean(){
    return new MyBeanClass(0);
  }
  
  @Bean 
  @Scope("prototype") 
  public MyBeanClass myBean1(){
    return new MyBeanClass(1);
  }
  
  
  @Bean (name = "lol") @Lazy
  public Collection<MyBeanClass> beans (final AutowireCapableBeanFactory factory) {
    Collection<MyBeanClass> result = new ArrayList<MyBeanClass> () {
      private static final long serialVersionUID = 1L;
      
      {
        log.warn ("CALLED Collection() for " + System.identityHashCode (this));
      }
      
      @PostConstruct
      private void init () {
        log.warn ("CALLED Collection.@PostConstruct");
        for (MyBeanClass m : this) {
          factory.initializeBean (m, UUID.randomUUID ().toString ());
        }
      }
    };
    
    result.add (new MyBeanClass (1));
    result.add (new MyBeanClass (2));
    
    return result;
  }
  
  private @Inject ConfigurableListableBeanFactory factory;
  
  @Override
  public void postProcessBeanFactory (ConfigurableListableBeanFactory beanFactory) throws BeansException {
    // TODO Auto-generated method stub

  }
  
  @Override
  public void postProcessBeanDefinitionRegistry (BeanDefinitionRegistry registry) throws BeansException {
    for (int i = 10; --i >= 0;) {
      ConstructorArgumentValues v = new ConstructorArgumentValues ();
      v.addGenericArgumentValue (i);
      registry.registerBeanDefinition (UUID.randomUUID ().toString (),
                                       new RootBeanDefinition  (MyBeanClass.class,
                                                               v,
                                                               null));
    }
  }
}
