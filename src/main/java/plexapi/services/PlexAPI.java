package plexapi.services;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import plexapi.beans.PlexEpisode;
import plexapi.beans.PlexSeason;
import plexapi.beans.PlexShow;
import plexapi.exceptions.PlexGetException;
import plexapi.exceptions.PlexScanException;
import plexapi.exceptions.PlexShowFetchingException;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.*;

/**
 * Created by Romain on 10/11/2018.
 */
public class PlexAPI {
    private String urlString;

    private String token;

    public PlexAPI(String urlString, String token) {
        this.urlString = urlString;
        this.token = token;
    }

    public List<PlexShow> getShows() throws PlexShowFetchingException {
        try {
            List<PlexShow> shows = new ArrayList<>();

            ObjectMapper mapper = new ObjectMapper();
            mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

            ArrayNode sectionsNode = (ArrayNode) mapper.readTree(getRequest(urlString + "/library/sections")).get("MediaContainer").get("Directory");
            for (JsonNode sectionNode : sectionsNode) {
                if (sectionNode.get("type").asText().equals("show")) {
                    String key = sectionNode.get("key").asText();
                    ArrayNode showsNode = (ArrayNode) mapper.readTree(getRequest(urlString + "/library/sections/" + key + "/all")).get("MediaContainer").get("Metadata");
                    for (JsonNode showNode : showsNode) {
                        PlexShow show = mapper.readValue(showNode.toString(), PlexShow.class);
                        show.setSeasons(getSeasons(show));
                        shows.add(show);
                    }
                }
            }

            return shows;
        } catch (PlexShowFetchingException e) {
            throw e;
        } catch (Exception e) {
            throw new PlexShowFetchingException();
        }
    }

    public PlexShow getShow(String title) throws PlexShowFetchingException {
        try {
            PlexShow show = null;

            ObjectMapper mapper = new ObjectMapper();
            mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

            ArrayNode sectionsNode = (ArrayNode) mapper.readTree(getRequest(urlString + "/library/sections")).get("MediaContainer").get("Directory");
            for (JsonNode sectionNode : sectionsNode) {
                if (sectionNode.get("type").asText().equals("show")) {
                    String key = sectionNode.get("key").asText();
                    ArrayNode showsNode = (ArrayNode) mapper.readTree(getRequest(urlString + "/library/sections/" + key + "/all")).get("MediaContainer").get("Metadata");
                    for (JsonNode showNode : showsNode) {
                        if (showNode.get("title").asText().equals(title)) {
                            show = mapper.readValue(showNode.toString(), PlexShow.class);
                            show.setSeasons(getSeasons(show));
                        }
                    }
                }
            }

            return show;
        } catch (Exception e) {
            throw new PlexShowFetchingException(title);
        }
    }

    public Map<Integer, PlexSeason> getSeasons(PlexShow show) throws PlexShowFetchingException {
        try {
            Map<Integer, PlexSeason> seasons = new HashMap<>();
            Integer seasonNumber = null;

            ObjectMapper mapper = new ObjectMapper();
            mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

            ArrayNode seasonsNode = (ArrayNode) mapper.readTree(getRequest(urlString + show.getKey())).get("MediaContainer").get("Metadata");
            for (JsonNode seasonNode : seasonsNode) {
                PlexSeason season = mapper.readValue(seasonNode.toString(), PlexSeason.class);
                season.setEpisodes(getEpisodes(season));
                seasonNumber = seasonNode.get("index").asInt();
                seasons.put(seasonNumber, season);
            }

            return seasons;
        } catch (Exception e) {
            throw new PlexShowFetchingException(show.getTitle());
        }
    }

    public Map<Integer, PlexEpisode> getEpisodes(PlexSeason season) throws PlexShowFetchingException {
        try {
            Map<Integer, PlexEpisode> episodes = new HashMap<>();

            ObjectMapper mapper = new ObjectMapper();
            mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

            ArrayNode showsNode = (ArrayNode) mapper.readTree(getRequest(urlString + season.getKey())).get("MediaContainer").get("Metadata");
            for (JsonNode seasonNode : showsNode) {
                PlexEpisode episode = mapper.readValue(seasonNode.toString(), PlexEpisode.class);
                episodes.put(seasonNode.get("index").asInt(), episode);
            }

            return episodes;
        } catch (Exception e) {
            throw new PlexShowFetchingException();
        }
    }

    public void scanLibrary() throws PlexScanException {
        try {
            ObjectMapper mapper = new ObjectMapper();
            mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

            ArrayNode sectionsNode = (ArrayNode) mapper.readTree(getRequest(urlString + "/library/sections")).get("MediaContainer").get("Directory");
            for (JsonNode sectionNode : sectionsNode) {
                scanSection(sectionNode.get("key").asText());
            }
        } catch (Exception e) {
            throw new PlexScanException();
        }
    }

    public void scanSection(String key) throws PlexScanException {
        try {
            getRequest(urlString + "/library/sections/" + key + "/refresh");
        } catch (Exception e) {
            throw new PlexScanException();
        }
    }

    private String getRequest(String url) throws PlexGetException {
        try {
            HttpURLConnection connection = (HttpURLConnection) new URL(url).openConnection();
            connection.setRequestProperty("X-Plex-Token", token);
            connection.setRequestProperty("Accept", "application/json");

            BufferedReader br = new BufferedReader(new InputStreamReader((connection.getInputStream())));
            StringBuilder sb = new StringBuilder();
            String output;
            while ((output = br.readLine()) != null) {
                sb.append(output);
            }

            return sb.toString();
        } catch (Exception e) {
            throw new PlexGetException(url);
        }
    }
}
