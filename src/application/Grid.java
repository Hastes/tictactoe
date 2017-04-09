package application;


public class Grid {
	boolean cell = false; // cell is filled
	boolean value = false; // default "O"

	public Grid(){}
	public Grid(boolean value){
		this.cell = true;
		this.value = value;
	}
	public Grid(Grid obj){    // Clone
		this.cell = obj.cell;
		this.value = obj.value;
	}
}
