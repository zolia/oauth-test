
package lt.tendo.mm.restapi.errors;


@SuppressWarnings("serial")
public class UserNotFoundException extends RuntimeException {

	public UserNotFoundException(String userId) {
		super("could not find user '" + userId + "'.");
	}
}

