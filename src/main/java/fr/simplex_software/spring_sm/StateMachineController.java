package fr.simplex_software.spring_sm;

import lombok.extern.slf4j.*;
import org.springframework.beans.factory.annotation.*;
import org.springframework.statemachine.*;
import org.springframework.statemachine.data.*;
import org.springframework.statemachine.service.*;
import org.springframework.stereotype.*;
import org.springframework.ui.*;
import org.springframework.util.*;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@Controller
@Slf4j
public class StateMachineController
{

  private final StateMachineLogListener listener = new StateMachineLogListener();

  @Autowired
  private StateMachineService<String, String> stateMachineService;

  @Autowired
  private StateMachinePersist<String, String, String> stateMachinePersist;

  /*@Autowired
  private TransitionRepository<? extends RepositoryTransition> transitionRepository;*/

  private StateMachine<String, String> currentStateMachine;

  @RequestMapping("/")
  public String home()
  {
    return "redirect:/state";
  }

  @RequestMapping("/state")
  public String feedAndGetStates(
    @RequestParam(value = "events", required = false) List<String> events,
    @RequestParam(value = "machine", required = false, defaultValue = "toto") String machine,
    Model model) throws Exception
  {
    log.info ("### Entered in controller");
    StateMachine<String, String> stateMachine = getStateMachine(machine);
    log.info ("### Got the state machine {}", stateMachine.getState().getId());
    if (events != null)
    {
      log.info ("### Found {} events", events.size());
      for (String event : events)
        stateMachine.sendEvent(event);
    }
    else
      log.info ("### No events");

    StringBuilder contextBuf = new StringBuilder();

    StateMachineContext<String, String> stateMachineContext = stateMachinePersist.read(machine);
    if (stateMachineContext != null)
    {
      log.info ("### Got the state machine context {}", stateMachineContext);
      contextBuf.append(stateMachineContext.toString());
    }
    else
      log.info ("### The state machine context is null");

    model.addAttribute("machine", machine);
    model.addAttribute("currentMachine", currentStateMachine);
    //model.addAttribute("allEvents", getEvents());
    model.addAttribute("messages", createMessages(listener.getMessages()));
    model.addAttribute("context", contextBuf.toString());
    log.info ("### Returning states");
    return "states";
  }

  private synchronized StateMachine<String, String> getStateMachine(String machineId) throws Exception
  {
    listener.resetMessages();
    if (currentStateMachine == null)
    {
      currentStateMachine = stateMachineService.acquireStateMachine(machineId, false);
      currentStateMachine.addStateListener(listener);
      currentStateMachine.start();
    } else if (!ObjectUtils.nullSafeEquals(currentStateMachine.getId(), machineId))
    {
      stateMachineService.releaseStateMachine(currentStateMachine.getId());
      currentStateMachine.stop();
      currentStateMachine = stateMachineService.acquireStateMachine(machineId, false);
      currentStateMachine.addStateListener(listener);
      currentStateMachine.start();
    }
    return currentStateMachine;
  }

  /*private String[] getEvents()
  {
    List<String> events = new ArrayList<>();
    for (RepositoryTransition t : transitionRepository.findAll())
    {
      events.add(t.getEvent());
    }
    return events.toArray(new String[0]);
  }*/

  private String createMessages(List<String> messages)
  {
    StringBuilder buf = new StringBuilder();
    for (String message : messages)
    {
      buf.append(message);
      buf.append("\n");
    }
    return buf.toString();
  }
}
