package application;

import java.util.ArrayList;
import java.util.List;


public class TicTacToe {
	public final static boolean O = false;
	public final static boolean X = true;

	public static boolean check_cell(int i, int j, Grid[] grid){
		if (grid[i+j] == null) return false;
		boolean condition1 = grid[i+j].cell;
		boolean condition2 = grid[i].value == grid[i + j].value;
		boolean res = condition1 && condition2;
		return res;
	}

	public static boolean win(Grid[] grid){
		boolean result = true;
		int[] cols = {0, 3, 6};
		int[][] diagonals = {{0, 4, 8},{2, 4, 6}};
		for(int i: cols) {
			result = true;
			for(int j = 0; j<=2; j++) {
				if(check_cell(i, j, grid)) continue;
				result = false;
			}
			if (result) return true;
		}
		for (int i = 0; i<=2; i++) {
			result = true;
			for (int j: cols) {
				if(check_cell(i, j, grid)) continue;
				result = false;
			}
			if (result) return true;
		}
		for (int[] i : diagonals) {
			result = true;
			if (grid[i[0]] == null){
				result = false; continue;
			}
			for (int j: i){
				if(grid[j]==null) {
					result = false;
					continue;
				}
				if(grid[j].cell && grid[i[0]].value == grid[j].value) continue;
				result = false;
			}
			if (result) return true;
		}
		return false;
	}

	// if all cells is done
	public static boolean grid_full(Grid[] grid){
		for(Grid obj: grid){
			if(obj == null) return false;
			if(!obj.cell) return false;
		}
		return true;
	}

	public static Grid[] move(Grid[] grid, int cell, Grid turn){
		grid[cell] = new Grid(turn);
		return grid;
	}

	public static List<Grid[]> moves(Grid[] grid, Grid turn){
		List<Grid[]> tree = new ArrayList<Grid[]>();
		for (int i = 0; i < grid.length; i++) {
			if (grid[i] == null || !grid[i].cell){
				tree.add(move(grid.clone(), i, turn));
			}
		}
		return tree;
	}

	public static BestMove minimax(Grid[] grid, Grid turn){
		if (win(grid)) {
			if (turn.value == O) return new BestMove(1, null);
			else return new BestMove(-1, null);
		}
		else if (grid_full(grid)) {
			return new BestMove(0, null);
		}
		else if (turn.value == X){                                  //for X
			BestMove best_values = new BestMove(-2, null);
			for (Grid[] move : moves(grid, new Grid(X))) {
				BestMove obj = minimax(move, new Grid(O));
				if (obj.score >= best_values.score){
					best_values.score = obj.score;
					best_values.move = move;
				}
			}
			return best_values;
		}
		else {                                                        //for O
			BestMove best_values = new BestMove(2, null);
			for (Grid[] move : moves(grid, new Grid(O))) {
				BestMove obj = minimax(move, new Grid(X));
				if (obj.score <= best_values.score){
					best_values.score = obj.score;
					best_values.move = move;
				}
			}
			return best_values;
		}
	}

}
