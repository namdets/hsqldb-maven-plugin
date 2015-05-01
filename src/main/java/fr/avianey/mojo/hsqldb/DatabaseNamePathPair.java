package fr.avianey.mojo.hsqldb;

public class DatabaseNamePathPair {
	private String name;
	private String path;

    public DatabaseNamePathPair(){
        super();
    }

	public DatabaseNamePathPair(String name, String path) {
		super();
		this.name = name;
		this.path = path;
	}

	public String getPath() {
		return path;
	}
	public void setPath(String path) {
		this.path = path;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
}
