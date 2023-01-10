package cloud.bangover.functions;

import cloud.bangover.BoundedContextId;
import cloud.bangover.functions.BusinessFunction.Context;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor(staticName = "createFor")
public class MockFunctionRunner<Q, S> {
  private final BoundedContextId boundedContextId;
  private final BusinessFunction<Q, S> function;

  public static final <Q, S> MockFunctionRunner<Q, S> createFor(BusinessFunction<Q, S> function) {
    return createFor(BoundedContextId.PLATFORM_CONTEXT, function);
  }

  public Result<S> executeFunction() {
    return executeFunction(null);
  }

  public Result<S> executeFunction(Q request) {
    MockContext context = new MockContext(boundedContextId, request);
    function.invoke(context);
    return context.getResult();
  }

  @Getter
  @RequiredArgsConstructor
  private class MockContext implements Context<Q, S> {
    private final BoundedContextId boundedContextId;
    private final Q request;
    private Result<S> result;

    @Override
    public void reply(S response) {
      this.result = () -> response;
    }

    @Override
    public void reject(Exception error) {
      this.result = () -> {
        throw error;
      };
    }

    public Result<S> getResult() {
      return result;
    }
  }

  public interface Result<S> {
    S getResult() throws Exception;
  }
}
