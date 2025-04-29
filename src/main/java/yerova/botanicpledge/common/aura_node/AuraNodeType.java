package yerova.botanicpledge.common.aura_node;

import java.util.Random;

public enum AuraNodeType {
  NORMAL,       // A balanced and stable node
  DARK,         // A corrupted or shadow-influenced node
  PURE,         // A pristine and purified node
  UNSTABLE,     // A chaotic node with unpredictable properties
  ANCIENT,      // A node with deep, forgotten magic
  DIMINISHED,   // A weakened or dying node
  CHAOTIC;      // A node with random, erratic behavior

  private static final AuraNodeType[] VALUES = values();
  private static final Random RANDOM = new Random();

  /**
   * Returns a random AuraNodeType.
   * @return A randomly selected AuraNodeType.
   */
  public static AuraNodeType getRandomType() {
    return VALUES[RANDOM.nextInt(VALUES.length)];
  }
}
