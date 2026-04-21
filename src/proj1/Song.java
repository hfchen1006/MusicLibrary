package proj1;

/**
 * Represents a song with a title, artist, and an optional rating.
 */
public class Song{
	
	private String title; // Title of the song
	private String artist; // Artist of the song
	private Rating rating; // Rating of the song 
	private String genre;
	private String year;
	private String album;
	private int playCount;
	
	/**
     * Constructor to initialize a Song object with a title and an artist.
     * The rating is initially set to null.
     * @param title The title of the song.
     * @param artist The artist of the song.
     */
	public Song(String title, String artist, String genre, String year, String album) {
		this.title = title;
		this.artist = artist;
		this.genre = genre;
		this.year = year;
		this.album = album;
		this.rating = null;
		this.playCount = 0;
	} 
	
	public String getTitle() {
        return this.title;
    }
	
	public void setRating(Rating rating) {
		this.rating = rating;
	}
	
	public String getArtist() {
        return this.artist;
    }
	
	public String getAlbum() {
        return this.album;
    }
	
	public String getGenre() {
        return this.genre;
    }
	
	public String getYear() {
        return this.year;
    }

	public Rating getRating() {
		return this.rating;
	}
	
	public int getCount() {
		return this.playCount;
	}
	
	public void play() {
		this.playCount += 1;
	}
	
}
