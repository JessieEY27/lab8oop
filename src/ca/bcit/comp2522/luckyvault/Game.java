package ca.bcit.comp2522.luckyvault;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;
import java.io.IOException;

/**
 * Demonstrates the country guessing game.
 *
 * @author Min Lee
 * @author Jessie Yuen
 *
 * @version 1.0
 */
public final class Game
{
    private static final String COUNTRY_FILE = "data/countries.txt";
    private static final String SCORE_FILE = "data/highscore.txt";
    private static final int INITIAL_GUESSING_COUNT = 0;
    private static final int CORRECT_WORD_COUNT = 0;

    private final WordList wordList;
    private final Scanner inputScanner;
    private final HighScoreService scoreService;

    /**
     *  Constructs a Game object
     *  Loads the county list from a file and prepares the user input scanner.
     */
    public Game() throws IOException
    {
        this.wordList = new WordList(COUNTRY_FILE);
        this.scoreService = new HighScoreService(SCORE_FILE);
        this.inputScanner = new Scanner(System.in, StandardCharsets.UTF_8);
    }

    /**
     * Executes the guessing game for one round
     */
    public void play()
    {
        final String secretWord;
        final int secretWordLength;
        final String lowerSecretWord;

        String input;
        String trimmedInput;
        String lowerInput;

        int guessingCount;
        boolean isValidInput;

        secretWord = this.wordList.getRandomCountry();
        secretWordLength = secretWord.length();
        lowerSecretWord = secretWord.toLowerCase();
        guessingCount = INITIAL_GUESSING_COUNT;

        System.out.println(secretWord); // 삭제 필요

        System.out.println("LUCKY VAULT - COUNTRY MODE. Type QUIT to exit");
        System.out.println("Secret word length: " + secretWordLength);
        System.out.println("Current best: —"); // === needs to be fixed =====

        while(true)
        {
            System.out.print("Your guess: ");
            input = this.inputScanner.nextLine();

            trimmedInput = input.trim();
            isValidInput = validateUserInput(trimmedInput);
            lowerInput = trimmedInput.toLowerCase();

            if(!isValidInput)
            {
                System.out.println("Empty guess. Try again.");
                continue;
            }

            if(trimmedInput.equalsIgnoreCase("QUIT"))
            {
                System.out.println("Bye!");
                break;
            }

            // guessing game start
            guessingCount++;

            if(trimmedInput.length() != secretWordLength)
            {
                System.out.println("Wrong length (" + trimmedInput.length() +")." +
                        " Need " + secretWordLength + ".");
                continue;
            }

            // Wrong answer
            if(!lowerInput.equals(lowerSecretWord))
            {
                int correctCount;
                correctCount = CORRECT_WORD_COUNT;


                for(int i= 0; i < secretWordLength; i++)
                {
                    if(lowerInput.charAt(i) == lowerSecretWord.charAt(i))
                    {
                        correctCount++;
                    }
                }

                System.out.println("Not it. " + correctCount +
                        " letter(s) correct (right position).");
            }
            else
            {
                // =   =====needs to be checked if it is the highscore. =====
                System.out.println("Correct in " + guessingCount + " attempts!" +
                        " Word was: " + secretWord);
                break;
            }


        }

    }

    /**
     * Validates if the input is appropriate for the game
     *
     * @param trimmedInput the user input without whitespace to validate.
     *
     * @return true if it is not null or empty, false otherwise
     */
    private boolean validateUserInput(final String trimmedInput)
    {
        if(trimmedInput == null || trimmedInput.isEmpty())
        {
            return false;
        }

        return true;
    }

    /**
     * Drives the program
     *
     * @param args unused.
     */
    public static void main(final String[] args)
    {
        final Game game;

        try
        {
            game = new Game();
            game.play();
        }
        catch (final IOException e)
        {
            System.err.println("Error: Could not load the word list. " + e.getMessage());
        }
        catch (final IllegalStateException e)
        {
            System.err.println("Error: The game state is invalid. " + e.getMessage());
        }
        catch (final Exception e)
        {
            System.err.println("Unexpected error: " + e.getMessage());
        }
    }
}
