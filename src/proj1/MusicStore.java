package proj1;
import java.util.*;

/**
 * Represents a music store containing a collection of songs and albums.
 * Provides functionality to search for songs and albums based on title or artist.
 */
public class MusicStore {
	private ArrayList<Song> songs;
	private ArrayList<Album> albums;
	
	
    /**
     * Constructor that initializes the music store by processing album data.
     * @param albums The list of albums stored in a structured format.
     */
	public MusicStore (ArrayList<HashMap<String, ArrayList<String>>> albums) {
		this.songs = new ArrayList<>();  
        this.albums = new ArrayList<>(); 
		setVariables(albums);
	}
	
	
	/**
     * Processes and organizes albums and their songs into the store.
     * @param albums The structured data containing album and song information.
     */
	private void setVariables(ArrayList<HashMap<String, ArrayList<String>>> albums) {
		for (HashMap<String, ArrayList<String>> map : albums) {
            for (Map.Entry<String, ArrayList<String>> entry : map.entrySet()) {
            	String[] titleLine = entry.getKey().split(",");
            	String title = titleLine[0];
            	String artist = titleLine[1]; 
            	if (titleLine.length>2) {
                	String genre = titleLine[2];
                	String year = titleLine[3];
	            	ArrayList<String> songLines = entry.getValue();
	            	ArrayList<Song> albumSongs = new ArrayList<>();
	            	for (String songTitle: songLines) {
	            		Song newSong = new Song(songTitle, artist, genre, year, title);
	            		albumSongs.add(newSong);
	            		this.songs.add(newSong);
	            	}
	            	Album newAlbum = new Album(albumSongs, artist, title, genre, year);
	            	this.albums.add(newAlbum);
            	}
            }
		}
	}
	
	/**
     * Retrieves an album from the store by its title.
     * @param title The title of the album to retrieve.
     * @return The Album object if found, otherwise null.
     */
	public Album getAlbum(String title) {
		for (Album a: albums) {
			if (a.getTitle().equals(title)) {
				return a;
			}
		} 
		return null;
	}
	
	/**
     * Retrieves a song from the store by its title.
     * @param title The title of the song to retrieve.
     * @return The Song object if found, otherwise null.
     */
	public Song getSong(String title) {
		for (Song s: songs) {
			if (s.getTitle().equals(title)) {
				return s;
			}
		} 
		return null;
	}
	
	/**
     * Searches for albums by title or artist.
     * @param nameType Type of search: "T" for title, "A" for artist.
     * @param name The title or artist to search for.
     * @return A list of matching albums.
     */
	public ArrayList<Album> searchAlbum(String nameType, String name) {
		ArrayList<Album> albumsFound = new ArrayList<>();
		if (nameType.equals("T")) {
			for (Album a: albums) {
				if (a.getTitle().equals(name)) {
					albumsFound.add(a);
				}
			}
		} else if (nameType.equals("A")) {
			for (Album a: albums) {
				if (a.getArtist().equals(name)) {
					albumsFound.add(a);
				}
			}
		} else {
			return null;
		}
		return albumsFound;
	}
	
	/**
     * Searches for songs by title or artist.
     * @param nameType Type of search: "T" for title, "A" for artist.
     * @param name The title or artist to search for.
     * @return A list of matching songs.
     */
	public ArrayList<Song> searchSong(String nameType, String name) {
		ArrayList<Song> songsFound = new ArrayList<>();
		if (nameType.equals("T")) {
			for (Song s: songs) {
				if (s.getTitle().equals(name)) {
					songsFound.add(s);
				}
			}
		} else if (nameType.equals("A")) {
			for (Song s: songs) {
				if (s.getArtist().equals(name)) {
					songsFound.add(s);
				}
			}
		} else {
			return null;
		}
		return songsFound;
	}
	
}
