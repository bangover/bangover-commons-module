package cloud.bangover.events;

import cloud.bangover.BoundedContextId;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

@Getter
@ToString
@EqualsAndHashCode
@RequiredArgsConstructor
public class EventDescriptor<E> {
  private final BoundedContextId boundedContext;
  private final EventType<E> eventType;
  private final E event;
}
