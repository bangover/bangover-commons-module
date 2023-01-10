package cloud.bangover.time;

import java.time.Instant;
import java.time.ZoneOffset;
import lombok.experimental.UtilityClass;

@UtilityClass
public class Time {
  private TimeProvider timeProvider = new NullTimeProvider();
  private ZoneProvider zoneProvider = new NullZoneProvider();

  public TimeProvider timeProvider() {
    return () -> timeProvider.getCurrentInstant();
  }

  public ZoneProvider zoneProvider() {
    return () -> zoneProvider.getZoneOffset();
  }

  public TimeModuleRegistrar configure() {
    return new TimeModuleRegistrar() {
      @Override
      public TimeModuleRegistrar reset() {
        return this.registerTimeProvider(new NullTimeProvider())
            .registerZoneProvider(new NullZoneProvider());
      }

      @Override
      public TimeModuleRegistrar registerZoneProvider(ZoneProvider zoneProvider) {
        Time.zoneProvider = zoneProvider;
        return this;
      }

      @Override
      public TimeModuleRegistrar registerTimeProvider(TimeProvider timeProvider) {
        Time.timeProvider = timeProvider;
        return this;
      }
    };
  }

  public interface TimeModuleRegistrar {
    TimeModuleRegistrar registerTimeProvider(TimeProvider timeProvider);

    TimeModuleRegistrar registerZoneProvider(ZoneProvider zoneProvider);

    TimeModuleRegistrar reset();
  }

  private static class NullTimeProvider implements TimeProvider {
    @Override
    public Instant getCurrentInstant() {
      return Instant.MIN;
    }
  }

  private static class NullZoneProvider implements ZoneProvider {
    @Override
    public ZoneOffset getZoneOffset() {
      return ZoneOffset.UTC;
    }
  }

}
