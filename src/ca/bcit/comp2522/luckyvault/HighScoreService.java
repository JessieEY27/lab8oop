package ca.bcit.comp2522.luckyvault;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;

/**
 * Read and write the best (the least) guessing score.
 *
 * @author Min Lee
 * @author Jessie Yuen
 *
 * @version 1.0
 */
public final class HighScoreService
{
    private static final String KEY = "COUNTRY=";
    public static final int NO_BEST_SCORE = Integer.MAX_VALUE;

    private final Path path;

    /**
     * Constructs a HighScoreService object
     *
     * @param fileName the filename to read
     */
    public HighScoreService(final String fileName)
    {
        this.path = Paths.get(fileName);
    }

    /**
     * Read the high score from the file.
     *
     * @return the best score or  NO_BEST_SCORE if none exists or the file is malformed.
     */
    public int readBestScore()
    {
        if(Files.notExists(this.path))
        {
            return NO_BEST_SCORE;
        }

        try
        {
            final String content;
            content = Files.readString(this.path).trim();

            if(content.startsWith(KEY))
            {
                return Integer.parseInt(content.substring(KEY.length()));
            }
        }
        catch (final IOException | NumberFormatException e)
        {

            return NO_BEST_SCORE;
        }

        return NO_BEST_SCORE;
    }

    /**
     * Save the high score to the file.
     *
     * @param newHighScore new score to be updated
     */
    public void saveHighScore(final int newHighScore)
    {
        final String updatedScore;
        updatedScore = KEY + newHighScore;

        try
        {
            if(Files.notExists(this.path))
            {
                Files.createFile(this.path);
            }

            Files.writeString(this.path, updatedScore,
                    StandardOpenOption.CREATE,
                    StandardOpenOption.WRITE,
                    StandardOpenOption.TRUNCATE_EXISTING);
        }
        catch (final IOException e)
        {
            System.err.println("Error saving high score" + e.getMessage());
        }
    }
}
