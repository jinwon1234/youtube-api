package apptive.youtubeAPI.youtube;

import com.fasterxml.jackson.core.JsonFactory;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.gson.GsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.services.youtube.YouTube;
import com.google.api.services.youtube.model.SearchListResponse;
import com.google.api.services.youtube.model.SearchResult;
import com.google.api.services.youtube.model.Video;
import com.google.api.services.youtube.model.VideoListResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

@Service
public class YoutubeService {

    @Value("${youtube.api.key}")
    private String apiKey;
    private static final GsonFactory gsonFactory = new GsonFactory();

    public List<YoutubeVideoResponse> searchVideo(YoutubeSearchRequest searchRequest) {


        YouTube youtube = new YouTube.
                Builder(
                        new NetHttpTransport(), gsonFactory, request -> {}
        ).build();

        try {
            SearchListResponse searchResponse = youtube.search()
                    .list(Collections.singletonList("id,snippet"))
                    .setQ(searchRequest.artist() + " " + searchRequest.title())
                    .setType(Collections.singletonList("video"))
                    .setMaxResults(5L)
                    .setKey(apiKey)
                    .execute();

            List<String> videoIds = searchResponse.getItems().stream()
                    .map(r -> r.getId().getVideoId())
                    .filter(Objects::nonNull)
                    .toList();


            VideoListResponse videoResponse = youtube.videos()
                    .list(Collections.singletonList("snippet,contentDetails,statistics"))
                    .setId(Collections.singletonList(String.join(",", videoIds)))
                    .setKey(apiKey)
                    .execute();

            Video first = videoResponse.getItems().getFirst();



            return videoResponse.getItems()
                    .stream().map(YoutubeVideoResponse::new).toList();

        } catch (IOException e) {
            throw new RuntimeException("유튜브 검색중 예외 발생");
        }
    }

}
