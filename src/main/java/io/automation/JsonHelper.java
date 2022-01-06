package io.automation;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public final class JsonHelper {

    public static final String MSG_NOT_POSSIBLE_TO_READ_JSON =
            "Not possible to read json file";

    private JsonHelper() {
    }

    public static JSONObject getJsonObject(final String configJsonPath) {
        JSONObject jsonObject;
        JSONParser parser = new JSONParser();
        try (InputStream inputStream = new FileInputStream(configJsonPath)) {
            Reader fileReader = new InputStreamReader(inputStream);
            jsonObject = (JSONObject) parser.parse(fileReader);
        } catch (IOException | ParseException e) {
            throw new RuntimeException(MSG_NOT_POSSIBLE_TO_READ_JSON);
        }
        return jsonObject;
    }
}
