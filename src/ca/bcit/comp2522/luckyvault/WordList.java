package ca.bcit.comp2522.luckyvault;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Load all country names from countries.txt and choose one country uniformly at random.
 *
 * @author Min Lee
 * @author Jessie Yuen
 *
 * @version 1.0
 */
public final class WordList
{
    private static final int EMPTY = 0;
    private final Path path;
    private final List<String> countries;
    private final Random random;

    /**
     * Constructs a WorldList object
     *
     * @param fileName the filename to read
     */
    public WordList(final String fileName) throws IOException
    {
        this.path = Paths.get(fileName);
        this.countries = new ArrayList<>();
        this.random = new Random();

        validatePath(this.path);
        loadCountries();
    }

    /**
     * Validates a file
     *
     * @param path the path of the file
     */
    private static void validatePath(final Path path) throws IOException
    {
        if(!Files.exists(path))
        {
            throw new IOException("File doesn't exist: " + path);
        }

        if(Files.size(path) == EMPTY)
        {
            throw new IOException("File is empty.");
        }
    }

    /**
     * Load the countries from a countries.txt file.
     */
    private void loadCountries() throws IOException
    {
        try(final BufferedReader reader = Files.newBufferedReader(this.path,
                                                    StandardCharsets.UTF_8))
        {
            String line;
            while((line = reader.readLine()) != null)
            {
                final String trimmedLine = line.trim();

                if(!trimmedLine.isEmpty())
                {
                    this.countries.add(trimmedLine);
                }
            }

            if(this.countries.isEmpty())
            {
                throw new IOException("No countries found in the file");
            }

        }
    }

    /**
     *  Choose one random country from the list
     */
    public String getRandomCountry() throws IllegalStateException
    {
        if(this.countries == null || this.countries.isEmpty())
        {
            throw new IllegalStateException("No countries in the list");
        }

        final int randomIndex;
        final String randomCountry;

        randomIndex = this.random.nextInt(this.countries.size());
        randomCountry = this.countries.get(randomIndex);

        return randomCountry;
    }
}
