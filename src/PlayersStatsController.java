public class PlayersStatsController {
    private WordleController wordleController;
    private PlayerStats currentPlayerStats;
    private Account currentAccount;

    public PlayerStats getCurrentPlayerStats() {
        return currentPlayerStats;
    }

    public void setWordleController(WordleController wordleController) {
        this.wordleController = wordleController;
    }

    public Account getCurrentAccount() {
        return currentAccount;
    }

    public void setCurrentAccount(Account account) {
        this.currentAccount = account;
    }
}