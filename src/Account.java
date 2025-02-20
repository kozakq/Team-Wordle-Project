
/*
 * Course: Software Tools & Process
 * Spring 2025
 * Group
 ** @author childressg
 ** @author OrugPeli
 ** @author Griffithjr
 ** @author kozakq
 * @version 1.0
 */
import java.util.List;
import java.util.Map;

public class Account {

	private int accountID;
	private Map<String, Integer> guessList;
	private List<Integer> numGuessList;
	private String password;
	private int playerID;
	private String username;
	private UserType userType;

	public Account(String username, String password){
		this.username = username;
		this.password = password;
		this.playerID = AccountID.getNextID();
		this.userType = UserType.USER;
	}

	public double getAverageGuesses(){
		return 0;
	}

	public List<String> getMostCommonGuesses(){
		return null;
	}

	public void saveToFile(){

	}

	public String getUsername() {
		return username;
	}

	public String getPassword() {
		return password;
	}

}