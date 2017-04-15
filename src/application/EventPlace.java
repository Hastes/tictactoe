package application;

import java.util.ArrayList;
import java.util.Random;
import java.util.stream.IntStream;

import javax.swing.JOptionPane;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.SplitMenuButton;
import javafx.scene.effect.DropShadow;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
public class EventPlace  {

	@FXML
	private GridPane main_grid;
	@FXML
	private Button newgameBtn;
	@FXML
	private Label current_turn_label;
	@FXML
	private Label difficulty_label;
	@FXML
	private Label winlose_lb;
	@FXML
	private SplitMenuButton difficulty_select;

	public Grid[] tictacs;
	public PairBtnGrid[] all_buttons = new PairBtnGrid[9];;
	private boolean current_turn = TicTacToe.X;
	private boolean player_skin = TicTacToe.X;
	private boolean first_turn;
	private boolean game_over = true;
	public int X_score = 0;
	public int O_score = 0;

	private final int high = 2;
	private final int middle = 1;
	private final int low = 0;
	private int difficulty = high;

	public final int[][] same_places = {{0, 2, 6, 8}, {1, 3, 5, 7}};

	public void initNewGame(){
		tictacs = new Grid[9];
		first_turn = true;
		game_over = false;
		elementsConfigure();

		for (int i = 0; i < tictacs.length; i++) tictacs[i] = new Grid();
		all_buttons = new PairBtnGrid[9];
		int number_cell = 0;
		for (int rowIndex = 0; rowIndex < main_grid.getRowConstraints().size(); rowIndex++) {
			for (int colIndex = 0; colIndex < main_grid.getColumnConstraints().size(); colIndex++) {
				main_grid.add(createCell(number_cell).button, colIndex, rowIndex);
				number_cell+=1;
			}
		}
		if(player_skin == TicTacToe.O){
			firstBotTurn();
		}
	}

	private void firstBotTurn() {
		int pos = 4;
		if(difficulty < high && new Random().nextBoolean())
			pos = randomTurn();
		else if(difficulty < middle) pos = randomTurn();

		tictacs[pos] = new Grid(TicTacToe.X);
		all_buttons[pos].button.setText(convert_to_sign(!player_skin));
		setStyleBtn(all_buttons[pos].button, !player_skin);
	}

	@FXML
	private void lowSetEvent(ActionEvent e){
		difficulty = low;
		scoreDrop();
		elementsConfigure();
	}

	@FXML
	private void middleSetEvent(ActionEvent e){

		difficulty = middle;
		scoreDrop();
		elementsConfigure();
	}

	@FXML
	private void highSetEvent(ActionEvent e){

		difficulty = high;
		scoreDrop();
		elementsConfigure();
	}
	private void scoreDrop(){
		X_score = 0;
		O_score = 0;
		scoreVisualUpd();
	}

	private void elementsConfigure() {
		currentTurnLabelUpdate();
		difficulty_label.setText(convert_to_sign(difficulty));
	}

	private void currentTurnLabelUpdate(){
		if(player_skin == TicTacToe.X) {
			current_turn_label.setText("X");
			current_turn_label.setTextFill(Color.web("red"));
		}
		else {
			current_turn_label.setText("O");
			current_turn_label.setTextFill(Color.web("#00beff"));
		}
	}
	@FXML
	private void changeStartTurn(MouseEvent e){
		if (!game_over) return;
		current_turn = !current_turn;
		player_skin = !player_skin;
		currentTurnLabelUpdate();
		scoreDrop();
	}

	private String convert_to_sign(boolean value){
		if(value) return "X";
		else return "O";
	}


	private String convert_to_sign(int value){
		if(value == 0) return "Легко";
		else if(value == 1) return "Средне";
		else return "Сложно";
	}


	private PairBtnGrid createCell(int number_cell) {
        PairBtnGrid btn= new PairBtnGrid(number_cell, new Button());
        all_buttons[number_cell] = btn;
        btn.button.setOnAction(e -> handleCellClicked(btn));
        return btn;
    }


	public void handleCellClicked(PairBtnGrid btn){
		// If cell is filled
		if (tictacs[btn.cell_number].cell) return;
		humanTurn(btn);
		botsTurn();
	}

	private void humanTurn(PairBtnGrid btn){
		if (game_over) return;
		// Human's turn
		all_buttons[btn.cell_number].button.setText(convert_to_sign(player_skin));
		setStyleBtn(all_buttons[btn.cell_number].button, player_skin);
		tictacs[btn.cell_number] = new Grid(current_turn);
		current_turn = !current_turn;
		if (winOrGridfullCheck(tictacs)){
			gameOver();
		}
	}

	private void botsTurn(){
		if (game_over) return;
		// Bot's turn
		BestMove mv = TicTacToe.minimax(tictacs, new Grid(current_turn));
		int position = getNewGridPos(tictacs, mv.move);

		if (first_turn && difficulty > low) {
			position = getEquivalentPosition(position);
			first_turn = false;
		}
		// 50% change do worse turn for low and middle, or 100% if difficulty is low.
		if (difficulty < high && new Random().nextBoolean() || difficulty == low)
			position = randomTurn();
		all_buttons[position].button.setText(convert_to_sign(!player_skin));
		setStyleBtn(all_buttons[position].button, !player_skin);
		tictacs[position] = new Grid(current_turn);
		current_turn = !current_turn;
		if (winOrGridfullCheck(tictacs)){
			gameOver();
		}
	}


	private void scoreCalc() {
		// TODO Auto-generated method stub
		if(current_turn == TicTacToe.X) {
			O_score++;
		}
		else X_score++;
		scoreVisualUpd();
	}

	private void setStyleBtn(Button button, boolean current_turn) {
		if (current_turn == TicTacToe.O)
			button.setId("cell-style-o");
		else button.setId("cell-style-x");
	}

	public int getNewGridPos(Grid[] original, Grid[] move){
		try {
			for (int i = 0; i < original.length; i++) {
				if(original[i].cell != move[i].cell) return i;
			}
		} catch (NullPointerException e) {
			return 0;
		}
		return 0;
	}

	private int getEquivalentPosition(int cell_number){
		// For high level
		if (difficulty == high) return cell_number;
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
		if (game_over) return true;
		if (TicTacToe.win(grid)){
			scoreCalc();
			for (int pos: TicTacToe.win_get_cells(grid)) {
				all_buttons[pos].button.setId("upper-cell");
			}
			return true;
		}
		if (TicTacToe.grid_full(grid)){
			return true;
		}
		return false;
	}


	@FXML
	private void newGameEvent(MouseEvent event){
		if (!game_over){
			if (player_skin == TicTacToe.X) O_score++;
			else X_score++;
			scoreVisualUpd();
			gameOver();
		}
		else newGame();
	}

	private void gameOver() {
		newgameBtn.setText("Новая игра");
		main_grid.setDisable(true);
		main_grid.setEffect(new DropShadow());
		current_turn_label.setDisable(false);
		difficulty_select.setDisable(false);
		game_over = true;
	}

	private void newGame() {
		main_grid.setDisable(false);
		for (int i = 0; i < all_buttons.length; i++)
			if (all_buttons[i] != null) main_grid.getChildren().remove(all_buttons[i].button);
		main_grid.setEffect(null);
		current_turn_label.setDisable(true);
		difficulty_select.setDisable(true);
		newgameBtn.setText("Сдаться");
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
	private void scoreVisualUpd(){
		winlose_lb.setText(X_score + "/" + O_score);
	}
}
