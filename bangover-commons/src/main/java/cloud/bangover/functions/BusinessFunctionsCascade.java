package cloud.bangover.functions;

import cloud.bangover.BoundedContextId;
import java.util.function.Consumer;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
class BusinessFunctionsCascade<Q, S, R> implements BusinessFunction<Q, R> {
  private final BusinessFunction<Q, S> original;
  private final BusinessFunction<S, R> chained;

  @Override
  public void invoke(Context<Q, R> context) {
    original.invoke(new OriginalFunctionContext(context, response -> {
      chained.invoke(new CascadedFunctionContext(context, response));
    }));
  }

  @RequiredArgsConstructor
  private class OriginalFunctionContext implements Context<Q, S> {
    private final Context<Q, R> cascadeContext;
    private final Consumer<S> originalFunctionResponseListener;
    
    @Override
    public Q getRequest() {
      return cascadeContext.getRequest();
    }

    @Override
    public void reply(S response) {
      originalFunctionResponseListener.accept(response);
    }

    @Override
    public void reject(Exception error) {
      cascadeContext.reject(error);
    }

    @Override
    public BoundedContextId getBoundedContextId() {
      return cascadeContext.getBoundedContextId();
    }
  }

  @RequiredArgsConstructor
  private class CascadedFunctionContext implements Context<S, R> {
    private final Context<Q, R> cascadeContext;
    private final S originalFunctionResponse;
    
    @Override
    public BoundedContextId getBoundedContextId() {
      return cascadeContext.getBoundedContextId();
    }

    @Override
    public S getRequest() {
      return originalFunctionResponse;
    }

    @Override
    public void reply(R response) {
      cascadeContext.reply(response);
    }

    @Override
    public void reject(Exception error) {
      cascadeContext.reject(error);
    }
  }
}
