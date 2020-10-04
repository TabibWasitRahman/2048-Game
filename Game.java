import java.awt.*;
import java.awt.event.*;
import java.util.*;
import javax.swing.*;

public class Game extends JPanel {		// this is to extend the JPanel class so that paintComponent can be overridden
	private static final long serialVersionUID = 1L;
	private static int n = 4;			// this variable is used to hold the size of the n x n grid on which the game is played
	private static int[][] board;		// this is the 2D integer array that is used to hold all the numbers of the tiles of the grid
	private static JButton restart;		// this is the button to restart the game
	private static JButton quit;		// this is the button to quit the game
	private static int moves;			// this variable is used to keep track of the number of valid moves made by the user
	private static int maxNumber;		// this variable is used to record the maximum number on the grid
	private static boolean win;			// this boolean is used to check if the user has won or not
	
	private Game() {												// this is the constructor of the class
		super();													// this is to call the constructor of the super class
		setSize(80*n+55, 80*n+200);									// this is to set the size of the panel on which the game is played
		setLayout(null);											// this is to set the layout to null so that components can be placed manually 
		KeyListener keyListener = new MyKeyListener();				// this is my implementation of KeyListener to handle the keyboard events
		addKeyListener(keyListener);								// this is to add the KeyListener to the panel
		setFocusable(true);											// this is to ensure the KeyListener is focused on the panel 
		restart = new JButton("Restart");							// this is to create the restart button
		restart.setBounds(80*n-60, 20, 80, 40);						// this is to set the location and size of the restart button
		restart.setFocusable(false);								// this is ensure the restart button does not grab the focus of the KeyListener
		ActionListener restartListener = new RestartListener();		// this is my implementation of ActionListener to handle the restart button press
		restart.addActionListener(restartListener);					// this is to add the ActionListener to the restart button
		quit = new JButton("Quit");									// this is to create the quit button
		quit.setBounds(80*n-60, 80, 80, 40);						// this is to set the location and size of the quit button
		quit.setFocusable(false);									// this is to ensure the quit button does not grab the focus of the KeyListener
		ActionListener quitListener = new QuitListener();			// this is my implementation of ActionListener to handle the quit button press
		quit.addActionListener(quitListener);						// this is to add the ActionListener to the quit button
		restart();													// this is to send the control to the restart method which basically starts the game
	}
	
	@Override
	protected void paintComponent(Graphics g) {										// this is the paintComponent method that is overridden
		g.setColor(getBackground());												// this is to get the background color of the panel
		g.fillRect(0, 0, getWidth(), getHeight());									// this is to draw a rectangle over the whole panel to remove possible glitches
		add(restart);																// this is to add the restart button to the panel
		add(quit);																	// this is to add the quit button to the panel
		JLabel movesLabel = new JLabel("Moves made: " + moves);						// this is to create the label indicating number of moves made
		movesLabel.setBounds(20, 20, 200, 40);										// this is to set the location and size of the label
		add(movesLabel);															// this is to add the label to the panel
		JLabel maxLabel = new JLabel("Maximum number: " + maxNumber);				// this is to create the label with the maximum number on the grid
		maxLabel.setBounds(20, 80, 200, 40);										// this is to set the size and location of the label
		add(maxLabel);																// this is to add the label to the panel
		for (int i = 0; i < n; i++) {												// this is to iterate over all the elements of board 
			for (int j = 0; j < n; j++) {
				JLabel label = new JLabel();										// this is to create a label to represent a tile on the grid
				label.setBounds(20+80*j, 140+80*i, 80, 80);							// this is to set the location and size of the tile
				label.setOpaque(true);												// this is set the tile to opaque so that the background can be colored
				label.setBorder(BorderFactory.createLineBorder(Color.BLACK));		// this is to create a border on the tile
				label.setBackground(backgroundColor(board[i][j]));					// this is to set the color returned by backgroundColor() method to the tile's background color
				label.setHorizontalAlignment(JLabel.CENTER);						// this is to ensure the number on the tile is horizontally centered
				label.setVerticalAlignment(JLabel.CENTER);							// this is to ensure the number on the tile is vertically centered
				label.setFont(new Font("", Font.PLAIN, 20));						// this it to make the font size of the number to 20
				if (board[i][j] == 4 | board[i][j] == 8 | board[i][j] == 64 |		// this is to choose which tiles will have white font 
						board[i][j] > 2048) label.setForeground(Color.WHITE);		
				else label.setForeground(Color.BLACK);								// this is to set all other tiles to black font
				if (board[i][j] == 0) label.setText("");							// this is to set the text of the tiles with zeros to a blank string
				else label.setText(String.valueOf(board[i][j]));					// this is to set the text of all other tiles 
				add(label);															// this is to add the label to the panel
			}
		}
	}

	private static Color backgroundColor(int i) { 	// this method gets the number on a tile and return the background color for that tile
		if (i == 0) return Color.WHITE;				// white background for tiles with number 0
		else if (i == 2) return Color.LIGHT_GRAY;	// light gray background for tiles with number 2
		else if (i == 4) return Color.GRAY;			// gray background for tiles with number 4
		else if (i == 8) return Color.DARK_GRAY;	// dark gray background for tiles with number 8
		else if (i == 16) return Color.GREEN;		// green background for tiles with number 16
		else if (i == 32) return Color.CYAN;		// cyan background for tiles with number 32
		else if (i == 64) return Color.BLUE;		// blue background for tiles with number 64
		else if (i == 128) return Color.MAGENTA;	// magenta background for tiles with number 128
		else if (i == 256) return Color.PINK;		// pink background for tiles with number 256
		else if (i == 512) return Color.RED;		// red background for tiles with number 512
		else if (i == 1024) return Color.ORANGE;	// orange background for tiles with number 1024
		else if (i == 2048) return Color.YELLOW;	// yellow background for tiles with number 2048
		else return Color.BLACK;					// black background for tiles with number greater than  2048
	}
	
	private class MyKeyListener implements KeyListener { 							// this is my implementation of KeyListener interface
		@Override
		public void keyPressed(KeyEvent e) {										// this is to handle the key press events
			if (e.getKeyCode() == 39 | e.getKeyCode() == 68) move("right");			// this is to give control to move() method with argument "right"
			else if (e.getKeyCode() == 37 | e.getKeyCode() == 65) move("left");		// this is to give control to move() method with argument "left"
			else if (e.getKeyCode() == 40 | e.getKeyCode() == 83) move("down");		// this is to give control to move() method with argument "down"
			else if (e.getKeyCode() == 38 | e.getKeyCode() == 87) move("up");		// this is to give control to move() method with argument "up"
			else if (e.getKeyCode() == 82) restartMessage();						// this is to give control to restartMessage() method
			else if (e.getKeyCode() == 81) quit();									// this is to give control to quit() method
		}
		
		@Override
		public void keyTyped(KeyEvent e) {}											// key typed events are ignored

		@Override
		public void keyReleased(KeyEvent e) {}										// key released events are ignored
	}
	
	private class RestartListener implements ActionListener {						// this is my implementation of ActionListener for restart button
		@Override
		public void actionPerformed(ActionEvent e) { restartMessage(); }			// this is to send control to restartMessage() method 
	}
	
	private class QuitListener implements ActionListener {							// this is my implementation of ActionListener for quit button
		@Override
		public void actionPerformed(ActionEvent e) { quit(); }						// this is to send control to quit method 
	}
	
	private static void randomNumber() {											// this method generates a random number on the grid
		Random random = new Random();												// this is the Random object
		int num = random.nextInt(10);												// this is to generate a number between 0 and 9
		if (num < 8) num = 2;														// this ensures probability of 2 is 0.8
		else num = 4;																// this ensures probability of 4 is 0.2
		ArrayList<Point> availableSpaces = new ArrayList<>();						// this is an ArrayList to hold all blank spaces
		for (int i = 0; i < n; i++) {												// this is iterate over the whole grid
		    for (int j = 0; j < n; j++) 
		    	if (board[i][j] == 0) availableSpaces.add(new Point(i,j));			// this is to check and add the tile coordinates to the list if it is blank
		} if (availableSpaces.size() != 0) {										// this is to ensure some blank spaces are left
			int index = random.nextInt(availableSpaces.size());						// this is to generate a random index of the list of blank tiles
			board[availableSpaces.get(index).x][availableSpaces.get(index).y] = num;// this is to add the randomly generated 2 or 4 to the randomly selected blank tile
		}
	}
	
	private static void setMaxNumber() {									// this method refreshes the maximum number on the grid
		for (int i = 0; i < n; i++) {										// this is to iterate over the whole grid
			for (int j = 0; j < n; j++) 									
				if (maxNumber < board[i][j]) maxNumber = board[i][j];		// this is to set max number to the current number if it is greater 
		}
	}
	
	private void move(String dir) {											// this method is to handle the arrow/WASD key presses
		for (int i = 0; i < 100; i++) System.out.println();					// this is to refresh the console
		System.out.println("Key pressed: " + dir);							// this is to print the key pressed onto the console
		if (isValidMove(dir)) System.out.println("The move is valid");		// this is to print if the move is valid
		else System.out.println("The move is not valid");					// this is to print if the move is not valid
		if (isValidMove(dir)) {												// this is to check if the move is valid
			merge(dir);														// this is to merge the possible tiles in the desired direction
			shift(dir);														// this is to shift the possible tiles in the desired direction
			randomNumber();													// this is generate another random number
			moves++;														// this is to increment the moves
			setMaxNumber();													// this is to refresh the maximum number
			removeAll();													// this is to remove the old components
			repaint();														// this is to repaint the updated panel
			if (maxNumber == 2048 & !win) {									// this is to handle the case when user had won
				String[] options = {"Keep going", "Restart", "Quit"};		// this is the list of options the user can choose for the JOptionPane below
				int x = JOptionPane.showOptionDialog(this, "Congrats, you won!\nWhat do you want to do?", "You won!",
						JOptionPane.DEFAULT_OPTION, JOptionPane.INFORMATION_MESSAGE, null, options, options[0]);
				if (x == 1) { restart(); }									// this is when user chooses to restart
				else if (x == 2) { System.exit(0);}							// this is when user chooses to quit
				else win = true;											// this is when user chooses to keep going
			} if (gameOver()) {												// this is to handle when user had lost
				String[] options = {"Try again", "Quit"};					// this is the list of options for the user for the JOptionPane below
				int x = JOptionPane.showOptionDialog(this, "Oops, game over!\nWhat do you want to do?", "Game over!", 
						JOptionPane.DEFAULT_OPTION, JOptionPane.INFORMATION_MESSAGE, null, options, options[0]);
				if (x == 0) restart(); 										// this is when the user chooses to try again
				else if (x == 1) System.exit(0);							// this is when the user chooses to quit
			}
		} System.out.println("Moves made: " + moves);						// this is to print the number of moves onto the console
		System.out.println("Maximum number: " + maxNumber);					// this is to print the maximum number on the console
	}

	private static void merge(String dir) {									// this method merges the tiles
		if (dir.equals("right")) {											// this is when the move was right
			for (int i = 0; i < n; i++) {									// this is to iterate over the whole grid
				for (int j = n-1; j > 0; j--) {
					if (board[i][j] != 0) {									// this is to ensure the tile is not blank
						for (int k = j-1; k >= 0; k--) {					// this is to find another tile
							if 	(board[i][k] != 0) {						// this is when a non-zero tile is found
								if (board[i][k] == board[i][j]) {			// this is when a matching tile is found
									board[i][j] += board[i][k];				// this is to merge the tiles
									board[i][k] = 0;						// this is to remove the other tile
								} break;									// this is to stop looking for tiles
							}
						}
					} 
				}
			}
		} else if (dir.equals("left")) {									// this is when the move was left
			for (int i = 0; i < n; i++) {									// this is to iterate over the whole grid
				for (int j = 0; j < n-1; j++) {
					if (board[i][j] != 0) {									// this is to ensure the tile is not blank
						for (int k = j+1; k < n ; k++) {					// this is to find another tile
							if 	(board[i][k] != 0) {						// this is when a non-zero tile is found
								if (board[i][k] == board[i][j]) {			// this is when a matching tile is found
									board[i][j] += board[i][k];				// this is to merge the tiles
									board[i][k] = 0;						// this is to remove the other tile
								} break;									// this is to stop looking for tiles
							}
						}
					}
				}
			}
		} else if (dir.equals("down")) {									// this is when the move was down
			for (int i = n-1; i > 0; i--) {									// this is to iterate over the whole grid
				for (int j = 0; j < n; j++) {
					if (board[i][j] != 0) {									// this is to ensure the tile is not blank
						for (int k = i-1; k >= 0; k--) {					// this is to find another tile
							if 	(board[k][j] != 0) {						// this is when a non-zero tile is found
								if (board[k][j] == board[i][j]) {			// this is when a matching tile is found
									board[i][j] += board[k][j];				// this is to merge the tiles
									board[k][j] = 0;						// this is to remove the other tile
								} break;									// this is to stop looking for tiles
							}
						}
					}
				}
			}
		} else {															// this is when the move was up
			for (int i = 0; i < n-1; i++) {									// this is to iterate over the whole grid
				for (int j = 0; j < n; j++) {
					if (board[i][j] != 0) {									// this is to ensure the tile is not blank
						for (int k = i+1; k < n; k++) {						// this is to find another tile
							if 	(board[k][j] != 0) {						// this is when a non-zero tile is found
								if (board[k][j] == board[i][j]) {			// this is when a matching tile is found
									board[i][j] += board[k][j];				// this is to merge the tiles
									board[k][j] = 0;						// this is to remove the other tile
								} break;									// this is to stop looking for tiles
							}
						}
					}
				}
			}
		}
	}
	
	private static void shift(String dir) {						// this method shifts tiles 
		if (dir.equals("right")) {								// this is when the move is right
			for (int i = 0; i < n; i++) {						// this is to iterate through the whole grid
				for (int j = n-2; j  >= 0; j--) {
					if (board[i][j] != 0) {						// this ensures the tile is not blank
						for (int k = n-1; k > j; k--) {			// this is to find a blank spot
							if (board[i][k] == 0) {				// this is when a blank tile is found
								board[i][k] = board[i][j];		// this is to shift the tile to the blank tile
								board[i][j] = 0;				// this is to remove the other tile
								break;							// this is stop looking for a blank tile
							}
						}
					}
				}
			}
		} else if (dir.equals("left")) {						// this is when the move is left
			for (int i = 0; i < n; i++) {						// this is to iterate through the whole grid
				for (int j = 1; j  < n; j++) {
					if (board[i][j] != 0) {						// this ensures the tile is not blank
						for (int k = 0; k < j; k++) {			// this is to find a blank spot
							if (board[i][k] == 0) {				// this is when a blank tile is found	
								board[i][k] = board[i][j];		// this is to shift the tile to the blank tile
								board[i][j] = 0;				// this is to remove the other tile
								break;							// this is stop looking for a blank tile 
							}
						}
					}
				}
			}
		} else if (dir.equals("down")) {						// this is when the move is down
			for (int i = n-2; i >= 0; i--) {					// this is to iterate through the whole grid
				for (int j = 0; j < n; j++) {
					if (board[i][j] != 0) {						// this ensures the tile is not blank
						for (int k = n-1; k > i; k--) {			// this is to find a blank spot
							if (board[k][j] == 0) {				// this is when a blank tile is found
								board[k][j] = board[i][j];		// this is to shift the tile to the blank tile
								board[i][j] = 0;				// this is to remove the other tile
								break;							// this is stop looking for a blank tile
							}
						}
					}
				}
			}
		} else {												// this is when the move is up
			for (int i = 1; i < n; i++) {						// this is to iterate through the whole grid
				for (int j = 0; j < n; j++) {
					if (board[i][j] != 0) {						// this ensures the tile is not blank
						for (int k = 0; k < i; k++) {			// this is to find a blank spot
							if (board[k][j] == 0) {				// this is when a blank tile is found
								board[k][j] = board[i][j];		// this is to shift the tile to the blank tile
								board[i][j] = 0;				// this is to remove the other tile
								break;							// this is stop looking for a blank tile
							}
						}
					}
				}
			}
		}
	}

	private static boolean isValidMove(String dir) {										// this method checks if the move is valid or not
		if (dir.equals("right")) {															// this is when the move was right
			for (int i = 0; i < n; i++) {													// this is to iterate over the whole grid
				for (int j = 0; j < n-1; j++) {
					if (board[i][j] != 0 & board[i][j+1] == 0) return true;					// this checks if shifting right is possible
					if (board[i][j] != 0 & board[i][j] == board[i][j+1]) return true;		// this checks if merging right is possible
				}
			} return false;																	// this is when the above checks failed
		} else if (dir.equals("left")) {													// this is when the move was left
			for (int i = 0; i < n; i++) {													// this is to iterate over the whole grid
				for (int j = 1; j < n; j++) {
					if (board[i][j] != 0 & board[i][j-1] == 0) return true;					// this checks if shifting left is possible
					if (board[i][j] != 0 & board[i][j] == board[i][j-1]) return true;		// this checks if merging left is possible
				}
			} return false;																	// this is when the above checks failed
		}  else  if (dir.equals("down")){													// this is when the move was down
			for (int i = 0; i < n-1; i++) {													// this is to iterate over the whole grid
				for (int j = 0; j < n; j++) {
					if (board[i][j] != 0 & board[i+1][j] == 0) return true;					// this checks if shifting down is possible
					if (board[i][j] != 0 & board[i][j] == board[i+1][j]) return true;		// this checks if merging down is possible
				}
			} return false;																	// this is when the above checks failed
		} else {																			// this is when the move was up
			for (int i = 1; i < n; i++) {													// this is to iterate over the whole grid
				for (int j = 0; j < n; j++) {												
					if (board[i][j] != 0 & board[i-1][j] == 0) return true;					// this checks if shifting up is possible
					if (board[i][j] != 0 & board[i][j] == board[i-1][j]) return true;		// this checks if merging up is possible
				}
			} return false;																	// this is when the above checks failed
		} 
	}
	
	private static boolean gameOver() {										// this method checks if the game is over or not 
		for (int i = 0; i < n; i++) {										// this is to iterate over the whole grid
			for (int j = 0; j < n; j++) {
				if (board[i][j] == 0) return false;							// this checks for blank tiles
				if ( j > 0 )												
					if (board[i][j] == board[i][j-1]) return false;			// this checks if left or right merging is possible
				if ( i > 0 )
					if (board[i][j] == board[i-1][j]) return false;			// this checks if up or down merging is possible
			}
		} return true;														// this is when none of the checks worked
	}
	
	private void restart() {	// this method restarts the game
		board = new int[n][n];	// this is to set all the tiles to 0
		randomNumber();			// this is to generate the first random number on the grid
		randomNumber();			// this is to generate the second random number on the grid
		moves = 0;				// this is to set the number of moves made back to 0
		maxNumber = 0;			// this is to set the maximum number on the board back to 0
		setMaxNumber();			// this is to set the maximum number considering the two random numbers on the board
		win = false;			// this is to set the boolean win back to false
		removeAll();			// removing all the previous components on the panel
		repaint();				// calling the paintComponent method to repaint the whole panel again
	}
	
	private void restartMessage() {		// this method pops a JOptionPane to ask for user confirmation to restart game
		int x = JOptionPane.showOptionDialog(this, "Are you sure you want to restart the game?", "Restart?",
				JOptionPane.YES_NO_OPTION, JOptionPane.INFORMATION_MESSAGE, null, null, null);
        if (x == 0) { restart(); }		// this is to send control to restart() method if user clicks "Yes" option
	}
	
	private void quit() {				// this method pops a JOptionPane to ask for user confirmation to quit game
        int x = JOptionPane.showOptionDialog(this, "Are you sure you want to quit the game?", "Quit?",
        		JOptionPane.YES_NO_OPTION, JOptionPane.INFORMATION_MESSAGE, null, null, null);
        if (x == 0) System.exit(0);		// this is to exit the program is user clicks "Yes" option
	}

	public static void main(String[] args) {					// this the main method of the game
		JFrame frame = new JFrame("2048");						// this is to create a JFrame for the game
		frame.setSize(80*n+55, 80*n+200);						// this is to set the size of the frame, the same as the panel of the game
		frame.setLayout(null);									// this is to have no layout for the frame
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);	// this is to terminate the program when close button is pressed
		Game game = new Game();									// this is create an instance of the game class 
		frame.add(game);										// this is to add the game panel to the frame
		frame.setVisible(true);									// this is to set the frame visible
	}
}