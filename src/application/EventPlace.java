package application;

import java.util.ArrayList;
import java.util.Random;
import java.util.stream.IntStream;

import javax.swing.JOptionPane;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
public class EventPlace  {

	@FXML
	private GridPane main_grid;
	public Grid[] tictacs;
	public PairBtnGrid[] all_buttons;
	public boolean current_turn = TicTacToe.X;
	private boolean first_turn;
	private boolean game_over;
	private int difficulty;
	private final int high = 2;
	private final int middle = 1;
	private final int low = 0;

	public final int[][] same_places = {{0, 2, 6, 8}, {1, 3, 5, 7}};

	public void initNewGame(){
		tictacs = new Grid[9];
		first_turn = true;
		game_over = false;
		difficulty = high;
		for (int i = 0; i < tictacs.length; i++) tictacs[i] = new Grid();
		all_buttons = new PairBtnGrid[9];
		int number_cell = 0;
		for (int rowIndex = 0; rowIndex < main_grid.getRowConstraints().size(); rowIndex++) {
			for (int colIndex = 0; colIndex < main_grid.getColumnConstraints().size(); colIndex++) {
				main_grid.add(createCell(number_cell).button, colIndex, rowIndex);
				number_cell+=1;
			}
		}
	}

	private PairBtnGrid createCell(int number_cell) {
        PairBtnGrid btn= new PairBtnGrid(number_cell, new Button());
        all_buttons[number_cell] = btn;
        btn.button.setOnAction(e -> handleCellClicked(btn));
        return btn;
    }


	public void handleCellClicked(PairBtnGrid btn){
		if (game_over) btn.button.setText("Game OVER");
		// If cell is filled
		if (tictacs[btn.cell_number].cell) return;

		// Human's turn
		all_buttons[btn.cell_number].button.setText(""+ current_turn);
		tictacs[btn.cell_number] = new Grid(current_turn);

		if (winOrGridfullCheck(tictacs)){
			game_over = true;
			return;
		}

		// Bot's turn
		BestMove mv = TicTacToe.minimax(tictacs, new Grid(!current_turn));
		int position = getNewGridPos(tictacs, mv.move);

		if (first_turn && difficulty > low) {
			position = getEquivalentPosition(position);
			first_turn = false;
		}
		// 50% change do worse turn for low and middle, or 100% if difficulty is low.
		if (difficulty < high && new Random().nextBoolean() || difficulty == low)
			position = randomTurn();
		all_buttons[position].button.setText(""+tictacs[position].value);
		tictacs[position] = new Grid(!current_turn);
		if (winOrGridfullCheck(tictacs))
			game_over = true;
	}

	public int getNewGridPos(Grid[] original, Grid[] move){
		for (int i = 0; i < original.length; i++) {
			if(original[i].cell != move[i].cell) return i;
		}
		return 0;
	}

	private int getEquivalentPosition(int cell_number){
		// For high level
		if (difficulty == high && cell_number == 7) return cell_number;
		for (int[] equivalent_numbers : same_places) {
			boolean is_contain = IntStream.of(equivalent_numbers).anyMatch(x -> x == cell_number);
			if (is_contain) {
				int idx = new Random().nextInt(equivalent_numbers.length);
				int rand_number = equivalent_numbers[idx];
				if (!tictacs[rand_number].cell) return rand_number; // if not filled
			}
		}
		return cell_number;
	}

	private int randomTurn(){
		ArrayList<Integer> empty_pos = new ArrayList<Integer>();
		for (int i = 0; i < tictacs.length; i++)
			if (!tictacs[i].cell) empty_pos.add(i);
		int index = new Random().nextInt(empty_pos.size());
        return empty_pos.get(index);
	}


	private boolean winOrGridfullCheck(Grid[] grid){
		if (TicTacToe.win(grid)){
			JOptionPane.showMessageDialog(null, "GAME OVER", "InfoBox: " + "", JOptionPane.INFORMATION_MESSAGE);
			return true;
		}
		if (TicTacToe.grid_full(grid)){
			JOptionPane.showMessageDialog(null, "Overflow", "InfoBox: " + "", JOptionPane.INFORMATION_MESSAGE);
			return true;
		}
		return false;
	}


	@FXML
	private void newGameEvent(MouseEvent event){
		main_grid.setDisable(false);
		main_grid.setEffect(null);
		initNewGame();
	}

	@FXML
	private void gridClicked(MouseEvent e){
		Node target = (Node)e.getSource() ;


		 if (target != main_grid) {
		        Node parent;
		        while ((parent = target.getParent()) != main_grid) {
		            target = parent;
		        }
		    }
		 Integer colIndex = GridPane.getColumnIndex(target);
	        System.out.println(colIndex);
	}

	@FXML
	private void exitBtnHandle(MouseEvent event){
		Platform.exit();
	}
}
