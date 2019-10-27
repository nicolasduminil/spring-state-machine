package fr.simplex_software.spring_sm;

import org.springframework.statemachine.*;
import org.springframework.statemachine.listener.*;

import java.util.*;

public class StateMachineLogListener extends StateMachineListenerAdapter<String, String>
{
  private final LinkedList<String> messages = new LinkedList<String>();

  public List<String> getMessages()
  {
    return messages;
  }

  public void resetMessages()
  {
    messages.clear();
  }

  @Override
  public void stateContext(StateContext<String, String> stateContext)
  {
    if (stateContext.getStage() == StateContext.Stage.STATE_ENTRY)
      messages.addFirst("Enter " + stateContext.getTarget().getId());
    else if (stateContext.getStage() == StateContext.Stage.STATE_EXIT)
      messages.addFirst("Exit " + stateContext.getSource().getId());
    else if (stateContext.getStage() == StateContext.Stage.STATEMACHINE_START)
      messages.addLast("Machine started");
    else if (stateContext.getStage() == StateContext.Stage.STATEMACHINE_STOP)
      messages.addFirst("Machine stopped");
  }
}
