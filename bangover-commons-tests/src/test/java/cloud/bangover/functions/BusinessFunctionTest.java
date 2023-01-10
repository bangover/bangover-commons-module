package cloud.bangover.functions;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import cloud.bangover.timer.Timeout;

@RunWith(JUnit4.class)
public class BusinessFunctionTest {
  @Test
  public void shouldFuntionCascadeBeResolved() throws Throwable {
    // Given
    BusinessFunction<Void, Long> original =
        new StubBusinessFunction<Void, Long>(10L, Timeout.ofSeconds(3L));
    BusinessFunction<Void, String> function = original.cascade(toStringFunction());
    // When
    String result = MockFunctionRunner.createFor(function).executeFunction().getResult();
    // Then
    Assert.assertEquals("10", result);
  }

  @Test(expected = RuntimeException.class)
  public void shouldFuntionCascadeBeRejected() throws Throwable {
    // Given
    BusinessFunction<Void, Long> original =
        new StubBusinessFunction<Void, Long>(new RuntimeException(), Timeout.ofSeconds(3L));
    BusinessFunction<Void, String> function = original.cascade(toStringFunction());
    // When
    MockFunctionRunner.createFor(function).executeFunction().getResult();
  }

  private <V> BusinessFunction<V, String> toStringFunction() {
    return context -> {
      context.reply(context.getRequest().toString());
    };
  }
}
