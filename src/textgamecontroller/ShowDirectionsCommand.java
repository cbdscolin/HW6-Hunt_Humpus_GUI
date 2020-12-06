package textgamecontroller;

import java.io.IOException;
import java.util.List;

import gamecontrollerdefault.AbstractCommand;
import gamemodel.IGameModel;
import maze.Direction;
import mazecreatures.CreatureType;

/**
 * Class that is used to handle the action of showing different direction where a player can
 * either move or shoot.
 */
public class ShowDirectionsCommand extends AbstractCommand {

  Appendable out;

  /**
   * Used to instantiate a class of this specific command. It accepts the maze model and
   * the output stream where the directions data is written into.
   * @param model maze model
   * @param out sink where data is written into.
   */
  public ShowDirectionsCommand(IGameModel model, Appendable out) {
    super(model);
    this.out = out;
  }

  @Override
  public void execute() throws IOException {
    List<Direction> directions = model.getValidDirectionsForMovement();

    out.append("Player can move in directions: [ ");
    for (Direction dir: directions) {
      out.append(dir.getDirectionString()).append(" ");
    }
    out.append("]\n");
    boolean canFeelDraft = model.checkCreatureInAdjacentCells(CreatureType.PIT);
    boolean canSmellWumpus = model.checkCreatureInAdjacentCells(CreatureType.WUMPUS);
    if (canFeelDraft) {
      out.append("Player can feel draft!\n");
    }
    if (canSmellWumpus) {
      out.append("Player can smell Wumpus!\n");
    }
  }
}
