# Trading212-Test
Test for the junior android developer position

Game instructions:
------------------
In order to run the game run the Launcher.java class file. Then enter the map file directory by following the instructions that
are printed in the console.

If everything went okay the game should be launched in another window. 
To start playing just hit any of the arrow keys and the snake will start slithering through the field!

If you are as impatient as I am to eat more fruits just press the 'F' button on your keyboard
and a new fruit will be spawned on the field. 

When the game has ended you can restart the game by pressing 'Enter' on your keyboard.

I would like to mention:
------------------------
- The tiles in the game have their coordinates saved in a two dimensional array. Because of the way that the two dimensional arrays
  work, the X and Y parameters of each tile are opposite of the 'conventional' ones that we are used to - 
  Y is left and right, X is up and down

- The arraylist of grass tiles is saved just because I'd like to get a random tile of grass fast in order to spawn new fruits.
  When the snake gets too big(for example more than half of the field), a random tile selection for the new fruit is 
  very likely to be a snake tile.
  
- Please check the javadocs to get more information about the methods(doc/index.html) or read the inline comments.

- Most of the testing was done in the 'System testing' phase by actually playing the game.
