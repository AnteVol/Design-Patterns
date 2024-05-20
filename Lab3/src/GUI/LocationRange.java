package GUI;

public class LocationRange {
	private Location begin;
	private Location end;
	
	public LocationRange(Location loc1, Location loc2) {
		if (loc1.getRow() < loc2.getRow() || (loc1.getRow() == loc2.getRow() && loc1.getColumn() < loc2.getColumn())) {
            this.begin = loc1;
            this.end = loc2;
        } else {
            this.begin = loc2;
            this.end = loc1;
        }
	}

	public Location getBegin() {
		return begin;
	}

	public void setBegin(Location begin) {
		this.begin = begin;
	}

	public Location getEnd() {
		return end;
	}

	public void setEnd(Location end) {
		this.end = end;
	}

	@Override
	public String toString() {
		return "LocationRange [begin=" + begin + ", end=" + end + "]";
	}
	
	
	
}
