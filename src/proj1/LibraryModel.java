package proj1;

import java.util.*;

/**
 * Manages the user's music library, including favorite songs, playlists,
 * albums, and individual songs. Provides functionality to search, add, and
 * remove songs and albums.
 */
public class LibraryModel implements Iterable<Song> {
	private ArrayList<Song> favorites; // List of user's favorite songs
	private ArrayList<Playlist> playlists; // List of user's playlists
	private ArrayList<Album> albums; // List of albums in the library
	private ArrayList<Song> songs; // List of all songs in the library
	private Playlist recent; // Playlist for most recently played songs
	private Playlist top; // Playlist for top 10 played songs
	private Playlist favoritesPL; // Playlist for favorite songs

	/**
	 * Constructor to initialize the LibraryModel with empty lists for songs,
	 * albums, and playlists.
	 */
	public LibraryModel() {
		this.favorites = new ArrayList<Song>();
		this.playlists = new ArrayList<Playlist>();
		this.albums = new ArrayList<Album>();
		this.songs = new ArrayList<Song>();
		this.recent = new Playlist("Most Recently Played Songs");
		this.top = new Playlist("Top 10 Played Songs");
		this.favoritesPL = new Playlist("Favorite Songs");
		playlists.add(recent);
		playlists.add(top);
		playlists.add(favoritesPL);
	}

	public ArrayList<Album> getAlbums() {
		return new ArrayList<>(this.albums);
	}

	public ArrayList<Song> getSongs() {
		return new ArrayList<>(this.songs);
	}

	public ArrayList<Song> getFavSongs() {
		return new ArrayList<>(this.favorites);
	}

	public ArrayList<Playlist> getPlaylists() {
		return new ArrayList<>(this.playlists);
	}

	public ArrayList<String> getArtists() {
		ArrayList<String> artists = new ArrayList<>();
		for (Album album : getAlbums()) {
			if (!artists.contains(album.getArtist())) {
				artists.add(album.getArtist());
			}
		}
		return artists;
	}

	/**
	 * Creates a new playlist with the given name.
	 * 
	 * @param name The name of the new playlist.
	 * @return True if the playlist was created successfully, false if a playlist
	 *         with the same name exists.
	 */
	public boolean createPlaylist(String name) {
		for (Playlist playlist : getPlaylists()) {
			if (playlist.getName().equals(name)) {
				return false;
			}
		}

		Playlist newPlaylist = new Playlist(name);
		playlists.add(newPlaylist);
		return true;
	}

	/**
	 * Searches for a playlist by name.
	 * 
	 * @param name The name of the playlist.
	 * @return The Playlist object if found, otherwise null.
	 */
	public Playlist searchPlaylist(String name) {
		for (Playlist playlist : getPlaylists()) {
			if (playlist.getName().equals(name)) {
				return playlist;
			}
		}
		System.out.println("Playlist not found: " + name);
		return null;
	}

	/**
	 * Searches for a song by title.
	 * 
	 * @param title The title of the song.
	 * @return The Song object if found, otherwise null.
	 */
	public Song searchSong(String title) {
		for (Song song : getSongs()) {
			if (song.getTitle().equals(title)) {
				return song;
			}
		}
		System.out.println("Song not found: " + title);
		return null;
	}

	/**
	 * Searches for an album by title.
	 * 
	 * @param title The title of the album.
	 * @return The Album object if found, otherwise null.
	 */
	public Album searchAlbum(String title) {
		for (Album album : getAlbums()) {
			if (album.getTitle().equals(title)) {
				return album;
			}
		}
		System.out.println("Album not in library: " + title);
		return null;
	}

	/**
	 * Adds a song to a specified playlist.
	 * 
	 * @param name  The name of the playlist.
	 * @param title The title of the song to add.
	 * @return True if the song was added successfully, false otherwise.
	 */
	public boolean addSongToPlaylist(String name, String title) {
		Playlist playlist = searchPlaylist(name);
		Song song = searchSong(title);

		// Check if the playlist exists and the song is in the library
		if (playlist != null && song != null) {
			// Check if the song is already in the playlist
			for (Song s : playlist.getSongs()) {
				if (s.getTitle().equals(title)) {
					// Song is already in the playlist, don't add it again
					return false;
				}
			}
			// Song is not in the playlist, so add it
			playlist.addSong(song);
			return true;
		}
		return false;
	}

	/**
	 * Removes a song from a specified playlist.
	 * 
	 * @param name  The name of the playlist.
	 * @param title The title of the song to remove.
	 * @return True if the song was removed successfully, false otherwise.
	 */
	public boolean removeSongFromPlaylist(String name, String title) {
		Playlist playlist = searchPlaylist(name);

		// Check if playlist exists
		if (playlist != null) {
			// Check each song in the playlist for a match by title
			for (Song song : playlist.getSongs()) {
				if (song.getTitle().equals(title)) {
					// If found, remove it
					playlist.removeSong(song);
					return true;
				}
			}
		}
		return false;
	}

	/**
	 * Adds a song to the favorites list.
	 * 
	 * @param name The title of the song.
	 */
	public void addFavorite(String name) {
		Song song = searchSong(name);
		if (song != null) {
			favorites.add(song);
			favoritesPL.addSong(song);
		}
	}

	/**
	 * Assigns a rating to a song and adds it to favorites if it receives the
	 * highest rating.
	 * 
	 * @param title  The title of the song.
	 * @param rating The rating to be assigned.
	 */
	public void rate(String title, String rating) {
		Song song = searchSong(title);
		int newRating = Integer.parseInt(rating);
		Playlist topRated = searchPlaylist("Top Rated");
		// if new rating is less than 4
		if (newRating < 4) {
			if (topRated != null) {
				topRated.removeSong(song);
			}
		}
		// If rating is no longer 5
		if (newRating < 5) {
			if (favoritesPL != null) {
				favoritesPL.removeSong(song);
			}
		}
		if (song != null) {
			song.setRating(new Rating(newRating));
		}
		if (newRating == 5) {
			addFavorite(title);
		}
		topRatedPlaylist();
	}

	/**
	 * Adds a song to the library.
	 * 
	 * @param song The song to add.
	 */
	public void addSong(Song song) {
		songs.add(song);
		playlistsByGenre();
		Album album = searchAlbum(song.getAlbum());
		if (album == null) {
			// Create new album with this song if not found
			album = new Album(new ArrayList<>(Arrays.asList(song)), song.getArtist(), song.getAlbum(), song.getGenre(),
					song.getYear());
			albums.add(album);
			System.out.println("Added new album: " + song.getAlbum() + " with the song: " + song.getTitle());
		} else {
			// If the album is found and the song is not already in the album, add it
			if (!album.getSongs().contains(song)) {
				album.getSongs().add(song);
				System.out.println("Added song: " + song.getTitle() + " to existing album: " + song.getAlbum());
			}
		}
	}

	/**
	 * Adds an album to the library.
	 * 
	 * @param album The album to add.
	 */
	public void addAlbum(Album album) {
		albums.add(album);
		for (Song s : album.getSongs()) {
			if (!songs.contains(s)) {
				songs.add(s);
				playlistsByGenre();
			}
		}
	}

	/**
	 * Find the minimum play count in a list of songs
	 * 
	 * @param songs The list to search through
	 */
	public Song findMin(ArrayList<Song> songs) {
		int minCount = 0;
		Song minSong = songs.get(0);
		for (Song s : songs) {
			int curr = s.getCount();
			if (curr > minCount) {
				minCount = curr;
				minSong = s;
			}
		}
		return minSong;
	}

	/**
	 * Plays a song and maintains applicable playlists
	 * 
	 * @param song The song being played
	 */
	public void songPlay(Song song) {
		song.play();
		// maintains 10 most recently played songs
		if (recent.getSongs().size() < 10) {
			if (!recent.getSongs().contains(song)) {
				recent.addSongEnd(song);
			}
		} else {
			if (!recent.getSongs().contains(song)) {
				recent.removeSong(9);
				recent.addSongEnd(song);
			}
		}
		if (top.getSongs().size() < 10) {
			if (!top.getSongs().contains(song)) {
				top.addSong(song);
			}
			// removes song with least plays
		} else {
			if (!top.getSongs().contains(song)) {
				top.addSong(song);
				Song min = findMin(top.getSongs());
				top.removeSong(min);
			}
		}
		// sort by play count
		top.sortByCount();
	}

	/**
	 * Sorts the songs in the library by title in alphabetical order.
	 * 
	 * @return A new list of songs sorted by title.
	 */
	public ArrayList<Song> sortByTitle() {
		ArrayList<Song> sortedList = new ArrayList<>(this.songs);
		sortedList.sort(Comparator.comparing(Song::getTitle));
		return sortedList;
	}

	/**
	 * Sorts the songs in the library by artist name in alphabetical order.
	 * 
	 * @return A new list of songs sorted by artist name.
	 */
	public ArrayList<Song> sortByArtist() {
		ArrayList<Song> sortedList = new ArrayList<>(this.songs);
		sortedList.sort(Comparator.comparing(Song::getArtist));
		return sortedList;
	}

	/**
	 * Sorts the songs in the library by rating, from highest to lowest. Songs
	 * without a rating are considered to have the lowest possible rating.
	 * 
	 * @return A new list of songs sorted by their rating.
	 */
	public ArrayList<Song> sortByRating() {
		ArrayList<Song> sortedList = new ArrayList<>(this.songs);
		sortedList.sort(Comparator
				.comparing(song -> song.getRating() != null ? song.getRating().getRating() : Integer.MAX_VALUE));
		return sortedList;
	}

	/**
	 * Removes a song from the library.
	 * 
	 * @param title The title of the song to remove.
	 * @return True if the song was removed successfully, false otherwise.
	 */
	public boolean removeSong(String title) {
		Song song = searchSong(title);
		if (song != null) {
			songs.remove(song);
			System.out.println("Song removed: " + title);
			return true;
		}
		System.out.println("Song not found: " + title);
		return false;
	}

	/**
	 * Removes an album from the library.
	 * 
	 * @param title The title of the album to remove.
	 * @return True if the album was removed successfully, false otherwise.
	 */
	public boolean removeAlbum(String title) {
		Album album = searchAlbum(title);
		if (album != null) {
			albums.remove(album);
			System.out.println("Album removed: " + title);
			return true;
		} else {
			System.out.println("Album not found: " + title);
		}

		return false;
	}

	/**
	 * Shuffles the songs in the library.
	 */
	public void shuffleLibrary() {
		Collections.shuffle(songs);
	}

	/**
	 * Shuffles the songs in a specified playlist. If the playlist exists, the songs
	 * within it are rearranged in a random order.
	 * 
	 * @param playlistName The name of the playlist to be shuffled.
	 */
	public void shufflePlaylist(String playlistName) {
		Playlist playlist = searchPlaylist(playlistName);
		if (playlist != null) {
			playlist.shuffle(); 
		}
	}

	/**
	 * Returns an iterator over elements of type Song.
	 * 
	 * @return an Iterator.
	 */
	@Override
	public Iterator<Song> iterator() {
		return songs.iterator();
	}

	/**
	 * Displays detailed information about an album associated with a given song. If
	 * the album is found in the library, it prints the album's title, artist, year,
	 * genre, and lists its songs. If the album is not in the library, it notifies
	 * the user accordingly.
	 * 
	 * @param song The song whose album details are to be retrieved.
	 */
	public void getAlbumDetail(Song song) {
		Album album = searchAlbum(song.getAlbum());
		if (album != null) {
			System.out.println("Album: " + album.getTitle() + " by " + album.getArtist() + ", Year: " + album.getYear()
					+ ", Genre: " + album.getGenre());
			System.out.println("Album is already in your library.");
			for (Song s : album.getSongs()) {
				System.out.println("- " + s.getTitle());
			}
		} else {
			System.out.println("Album is not currently in your library.");
		}
	}

	/**
	 * Searches for songs by genre.
	 * 
	 * @param genre The genre to search for.
	 * @return A list of songs that match the genre.
	 */
	public ArrayList<Song> searchSongsByGenre(String genre) {
		ArrayList<Song> filteredSongs = new ArrayList<>();
		for (Song song : songs) {
			if (song.getGenre().equalsIgnoreCase(genre)) {
				filteredSongs.add(song);
			}
		}
		return filteredSongs;
	}

	/**
	 * Organizes songs into playlists based on their genre. Creates or updates
	 * playlists for each genre that has at least 10 songs. Each playlist is named
	 * after its genre followed by "Hits" (e.g., "Jazz Hits"). Prevents duplication
	 * of songs in the playlists.
	 */
	public void playlistsByGenre() {
		Map<String, List<Song>> genreMap = new HashMap<>();

		// Accumulate songs by genre in a map
		for (Song song : songs) {
			String genre = song.getGenre();
			genreMap.computeIfAbsent(genre, k -> new ArrayList<>()).add(song);
		}

		// Process each genre
		for (Map.Entry<String, List<Song>> entry : genreMap.entrySet()) {
			String genre = entry.getKey();
			List<Song> genreSongs = entry.getValue();
			String playlistName = genre + " Hits";

			// Only create/update a playlist if there are at least 10 songs in this genre
			if (genreSongs.size() >= 10) {
				Playlist genrePlaylist = null;

				// Check if there is already a playlist for this genre
				for (Playlist playlist : playlists) {
					if (playlist.getName().equals(playlistName)) {
						genrePlaylist = playlist;
						break;
					}
				}

				// If no playlist exists, create a new one
				if (genrePlaylist == null) {
					genrePlaylist = new Playlist(playlistName);
					playlists.add(genrePlaylist);
				}

				// Add songs to the playlist, avoiding duplicates
				for (Song song : genreSongs) {
					if (!genrePlaylist.getSongs().contains(song)) {
						genrePlaylist.addSong(song);
					}
				}
			}
		}
	}

	/**
	 * Creates/update a playlist for songs rated 4 or 5.
	 */
	public void topRatedPlaylist() {
		Playlist topRated = searchPlaylist("Top Rated");
		if (topRated == null) {
			topRated = new Playlist("Top Rated");
			playlists.add(topRated);
		} else {
			topRated.getSongs().removeIf(song -> song.getRating() == null
					|| (song.getRating().getRating() != 4 && song.getRating().getRating() != 5));
		}

		for (Song song : songs) {
			if (song.getRating() != null && (song.getRating().getRating() == 4 || song.getRating().getRating() == 5)
					&& !topRated.getSongs().contains(song)) {
				topRated.addSong(song);
			}
		}
	}

}
