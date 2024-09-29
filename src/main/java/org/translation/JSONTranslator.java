package org.translation;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;

/**
 * An implementation of the Translator interface which reads in the translation
 * data from a JSON file. The data is read in once each time an instance of this class is constructed.
 */
public class JSONTranslator implements Translator {
    // map to store "country code" : map of "language code"

    private final Map<String, Map<String, Object>> translateMap = new LinkedHashMap<>();

    /**
     * Constructs a JSONTranslator using data from the sample.json resources file.
     */
    public JSONTranslator() {
        this("sample.json");
    }

    /**
     * Constructs a JSONTranslator populated using data from the specified resources file.
     * @param filename the name of the file in resources to load the data from
     * @throws RuntimeException if the resource file can't be loaded properly
     */
    public JSONTranslator(String filename) {
        // read the file to get the data to populate things...
        try {

            String jsonString = Files.readString(Paths.get(getClass().getClassLoader().getResource(filename).toURI()));
            JSONArray jsonArray = new JSONArray(jsonString);

            for (int i = 0; i < jsonArray.length(); i++) {
                // languageCodes : map of all the languages a country is available in
                Map<String, Object> languageCodes;
                languageCodes = jsonArray.getJSONObject(i).toMap();
                languageCodes.remove("alpha3");
                languageCodes.remove("id");
                languageCodes.remove("alpha2");
                this.translateMap.put(jsonArray.getJSONObject(i).getString("alpha3"), languageCodes);
            }
            System.out.println(this.translateMap);

        }
        catch (IOException | URISyntaxException ex) {
            throw new RuntimeException(ex);
        }
    }

    @Override
    public List<String> getCountryLanguages(String country) {
        return new ArrayList<>(this.translateMap.get(country).keySet());

    }

    @Override
    public List<String> getCountries() {
        return new ArrayList<>(this.translateMap.keySet());
    }

    @Override
    public String translate(String country, String language) {
        return this.translateMap.get(country).get(language).toString();
    }
}
