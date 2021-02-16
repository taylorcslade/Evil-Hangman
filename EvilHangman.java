package hangman;
import java.io.File;
import java.io.IOException;
import java.util.Scanner;
import java.lang.String;
import java.lang.StringBuilder;
import java.util.Set;

public class EvilHangman {

    public static void main(String[] args) throws IOException, EmptyDictionaryException, GuessAlreadyMadeException {
        File dictionaryOfChoice = new File(args[0]);
        int wordLength = Integer.valueOf(args[1]);
        int numGuesses = Integer.valueOf(args[2]);
        String fileName = args[0];
        StringBuilder wordToPrint = new StringBuilder();

        if((wordLength > 2) && (numGuesses > 1)){
            System.out.print("java EvilHangman " + fileName + " " + wordLength + " " + numGuesses + "\n");
            EvilHangmanGame game = new EvilHangmanGame();
            game.startGame(dictionaryOfChoice, wordLength);
            for(int i = 0; i < wordLength; ++i){
                wordToPrint.append('-');
            }

            while(numGuesses != 0){
                System.out.print("You have " + numGuesses + " left \n");
                System.out.print("Used letters: " + game.guessedLettersToString() + "\n");
                System.out.print("Word: " + wordToPrint + "\n");
                char userChar = getUserInput();

                try {
                    game.makeGuess(userChar);
                    if (game.currentPattern.contains(Character.toString(userChar))) {
                        int numTimesCharOccurs = 0;
                        for (int i = 0; i < wordToPrint.length(); ++i) {
                            if (game.currentPattern.charAt(i) == userChar) {
                                numTimesCharOccurs++;
                                wordToPrint.setCharAt(i, userChar);
                            }
                        }
                        System.out.print("Yes, there is " + numTimesCharOccurs + " " + userChar + "\n\n");
                        if(!wordToPrint.toString().contains("-")){
                            System.out.print("You win! You guessed the word: " + wordToPrint + "\n");
                            break;
                        }
                    } else {
                        numGuesses--;
                        System.out.print("Sorry, there are no " + userChar + "\n\n");
                    }
                }catch (GuessAlreadyMadeException error)
                {
                    System.out.print(error.printError() + "\n\n");
                }
            }
            if(numGuesses == 0){
                String rightWord = "";
                for(String word: game.PossibleWords){
                    rightWord = word;
                    break;
                }
                System.out.print("You Lost!  The word was: " + rightWord + "\n");
            }
        }
    }

    public static char getUserInput() {
        System.out.print("Enter guess: ");
        Scanner in = new Scanner(System.in);
        String userInput = in.next();
        char userChar = ' ';

        if(!userInput.isEmpty()) {
            userChar = userInput.charAt(0);
            userChar = Character.toLowerCase(userChar);
            if((!Character.isAlphabetic(userChar)) || (userInput.length() > 1)){
                System.out.print("Invalid input! ");
                getUserInput();
            }
        }
        return userChar;
    }

}
