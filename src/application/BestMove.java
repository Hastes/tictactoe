package application;

public class BestMove {
	int score;
    Grid[] move;

	public BestMove(int score, Object best_move) {
		this.score = score;
		this.move = (Grid[]) best_move;
		// TODO Auto-generated constructor stub
	}
}
