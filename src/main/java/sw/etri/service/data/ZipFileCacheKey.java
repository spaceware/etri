package sw.etri.service.data;

import java.util.Objects;

public class ZipFileCacheKey {
	private final String yyyymmdd;
	private final String type;
	private final String layerName;
	private final int level;
	private final int col;
	private final int row;
	
	public ZipFileCacheKey(String yyyymmdd, String type, String layerName, int level, int col, int row) {
		this.yyyymmdd = yyyymmdd;
		this.type = type;
		this.layerName = layerName;
		this.level = level;
		this.col = col;
		this.row = row;
	}
	
	public static ZipFileCacheKey of(String yyyymmdd, String type, String layerName, int level, int col, int row) {
		return new ZipFileCacheKey(yyyymmdd, type, layerName, level, col, row);
	}

	public String getYyyymmdd() { return this.yyyymmdd; }

	public String getType() { return this.type; }

	public String getLayerName() {
		return this.layerName;
	}
	
	public int getLevel() {
		return this.level;
	}

	public int getCol() {
		return this.col;
	}

	public int getRow() {
		return this.row;
	}

	@Override
	public int hashCode() {
		return Objects.hash(type, layerName, level, col, row);
	}
	
	@Override
	public boolean equals(Object o) {
        if (o == this) return true;
        if (!(o instanceof ZipFileCacheKey)) {
            return false;
        }
        ZipFileCacheKey other = (ZipFileCacheKey) o;
        return 	level == other.level && row == other.row && 
        		col == other.col && Objects.equals(yyyymmdd, other.yyyymmdd)
				&& Objects.equals(type, other.type) &&
				Objects.equals(layerName, other.layerName);
	}


	public String toFilePath() {
		return this.yyyymmdd + "/" + this.type + "/" + this.layerName;
	}

	
	@Override
	public String toString() {
		return this.yyyymmdd + "/" + this.type + "/" + this.layerName + "/" +
				this.level + "/" + this.col + "/" + this.row;
	}
}
