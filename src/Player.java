import java.util.ArrayList;


public class Player {
	private int playerID;
	private ArrayList<Integer> playerTiles = new ArrayList<Integer>();
	private int winCounter;
	private boolean playerMove;
	
	public Player(int id) {
		this.playerID = id;
		this.setWinCounter(0);
	}

	public int getWinCounter() {
		return winCounter;
	}
	
	public void addWinCounter() {
		this.winCounter++;
	}

	public void setWinCounter(int num) {
		this.winCounter = num;
	}

	public ArrayList<Integer> getPlayerTiles() {
		return playerTiles;
	}

	public void addPlayerTiles(int tile) {
		this.playerTiles.add(tile);
	}

	public int getPlayerID() {
		return playerID;
	}

	public boolean isPlayerMove() {
		return playerMove;
	}

	public void setPlayerMove(boolean playerMove) {
		this.playerMove = playerMove;
	}
	
	public void resetValues() {
		setWinCounter(0);
		playerTiles.clear();
	}
	
}
