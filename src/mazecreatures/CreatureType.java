package mazecreatures;

import java.util.ArrayList;
import java.util.List;

/**
 * Enumerations that is used to represent features of bat, pits, & wumpus.
 */
public enum CreatureType {
  PIT(0, "Pit"),
  BAT(1, "Bat"),
  WUMPUS(2, "Wumpus",1);

  private final int priority;

  private final String creatureName;

  private final boolean canCustomize;

  private final int creatureTotalOccurrences;

  /**
   * Initializes the creature with a priority. If a cell has multiple creatures of different types
   * this priority is used to determine action on the player. Higher the priority, the behaviour
   * of this creatures is enforced first.
   * @param priority priority of this creature in a cell.
   * @param creatureName name of this creature such as bat, pit or wumpus
   * @param canCustomize true if the number of creatures in maze can be modified
   * @param defaultValue if the canCustomize attribute is false, then the number of creatures
   *                     of this type that can be existing simultaneously.
   * @throws IllegalArgumentException thrown when creature name is null or empty
   */
  CreatureType(int priority, String creatureName, boolean canCustomize,
               int defaultValue) throws IllegalArgumentException {
    if (creatureName == null || creatureName.isEmpty()) {
      throw new IllegalArgumentException("Null creature name cannot be used");
    }
    this.priority = priority;
    this.creatureName = creatureName;
    this.canCustomize = canCustomize;
    this.creatureTotalOccurrences = defaultValue;
  }

  /**
   * Initializes the creature with a priority. If a cell has multiple creatures of different types
   * this priority is used to determine action on the player. Higher the priority, the behaviour
   * of this creatures is enforced first.
   * @param priority priority of this creature in a cell.
   * @param creatureName name of this creature such as bat, pit or wumpus
   * @throws IllegalArgumentException thrown when creature name is null or empty
   */
  CreatureType(int priority, String creatureName)
          throws IllegalArgumentException {
    this(priority, creatureName, true, 0);
  }

  CreatureType(int priority, String creatureName, int defaultValue)
          throws IllegalArgumentException {
    this(priority, creatureName, false, defaultValue);
    if (this.creatureTotalOccurrences <= 0) {
      throw new IllegalArgumentException("Default value has to be greater than zero "
              + "when specified");
    }
  }

  /**
   * Function returns the creature's name.
   * @return creature's name such as bat, wumpus, pit
   */
  public String getCreatureName() {
    return creatureName;
  }

  /**
   * Function returns the number of creatures that can be present in a maze simultaneously.
   * This is valid only for a Wumpus creature.
   * @return the number of creatures that can be present in a maze simultaneously.
   * @throws UnsupportedOperationException thrown when this function is called for a bat, pit
   */
  public int getCreatureTotalOccurrences() throws UnsupportedOperationException {
    if (canCustomize) {
      throw new UnsupportedOperationException("Default value is invalid and should not be used");
    }
    return this.creatureTotalOccurrences;
  }

  /**
   * Returns the priority of this creature. The priority is used to determine the order of
   * execution of the action that a creature performs when a player enters a cell that has a
   * creature. Since wumpus has higher priority, a cave that has bat, pit & wumpus, wumpus
   * first completes its action which is kill the player.
   * @return the priority of the creature.
   */
  public int getPriority() {
    return priority;
  }

  /**
   * Returns a list of creatures whose number can be dynamically varied. Currently this list
   * contains bat & pits only.
   * @return the list of creatures that can be present in  different numbers in maze
   */
  public static List<CreatureType> getCustomizableCreatures() {
    List<CreatureType> creatures = new ArrayList<>();
    for (CreatureType crType: CreatureType.values()) {
      if (crType.canCustomize) {
        creatures.add(crType);
      }
    }
    return creatures;
  }
}
