Demo Runs: 

demo_1.txt contains a run when a bat moves a player to a ranodm cell.

demo_2.txt contains a run when a player dies by falling into a pit.

demo_3.txt contains a run when a player dies as Wumpus eats them.

demo_4.txt contains a run when a player kills the Wumpus in the game.

Run the program using java -jar HW5-Hunt_Wumpus.jar

Use integers to give inputs to the controller when requested. The directions can be specified using 
the alphabet N, S, E, W. The operations to shoot and move may be given using alpabets m (for moving)
or s (for shooting).

There is an option to disable displaying the maze, that contains the position of player, pits, bats
the wumpus after each move and shoot command.

When the text - "Do you want to view the table with Wumpus/Player/Pit positions?:
Type Y or y to enable displaying table, press N otherwise" appears press a key such as N or n. This
disables displaying the maze after each move. Once the game ends (when player wins or loses) the
final maze is displayed with the position of wumpus, player, bats and pits.

Differences between Mazes assingment:

The mazes assignment did not have implementation related to room mazes and wrapping mazes.
The current assingment has those. The unneccessary gold and thief functionality and the relevant 
test cases are removed. A perfect maze is represented by the abstract maze class and can be 
created by using the constructor of the class NonWrappingRoomMaze.

Apart from this, these are the assumptions made:

1. After an object to create wrapping and non-wrapping mazes is created removeWalls() function has to be 
called. This function though can be called multiple time has no effect. The function receives a map
which consists of the percentage distribution of bats and pits in the maze. This can be null or the the key 
related to a specific creature (bat/pit) can be 0 to avoid adding any bats and pits to the maze.

2. The distribution of bats and pits in the maze is decided based on percentage instead of probability. 
A number betwene 0 and 100 is sent and the number of cells that are not tunnels have bats and pits based 
on this percentage.

3. Only 1 bat can be added to a cell. This means each cell can have atmost 1 bat and 1 pit.

4. LinkedHashMap is used in tests while adding a map containing the distribution percentage of bats and pits
to have an ordering when the tests run. Adding other types of maps that don't have an internal order can
result in random responses and mazes.

5. When an arrow is fired at a wall, the number of arrows remain the same. This behaviour is handled by the 
controller.

6. A player may get killed as soon as the game starts. A random cell is selected where a player is placed. 
It is not always possible to add player to a safe place when the game starts. Sometimes, the player may
end-up in the cell that has pit or wumpus and may die as soon as the game starts. Also if bats are 
present in a cell, they may transport the player to the same cell or to a different cell as soon as the game
starts. 

7. A bat may drop a player to a cellthat contains a bat. The second bat then again may pick the player and 
drop them in another random cell.

8. There is a limit on the number of internal walls and border walls that can be removed. If the parameter
passed to the model is incorrect, then the model simply throws an exception instead of continuing.