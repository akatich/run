package tich.run.model;

public class Song {

    private int id;
    private String title;
    private String artist;
    private int speed;
    public final static int UNDEFINED = 0;
    public final static int SLOW = 1;
    public final static int MEDIUM = 2;
    public final static int FAST = 3;

    public Song(int songId, String songTitle, String songArtist) {
        id=songId;
        title=songTitle;
        artist=songArtist;
        speed = UNDEFINED;
    }

    public Song(int songId, String songTitle, String songArtist, int songSpeed) {
        id=songId;
        title=songTitle;
        artist=songArtist;
        speed = songSpeed;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getArtist() {
        return artist;
    }

    public void setArtist(String artist) {
        this.artist = artist;
    }

    public int getSpeed() {
        return speed;
    }

    public void setSpeed(int speed) {
        this.speed = speed;
    }
}
