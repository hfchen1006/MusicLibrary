package proj1;

import java.util.HashMap;
import java.util.Map;
import java.security.spec.InvalidKeySpecException;
import java.security.NoSuchAlgorithmException;
import java.io.FileWriter;
import java.io.IOException;

/**
 * Manages a collection of user accounts. Provides functionality to add users,
 * log in, and store user data.
 */
public class Users {
	private Map<String, User> users;
	private FileWriter writer;

	/**
	 * Constructor to initialize the Users object. Creates an empty user list and
	 * opens a file to store user data.
	 * 
	 * @throws IOException If an I/O error occurs.
	 */
	public Users() throws IOException {
		this.users = new HashMap<String, User>();
		this.writer = new FileWriter("user_data.txt", true);
	}

	/**
	 * Adds a new user to the system. If the username already exists, the user is
	 * not added.
	 * 
	 * @param username The username of the new user.
	 * @param password The password for the new user.
	 * @return The newly created User object, or null if the username already
	 *         exists.
	 * @throws InvalidKeySpecException  If the key specification is invalid.
	 * @throws NoSuchAlgorithmException If the hashing algorithm is not available.
	 * @throws IOException              If an I/O error occurs while writing user
	 *                                  data.
	 */
	public User addUser(String username, String password)
			throws InvalidKeySpecException, NoSuchAlgorithmException, IOException {
		User user = users.get(username);
		if (user == null) {
			User newUser = new User(username, password);
			users.put(username, newUser);
			newUser.writeData(this.writer);
			this.writer.flush();
			return newUser;
		}
		return null;
	}

	/**
	 * Logs in a user by verifying the username and password. If the username or
	 * password is incorrect, the login fails.
	 * 
	 * @param username The username of the user attempting to log in.
	 * @param password The password of the user attempting to log in.
	 * @return The User object if login is successful, otherwise null.
	 * @throws InvalidKeySpecException  If the key specification is invalid.
	 * @throws NoSuchAlgorithmException If the hashing algorithm is not available.
	 */
	public User login(String username, String password) throws InvalidKeySpecException, NoSuchAlgorithmException {
		User user = users.get(username);
		if (user != null) {
			if (user.verifyPassword(password)) {
				System.out.println("Login Successful!");
				return user;
			} else {
				System.out.println("Incorrect Password!");
				return null;
			}
		} else {
			System.out.println("Incorrect Username!");
			return null;
		}
	}
}
