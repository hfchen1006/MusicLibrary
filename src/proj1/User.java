package proj1;

import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import java.util.Base64;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import java.io.FileWriter;
import java.io.IOException;

/**
 * Represents a user in the system. Each user has a username, a password (stored
 * securely), and a music library.
 */
public class User {
	private static final int SALT_LENGTH = 16; // Length of the salt used for hashing
	private static final int ITERATIONS = 65536; // Number of iterations for key derivation
	private static final int KEY_LENGTH = 256; // Length of the derived key in bits

	private String username;
	private LibraryModel library;
	private String salt;
	private String hashedPassword;

	/**
	 * Constructor to create a new user account. The password is hashed using a salt
	 * and stored securely.
	 * 
	 * @param username The username of the user.
	 * @param password The plain text password to be hashed and stored.
	 * @throws NoSuchAlgorithmException If the hashing algorithm is not available.
	 * @throws InvalidKeySpecException  If the key specification is invalid.
	 */
	public User(String username, String password) throws NoSuchAlgorithmException, InvalidKeySpecException {
		this.username = username;
		this.library = new LibraryModel();
		this.salt = generateSalt();
		this.hashedPassword = hashPassword(password, Base64.getDecoder().decode(salt));
	}

	/**
	 * Verifies if the entered password matches the stored hashed password.
	 * 
	 * @param enteredPassword The password entered by the user.
	 * @return True if the password is correct, false otherwise.
	 * @throws NoSuchAlgorithmException If the hashing algorithm is not available.
	 * @throws InvalidKeySpecException  If the key specification is invalid.
	 */
	public boolean verifyPassword(String enteredPassword) throws NoSuchAlgorithmException, InvalidKeySpecException {
		String hashedEnteredPassword = hashPassword(enteredPassword, Base64.getDecoder().decode(this.salt));
		return hashedEnteredPassword.equals(this.hashedPassword);
	}

	/**
	 * Generates a new salt value for password hashing.
	 * 
	 * @return A base64-encoded string representing the generated salt.
	 */
	private static String generateSalt() {
		SecureRandom random = new SecureRandom();
		byte[] salt = new byte[SALT_LENGTH];
		random.nextBytes(salt);
		return Base64.getEncoder().encodeToString(salt);
	}

	/**
	 * Hashes the password using PBKDF2 with HMAC-SHA256.
	 * 
	 * @param password The password to be hashed.
	 * @param salt     The salt used for hashing.
	 * @return The base64-encoded hashed password.
	 * @throws NoSuchAlgorithmException If the hashing algorithm is not available.
	 * @throws InvalidKeySpecException  If the key specification is invalid.
	 */
	private static String hashPassword(String password, byte[] salt)
			throws NoSuchAlgorithmException, InvalidKeySpecException {
		PBEKeySpec spec = new PBEKeySpec(password.toCharArray(), salt, ITERATIONS, KEY_LENGTH);
		SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256");
		byte[] hash = factory.generateSecret(spec).getEncoded();
		return Base64.getEncoder().encodeToString(hash);
	}

	public String getUsername() {
		return username;
	}

	public LibraryModel getLibrary() {
		return library;
	}

	/**
	 * Writes the user's data (username, salt, and hashed password) to a file.
	 * 
	 * @param writer The FileWriter object used to write data to the file.
	 * @throws IOException If an I/O error occurs.
	 */
	public void writeData(FileWriter writer) throws IOException {
		writer.write("Username: " + this.username + "\n");
		writer.write("Salt: " + this.salt + "\n");
		writer.write("Hashed Password: " + this.hashedPassword + "\n");
	}
}
