package uatransport.telegrambot.converters;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import uatransport.telegrambot.entity.FeedbackModel;
import uatransport.telegrambot.models.FeedbackDTO;

import java.util.List;

@Component
public class FeedBackConverter {
    @Autowired
    private RestTemplate restTemplate;

    public FeedbackDTO convert(FeedbackModel feedbackModel) {
        if (feedbackModel.getQuestion().getType().equals("RATING")) {
            FeedbackDTO feedbackDTO = new FeedbackDTO();
            feedbackDTO.setCriteriaId(feedbackModel.getQuestion().getCriterieId());
            feedbackDTO.setTransitId(feedbackModel.getTransitId());
            Integer answer = Integer.parseInt(feedbackModel.getAnswer());
            Integer weight = feedbackModel.getQuestion().getWeight();
            feedbackDTO.setAnswer("[{\"answer\":" + answer + ", \"weight\":" + weight + "}]");
            feedbackDTO.setUserId(1);
            return feedbackDTO;
        } else if (feedbackModel.getQuestion().getType().equals("SIMPLE")) {
            FeedbackDTO feedbackDTO = new FeedbackDTO();
            feedbackDTO.setCriteriaId(feedbackModel.getQuestion().getCriterieId());
            feedbackDTO.setTransitId(feedbackModel.getTransitId());
            if (feedbackModel.getAnswer().equals("Так")) {
                feedbackDTO.setAnswer("\"YES\"");
            } else {
                feedbackDTO.setAnswer("\"NO\"");
            }
            feedbackDTO.setUserId(1);
            return feedbackDTO;
        } else return null;
    }

    public String post(FeedbackDTO feedbackDTO) {

        ObjectMapper mapper = new ObjectMapper();
        String jsonInString = null;
        try {
            jsonInString = mapper.writeValueAsString(feedbackDTO);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        String url = "http://localhost:8443/feedback";

        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Bearer eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJpaC5pcnluYUBnbWFpbC5jb20iLCJhdXRoIjoiVVNFUiIsImlkIjo2MCwiaWF0IjoxNTMxODIwNzQyLCJleHAiOjE1MzIwNzk5NDJ9.c6F6Xgrhbsmj-qcx0hmO6-q2k3bzvVDEb2YkFU0VA1c");
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<String> entity = new HttpEntity<String>(jsonInString, headers);

        String answer = restTemplate.postForObject(url, entity, String.class);
        return jsonInString;
    }

    public void executePost(List<FeedbackModel> feedbackModels) {
        for (FeedbackModel feedbackModel : feedbackModels) {
            post(convert(feedbackModel));

        }
    }
}




