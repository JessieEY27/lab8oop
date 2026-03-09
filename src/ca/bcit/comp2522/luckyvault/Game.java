package ca.bcit.comp2522.luckyvault;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;

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
    private final LoggerService loggerService;

    /**
     *  Constructs a Game object
     *  Loads the county list from a file and prepares the user input scanner.
     */
    public Game() throws IOException
    {
        this.wordList = new WordList(COUNTRY_FILE);
        this.scoreService = new HighScoreService(SCORE_FILE);
        this.inputScanner = new Scanner(System.in, StandardCharsets.UTF_8);
        this.loggerService = new LoggerService();
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

        System.out.println("LUCKY VAULT - COUNTRY MODE. Type QUIT to exit");
        System.out.println("Secret word length: " + secretWordLength);
        displayHighScore();

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

            // Guessing game start
            guessingCount++;

            if(trimmedInput.length() != secretWordLength)
            {
                System.out.println("Wrong length (" + trimmedInput.length() +")." +
                        " Need " + secretWordLength + ".");
                loggerService.log(trimmedInput, "wrong_length");
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
                loggerService.log(trimmedInput, "matches=" + correctCount);
            }
            else
            {
                System.out.println("Correct in " + guessingCount + " attempts!" +
                        " Word was: " + secretWord);
                loggerService.log(trimmedInput, "CORRECT in " + guessingCount);

                compareHighScore(guessingCount);
                break;
            }
        }

        loggerService.closeFile();
    }

    /**
     *  Read the current best score from the file and display it.
     */
    private void displayHighScore()
    {
        final int bestScore;
        bestScore = this.scoreService.readBestScore();

        if(bestScore == HighScoreService.NO_BEST_SCORE)
        {
            System.out.println("Current best: —");
        }
        else
        {
            System.out.println("Current best: " + bestScore + "attempts");
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
     * Compares the current guessing count with the best score.
     * Update the high score if the current count is lower
     *
     * @param guessingCounts the number of guesses taken to find the correct answer.
     */
    private void compareHighScore(final int guessingCounts)
    {
        final int bestScore;
        bestScore = this.scoreService.readBestScore();

        if(bestScore == HighScoreService.NO_BEST_SCORE ||
                guessingCounts < bestScore)
        {
            this.scoreService.saveHighScore(guessingCounts);
            System.out.println("NEW BEST for COUNTRY mode!");
        }
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
        catch(final IOException e)
        {
            System.err.println("Error: Could not load the word list. " + e.getMessage());
        }
        catch(final IllegalStateException e)
        {
            System.err.println("Error: The game state is invalid. " + e.getMessage());
        }
        catch(final Exception e)
        {
            System.err.println("Unexpected error: " + e.getMessage());
        }
    }
}
