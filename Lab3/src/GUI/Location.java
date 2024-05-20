package GUI;

public class Location {
	private int row;
	private int column;
	
	public Location(int row, int column) {
		this.row = row;
		this.column = column;
	}
	
	public void printLoaction() {
		System.out.println("Row: " + this.row + ", column: " + this.column);
	}

	public int getRow() {
		return row;
	}

	public void setRow(int row) {
		this.row = row;
	}

	public int getColumn() {
		return column;
	}

	public void setColumn(int column) {
		this.column = column;
	}

	@Override
	public String toString() {
		return "Location [row=" + row + ", column=" + column + "]";
	}
	
	
}
