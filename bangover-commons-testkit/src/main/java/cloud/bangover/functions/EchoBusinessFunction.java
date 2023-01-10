package cloud.bangover.functions;

import cloud.bangover.timer.Timeout;
import cloud.bangover.timer.Timer;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class EchoBusinessFunction<T> implements BusinessFunction<T, T> {
  private final Timeout sleepingTime;

  @Override
  public void invoke(Context<T, T> context) {
    Timer.sleep(sleepingTime);
    context.reply(context.getRequest());
  }
}
