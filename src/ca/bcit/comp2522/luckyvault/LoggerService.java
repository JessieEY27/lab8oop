package ca.bcit.comp2522.luckyvault;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Manages the logging of each game session.
 *
 * @author Min Lee
 * @author Jessie Yuen
 *
 * @version 1.0
 */
public class LoggerService
{
    private static final String LOG_DIRECTORY = "data/logs";
    private static final String FILE_SUFFIX = "_COUNTRY.txt";

    private final BufferedWriter writer;

    /**
     * Constructs a LoggerService object and makes a session log file.
     */
    public LoggerService() throws IOException {
        final Path logDirectory;
        final String timestamp;
        final Path logFile;

        logDirectory = Paths.get(LOG_DIRECTORY);
        Files.createDirectories(logDirectory);

        timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd_HH-mm-ss"));
        logFile = logDirectory.resolve(timestamp + FILE_SUFFIX);

        writer = Files.newBufferedWriter(logFile,
                StandardOpenOption.CREATE,
                StandardOpenOption.APPEND);
    }

    /**
     * Logs a guess and the result.
     *
     * @param guess the player's guess
     * @param outcome the result of the guess
     */

    public void log(final String guess,
                    final String outcome)
    {
        final String time;
        final String line;

        try
        {
            time = LocalDateTime.now().format(DateTimeFormatter.ofPattern("HH:mm:ss"));

            line = time + " | " + guess + " | " + outcome;

            writer.write(line);
            writer.newLine();
            writer.flush();
        }
        catch(final IOException e)
        {
            System.err.println("Logging error: " + e.getMessage());
        }
    }

    /**
     * Closes the log file.
     */

    public void closeFile()
    {
        try
        {
            writer.close();
        }
        catch(final IOException e)
        {
            System.err.println("Failed to close log file.");
        }
    }
}
