package proj1;

/**
 * Represents a rating system for songs. Ensures that ratings are within a valid
 * range (1 to 5).
 */
public class Rating {

	private int rating; // The rating value (1 to 5)

	/**
	 * Constructor to initialize a Rating object with a given value.
	 * 
	 * @param initialRating The initial rating value.
	 * @throws IllegalArgumentException if the rating is not between 1 and 5.
	 */
	public Rating(int initialRating) {
		setRating(initialRating);
	}

	/**
	 * Sets the rating value.
	 * 
	 * @param newRating The new rating to be set.
	 * @throws IllegalArgumentException if the rating is not within the valid range
	 *                                  (1 to 5).
	 */
	public void setRating(int newRating) {

		if (newRating < 1 || newRating > 5) {
			throw new IllegalArgumentException("Rating must be between 1 and 5.");
		}
		this.rating = newRating;
	}

	/**
	 * Retrieves the current rating value.
	 * 
	 * @return The rating as an integer.
	 */
	public int getRating() {
		return rating;
	}

}
