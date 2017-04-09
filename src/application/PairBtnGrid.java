package application;

import javax.swing.JOptionPane;

import javafx.event.ActionEvent;
import javafx.scene.control.Button;

public class PairBtnGrid {
  public final int cell_number;
  public final Button button;
  

  public PairBtnGrid(int cell_number, Button button) {
    this.cell_number = cell_number;
    this.button = button;
    button.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
  }

}
