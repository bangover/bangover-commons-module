package cloud.bangover.time;

import java.time.Instant;
import java.time.ZoneOffset;
import lombok.NoArgsConstructor;

@NoArgsConstructor(staticName = "create")
public class MockTimeConfigurer {
  private MockTimeProvider timeProvider = new MockTimeProvider();
  private MockZoneProvider zoneProvider = new MockZoneProvider();

  public void init(Instant currentTime) {
    Time.configure().registerTimeProvider(timeProvider);
    timeProvider.configurer().initializeProvidedInstant(currentTime);
  }

  public void init(ZoneOffset currentTimeZone) {
    Time.configure().registerZoneProvider(zoneProvider);
    zoneProvider.configurer().initializeProvidedZoneOffset(currentTimeZone);
  }

  public void init(Instant currentTime, ZoneOffset currentTimeZone) {
    init(currentTime);
    init(currentTimeZone);
  }

  public void reset() {
    Time.configure().reset();
    timeProvider.configurer().reset();
    zoneProvider.configurer().reset();
  }
}
