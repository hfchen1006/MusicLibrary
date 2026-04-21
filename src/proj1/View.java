package proj1;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;

/**
 * Represents the user interface for interacting with the music library.
 * Provides functionalities for searching, managing playlists, listing items,
 * and rating songs.
 */
public class View {
	private MusicStore store; // Music store containing available songs and albums
	private LibraryModel model; // User's personal music library
	private Users users; // database of active User profiles

	/**
	 * Constructor to initialize the View with a music store and library model.
	 * 
	 * @param store The music store.
	 * @param model The user's music library model.
	 */
	public View(MusicStore store, LibraryModel model, Users users) {
		this.store = store;
		this.model = model;
		this.users = users;
	}

	/**
	 * Starts the user interface, allowing the user to navigate the music library.
	 * 
	 * @throws IOException
	 * @throws NoSuchAlgorithmException
	 * @throws InvalidKeySpecException
	 */
	private void operation() throws InvalidKeySpecException, NoSuchAlgorithmException, IOException {
		Scanner input = new Scanner(System.in);
		pageOne(input);
	}

	/**
	 * Displays the main menu and processes user commands.
	 * 
	 * @param input Scanner for reading user input.
	 * @throws IOException
	 * @throws NoSuchAlgorithmException
	 * @throws InvalidKeySpecException
	 */
	private void pageOne(Scanner input) throws InvalidKeySpecException, NoSuchAlgorithmException, IOException {
		String baseCommands = "COMMANDS:\n" + "- SEARCH: Search for songs, albums, artists, or playlists\n"
				+ "- PL: Manage playlists (create, add, remove songs)\n"
				+ "- REMOVE: Remove a song or album from your library\n"
				+ "- LIST: Get a list of items from your library\n"
				+ "- SHUFFLE: Shuffle your entire library or a specific playlist\n"
				+ "- SP: Search for a playlist by name\n" + "- LOGIN: Login or create an account\n"
				+ "- HELP: Repeat this list of commands\n" + "- EXIT: Exit the application";

		System.out.println("Hello! Welcome to the music library!");
		System.out.println(baseCommands);

		while (true) {
			String userCommand = input.nextLine().toUpperCase();
			switch (userCommand) {
			case "SEARCH":
				search(input);
				break;
			case "PL":
				addPlaylist(input);
				break;
			case "REMOVE":
				removeItems(input);
				break;
			case "LIST":
				listLibrary(input);
				break;
			case "SHUFFLE":
				shuffleOptions(input);
				break;
			case "SP":
				searchPlaylist(input);
				break;
			case "LOGIN":
				homeScreen();
				break;
			case "HELP":
				System.out.println(baseCommands);
				break;
			case "EXIT":
				System.out.println("Goodbye!");
				input.close();
				System.exit(0);
			default:
				System.out.println("Command not recognized. Please try again!");
				break;
			}
		}
	}

	/**
	 * Handles shuffle options for the music library or a specific playlist.
	 * 
	 * @param input Scanner for reading user input.
	 */
	private void shuffleOptions(Scanner input) {
		System.out.println("Do you want to shuffle (1) Entire Library or (2) A specific Playlist? Enter 1 or 2:");
		String choice = input.nextLine();
		switch (choice) {
		case "1":
			model.shuffleLibrary();
			System.out.println("Entire Library shuffled.");
			break;
		case "2":
			System.out.println("Enter the name of the playlist to shuffle:");
			String playlistName = input.nextLine();
			model.shufflePlaylist(playlistName);
			System.out.println("Playlist '" + playlistName + "' shuffled.");
			break;
		default:
			System.out.println("Invalid option. Please try again.");
			shuffleOptions(input);
			break;
		}
	}

	/**
	 * Handles the removal of songs or albums from the user's library.
	 * 
	 * @param input Scanner for reading user input.
	 * @throws IOException              If an I/O error occurs.
	 * @throws NoSuchAlgorithmException If the hashing algorithm is not available.
	 * @throws InvalidKeySpecException  If the key specification is invalid.
	 */
	private void removeItems(Scanner input) throws IOException, NoSuchAlgorithmException, InvalidKeySpecException {
		System.out.println("Would you like to remove a (1) song or (2) album? Enter 1 or 2:");
		String choice = input.nextLine();
		switch (choice) {
		case "1":
			removeSong(input);
			break;
		case "2":
			removeAlbum(input);
			break;
		default:
			System.out.println("Invalid option. Please try again.");
			removeItems(input);
			break;
		}
		pageOne(input);
	}

	/**
	 * Removes a song from the user's library based on the song title.
	 * 
	 * @param input Scanner for reading user input.
	 */
	private void removeSong(Scanner input) {
		System.out.print("Enter the title of the song to remove: ");
		String title = input.nextLine();
		if (model.removeSong(title)) {
			System.out.println("Song removed successfully.");
		} else {
			System.out.println("Song not found.");
		}
	}

	/**
	 * Removes an album from the user's library based on the album title.
	 * 
	 * @param input Scanner for reading user input.
	 */
	private void removeAlbum(Scanner input) {
		System.out.print("Enter the title of the album to remove: ");
		String title = input.nextLine();
		if (model.removeAlbum(title)) {
			System.out.println("Album removed successfully.");
		} else {
			System.out.println("Album not found.");
		}
	}

	/**
	 * Lists available items in the user's library based on user selection.
	 * 
	 * @param input Scanner for reading user input.
	 * @throws IOException
	 * @throws NoSuchAlgorithmException
	 * @throws InvalidKeySpecException
	 */
	private void listLibrary(Scanner input) throws InvalidKeySpecException, NoSuchAlgorithmException, IOException {
		String listCommands = "- SONGS: List all song titles\n" + "- ARTISTS: List all artist names\n"
				+ "- ALBUMS: List all album titles\n" + "- PLAYLISTS: List all playlist names\n"
				+ "- FAVORITES: List all favorite songs\n" + "- BACK: Go back to main menu";

		System.out.println("What would you like to list?");
		System.out.println(listCommands);

		String userCommand = input.nextLine().toUpperCase();
		switch (userCommand) {
		case "SONGS":
			listSongs(input);
			break;
		case "ARTISTS":
			listArtists();
			break;
		case "ALBUMS":
			listAlbums();
			break;
		case "PLAYLISTS":
			listPlaylists();
			break;
		case "FAVORITES":
			listFavorites();
			break;
		case "BACK":
			pageOne(input);
			return;
		default:
			System.out.println("Command not recognized. Please try again!");
			listLibrary(input);
			return;
		}

		// Ask if the user wants to view another list
		System.out.println("Would you like to view another list? (YES/NO)");
		String continueList = input.nextLine().toUpperCase();
		if (continueList.equals("YES")) {
			listLibrary(input);
		} else {
			pageOne(input);
		}
	}

	/**
	 * Lists all songs in the library.
	 * 
	 * @throws IOException
	 * @throws NoSuchAlgorithmException
	 * @throws InvalidKeySpecException
	 */
	private void listSongs(Scanner input) throws InvalidKeySpecException, NoSuchAlgorithmException, IOException {
		ArrayList<Song> songs = model.getSongs();
		if (songs.isEmpty()) {
			System.out.println("No songs found in your library.");
		} else {
			System.out.println("How would you like the list to be sorted?\n" + "- TITLE\n" + "- ARTISTS\n"
					+ "- RATING\n" + "- DEFAULT");
			String userCommand = input.nextLine().toUpperCase();
			ArrayList<Song> sortedList = new ArrayList<Song>();
			switch (userCommand) {
			case "TITLE":
				sortedList = model.sortByTitle();
				for (Song song : sortedList) {
					System.out.println("- " + song.getTitle());
				}
				break;
			case "ARTISTS":
				sortedList = model.sortByArtist();
				for (Song song : sortedList) {
					System.out.println("- " + song.getTitle() + " By " + song.getArtist());
				}
				break;
			case "RATING":
				sortedList = model.sortByRating();
				for (Song song : sortedList) {
					if (!(song.getRating() == null)) {
						if ((song.getRating().getRating() >= 1 && song.getRating().getRating() <= 5)) {
							System.out.println("- " + song.getTitle() + " " + song.getRating().getRating() + "/5");
						}
					}
				}
				break;
			default:
				System.out.println("Sorted by default");
				for (Song song : songs) {
					System.out.println("- " + song.getTitle());
				}
				return;
			}
			pageOne(input);
		}

	}

	private void listArtists() {
		ArrayList<String> artists = model.getArtists();
		if (artists.isEmpty()) {
			System.out.println("No artists found in your library.");
		} else {
			System.out.println("Artists in your library:");
			for (String artist : artists) {
				System.out.println("- " + artist);
			}
		}
	}

	private void listAlbums() {
		ArrayList<Album> albums = model.getAlbums();
		if (albums.isEmpty()) {
			System.out.println("No albums found in your library.");
		} else {
			System.out.println("Albums in your library:");
			for (Album album : albums) {
				System.out.println("- " + album.getTitle());
			}
		}
	}

	private void listPlaylists() {
		ArrayList<Playlist> playlists = model.getPlaylists();
		if (playlists.isEmpty()) {
			System.out.println("No playlists found in your library.");
		} else {
			System.out.println("Playlists in your library:");
			for (Playlist playlist : playlists) {
				System.out.println("- " + playlist.getName());
				for (Song s : playlist.getSongs()) {
					if (playlist.getName().equals("Top 10 Played Songs")) {
						System.out.println("     - " + s.getTitle() + " - " + s.getCount() + " time(s)");
					} else {
						System.out.println("     - " + s.getTitle());
					}
				}
			}
		}
	}

	private void listFavorites() {
		ArrayList<Song> favorites = model.getFavSongs();
		if (favorites.isEmpty()) {
			System.out.println("No favorite songs found.");
		} else {
			System.out.println("Favorite songs:");
			for (Song song : favorites) {
				System.out.println("- " + song.getTitle() + " by " + song.getArtist());
			}
		}
	}

	/**
	 * Handles playlist-related operations such as creating and removing songs from
	 * playlists.
	 * 
	 * @param input Scanner for reading user input.
	 * @throws IOException
	 * @throws NoSuchAlgorithmException
	 * @throws InvalidKeySpecException
	 */
	private void addPlaylist(Scanner input) throws InvalidKeySpecException, NoSuchAlgorithmException, IOException {
		String playlistCommands = "- CREATE: Create a playlist\n" + "- REMOVE: remove a song from a playlist\n"
				+ "- BACK: go back a screen";
		System.out.println(playlistCommands);
		while (true) {
			String userCommand = input.nextLine().toUpperCase();
			switch (userCommand) {
			case ("CREATE"):
				createPlaylist(input);
				break;
			case ("REMOVE"):
				pickPlaylist(input);
				break;
			case ("BACK"):
				pageOne(input);
				break;
			case ("HELP"):
				System.out.println(playlistCommands);
			default:
				System.out.println("Command not recognized. Please try again!");
			}
		}
	}

	/**
	 * Prompts the user to enter a playlist name and searches for the corresponding
	 * playlist.
	 * 
	 * @param input Scanner for reading user input.
	 * @throws IOException
	 * @throws NoSuchAlgorithmException
	 * @throws InvalidKeySpecException
	 */
	private void searchPlaylist(Scanner input) throws InvalidKeySpecException, NoSuchAlgorithmException, IOException {
		System.out.print("Enter the name of the playlist: ");
		String name = input.nextLine();
		Playlist playlist = model.searchPlaylist(name);

		if (playlist != null) {
			System.out.println("Playlist Found: " + playlist.getName());
			System.out.println("Songs in this playlist:");
			for (Song song : playlist.getSongs()) {
				System.out.println("- " + song.getTitle() + " by " + song.getArtist());
			}
		} else {
			System.out.println("Playlist not found.");
		}

		// Ask if the user wants to search for another playlist
		System.out.println("Would you like to search for another playlist? (YES/NO)");
		String continueSearch = input.nextLine().toUpperCase();
		if (continueSearch.equals("YES")) {
			searchPlaylist(input);
		} else {
			pageOne(input);
		}
	}

	/**
	 * Prompts the user to create a new playlist by entering a name.
	 * 
	 * @param input Scanner for reading user input.
	 * @return The name of the newly created playlist.
	 */
	private String createPlaylist(Scanner input) {
		String inquiry = "Enter the name of your playlist: ";
		System.out.print(inquiry);
		String name = input.nextLine();
		model.createPlaylist(name);
		System.out.println("Playlist has been added!\nType BACK to continue!");
		return name;
	}

	/**
	 * Displays a list of existing playlists and allows the user to pick one for
	 * song removal.
	 * 
	 * @param input Scanner for reading user input.
	 * @throws IOException
	 * @throws NoSuchAlgorithmException
	 * @throws InvalidKeySpecException
	 */
	private void pickPlaylist(Scanner input) throws InvalidKeySpecException, NoSuchAlgorithmException, IOException {
		String plList = "These are your active playlists: ";
		ArrayList<Playlist> playlists = model.getPlaylists();
		for (Playlist p : playlists) {
			plList += p.getName() + ", ";
		}
		// remove trailing ", "
		plList = plList.substring(0, plList.length() - 2);
		System.out.println(plList);
		System.out.print("Which playlist would you like to remove from: ");
		while (true) {
			boolean found = false;
			String name = input.nextLine();
			if (name.equals("BACK"))
				addPlaylist(input);
			for (Playlist p : playlists) {
				if (p.getName().equals(name)) {
					removePlaylist(input, p);
					found = true;
					break;
				}
			}
			if (!found)
				System.out.println("Name not recoginized. Please try again");
		}
	}

	/**
	 * Allows the user to remove a song from a selected playlist.
	 * 
	 * @param input Scanner for reading user input.
	 * @param p     The playlist from which a song should be removed.
	 * @throws IOException
	 * @throws NoSuchAlgorithmException
	 * @throws InvalidKeySpecException
	 */
	private void removePlaylist(Scanner input, Playlist p)
			throws InvalidKeySpecException, NoSuchAlgorithmException, IOException {
		String songs = "Here are the songs in your playlist: ";
		for (Song s : p.getSongs()) {
			songs += s.getTitle() + ", ";
		}
		// remove trailing ", "
		songs = songs.substring(0, songs.length() - 2);
		System.out.println(songs);
		System.out.print("Which song would you like to remove: ");
		while (true) {
			boolean found = false;
			String name = input.nextLine();
			String command = name.toUpperCase();
			if (command.equals("BACK"))
				pickPlaylist(input);
			for (Song s : p.getSongs()) {
				if (s.getTitle().equals(name)) {
					p.removeSong(s);
					found = true;
					System.out.println("Song removed!");
					break;
				}
			}
			if (!found)
				System.out.println("Name not recoginized. Please try again");
			else {
				break;
			}
		}
		pageOne(input);
	}

	/**
	 * Handles searching functionality, allowing the user to search for songs,
	 * albums, or artists in either the Music Store or their personal Library.
	 * 
	 * @param input Scanner for reading user input.
	 * @throws IOException
	 * @throws NoSuchAlgorithmException
	 * @throws InvalidKeySpecException
	 */
	private void search(Scanner input) throws InvalidKeySpecException, NoSuchAlgorithmException, IOException {
		String searchFromCommands = "- MS: Music Store (You can add to your library)"
				+ "- LIBRARY: Personal Library (You can rate, play, or add to a playlist)" + "- BACK: Go back";

		String searchByCommands = "- SONG: Search by song title\n" + "- ARTIST: Search by artist name\n"
				+ "- ALBUM: Search by album title\n" + "- GENRE: Search by genre\n" + "- BACK: Go back";

		System.out.println("Where do you want to search from?");
		System.out.println(searchFromCommands);

		String userCommand = input.nextLine().toUpperCase();
		boolean isMusicStore = false;
		switch (userCommand) {
		case "MS":
			isMusicStore = true;
			break;
		case "LIBRARY":
			isMusicStore = false;
			break;
		case "HELP":
			System.out.println(searchFromCommands);
			search(input);
			return;
		case "BACK":
			pageOne(input);
			break;
		default:
			System.out.println("Command not recognized. Please try again!");
			search(input);
			return;
		}

		System.out.println("Search by:");
		System.out.println(searchByCommands);

		String searchType = input.nextLine().toUpperCase();
		switch (searchType) {
		case "SONG":
			searchSong(input, isMusicStore);
			break;
		case "ARTIST":
			searchArtist(input, isMusicStore);
			break;
		case "ALBUM":
			searchAlbum(input, isMusicStore);
			break;
		case "GENRE":
			if (isMusicStore) {
				System.out.println("You cannot search by genre in Music Store");
				search(input);
			}
			searchByGenre(input);
			break;
		case "BACK":
			search(input);
			return;
		default:
			System.out.println("Command not recognized. Please try again!");
			search(input);
		}
	}

	/**
	 * Handles the functionality to search for songs, albums, or artists by genre.
	 * 
	 * @param input Scanner for reading user input.
	 */
	private void searchByGenre(Scanner input) throws InvalidKeySpecException, NoSuchAlgorithmException, IOException {
		System.out.print("Enter the genre you want to search for: ");
		String genre = input.nextLine();

		ArrayList<Song> songsByGenre = model.searchSongsByGenre(genre);

		if (!songsByGenre.isEmpty()) {
			System.out.println("Songs found in the genre '" + genre + "':");
			for (Song song : songsByGenre) {
				System.out.println("- " + song.getTitle() + " by " + song.getArtist());
			}
		} else {
			System.out.println("No songs found for the genre '" + genre + "'.");
		}

		// Offer the user to continue searching or return to the main menu
		System.out.println("Would you like to search for another genre? (YES/NO)");
		String continueSearch = input.nextLine().toUpperCase();
		if (continueSearch.equals("YES")) {
			searchByGenre(input);
		} else {
			pageOne(input);
		}
	}

	/**
	 * Searches for a song by title in either the Music Store or the user's Library.
	 * Allows users to add the song to their library, rate it, or add it to a
	 * playlist.
	 * 
	 * @param input        Scanner for reading user input.
	 * @param isMusicStore True if searching in the Music Store, false if searching
	 *                     in the Library.
	 * @throws IOException
	 * @throws NoSuchAlgorithmException
	 * @throws InvalidKeySpecException
	 */
	private void searchSong(Scanner input, boolean isMusicStore)
			throws InvalidKeySpecException, NoSuchAlgorithmException, IOException {
		System.out.print("Enter song title: ");
		String title = input.nextLine();

		if (isMusicStore) {
			Song song = store.getSong(title);
			if (song != null) {
				System.out.println("Song Found: " + song.getTitle() + " by " + song.getArtist());
				System.out.println("Would you like to add this song to your library? (YES/NO)");
				String add = input.nextLine().toUpperCase();
				if (add.equals("YES")) {
					model.addSong(song);
					System.out.println("Song added to your library.");
				}
			} else {
				System.out.println("Song not found in the Music Store.");
			}
		} else {
			Song song = model.searchSong(title);
			if (song != null) {
				System.out.println("Song Found: " + song.getTitle() + " by " + song.getArtist());
				System.out.println("Options:");
				System.out.println("- RATE: Rate this song");
				System.out.println("- FAV: Mark as favorite");
				System.out.println("- PL: Add to a playlist");
				System.out.println("- PLAY: Play this song");
				System.out.println("- MORE: More information");
				System.out.println("- BACK: Go back");

				String action = input.nextLine().toUpperCase();
				switch (action) {
				case "RATE":
					rateSong(input, title);
					break;
				case "FAV":
					model.addFavorite(title);
					System.out.println("Song marked as favorite.");
					break;
				case "PL":
					addSongToPlaylist(input, title);
					break;
				case "PLAY":
					model.songPlay(song);
					System.out.println("Now playing: " + song.getTitle() + " by " + song.getArtist());
					break;
				case "BACK":
					search(input);
					break;
				case "MORE":
					model.getAlbumDetail(song);
				default:
					System.out.println("Command not recognized. Going back.");
				}
			} else {
				System.out.println("Song not found in your library.");
			}
		}

		// Ask if the user wants to continue searching
		System.out.println("Would you like to search for something else? (YES/NO)");
		String continueSearch = input.nextLine().toUpperCase();
		if (continueSearch.equals("YES")) {
			search(input);
		} else {
			pageOne(input);
		}
	}

	/**
	 * Searches for an album by title in either the Music Store or the user's
	 * Library. Allows users to add the album to their library.
	 * 
	 * @param input        Scanner for reading user input.
	 * @param isMusicStore True if searching in the Music Store, false if searching
	 *                     in the Library.
	 * @throws IOException
	 * @throws NoSuchAlgorithmException
	 * @throws InvalidKeySpecException
	 */
	private void rateSong(Scanner input, String title)
			throws InvalidKeySpecException, NoSuchAlgorithmException, IOException {
		Song song = model.searchSong(title);

		if (song != null) {
			System.out.println("You are rating: " + song.getTitle() + " by " + song.getArtist());
			System.out.println("Please enter a rating from 1 to 5:");

			String ratingInput = input.nextLine();
			int rating;

			try {
				rating = Integer.parseInt(ratingInput);

				if (rating < 1 || rating > 5) {
					System.out.println("Invalid rating! Please enter a number between 1 and 5.");
					rateSong(input, title); // Retry
					return;
				}

				model.rate(title, ratingInput);
				System.out.println("You rated " + song.getTitle() + " as " + rating + " stars.");

				// Automatically mark as favorite if rated 5
				if (rating == 5) {
					System.out.println(song.getTitle() + " is now marked as a favorite!");
				}
			} catch (NumberFormatException e) {
				System.out.println("Invalid input! Please enter a number.");
				rateSong(input, title); // Retry
				return;
			}
		} else {
			System.out.println("Song not found in your library.");
		}

		// Ask if the user wants to rate another song
		System.out.println("Would you like to rate another song? (YES/NO)");
		String continueRate = input.nextLine().toUpperCase();
		if (continueRate.equals("YES")) {
			search(input); // Go back to search to find another song to rate
		} else {
			pageOne(input); // Return to the main menu
		}
	}

	/**
	 * Allows the user to add a song to a playlist. If no playlists exist, the user
	 * is prompted to create one first.
	 * 
	 * @param input Scanner for reading user input.
	 * @param title The title of the song to be added.
	 * @throws IOException
	 * @throws NoSuchAlgorithmException
	 * @throws InvalidKeySpecException
	 */
	private void addSongToPlaylist(Scanner input, String title)
			throws InvalidKeySpecException, NoSuchAlgorithmException, IOException {
		Song song = model.searchSong(title);

		if (song != null) {
			System.out.println("You are adding: " + song.getTitle() + " by " + song.getArtist() + " to a playlist.");

			ArrayList<Playlist> playlists = model.getPlaylists();

			if (playlists.isEmpty()) {
				System.out.println("You don't have any playlists yet.");
				System.out.println("Would you like to create a new playlist? (YES/NO)");
				String createNew = input.nextLine().toUpperCase();
				if (createNew.equals("YES")) {
					String name = createPlaylist(input); // Reuse the existing createPlaylist method
					System.out.println("New playlist created successfully.");

					System.out.println("Would you like to add " + song.getTitle() + " to this new playlist? (YES/NO)");
					String addToNew = input.nextLine().toUpperCase();
					if (addToNew.equals("YES")) {
						model.addSongToPlaylist(name, title);
						System.out.println(song.getTitle() + " was added to the new playlist.");
					} else {
						System.out.println("Song was not added to the new playlist.");
					}
				} else {
					System.out.println("No playlist created. Returning to main menu.");
					pageOne(input);
					return;
				}
			} else {
				System.out.println("Select a playlist to add the song to:");
				for (int i = 0; i < playlists.size(); i++) {
					System.out.println((i + 1) + ". " + playlists.get(i).getName());
				}
				System.out.println((playlists.size() + 1) + ". Create a new playlist");

				String choice = input.nextLine();
				int selectedIndex;
				try {
					selectedIndex = Integer.parseInt(choice) - 1;
					if (selectedIndex >= 0 && selectedIndex < playlists.size()) {
						Playlist selectedPlaylist = playlists.get(selectedIndex);
						if (selectedPlaylist.getSongs().contains(song)) {
							System.out.println(
									song.getTitle() + " is already in the playlist: " + selectedPlaylist.getName());
						} else {
							model.addSongToPlaylist(selectedPlaylist.getName(), title);
							System.out.println(
									song.getTitle() + " was added to the playlist: " + selectedPlaylist.getName());
						}
					} else if (selectedIndex == playlists.size()) {
						String name = createPlaylist(input); // Reuse the existing createPlaylist method
						System.out.println("New playlist created successfully.");

						System.out.println(
								"Would you like to add " + song.getTitle() + " to this new playlist? (YES/NO)");
						String addToNew = input.nextLine().toUpperCase();
						if (addToNew.equals("YES")) {
							model.addSongToPlaylist(name, title);
							System.out.println(song.getTitle() + " was added to the new playlist.");
						} else {
							System.out.println("Song was not added to the new playlist.");
						}
					} else {
						System.out.println("Invalid selection. Returning to main menu.");
						pageOne(input);
						return;
					}
				} catch (NumberFormatException e) {
					System.out.println("Invalid input. Please enter a number.");
					addSongToPlaylist(input, title);
					return;
				}
			}
		} else {
			System.out.println("Song not found in your library.");
		}

		// Ask if the user wants to add another song to a playlist
		System.out.println("Would you like to add another song to a playlist? (YES/NO)");
		String continueAdding = input.nextLine().toUpperCase();
		if (continueAdding.equals("YES")) {
			search(input); // Go back to search to find another song
		} else {
			pageOne(input); // Return to the main menu
		}
	}

	/**
	 * Searches for an artist in either the Music Store or the user's Library. The
	 * user can choose to search for either songs or albums by the artist.
	 * 
	 * @param input        Scanner for reading user input.
	 * @param isMusicStore True if searching in the Music Store, false if searching
	 *                     in the Library.
	 * @throws IOException
	 * @throws NoSuchAlgorithmException
	 * @throws InvalidKeySpecException
	 */
	private void searchArtist(Scanner input, boolean isMusicStore)
			throws InvalidKeySpecException, NoSuchAlgorithmException, IOException {
		System.out.print("Enter artist name: ");
		String artist = input.nextLine();

		// Ask the user whether to search for songs or albums
		System.out.println("Would you like to search for:");
		System.out.println("- SONGS: Search for songs by this artist");
		System.out.println("- ALBUMS: Search for albums by this artist");
		System.out.print("Enter your choice: ");

		String searchType = input.nextLine().toUpperCase();

		if (searchType.equals("SONGS")) {
			ArrayList<Song> songs;
			if (isMusicStore) {
				songs = store.searchSong("A", artist);
			} else {
				songs = new ArrayList<>();
				for (Song song : model.getSongs()) {
					if (song.getArtist().equals(artist)) {
						songs.add(song);
					}
				}
			}

			if (!songs.isEmpty()) {
				System.out.println("Songs by " + artist + ":");
				for (int i = 0; i < songs.size(); i++) {
					System.out.println((i + 1) + ". " + songs.get(i).getTitle());
				}

				if (isMusicStore) {
					handleAddingSongsToLibrary(input, songs);
				} else {
					handleAddingSongsToPlaylist(input, songs);
				}
			} else {
				System.out.println("No songs found for this artist.");
			}

		} else if (searchType.equals("ALBUMS")) {
			ArrayList<Album> albums;
			if (isMusicStore) {
				albums = store.searchAlbum("A", artist);
			} else {
				albums = new ArrayList<>();
				for (Album album : model.getAlbums()) {
					if (album.getArtist().equals(artist)) {
						albums.add(album);
					}
				}
			}

			if (!albums.isEmpty()) {
				System.out.println("Albums by " + artist + ":");
				for (Album album : albums) {
					System.out.println("- " + album.getTitle());
				}

				if (isMusicStore) {
					System.out.println("Would you like to add these albums to your library? (YES/NO)");
					String addOption = input.nextLine().toUpperCase();
					if (addOption.equals("YES")) {
						for (Album album : albums) {
							for (Song song : album.getSongs()) {
								model.addSong(song);
							}
							model.addAlbum(album);
						}
						System.out.println("All albums added to your library.");
					}
				}
			} else {
				System.out.println("No albums found for this artist.");
			}
		} else {
			System.out.println("Invalid option. Returning to main menu.");
			pageOne(input);
			return;
		}

		// Ask if the user wants to continue searching
		System.out.println("Would you like to search for something else? (YES/NO)");
		String continueSearch = input.nextLine().toUpperCase();
		if (continueSearch.equals("YES")) {
			search(input);
		} else {
			pageOne(input);
		}
	}

	/**
	 * Handles adding songs from the Music Store to the user's Library. Allows the
	 * user to add all songs, select specific songs, or skip adding.
	 * 
	 * @param input Scanner for reading user input.
	 * @param songs List of songs found in the search.
	 * @throws IOException
	 * @throws NoSuchAlgorithmException
	 * @throws InvalidKeySpecException
	 */
	private void handleAddingSongsToLibrary(Scanner input, ArrayList<Song> songs)
			throws InvalidKeySpecException, NoSuchAlgorithmException, IOException {
		System.out.println("Would you like to add these songs to your library?");
		System.out.println("- ALL: Add all songs");
		System.out.println("- SOME: Select specific songs");
		System.out.println("- NONE: Skip adding");
		String addOption = input.nextLine().toUpperCase();

		switch (addOption) {
		case "ALL":
			for (Song song : songs) {
				model.addSong(song);
			}
			System.out.println("All songs added to your library.");
			break;
		case "SOME":
			System.out.println("Enter the numbers of the songs you want to add, separated by commas:");
			String[] selectedSongs = input.nextLine().split(",");
			for (String index : selectedSongs) {
				try {
					int songIndex = Integer.parseInt(index.trim()) - 1;
					if (songIndex >= 0 && songIndex < songs.size()) {
						model.addSong(songs.get(songIndex));
						System.out.println(songs.get(songIndex).getTitle() + " added to your library.");
					} else {
						System.out.println("Invalid selection: " + (songIndex + 1));
					}
				} catch (NumberFormatException e) {
					System.out.println("Invalid input. Please enter numbers only.");
				}
			}
			break;
		case "NONE":
			System.out.println("No songs were added.");
			break;
		default:
			System.out.println("Invalid option. Returning to main menu.");
			pageOne(input);
		}
	}

	/**
	 * Handles adding songs to a playlist from the user's Library. The user can add
	 * all songs, select specific songs, or skip adding.
	 * 
	 * @param input Scanner for reading user input.
	 * @param songs List of songs found in the search.
	 * @throws IOException
	 * @throws NoSuchAlgorithmException
	 * @throws InvalidKeySpecException
	 */
	private void handleAddingSongsToPlaylist(Scanner input, ArrayList<Song> songs)
			throws InvalidKeySpecException, NoSuchAlgorithmException, IOException {
		System.out.println("Would you like to add these songs to a playlist?");
		System.out.println("- ALL: Add all songs");
		System.out.println("- SOME: Select specific songs");
		System.out.println("- NONE: Skip adding");
		String addOption = input.nextLine().toUpperCase();

		if (!addOption.equals("NONE")) {
			System.out.print("Enter the playlist name: ");
			String playlistName = input.nextLine();

			switch (addOption) {
			case "ALL":
				for (Song song : songs) {
					model.addSongToPlaylist(playlistName, song.getTitle());
				}
				System.out.println("All songs added to the playlist.");
				break;
			case "SOME":
				System.out.println("Enter the numbers of the songs you want to add, separated by commas:");
				String[] selectedSongs = input.nextLine().split(",");
				for (String index : selectedSongs) {
					try {
						int songIndex = Integer.parseInt(index.trim()) - 1;
						if (songIndex >= 0 && songIndex < songs.size()) {
							model.addSongToPlaylist(playlistName, songs.get(songIndex).getTitle());
							System.out.println(songs.get(songIndex).getTitle() + " added to the playlist.");
						} else {
							System.out.println("Invalid selection: " + (songIndex + 1));
						}
					} catch (NumberFormatException e) {
						System.out.println("Invalid input. Please enter numbers only.");
					}
				}
				break;
			case "NONE":
				System.out.println("No songs were added.");
				break;
			default:
				System.out.println("Invalid option. Returning to main menu.");
				pageOne(input);
			}
		} else {
			System.out.println("No songs were added.");
		}
	}

	/**
	 * Searches for an album by title in either the Music Store or the user's
	 * Library. Allows users to add the album to their library.
	 * 
	 * @param input        Scanner for reading user input.
	 * @param isMusicStore True if searching in the Music Store, false if searching
	 *                     in the Library.
	 * @throws IOException
	 * @throws NoSuchAlgorithmException
	 * @throws InvalidKeySpecException
	 */
	private void searchAlbum(Scanner input, boolean isMusicStore)
			throws InvalidKeySpecException, NoSuchAlgorithmException, IOException {
		System.out.print("Enter album title: ");
		String title = input.nextLine();

		Album album;
		if (isMusicStore) {
			album = store.getAlbum(title);
		} else {
			album = model.searchAlbum(title);
		}

		if (album != null) {
			System.out.println("Album Found: " + album.getTitle() + " by " + album.getArtist());
			System.out.println("Songs:");
			for (Song song : album.getSongs()) {
				System.out.println("- " + song.getTitle());
			}

			if (isMusicStore) {
				System.out.println("Would you like to add this album to your library? (YES/NO)");
				String add = input.nextLine().toUpperCase();
				if (add.equals("YES")) {
					model.addAlbum(album);
					System.out.println("Album added to your library.");
				}
			}
		} else {
			System.out.println("Album not found.");
		}

		// Ask if the user wants to continue searching
		System.out.println("Would you like to search for something else? (YES/NO)");
		String continueSearch = input.nextLine().toUpperCase();
		if (continueSearch.equals("YES")) {
			search(input);
		} else {
			pageOne(input);
		}
	}

	public void homeScreen() throws InvalidKeySpecException, NoSuchAlgorithmException, IOException {
		Scanner scanner = new Scanner(System.in);

		while (true) {
			System.out.println("\n==== Welcome! ====");
			System.out.println("1. Register a new user");
			System.out.println("2. Login");
			System.out.println("3. Exit");
			System.out.print("Choose a number: ");

			String choice = scanner.nextLine();

			switch (choice) {
			case "1": // Register
				System.out.print("Enter a username: ");
				String newUsername = scanner.nextLine();

				System.out.print("Enter a password: ");
				String newPassword = scanner.nextLine();
				User newUser = this.users.addUser(newUsername, newPassword);
				if (newUser != null) {
					System.out.println("User registered successfully!");
					this.model = newUser.getLibrary();
				} else {
					System.out.println("Username already exists. Try again.");
				}
				break;

			case "2": // Login
				System.out.print("Enter your username: ");
				String username = scanner.nextLine();

				System.out.print("Enter your password: ");
				String password = scanner.nextLine();

				User curr = users.login(username, password);
				if (curr != null) {
					this.model = curr.getLibrary();
					operation();
				}
				break;

			case "3": // Exit
				operation();
				return;

			default:
				System.out.println("Invalid choice. Please enter 1, 2, or 3.");
			}
		}
	}

	public static void main(String[] args)
			throws FileNotFoundException, IOException, InvalidKeySpecException, NoSuchAlgorithmException {
		String folderPath = "C:\\Users\\ethan\\git\\CSc335proj1\\src\\proj1\\albums";
		File folder = new File(folderPath);
		ArrayList<HashMap<String, ArrayList<String>>> albums = new ArrayList<>();
		if (folder.exists() && folder.isDirectory()) {
			File[] files = folder.listFiles((dir, name) -> name.toLowerCase().endsWith(".txt"));
			for (File f : files) {
				Scanner scanner = new Scanner(f);
				HashMap<String, ArrayList<String>> album = new HashMap<>();
				ArrayList<String> songs = new ArrayList<>();
				String titleLine = scanner.nextLine();
				while (scanner.hasNextLine()) {
					String song = scanner.nextLine();
					songs.add(song);
				}
				album.put(titleLine, songs);
				albums.add(album);
			}
		}
		Users users = new Users();
		MusicStore store = new MusicStore(albums);
		LibraryModel model = new LibraryModel();
		View view = new View(store, model, users);
		view.homeScreen();
	}

}
