package cloud.bangover.generators;

import java.util.UUID;

public class UuidGenerator implements Generator<UUID> {
  @Override
  public UUID generateNext() {
    return UUID.randomUUID();
  }

  /**
   * Return generator which generates the transformed to {@link String} value instead of
   * {@link UUID}.
   * 
   * @return The generator
   */
  public Generator<String> generateStrings() {
    return () -> UuidGenerator.this.generateNext().toString();
  }
}
