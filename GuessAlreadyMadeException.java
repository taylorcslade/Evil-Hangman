package hangman;

public class GuessAlreadyMadeException extends Exception {
    public char character;

    public GuessAlreadyMadeException(char guess) {
        character = guess;
    }
    public String printError(){
        return ("Guess already made! ");
    }
}
