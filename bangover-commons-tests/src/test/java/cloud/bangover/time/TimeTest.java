package cloud.bangover.time;

import java.time.Instant;
import java.time.ZoneOffset;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

@RunWith(JUnit4.class)
public class TimeTest {
  MockTimeConfigurer timeModuleConfigurer = MockTimeConfigurer.create();
  private static final Instant MOCK_INSTANT = Instant.now();
  private static final ZoneOffset MOCK_ZONE = ZoneOffset.ofHours(4);

  @Test
  public void shouldTimeModuleBeInitializedByDefaultProviders() {
    // Given
    timeModuleConfigurer.reset();
    // When
    TimeProvider timeProvider = Time.timeProvider();
    ZoneProvider zoneProvider = Time.zoneProvider();
    // Then
    Assert.assertEquals(Instant.MIN, timeProvider.getCurrentInstant());
    Assert.assertEquals(ZoneOffset.UTC, zoneProvider.getZoneOffset());
  }

  @Test
  public void shouldTimeModuleBeCorrectlyConfigured() {
    // Given
    timeModuleConfigurer.init(MOCK_INSTANT, MOCK_ZONE);

    // When
    TimeProvider timeProvider = Time.timeProvider();
    ZoneProvider zoneProvider = Time.zoneProvider();

    // Then
    Assert.assertEquals(MOCK_INSTANT, timeProvider.getCurrentInstant());
    Assert.assertEquals(MOCK_ZONE, zoneProvider.getZoneOffset());

    // Cleanup
    timeModuleConfigurer.reset();
  }
}
