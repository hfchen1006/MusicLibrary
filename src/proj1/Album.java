package proj1;

import java.util.*;

/**
 * Represents an Album containing multiple songs. Each album has a title, an
 * artist, and a list of songs.
 */
public class Album {
	private ArrayList<Song> songs; // List of songs in the album
	private String artist; // Artist of the album
	private String title; // Title of the album
	private String genre; // Genre of the album
	private String year; // Release year of the album

	/**
	 * Constructor to initialize an Album object with details.
	 * 
	 * @param songs  The list of songs in the album.
	 * @param artist The artist of the album.
	 * @param title  The title of the album.
	 * @param genre  The genre of the album.
	 * @param year   The release year of the album.
	 */
	public Album(ArrayList<Song> songs, String artist, String title, String genre, String year) {
		this.songs = songs;
		this.artist = artist;
		this.title = title;
		this.genre = genre;
		this.year = year;
	}

	public String getArtist() {
		return artist;
	}

	public String getTitle() {
		return title;
	}

	public String getGenre() {
		return genre;
	}

	public String getYear() {
		return year;
	}

	public ArrayList<Song> getSongs() {
		return new ArrayList<Song>(this.songs);
	}

	/**
	 * Adds a song to the album if it is not already included.
	 * 
	 * @param song The song to be added.
	 */
	public void addSong(Song song) {
		if (!songs.contains(song)) {
			songs.add(song);
		}
	}
}
