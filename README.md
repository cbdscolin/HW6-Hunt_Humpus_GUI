# Hunt the Wumpus game

##### Running in text mode

Use `java -jar HW6-Hunt_Humpus_GUI.jar --text` command
Demo runs for --text input: 
1. demo1.txt: shows player killed by wumpus in 2 player mode.
2. demo2.txt: show player killing a wumpus in 2 player mode.
Assumptions: 
3. demo3.txt: shows player carried away by bats.
4. demo4.txt: shows player killed by pits.

##### Running in GUI mode:
Use `java -jar HW6-Hunt_Humpus_GUI.jar --gui` command

1. `Input_Screen.png:` This is the first screen that you see. Enter the various parameters for the maze. Enter the number of rows, columns, players, number of arrows provided to each player, the percentage of bats and bits in this screen. Enter the number of internal walls to be removed to make the perfact maze non-wrapping maze. Enter the number of border walls to be removed to make the maze a wrapping maze.
2. The checkbox `Repeat previous maze` can be checked to play the last maze generated. If the maze is the first maze created, then this option is disregarded. Next press `Create Maze` button to start a game.
3. `Maze_screen.png:` This is the screen where players can move and shoot the wumpus. The status of the game and current players turn is displayed at the right side of this screen. Use the following commands to play the game.
    - Use **w** key to move the player towards North. 
    - Use **s** key to move the player towards South.
    - Use **a** key to move the player towards West.
    - Use **d** key to move the player towards East.
    - If you want to shoot an arrow, press and hold **Control** key and then press
        **w**, **s**, **a** or **d** key and then enter an integer value in the popup screen shown which appears as shown in `Shoot_arrow.png` image.
    - To start a new game while on this screen press **q** key.
    
**All parts of this assignment are completed**

#### Assumptions:
1. When program is run in text mode, it assumes that the game ends when any one of the two players is killed, even though the other player is alive. However the program when run in gui mode, continues the game until any one player wins or the wumpus kills all of them.
2. Shooting or moving in a invalid direction is not valid. When player tries such a move they don't lose their turn and continue to play till they perform a valid move. 
3. The distribution of bats and pits in the maze is decided based on percentage instead of probability. A number between 0 and 100 is sent and the number of cells that are not tunnels have bats and pits in the cells based on this percentage.

4. LinkedHashMap is used in tests while adding a map containing the distribution percentage of bats and pits to have an ordering when the tests run. Adding other types of maps that don't have an internal order can result in random responses and mazes.

6. A player may get killed as soon as the game starts. The game tries to allocate a starting position to a player first by finding cells that are safe. The safe cells don't have any pits or wumpus. However, in text mode if it doens't find a cell then
it assigns the player to a random cell and they may get killed. In gui mode, the user
is forced to reenter the options if such case arises. However, a player may be asigned to a cell with bats and the bats may carry the player to a cell with pits or wumpus and end up killing them.

7. A bat may drop a player to a cellthat contains a bat. The second bat then again may pick the player and drop them in another random cell.

8. There is a limit on the number of internal walls and border walls that can be removed. If the parameter passed to the model is incorrect, then the model simply throws an exception instead of continuing and the controller prompts new input.


#### Design Changes:
1. In the GUI mode, the controller tries to get input from the user again if it finds that the user inputs can kill the player even before they decide to do an action. However sometimes this is not possible and the model forces the player to a cell
where they may get killed. This can happen when a player may be asigned to a cell with bats and the bats may carry the player to a cell with pits or wumpus and end up killing them.

2. The following icons are used:
    - ![Player 1](./src/resources/extras/download_circle.png) 





