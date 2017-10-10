import java.util.*;

// This class is meant for testing the TicTacToe
// The main classes are TicTacToe.java, AI.java, and Player.java
public class Data {
	private static final int[][] WINNER_TILES = {{1,2,3}, {4,5,6}, {7,8,9}, {1,4,7}, {2,5,8}, {3,6,9}, {1,5,9}, {3,5,7}};
	private static List<List<Integer>> winTiles = new ArrayList<List<Integer>>();
	 
	public Data(){
		 
	}
	 
	/*public static void main(String[] args) {
		ArrayList<Integer> playerArray = new ArrayList<Integer>();
		//playerArray.add(1);
		//playerArray.add(2);
		//playerArray.add(5);
		playerArray.add(1); 
		playerArray.add(6);
		
		ArrayList<Integer> AIArray = new ArrayList<Integer>();
		//AIArray.add(4);
		//AIArray.add(3);
		AIArray.add(5);
		//AIArray.add(2);
		
		winTiles.add(Arrays.asList(1,2,3));
		winTiles.add(Arrays.asList(4,5,6));
		winTiles.add(Arrays.asList(7,8,9));
		winTiles.add(Arrays.asList(1,4,7));
		winTiles.add(Arrays.asList(2,5,8));
		winTiles.add(Arrays.asList(3,6,9));
		winTiles.add(Arrays.asList(1,5,9));
		winTiles.add(Arrays.asList(3,5,7));
		
		chooseRandomPossibleTiles(playerArray, AIArray);
	}*/
	
	private static ArrayList<Integer> AIMove(ArrayList<Integer> playerMoves) {
		System.out.println("--- AI MOVE: BLOCK WIN TILE ---");
		ArrayList<Integer> possibleMoves = new ArrayList<Integer>();
		ArrayList<Integer> playerMovesClone = new ArrayList<Integer>();
		
		int winCounter = 0;
		for (int row=0; row < winTiles.size(); row++) {
			playerMovesClone.addAll(winTiles.get(row));
			System.out.println("ROW: " + playerMovesClone.toString());
		    for (int col=0; col < winTiles.get(row).size(); col++) {
		        int value = winTiles.get(row).get(col); // get the individual value of WIN_TILES
		        if(playerMoves.contains(value)) {
		        	winCounter++;
		        	System.out.println("winCounter: " + winCounter);
		        	if(winCounter==2){ // if the player's next move can make a WIN
		        		playerMovesClone.removeAll(playerMoves);
		        		int possibleTile = playerMovesClone.get(0);
		        		possibleMoves.add(possibleTile); // get the tile that will block the WIN
		        		break;
			        }
		        }
		    }
		    winCounter = 0; playerMovesClone.clear();
		}
		System.out.println("DONE");
		
		System.out.println("Possible moves: " + possibleMoves.toString());
		return possibleMoves;
	}
	
	private static ArrayList<Integer> goForTheWinAI(ArrayList<Integer> playerMoves, ArrayList<Integer> AIArray){
		System.out.println("--- GO FOR THE WIN ---");
		ArrayList<Integer> possibleMoves = new ArrayList<Integer>();
		List<List<Integer>> tilesForWin = new ArrayList<List<Integer>>();
		ArrayList<Integer> winnerTilesClone = new ArrayList<Integer>();
		boolean isWinningTile = false;
		
		int winCounter = 0;
		System.out.println("AIArray: " + AIArray.toString());
		
		// Check if AI can make a "WIN" move
		for (int row=0; row < winTiles.size(); row++) {
			winnerTilesClone.addAll(winTiles.get(row));
			System.out.println("ROW: " + winnerTilesClone.toString());
			for (int col=0; col < winTiles.get(row).size(); col++) {
		        int value = winTiles.get(row).get(col);
		        if(AIArray.contains(value)) {
		        	winCounter++;
		        	isWinningTile = true;
		        	System.out.println("winCounter: " + winCounter);
		        	if(winCounter==2){
		        		winnerTilesClone.removeAll(AIArray);
		        		int possibleTile = winnerTilesClone.get(0);
		        		if(!playerMoves.contains(possibleTile)){
		        			possibleMoves.add(possibleTile);
		        		}
		        		break;
			        }
		        }
		    }
			
			if(isWinningTile){
				tilesForWin.add(winTiles.get(row));
				isWinningTile = false;
			}
				
		    winCounter = 0; winnerTilesClone.clear();
		}
		
		int selectedTile; 
		List<Integer> selectedPossibleTiles = new ArrayList<Integer>();
		if(possibleMoves.isEmpty()){
			// If AI can't make the next move to WIN
			// then select a tile that can lead to a WIN
			System.out.println("AI can't win on the next move..");
			System.out.println("tilesForWin: " + tilesForWin.toString());
			Random r = new Random();
			int randChoice = 0;
			outerloop:
			while(true){
				randChoice = r.nextInt(tilesForWin.size());
				selectedPossibleTiles.addAll(tilesForWin.get(randChoice));
				System.out.println("selectedPossibleTiles: " + selectedPossibleTiles.toString());
				while(true){
					// select randomly by its index
					randChoice = r.nextInt(selectedPossibleTiles.size()); 
					selectedTile = selectedPossibleTiles.get(randChoice);
					System.out.println("selectedTile: " + selectedTile);
					if(!playerMoves.contains(selectedTile) && !AIArray.contains(selectedTile)){
						possibleMoves.add(selectedTile);
						break outerloop;
					}else {
						selectedPossibleTiles.remove(randChoice);
					}
				}
			}
			
		}
		System.out.println("DONE");
		
		System.out.println("Possible moves: " + possibleMoves.toString());
		return possibleMoves;
	}
	
	private static void chooseRandomPossibleTiles(ArrayList<Integer> playerMoves, ArrayList<Integer> AIArray) {
		System.out.println("--- Random Possible Tiles ---");
		ArrayList<Integer> possibleTiles;
		Random r = new Random();
		int randChoice = 0, selectedTile = 0;
		possibleTiles = goForTheWinAI(playerMoves, AIArray);
		if(possibleTiles.size()==1){
			selectedTile = possibleTiles.get(0);
		}else if(possibleTiles.size()==0){	
			possibleTiles = AIMove(playerMoves);
			if(possibleTiles.isEmpty()){
				// If the AI can't make a move that will prevent player to win
				// then choose a random tile
				selectedTile = chooseTile(playerMoves, AIArray);
			}else{
				System.out.println("--- RANDOMLY CHOOSE ---");
				while(true){
					randChoice = r.nextInt(possibleTiles.size());
					System.out.println("RANDCHOICE: " + randChoice);
					selectedTile = possibleTiles.get(randChoice);
					if(!AIArray.contains(selectedTile)){
						break;
					}else{
						possibleTiles.remove(randChoice); // remove non-empty tile
					}
				}
			}
		}
		System.out.println("possibleTiles: " + possibleTiles.toString());
		System.out.println("selectedTile: " + selectedTile);
	}
	
	private static int chooseTile(ArrayList<Integer> playerMoves, ArrayList<Integer> AIArray) {
		System.out.println("--- CHOOSE TILE ---");
		Random r = new Random();
		int randChoice = 0;
		while(true){
			randChoice = r.nextInt(9)+1;
			if(!playerMoves.contains(randChoice) && !AIArray.contains(randChoice))
				break;
		}
		return randChoice;
	}
}
