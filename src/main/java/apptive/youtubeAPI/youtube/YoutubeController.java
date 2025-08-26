package apptive.youtubeAPI.youtube;

import com.google.api.services.youtube.model.SearchResult;
import lombok.RequiredArgsConstructor;
import org.apache.catalina.connector.Response;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class YoutubeController {

    private final  YoutubeService youtubeService;

    @GetMapping("/musics")
    public String playForm(@ModelAttribute YoutubeSearchRequest searchRequest,
                              Model model) {

        List<YoutubeVideoResponse> response = youtubeService.searchVideo(searchRequest);

        model.addAttribute("videos", response);

        return "video";
    }

    @GetMapping()
    public String searchForm() {
        return "home";
    }



}
