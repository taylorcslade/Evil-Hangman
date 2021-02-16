package hangman;

public class EmptyDictionaryException extends Exception {
	//Thrown when dictionary file is empty or no words in dictionary match the length asked for
    public String showMessage;
    public EmptyDictionaryException(String messageToShow){
        showMessage = messageToShow;
    }

    public void printMessage(){
        System.out.print(showMessage);
    }
}
