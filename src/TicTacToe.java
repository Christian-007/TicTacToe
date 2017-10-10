import java.awt.Container;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.HashSet;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;


public class TicTacToe extends JFrame{
	private static final int BOARD_SIZE = 9;
	private ArrayList<JButton> tileButtons = new ArrayList<JButton>();
	private Player player1 = new Player(1);
	private Player player2 = new Player(2);
	private AI ai = new AI();
	private boolean isWinner;
	
	public TicTacToe() {
		setTitle("Tic Tac Toe");
		setSize(new Dimension(500,400));
		Container contentPane = this.getContentPane();
		
		JPanel p = new JPanel();
		for(int i=0; i<BOARD_SIZE; i++){
			makeButtons(p, i+1+"");
		}
		p.setLayout(new GridLayout(3,3));
		contentPane.add(p);
	}
	
	private void makeButtons(JPanel p, String name){
		JButton emptyTiles = new JButton();
		p.add(emptyTiles);
		tileButtons.add(emptyTiles); // add tiles reference to a list
		
		ButtonAction action = new ButtonAction(name);
		emptyTiles.addActionListener(action);
	}
	
	private class ButtonAction implements ActionListener {
	    private String theLabel;
	    private String message;
	    private int[][] winnerTiles = {{1,2,3}, {4,5,6}, {7,8,9}, {1,4,7}, {2,5,8}, {3,6,9}, {1,5,9}, {3,5,7}};
	    
	    private ButtonAction(String name) {
	    	this.theLabel = name;
	    	this.message = null;
		}
	    
		@Override
		public void actionPerformed(ActionEvent e) {
			Object source = e.getSource();
			message = "Pressed Button " + theLabel;
			System.out.println(message);
			player1Move(source); // saves the player's move
			determineWinner(); // check if the player wins
			if(!isWinner){
				AIMove(); // no winner, AI makes a move
				determineWinner(); // checks if the AI wins
			}
		}
		
		private void player1Move(Object source){
			player1.addPlayerTiles(Integer.parseInt(theLabel)); // add the tile number to an array
			JButton btn = null;
			// set the selected tile to be "O" and disabled it
			if (source instanceof JButton) {
				btn = (JButton) source;
				btn.setText("O");
				btn.setEnabled(false);
			}
		}
		
		private void determineWinner() {
			String winner = checkTiles(); // check if there is any winner
			if(winner != null){
				alertGame(winner); // alert the winner
				isWinner = true;
			}else{
				// the game is draw if player 1 has made moves for 5 times and no winner
				if(player1.getPlayerTiles().size() == 5){
					alertGame("draw");
					isWinner = true;
				}else{
					// if there is no draw, AI can move
					isWinner = false;
				}
			}
		}
		
		private void AIMove() {
			int choice = ai.hardAI(player1.getPlayerTiles(), player2.getPlayerTiles());
			System.out.println("AI Choice: Button " + choice);
			JButton tile = (JButton) tileButtons.get(choice-1);
			tile.setText("X");
			tile.setEnabled(false);
			player2.addPlayerTiles(choice);
		}
		
		// Check the set of tiles of Player 1 and Player 2 by comparing them to the winner tiles.
		// if the set of tiles matches the winner tiles, that player wins.
		private String checkTiles() {
			System.out.println("Player 1 Moves: " + player1.getPlayerTiles().toString());
			System.out.println("Player 2 Moves: " + player2.getPlayerTiles().toString());
			for (int row=0; row < winnerTiles.length; row++)
			{
			    for (int col=0; col < winnerTiles[row].length; col++)
			    {
			        int value = winnerTiles[row][col];
			        if(player1.getPlayerTiles().contains(value)){
			        	player1.addWinCounter(); // determine the winner if the counter reaches 3 
			        	if(player1.getWinCounter()==3){
				        	return "Player 1";
				        }
			        }
			        
			        if(player2.getPlayerTiles().contains(value)){
			        	player2.addWinCounter();
			        	if(player2.getWinCounter()==3){
			        		return "Player 2";
				        }
			        }
			    }
			    player1.setWinCounter(0); player2.setWinCounter(0);
			}
			return null;
		}
		
		private void alertGame(String playerName) {
			String dialogTitle = "", bodyMsg = "";
			if(playerName.equals("draw")){
				dialogTitle = "Draw";
				bodyMsg = "The game has finished. It's a Draw. Restart the game? \nIf 'No' is selected, the program will exit.";
			}else{
				dialogTitle = playerName+" Won";
				bodyMsg = "The game has finished. " + playerName + " won. Restart the game? \nIf 'No' is selected, the program will exit.";
			}
			int n = JOptionPane.showConfirmDialog(null,
				    bodyMsg,
				    dialogTitle,
				    JOptionPane.YES_NO_OPTION);
			System.out.println("n: " + n);
			// "YES" option
			if(n==0){
				resetGame();
			}else if(n==1){ // "NO Option"
				System.exit(0);
			}
		}
		
		private void resetGame() {
			player1.resetValues(); 
			player2.resetValues();
			for(int i = 0; i < tileButtons.size(); i++){
				JButton tile = (JButton) tileButtons.get(i);
				tile.setText("");
				tile.setEnabled(true);
			}
		}
	    
	}
	
	public static void main(String[] args) {
		JFrame frm = new TicTacToe();
		frm.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frm.setVisible(true);
	}
	
}
