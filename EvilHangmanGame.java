package hangman;

import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.Set;
import java.util.SortedSet;
import java.util.Map;

public class EvilHangmanGame implements IEvilHangmanGame{
    public SortedSet<Character> guessedLetters = new TreeSet<>();
    public Set<String> PossibleWords = new TreeSet<>();
    public String currentPattern = "";

    @Override
    public void startGame(File dictionary, int wordLength) throws IOException, EmptyDictionaryException {
        Scanner scanner = new Scanner(dictionary);
        String wordToAdd;
        PossibleWords.clear();
        guessedLetters.clear();

        if(dictionary.length() > 0) {
            while (scanner.hasNext()) {
                wordToAdd = scanner.next();
                if (wordToAdd.length() == wordLength) {
                    PossibleWords.add(wordToAdd);
                } else {
                    continue;
                }
            }
        }
        else{
            throw new EmptyDictionaryException("There are no words of given length");
        }

        if(PossibleWords.isEmpty()){
            throw new EmptyDictionaryException("The dictionary is empty");
        }

        for(int i = 0; i < wordLength; ++i){
            currentPattern = currentPattern + "-";
        }
    }

    @Override
    public Set<String> makeGuess(char guess) throws GuessAlreadyMadeException {
        SortedMap<String, Set<String>> patternMap = new TreeMap<>();
        guess = Character.toLowerCase(guess);

        for(char character : guessedLetters){
            if(character == guess){
                throw new GuessAlreadyMadeException(guess);
            }
        }
        guessedLetters.add(guess);

        for(String word: PossibleWords){
            String pattern = patternMaker(word, guess);
            if(patternMap.containsKey(pattern)){
                patternMap.get(pattern).add(word);
            }
            else{
                Set<String> wordsOfSpecificPattern = new TreeSet<>();
                wordsOfSpecificPattern.add(word);
                patternMap.put(pattern, wordsOfSpecificPattern);
            }
        }

        Map.Entry<String, Set<String>> chooseThisSet = null;
        int maxSetSize = 0;
        for(Map.Entry<String, Set<String>> entry : patternMap.entrySet()){
            int setSize = entry.getValue().size();
            if(setSize > maxSetSize){
                maxSetSize = setSize;
                chooseThisSet = entry;
            }
            else if(setSize == maxSetSize){
                if(!entry.getKey().contains(Character.toString(guess))){
                    chooseThisSet = entry;
                }
                else if (!chooseThisSet.getKey().contains(Character.toString(guess))){
                    continue;
                }
                else if((entry.getKey().contains(Character.toString(guess))) &&
                        (chooseThisSet.getKey().contains(Character.toString(guess)))) {
                    int chooseNum = getNumChars(chooseThisSet.getKey(), guess);
                    int entryNum = getNumChars(entry.getKey(), guess);
                    if(entryNum < chooseNum){
                        chooseThisSet = entry;
                    }
                    else if (entryNum == chooseNum){
                        if(entry.getKey().lastIndexOf(guess) > chooseThisSet.getKey().lastIndexOf(guess)) {
                            chooseThisSet = entry;
                        }
                    }
                }
            }
        }

        PossibleWords = chooseThisSet.getValue();
        currentPattern = chooseThisSet.getKey();

        return PossibleWords;
    }

    public int getNumChars(String patternToParse, char guess){
        int numChars = 0;
        for(int i = 0; i < patternToParse.length(); ++i){
            if(patternToParse.charAt(i) == guess){
                numChars ++;
            }
        }
        return numChars;
    }

    public String patternMaker(String word, char guess){
        String returnPattern = "";

        for(int i = 0; i < word.length(); ++i){
            if(word.charAt(i) == guess){
                returnPattern = returnPattern + guess;
            }
            else{
                returnPattern = returnPattern + '-';
            }
        }
        //System.out.print("returnPattern: " + returnPattern + "\n");
        return returnPattern;
    }

    @Override
    public SortedSet<Character> getGuessedLetters() {
        return guessedLetters;
    }

    public String guessedLettersToString(){
        String letterString = "";
        for (char character : guessedLetters){
            letterString = letterString + " " + character;
        }
        return letterString;
    }

}
