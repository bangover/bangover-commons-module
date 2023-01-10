package cloud.bangover.functions;

import java.util.function.Consumer;

import cloud.bangover.StubbingQueue;
import cloud.bangover.StubbingQueue.StubbingQueueConfigurer;
import cloud.bangover.timer.Timeout;
import cloud.bangover.timer.Timer;

public class StubBusinessFunction<Q, S> implements BusinessFunction<Q, S> {
  private final StubbingQueue<Consumer<Context<Q, S>>> defaultStubbingQueue;
  private final Timeout sleepingTime;

  public StubBusinessFunction(S defaultResponse, Timeout sleepingTime) {
    this.defaultStubbingQueue = new StubbingQueue<>(context -> context.reply(defaultResponse));
    this.sleepingTime = sleepingTime;
  }

  public StubBusinessFunction(Exception defaultReject, Timeout sleepingTime) {
    this.defaultStubbingQueue = new StubbingQueue<>(context -> context.reject(defaultReject));
    this.sleepingTime = sleepingTime;
  }

  public StubBusinessFunctionConfigurer<Q, S> configurer() {
    StubbingQueueConfigurer<Consumer<Context<Q, S>>> queueConfigurer =
        defaultStubbingQueue.configure();

    return new StubBusinessFunctionConfigurer<Q, S>() {
      @Override
      public StubBusinessFunctionConfigurer<Q, S> withReply() {
        queueConfigurer.withNextEntry(context -> context.reply());
        return this;
      }

      @Override
      public StubBusinessFunctionConfigurer<Q, S> withReply(S result) {
        queueConfigurer.withNextEntry(context -> context.reply(result));
        return this;
      }

      @Override
      public StubBusinessFunctionConfigurer<Q, S> withReject(Exception error) {
        queueConfigurer.withNextEntry(context -> context.reject(error));
        return this;
      }
    };
  }

  @Override
  public void invoke(Context<Q, S> context) {
    Consumer<Context<Q, S>> function = defaultStubbingQueue.peek();
    Timer.sleep(sleepingTime);
    function.accept(context);
  }

  public interface StubBusinessFunctionConfigurer<Q, S> {
    StubBusinessFunctionConfigurer<Q, S> withReply();

    StubBusinessFunctionConfigurer<Q, S> withReply(S result);

    StubBusinessFunctionConfigurer<Q, S> withReject(Exception error);
  }
}
