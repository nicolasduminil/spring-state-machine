package fr.simplex_software.spring_sm;

import org.springframework.beans.factory.annotation.*;
import org.springframework.context.annotation.*;
import org.springframework.statemachine.config.*;
import org.springframework.statemachine.config.builders.*;
import org.springframework.statemachine.config.model.*;
import org.springframework.statemachine.data.jpa.*;
import org.springframework.statemachine.persist.*;
import org.springframework.statemachine.service.*;
import org.springframework.statemachine.uml.*;

@Configuration
@EnableStateMachineFactory
public class StateMachineConfig extends StateMachineConfigurerAdapter<String, String>
{
  @Bean
  public StateMachineRuntimePersister<String, String, String> stateMachineRuntimePersister(JpaStateMachineRepository jpaStateMachineRepository)
  {
    return new JpaPersistingStateMachineInterceptor<>(jpaStateMachineRepository);
  }

  @Bean
  public StateMachineService<String, String> stateMachineService(StateMachineFactory<String, String> stateMachineFactory, StateMachineRuntimePersister<String, String, String> stateMachineRuntimePersister)
  {
    return new DefaultStateMachineService<String, String>(stateMachineFactory, stateMachineRuntimePersister);
  }

  /*@Bean
  public StateMachineJackson2RepositoryPopulatorFactoryBean jackson2RepositoryPopulatorFactoryBean()
  {
    StateMachineJackson2RepositoryPopulatorFactoryBean factoryBean = new StateMachineJackson2RepositoryPopulatorFactoryBean();
    factoryBean.setResources(new Resource[]{new ClassPathResource("datajpamultipersist.json")});
    return factoryBean;
  }*/

  /*@Autowired
  private StateRepository<? extends RepositoryState> stateRepository;

  @Autowired
  private TransitionRepository<? extends RepositoryTransition> transitionRepository;*/

  @Autowired
  private StateMachineRuntimePersister<String, String, String> stateMachineRuntimePersister;

  @Override
  public void configure(StateMachineConfigurationConfigurer<String, String> config) throws Exception
  {
    config.withPersistence().runtimePersister(stateMachineRuntimePersister);
  }

  @Override
  public void configure(StateMachineModelConfigurer<String, String> model) throws Exception
  {
    model.withModel().factory(modelFactory());
  }

  /*@Bean
  public StateMachineModelFactory<String, String> modelFactory()
  {
    return new RepositoryStateMachineModelFactory(stateRepository, transitionRepository);
  }*/

  @Bean
  public StateMachineModelFactory<String, String> modelFactory()
  {
    UmlStateMachineModelFactory factory = new UmlStateMachineModelFactory("classpath:rdf-fact.uml");
    //factory.setStateMachineComponentResolver(stateMachineComponentResolver());
    return factory;
  }

  /*@Bean
  public StateMachineComponentResolver<String, String> stateMachineComponentResolver()
  {
    DefaultStateMachineComponentResolver<String, String> resolver = new DefaultStateMachineComponentResolver<>();
    resolver.registerAction("myAction", myAction());
    resolver.registerGuard("myGuard", myGuard());
    return resolver;
  }

  public Action<String, String> myAction()
  {
    return new Action<String, String>()
    {

      @Override
      public void execute(StateContext<String, String> context)
      {
        System.out.println("in E1");
      }
    };
  }

  public Guard<String, String> myGuard()
  {
    return new Guard<String, String>()
    {
      @Override
      public boolean evaluate(StateContext<String, String> context)
      {
        return false;
      }
    };
  }*/
}