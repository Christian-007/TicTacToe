import java.util.*;

public class AI {
	private final static int MAX_TILES = 9; // total number of tiles in the tic tac toe board
	private final int[][] WINNER_TILES = {{1,2,3}, {4,5,6}, {7,8,9}, {1,4,7}, {2,5,8}, {3,6,9}, {1,5,9}, {3,5,7}};
	private List<List<Integer>> winTiles = new ArrayList<List<Integer>>();
	private boolean aboutToWin = false;
	private boolean isSubset;
	
	public AI(){
		getWinTiles().add(Arrays.asList(1,2,3));
		getWinTiles().add(Arrays.asList(4,5,6));
		getWinTiles().add(Arrays.asList(7,8,9));
		getWinTiles().add(Arrays.asList(1,4,7));
		getWinTiles().add(Arrays.asList(2,5,8));
		getWinTiles().add(Arrays.asList(3,6,9));
		getWinTiles().add(Arrays.asList(1,5,9));
		getWinTiles().add(Arrays.asList(3,5,7));
	}
	
	// Randomly choose any tile on the tictactoe board
	public int chooseRandomTile(ArrayList<Integer> player1Moves, ArrayList<Integer> player2Moves){
		Random r = new Random();
		int randChoice = 0;
		while(true){
			randChoice = r.nextInt(getMaxTiles())+1;
			if(!player1Moves.contains(randChoice) && !player2Moves.contains(randChoice))
				break;
		}
		return randChoice;
	}
	
	// AI will try to block the tile that can get the player to WIN
	private ArrayList<Integer> AIMove(ArrayList<Integer> playerMoves, ArrayList<Integer> AIArray) {
		ArrayList<Integer> possibleMoves = new ArrayList<Integer>();
		ArrayList<Integer> winnerTilesClone = new ArrayList<Integer>();
		
		int winCounter = 0;
		for (int row=0; row < this.getWinTiles().size(); row++) {
			winnerTilesClone.addAll(this.getWinTiles().get(row));
			System.out.println("ROW: " + winnerTilesClone.toString());
		    for (int col=0; col < this.getWinTiles().get(row).size(); col++) {
		        int value = this.getWinTiles().get(row).get(col);
		        if(playerMoves.contains(value)) {
		        	winCounter++;
		        	System.out.println("winCounter: " + winCounter);
		        	if(winCounter==2){
		        		winnerTilesClone.removeAll(playerMoves);
		        		int possibleTile = winnerTilesClone.get(0);
		        		if(!AIArray.contains(possibleTile)){ // check if AI doesn't have the value
		        			possibleMoves.add(possibleTile);
		        			break;
		        		}
			        }
		        }
		    }
		    winCounter = 0; winnerTilesClone.clear();
		    System.out.println("BREAKING");
		}
		System.out.println("DONE");
		
		System.out.println("Possible moves: " + possibleMoves.toString());
		return possibleMoves;
	}
	
	// AI will select the tile that can lead to WIN on the next move
	// if the next move can't make the AI WIN, then it selects a tile that can lead to a WIN
	private ArrayList<Integer> goForTheWinAI(ArrayList<Integer> playerMoves, ArrayList<Integer> AIArray){
		System.out.println("--- GO FOR THE WIN ---");
		ArrayList<Integer> possibleMoves = new ArrayList<Integer>();
		List<List<Integer>> tilesForWin = new ArrayList<List<Integer>>();
		ArrayList<Integer> winnerTilesClone = new ArrayList<Integer>();
		boolean isWinningTile = false;
		
		int winCounter = 0;
		System.out.println("AIArray: " + AIArray.toString());
		
		// Check if AI can make a "WIN" move
		for (int row=0; row < this.getWinTiles().size(); row++) {
			winnerTilesClone.addAll(this.getWinTiles().get(row));
			System.out.println("ROW: " + winnerTilesClone.toString());
			for (int col=0; col < this.getWinTiles().get(row).size(); col++) {
		        int value = this.getWinTiles().get(row).get(col);
		        if(AIArray.contains(value)) {
		        	winCounter++;
		        	isWinningTile = true;
		        	System.out.println("winCounter: " + winCounter);
		        	if(winCounter==2){
		        		isWinningTile = false;
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
				tilesForWin.add(this.getWinTiles().get(row));
				isWinningTile = false;
			}
				
		    winCounter = 0; winnerTilesClone.clear();
		}
		
		// If AI can't make the next move to WIN
		// then select a tile that can lead to a WIN
		int selectedTile; 
		List<Integer> selectedPossibleTiles = new ArrayList<Integer>();
		if(possibleMoves.isEmpty()){
			possibleMoves = AIMove(playerMoves, AIArray);
			if(possibleMoves.isEmpty()){
				System.out.println("AI can't win on the next move..");
				System.out.println("tilesForWin: " + tilesForWin.toString());
				if(!tilesForWin.isEmpty()){
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
								if(selectedPossibleTiles.isEmpty()){
									break;
								}
							}
						}
					}
				}
			}
		}
		System.out.println("DONE");
		
		System.out.println("Possible moves: " + possibleMoves.toString());
		return possibleMoves;
	}
	
	// this is the prioritisation of moves for the AI
	// 1st: AI will try to go for a "WIN" tile 
	// 2nd: AI will block a "WIN" tile for the player 
	// 3rd: if there' no tile to block and can't win on the next move
	// then, it will select a tile that can lead to a WIN
	// 4th: otherwise, just randomly select a tile
	public int hardAI(ArrayList<Integer> playerMoves, ArrayList<Integer> AIArray){
		ArrayList<Integer> possibleTiles;
		Random r = new Random();
		int randChoice = 0, selectedTile = 0;
		possibleTiles = goForTheWinAI(playerMoves, AIArray);
		//possibleTiles = AIMove(playerMoves, AIArray);
		if(possibleTiles.size()==1){
			selectedTile = possibleTiles.get(0);
		}else if(possibleTiles.size()==0){	
			//possibleTiles = goForTheWinAI(playerMoves, AIArray);
			possibleTiles = AIMove(playerMoves, AIArray);
			if(possibleTiles.isEmpty()){
				// If the AI can't make a "WIN" move
				// then choose a random tile
				selectedTile = chooseRandomTile(playerMoves, AIArray);
			}else{
				// Choose the "WIN" tile
				selectedTile = possibleTiles.get(0);
			}
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
		System.out.println("possibleTiles: " + possibleTiles.toString());
		System.out.println("selectedTile: " + selectedTile);
		return selectedTile;
		
		
	}

	public int getMaxTiles() {
		return MAX_TILES;
	}

	public int[][] getWinnerTiles() {
		return WINNER_TILES;
	}

	public boolean isAboutToWin() {
		return aboutToWin;
	}

	public void setAboutToWin(boolean aboutToWin) {
		this.aboutToWin = aboutToWin;
	}

	public boolean isSubset() {
		return isSubset;
	}

	public void setSubset(boolean isSubset) {
		this.isSubset = isSubset;
	}

	public List<List<Integer>> getWinTiles() {
		return winTiles;
	}

	public void setWinTiles(List<List<Integer>> winTiles) {
		this.winTiles = winTiles;
	}

}
