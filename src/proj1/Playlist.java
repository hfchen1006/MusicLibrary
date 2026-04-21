package proj1;
import java.util.*;

/**
 * Represents a user-created playlist containing multiple songs.
 * Provides functionality to add, remove, and check for songs in the playlist.
 */
public class Playlist implements Iterable<Song> {
	
	private String name; // Name of the playlist
	private ArrayList<Song> songs; // List of songs in the playlist
	
	/**
     * Constructor to initialize a Playlist object with a given name.
     * @param name The name of the playlist.
     */
	public Playlist(String name) {
		this.name = name;
		this.songs = new ArrayList<Song>();
	}
	
	public String getName() {
		return this.name;
	}
	
	public ArrayList<Song> getSongs() {
		return new ArrayList<>(this.songs);
	}
	
	public void sortByCount() {
		songs.sort(Comparator.comparing(Song::getCount).reversed());
	}
	
	/**
     * Adds a song to the playlist.
     * @param song The song to be added.
     */
	public void addSong(Song song) {
		this.songs.add(song);
	}
	
	public void addSongEnd(Song song) {
		this.songs.add(0, song);
	}
	
	/**
     * Removes a song from the playlist.
     * @param song The song to be removed.
     * @return True if the song was successfully removed, false otherwise.
     */
	public boolean removeSong(Song song) {
        return this.songs.remove(song);
    }
	
	public Song removeSong(int i) {
        return this.songs.remove(i);
    }
	
	/**
     * Checks if a song is in the playlist.
     * @param song The song to check.
     * @return True if the song is in the playlist, false otherwise.
     */
	public boolean containsSong(Song song) {
        return this.songs.contains(song);
    }
	
	/**
     * Shuffles the songs in the playlist.
     */
    public void shuffle() {
        Collections.shuffle(songs);
    }
    
    /**
     * Returns an iterator over elements of type Song.
     * @return an Iterator.
     */
    @Override
    public Iterator<Song> iterator() {
        return songs.iterator();
    }
}
