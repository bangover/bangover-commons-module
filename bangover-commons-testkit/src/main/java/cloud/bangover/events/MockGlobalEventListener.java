package cloud.bangover.events;

import cloud.bangover.MockHistory;
import cloud.bangover.events.EventBus.GlobalEventListener;
import lombok.Getter;

@Getter
public class MockGlobalEventListener implements GlobalEventListener {
  private final MockHistory<EventDescriptor<Object>> history = new MockHistory<>();

  @Override
  public void onEvent(EventDescriptor<Object> eventDescriptor) {
    this.history.put(eventDescriptor);
  }
}
