package proj1;

import static org.junit.jupiter.api.Assertions.*;
import java.util.List;
import java.io.FileWriter;
import java.io.IOException;
import java.io.StringWriter;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
public class JUnitTest {

    
	// Test for Album Class
    @Test
    void testAlbum() {
        ArrayList<Song> songs = new ArrayList<>();
        Song song1 = new Song("Song1", "Artist1", "Classical", "2020", "Album1");
        Song song2 = new Song("Song2", "Artist1", "Classical", "2020", "Album1");
        songs.add(song1);
        songs.add(song2);
        Album album = new Album(songs, "Artist1", "Album1", "Classical", "2020");
        Song song3 = new Song("Song3", "Artist1", "Classical", "2020", "Album1");
        
        assertTrue(album.getSongs().contains(song1));
        assertEquals("Artist1", album.getArtist());
        assertEquals("Album1", album.getTitle());
        assertEquals("Classical", song1.getGenre());
        assertEquals("2020", song1.getYear());
        song1.play();
        assertEquals(1, song1.getCount());
        assertEquals("Classical", album.getGenre());
        assertEquals("2020", album.getYear());
        album.addSong(song3);
    }

    // Test for LibraryModel Class
    @Test
    void testLibraryModel() {
        LibraryModel library = new LibraryModel();
        Song song1 = new Song("Song1", "Artist1", "Classical", "2020", "Album1");
        Song song2 = new Song("Song2", "Artist1", "Classical", "2020", "Album1");
        Song song3 = new Song("Song3", "Artist2", "Pop", "2019", "Album2");
        ArrayList<Song> songs = new ArrayList<>();
        songs.add(song1);
        songs.add(song2);
        Album album = new Album(songs, "Artist1", "Album1", "Classical", "2020");

        // Add songs and album
        library.addSong(song1);
        library.addSong(song2);
        library.addAlbum(album);

        // Check additions
        assertTrue(library.getSongs().contains(song1));
        assertTrue(library.getSongs().contains(song2));
        assertTrue(library.getAlbums().contains(album));

        // Create and search playlist
        assertTrue(library.createPlaylist("Playlist1"));
        Playlist playlist = library.searchPlaylist("Playlist1");
        assertNotNull(playlist);
        assertEquals("Playlist1", playlist.getName());

        // Search Song and Album
        assertEquals(song1, library.searchSong("Song1"));

        // Add Song to Playlist
        assertTrue(library.addSongToPlaylist("Playlist1", "Song1"));
        assertTrue(playlist.getSongs().contains(song1));
        
        // Test: Playlist should contain song1 and song2 but not song3
        assertTrue(playlist.containsSong(song1), "Playlist should contain Song1");
    
        // Test removing a song from playlist
        assertTrue(library.removeSongFromPlaylist("Playlist1", "Song1"));
        assertFalse(playlist.getSongs().contains(song1));

        // Test adding favorite
        library.addFavorite("Song2");
        assertTrue(library.getFavSongs().contains(song2));

        // Test rating a song
        library.rate("Song1", "5");
        assertNotNull(song1.getRating());
        assertEquals(5, song1.getRating().getRating());
        
        // Check if song is added to favorites when rated 5
        assertTrue(library.getFavSongs().contains(song1));
        
        assertThrows(IllegalArgumentException.class, () -> new Rating(0));

        // Test rating a song not in the library
        library.rate("NonExistentSong", "3");
        
        assertFalse(playlist.containsSong(song3), "Playlist should NOT contain Song3");
        
        // Edge Case: Check with null
        assertFalse(playlist.containsSong(null), "Playlist should NOT contain null");

        // Edge case: Search for non-existent album 
        assertNull(library.searchAlbum("NonExistentAlbum"));
        
        // Edge case: Search for non-existent song
        assertNull(library.searchSong("NonExistentSong"));
        
        // Edge case: Search for non-existent playlist
        assertNull(library.searchPlaylist("NonExistentPlaylist"));
        
        // Test removing a song not in the playlist
        assertFalse(library.removeSongFromPlaylist("Playlist1", "Song3"));
        assertFalse(library.removeSongFromPlaylist("Playlist1", "Song2"));
    }
    
    // Test for addSongToPlaylist() in LibraryModel Class
    @Test
    void testAddSongToPlaylist() {
        LibraryModel library = new LibraryModel();
        Song song1 = new Song("Song1", "Artist1", "Classical", "2020", "Album1");
        Song song2 = new Song("Song2", "Artist1", "Classical", "2020", "Album1");
        Song song3 = new Song("Song3", "Artist2", "Pop", "2019", "Album2");
        
        // Add songs to library
        library.addSong(song1);
        library.addSong(song2);

        // Create a playlist
        assertTrue(library.createPlaylist("Playlist1"));
        Playlist playlist = library.searchPlaylist("Playlist1");
        assertNotNull(playlist);

        // 1. Add Song1 to Playlist1 (Should be successful)
        assertTrue(library.addSongToPlaylist("Playlist1", "Song1"));
        assertTrue(playlist.getSongs().contains(song1));

        // 2. Add Song1 again to Playlist1 (Should fail because it's a duplicate)
        assertFalse(library.addSongToPlaylist("Playlist1", "Song1"));
        assertEquals(1, playlist.getSongs().size()); // Size should still be 1

        // 3. Add a non-existent song (Song3) to Playlist1 (Should fail)
        assertFalse(library.addSongToPlaylist("Playlist1", "Song3"));
        assertFalse(playlist.getSongs().contains(song3));

        // 4. Add a song to a non-existent playlist (Should fail)
        assertFalse(library.addSongToPlaylist("NonExistentPlaylist", "Song1"));
    }

    // Test for creating Playlist with the Same Name in LibraryModel Class
    @Test
    void testCreatePlaylistWithSameName() {
        LibraryModel library = new LibraryModel();

        // 1. Create the first Playlist1 (Should be successful)
        assertTrue(library.createPlaylist("Playlist1"));
        assertNotNull(library.searchPlaylist("Playlist1"));

        // 2. Try to create another Playlist1 (Should fail)
        assertFalse(library.createPlaylist("Playlist1"));

        // 3. Confirm only one Playlist1 exists
        ArrayList<Playlist> playlists = library.getPlaylists();
        int count = 0;
        for (Playlist p : playlists) {
            if (p.getName().equals("Playlist1")) {
                count++;
            }
        }
        assertEquals(1, count);
    }

    // Test for getArtists() Method in LibraryModel Class
    @Test
    void testGetArtists() {
        LibraryModel library = new LibraryModel();

        // Create songs with different artists
        Song song1 = new Song("Song1", "Artist1", "Genre1", "2020", "Album1");
        Song song2 = new Song("Song2", "Artist1", "Genre2", "2021", "Album1");
        Song song3 = new Song("Song3", "Artist2", "Genre3", "2022", "Album2");
        Song song4 = new Song("Song4", "Artist2", "Genre1", "2020", "Album2");

        // Add songs to albums
        ArrayList<Song> album1Songs = new ArrayList<>();
        album1Songs.add(song1);
        album1Songs.add(song2);
        Album album1 = new Album(album1Songs, "Artist1", "Album1", "Genre1", "2020");

        ArrayList<Song> album2Songs = new ArrayList<>();
        album2Songs.add(song3);
        album2Songs.add(song4);
        Album album2 = new Album(album2Songs, "Artist3", "Album2", "Genre2", "2021");

        // Add albums to library
        library.addAlbum(album1);
        library.addAlbum(album2);

        // Fetch and assert the initial list of artists
        ArrayList<String> artists = library.getArtists();
        assertEquals(2, artists.size(), "There should be two unique artists.");

        // Add another album with a new artist to test dynamic updates
        ArrayList<Song> album3Songs = new ArrayList<>();
        album3Songs.add(new Song("Song5", "Artist4", "Genre3", "2023", "Album3"));
        Album album3 = new Album(album3Songs, "Artist4", "Album3", "Genre3", "2023");
        library.addAlbum(album3);

        // Re-fetch artists and assert the updated list
        artists = library.getArtists();
        assertEquals(3, artists.size(), "There should now be three unique artists after adding another.");
        assertTrue(artists.contains("Artist4"), "Artists should now include 'Artist4'.");
    }

    
 // Test for MusicStore Class
    @Test
    void testMusicStore() {
        ArrayList<HashMap<String, ArrayList<String>>> albums = new ArrayList<>();
        HashMap<String, ArrayList<String>> album1 = new HashMap<>();
        ArrayList<String> songs = new ArrayList<>();
        songs.add("Song1");
        songs.add("Song2");
        album1.put("Album1,Artist1,genre1,year1", songs);
        albums.add(album1);

        MusicStore musicStore = new MusicStore(albums);

        Album album = musicStore.getAlbum("Album1");
        assertNotNull(album);
        assertEquals("Album1", album.getTitle());
        assertEquals("Artist1", album.getArtist());

        Song song = musicStore.getSong("Song1");
        assertNotNull(song);
        assertEquals("Song1", song.getTitle());
        assertEquals("Artist1", song.getArtist());

        // Test searchAlbum by title
        ArrayList<Album> foundAlbums = musicStore.searchAlbum("T", "Album1");
        assertEquals(1, foundAlbums.size());
        assertEquals("Album1", foundAlbums.get(0).getTitle());

        // Test searchAlbum by artist
        foundAlbums = musicStore.searchAlbum("A", "Artist1");
        assertEquals(1, foundAlbums.size());
        assertEquals("Artist1", foundAlbums.get(0).getArtist());

        // Test searchSong by title
        ArrayList<Song> foundSongs = musicStore.searchSong("T", "Song1");
        assertEquals(1, foundSongs.size());
        assertEquals("Song1", foundSongs.get(0).getTitle());

        // Test searchSong by artist
        foundSongs = musicStore.searchSong("A", "Artist1");
        assertEquals(2, foundSongs.size());

        // Edge case: Search non-existent album
        foundAlbums = musicStore.searchAlbum("T", "NonExistentAlbum");
        assertTrue(foundAlbums.isEmpty());

        // Edge case: Search non-existent song
        foundSongs = musicStore.searchSong("T", "NonExistentSong");
        assertTrue(foundSongs.isEmpty());

        // Edge case: Invalid search type
        foundAlbums = musicStore.searchAlbum("X", "Album1");
        assertNull(foundAlbums);

        foundSongs = musicStore.searchSong("X", "Song1");
        assertNull(foundSongs);
    }
    
    @Test
    void testAutomaticPlaylistCreationOnSongAdd() {
        LibraryModel library = new LibraryModel();
        // Adding multiple songs of the same genre to check if the playlist is created automatically
        for (int i = 0; i < 12; i++) {
            library.addSong(new Song("Jazz Song " + i, "Various Artists", "Jazz", "2021", "Jazz Hits Volume " + i));
        }
        Playlist jazzPlaylist = library.searchPlaylist("Jazz Hits");
        assertNotNull(jazzPlaylist, "Jazz Hits playlist should be created automatically");
        assertEquals(12, jazzPlaylist.getSongs().size(), "Jazz Hits playlist should contain 12 songs");
    }

    @Test
    void testNoPlaylistCreationForFewerSongs() {
        LibraryModel library = new LibraryModel();
        // Adding fewer than 10 songs to ensure no playlist is created
        for (int i = 0; i < 9; i++) {
            library.addSong(new Song("Blues Song " + i, "Various Artists", "Blues", "2021", "Blues Album"));
        }
        Playlist bluesPlaylist = library.searchPlaylist("Blues Hits");
        assertNull(bluesPlaylist, "Blues Hits playlist should not be created because there are fewer than 10 songs");
    }
    
    @Test
    void testTopRatedPlaylistUpdates() {
        LibraryModel library = new LibraryModel();
        Song topSong1 = new Song("Top Rated Song 1", "Artist1", "Classical", "2020", "Best of 2020");
        Song topSong2 = new Song("Top Rated Song 2", "Artist2", "Classical", "2020", "Classical Masterpieces");
        library.addSong(topSong1);
        library.addSong(topSong2);
        
        // Initially, none of these should be in the Top Rated playlist
        Playlist topRated = library.searchPlaylist("Top Rated");
        assertNull(topRated, "Top Rated playlist should initially be null");

        // Rate songs high enough to be in Top Rated playlist
        library.rate("Top Rated Song 1", "5");
        library.rate("Top Rated Song 2", "4");

        topRated = library.searchPlaylist("Top Rated");
        assertNotNull(topRated, "Top Rated playlist should be created after rating songs 4 or 5");
        assertTrue(topRated.getSongs().contains(topSong1), "Top Rated playlist should contain Top Rated Song 1");
        assertTrue(topRated.getSongs().contains(topSong2), "Top Rated playlist should contain Top Rated Song 2");
        
        // Change the rating to below 4 and check if it gets removed from the playlist
        library.rate("Top Rated Song 2", "3");
        assertFalse(topRated.getSongs().contains(topSong2), "Top Rated Song 2 should be removed from Top Rated playlist after lowering rating");
    }
    
    @Test
    void testInvalidRatingException() {
        LibraryModel library = new LibraryModel();
        library.addSong(new Song("Rock Song", "Rockstar", "Rock", "2019", "Rock Forever"));
        
        assertThrows(IllegalArgumentException.class, () -> library.rate("Rock Song", "6"),
            "Rating a song above 5 should throw IllegalArgumentException");
        assertThrows(IllegalArgumentException.class, () -> library.rate("Rock Song", "0"),
            "Rating a song below 1 should throw IllegalArgumentException");
    }

    @Test
    void testAlbumInitializationAndSongAdding() {
        Song song1 = new Song("Song1", "Artist1", "Classical", "2020", "Album1");
        ArrayList<Song> songs = new ArrayList<>(Arrays.asList(song1));
        Album album = new Album(songs, "Artist1", "Album1", "Classical", "2020");

        assertNotNull(album.getSongs(), "Album songs should not be null");
        assertTrue(album.getSongs().contains(song1), "Album should contain song1");
        assertEquals("Artist1", album.getArtist(), "Album artist should match 'Artist1'");
        assertEquals("Classical", album.getGenre(), "Genre should match 'Classical'");
    }

    @Test
    void testLibraryModelWithEmptyInitialization() {
        LibraryModel library = new LibraryModel();
        assertNotNull(library.getSongs(), "Songs list should be initialized and not null");
        assertTrue(library.getAlbums().isEmpty(), "Initially, albums should be empty");
        
        // Test adding a song to an empty library
        Song song = new Song("NewSong", "NewArtist", "Genre", "2020", "NewAlbum");
        library.addSong(song);
        assertEquals(1, library.getSongs().size(), "Library should have one song after addition");
        assertNotNull(library.searchSong("NewSong"), "Search for the new song should not be null");
    }

    @Test
    void testRatingOutOfBounds() {
        assertThrows(IllegalArgumentException.class, () -> new Rating(6), "Rating above 5 should throw IllegalArgumentException");
        assertThrows(IllegalArgumentException.class, () -> new Rating(0), "Rating below 1 should throw IllegalArgumentException");
    }

    @Test
    void testSearchNonExistentSongInMusicStore() {
        MusicStore store = new MusicStore(new ArrayList<>()); // Assuming a constructor that accepts a list
        assertNull(store.getSong("NonExistent"), "Searching for a non-existent song should return null");
    }
    
    @Test
    void testSortByCount() {
        Playlist playlist = new Playlist("MyPlaylist");
        Song song1 = new Song("Song1", "Artist1", "Pop", "2021", "Album1");
        Song song2 = new Song("Song2", "Artist2", "Rock", "2021", "Album2");
        Song song3 = new Song("Song3", "Artist3", "Jazz", "2021", "Album3");

        // Play songs different numbers of times
        song1.play(); song1.play(); // Played 2 times
        song3.play(); song3.play(); song3.play(); // Played 3 times

        // Add songs to playlist
        playlist.addSong(song1);
        playlist.addSong(song2);
        playlist.addSong(song3);

        playlist.sortByCount();
        List<Song> sortedSongs = playlist.getSongs();
        assertEquals(song3, sortedSongs.get(0), "Song3 should be first after sort by count");
        assertEquals(song1, sortedSongs.get(1), "Song1 should be second after sort by count");
        assertEquals(song2, sortedSongs.get(2), "Song2 should be third after sort by count");
    }

    @Test
    void testAddSongEnd() {
        Playlist playlist = new Playlist("MyPlaylist");
        Song newSong = new Song("NewSong", "NewArtist", "Classical", "2021", "NewAlbum");
        playlist.addSongEnd(newSong);
        assertEquals(newSong, playlist.getSongs().get(0), "NewSong should be the first in the list");
    }

    @Test
    void testRemoveSongByIndex() {
        Playlist playlist = new Playlist("MyPlaylist");
        Song song1 = new Song("Song1", "Artist1", "Pop", "2021", "Album1");
        Song song2 = new Song("Song2", "Artist2", "Rock", "2021", "Album2");

        // Add songs to playlist
        playlist.addSong(song1);
        playlist.addSong(song2);

        Song removedSong = playlist.removeSong(0); // Removing song1, indexed at 0
        assertFalse(playlist.containsSong(song1), "Song1 should be removed from the playlist");
        assertEquals(song1, removedSong, "The removed song should be Song1");
        assertEquals(1, playlist.getSongs().size(), "Playlist should have 1 song after removal");
    }

    @Test
    void testShuffle() {
        Playlist playlist = new Playlist("MyPlaylist");
        Song song1 = new Song("Song1", "Artist1", "Pop", "2021", "Album1");
        Song song2 = new Song("Song2", "Artist2", "Rock", "2021", "Album2");
        Song song3 = new Song("Song3", "Artist3", "Jazz", "2021", "Album3");

        // Add songs to playlist
        playlist.addSong(song1);
        playlist.addSong(song2);
        playlist.addSong(song3);

        List<Song> originalOrder = new ArrayList<>(playlist.getSongs());
        playlist.shuffle();
        List<Song> shuffledOrder = playlist.getSongs();
        assertNotEquals(originalOrder, shuffledOrder, "Shuffled order should not match original order");
    }

    @Test
    void testShuffleEmptyPlaylist() {
        Playlist emptyPlaylist = new Playlist("EmptyPlaylist");
        emptyPlaylist.shuffle(); // should not throw any exception
        assertTrue(emptyPlaylist.getSongs().isEmpty(), "Empty playlist should remain empty after shuffle");
    }
    
    @Test
    void testFindMin() {
        LibraryModel library = new LibraryModel();
        Song song1 = new Song("Song1", "Artist1", "Genre1", "2021", "Album1");
        Song song2 = new Song("Song2", "Artist1", "Genre1", "2021", "Album1");
        Song song3 = new Song("Song3", "Artist1", "Genre1", "2021", "Album1");
        
        song1.play();
        song1.play();
        song2.play();
        song2.play();
        song2.play();
        song3.play();

        ArrayList<Song> songs = new ArrayList<>();
        songs.add(song1);
        songs.add(song2);
        songs.add(song3);
        assertEquals(song2, library.findMin(songs), "Should return song2 as it has the highest play count");
    }

    @Test
    void testSongPlay() {
        LibraryModel library = new LibraryModel();
        Song song = new Song("Song1", "Artist1", "Genre1", "2021", "Album1");
        library.addSong(song);
        library.songPlay(song);
        assertTrue(library.getPlaylists().get(0).getSongs().contains(song), "Song should be in the recently played playlist");
    }

    @Test
    void testSortByTitle() {
        LibraryModel library = new LibraryModel();
        Song song1 = new Song("B Song", "Artist2", "Genre1", "2021", "Album1");
        Song song2 = new Song("A Song", "Artist1", "Genre1", "2021", "Album2");
        library.addSong(song1);
        library.addSong(song2);
        ArrayList<Song> sortedSongs = library.sortByTitle();
        assertEquals("A Song", sortedSongs.get(0).getTitle(), "Songs should be sorted by title in ascending order");
    }

    @Test
    void testSortByArtist() {
        LibraryModel library = new LibraryModel();
        Song song1 = new Song("Song1", "B Artist", "Genre1", "2021", "Album1");
        Song song2 = new Song("Song2", "A Artist", "Genre1", "2021", "Album2");
        library.addSong(song1);
        library.addSong(song2);
        ArrayList<Song> sortedSongs = library.sortByArtist();
        assertEquals("A Artist", sortedSongs.get(0).getArtist(), "Songs should be sorted by artist in ascending order");
    }

    @Test
    void testRemoveSong() {
        LibraryModel library = new LibraryModel();
        Song song = new Song("Song1", "Artist1", "Genre1", "2021", "Album1");
        library.addSong(song);
        assertTrue(library.removeSong("Song1"), "Should return true as the song exists and can be removed");
        assertFalse(library.removeSong("Song1"), "Should return false as the song does not exist anymore");
    }

    @Test
    void testRemoveAlbum() {
        LibraryModel library = new LibraryModel();
        Song song = new Song("Song1", "Artist1", "Genre1", "2021", "Album1");
        Album album = new Album(new ArrayList<>(), "Artist1", "Album1", "Genre1", "2021");
        album.addSong(song);
        library.addAlbum(album);
        assertTrue(library.removeAlbum("Album1"), "Should return true as the album exists and can be removed");
        assertFalse(library.removeAlbum("Album1"), "Should return false as the album does not exist anymore");
    }

    @Test
    void testShuffleLibrary() {
        LibraryModel library = new LibraryModel();
        Song song1 = new Song("Song1", "Artist1", "Genre1", "2021", "Album1");
        Song song2 = new Song("Song2", "Artist1", "Genre1", "2021", "Album2");
        library.addSong(song1);
        library.addSong(song2);
        library.shuffleLibrary();
        assertNotNull(library.getSongs(), "Songs list should not be null after shuffling");
    }

    @Test
    void testSearchSongsByGenre() {
        LibraryModel library = new LibraryModel();
        Song song1 = new Song("Song1", "Artist1", "Pop", "2021", "Album1");
        Song song2 = new Song("Song2", "Artist1", "Rock", "2021", "Album1");
        library.addSong(song1);
        library.addSong(song2);
        ArrayList<Song> popSongs = library.searchSongsByGenre("Pop");
        assertTrue(popSongs.contains(song1) && !popSongs.contains(song2), "Should only return pop songs");
    }
    
    @Test
    void testShuffleExistingPlaylist() {
        LibraryModel library = new LibraryModel();
        Playlist playlist = new Playlist("TestPlaylist");
        Song song1 = new Song("Song1", "Artist1", "Genre1", "2021", "Album1");
        Song song2 = new Song("Song2", "Artist1", "Genre2", "2021", "Album2");
        playlist.addSong(song1);
        playlist.addSong(song2);

        library.shufflePlaylist("TestPlaylist");
        Playlist retrievedPlaylist = library.searchPlaylist("TestPlaylist");
    }

    @Test
    void testShuffleNonExistingPlaylist() {
        LibraryModel library = new LibraryModel();
        library.shufflePlaylist("NonExistentPlaylist");
        Playlist retrievedPlaylist = library.searchPlaylist("NonExistentPlaylist");
        assertNull(retrievedPlaylist, "Non-existent playlist should not be created after shuffling attempt");
    }

    
    @Test
    void testSortByRatingWithMixedRatings() {
        LibraryModel library = new LibraryModel();
        Song song1 = new Song("Song1", "Artist1", "Genre1", "2021", "Album1");
        Song song2 = new Song("Song2", "Artist1", "Genre2", "2021", "Album2");
        Song song3 = new Song("Song3", "Artist1", "Genre3", "2021", "Album3");
        Song song4 = new Song("Song4", "Artist1", "Genre1", "2021", "Album1");

        // Set ratings
        song1.setRating(new Rating(5)); // Highest rating
        song2.setRating(new Rating(3)); // Mid rating
        song3.setRating(new Rating(1)); // Lowest rating
        // song4 has no rating and should be treated as Integer.MAX_VALUE

        library.addSong(song1);
        library.addSong(song2);
        library.addSong(song3);
        library.addSong(song4);

        ArrayList<Song> sortedSongs = library.sortByRating();
        assertEquals(song3, sortedSongs.get(0), "Song3 with the lowest rating should be first");
        assertEquals(song2, sortedSongs.get(1), "Song2 with the mid rating should be second");
        assertEquals(song1, sortedSongs.get(2), "Song1 with the highest rating should be third");
        assertEquals(song4, sortedSongs.get(3), "Song4 with no rating should be last");
    }

    @Test
    void testSortByRatingAllUnrated() {
        LibraryModel library = new LibraryModel();
        Song song1 = new Song("Song1", "Artist1", "Genre1", "2021", "Album1");
        Song song2 = new Song("Song2", "Artist1", "Genre1", "2021", "Album2");

        // No ratings set for any song
        library.addSong(song1);
        library.addSong(song2);

        ArrayList<Song> sortedSongs = library.sortByRating();
        assertEquals(song1, sortedSongs.get(0), "Order should remain as added since all are treated as MAX_VALUE");
        assertEquals(song2, sortedSongs.get(1), "Order should remain as added since all are treated as MAX_VALUE");
    }

    @Test
    void testSortByRatingEmptyList() {
        LibraryModel library = new LibraryModel();
        ArrayList<Song> sortedSongs = library.sortByRating();
        assertTrue(sortedSongs.isEmpty(), "Sorted list should be empty if there are no songs in the library");
    }
    
    private User user;
    private String password = "securePassword";

    @BeforeEach
    void setUp() throws NoSuchAlgorithmException, InvalidKeySpecException {
        user = new User("testUser", password);
    }

    @Test
    void testUsername() {
        assertEquals("testUser", user.getUsername());
    }

    @Test
    void testPasswordVerification() throws NoSuchAlgorithmException, InvalidKeySpecException {
        assertTrue(user.verifyPassword(password));
        assertFalse(user.verifyPassword("wrongPassword"));
    }

    @Test
    void testWriteData() throws IOException {
        StringWriter stringWriter = new StringWriter();
        FileWriter fileWriter = new FileWriter("test_output.txt");
        user.writeData(fileWriter);
        fileWriter.close();
    }
    
    private Users users;

    @BeforeEach
    void setUp2() throws IOException {
        users = new Users();
    }

    @Test
    void testAddUser() throws NoSuchAlgorithmException, InvalidKeySpecException, IOException {
        User user = users.addUser("newUser", "password123");
        assertNotNull(user);
        assertEquals("newUser", user.getUsername());
        assertNull(users.addUser("newUser", "password123")); // Should not add duplicate user
    }

    @Test
    void testLogin() throws NoSuchAlgorithmException, InvalidKeySpecException, IOException {
        users.addUser("testUser", "testPass");
        assertNotNull(users.login("testUser", "testPass"));
        assertNull(users.login("testUser", "wrongPass"));
        assertNull(users.login("wrongUser", "testPass"));
    }

}