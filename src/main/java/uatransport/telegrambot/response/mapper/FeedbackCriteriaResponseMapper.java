package uatransport.telegrambot.response.mapper;


import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import uatransport.telegrambot.response.FeedbackCriteriaResponse;

@Component
public class FeedbackCriteriaResponseMapper {
    @Value("${service.url}")
    private String serviceUrl;

    @Autowired
    private RestTemplate restTemplate;

    @SneakyThrows
    public FeedbackCriteriaResponse[] getFeedbackCriteriaResponseByCategoryIdAndTypeRating(Integer categoryId) {
        return new ObjectMapper().readValue(new RestTemplate().getForEntity(serviceUrl+"/feedback-criteria/categoryId/"+categoryId+"/type/RATING", String.class).getBody(),
                FeedbackCriteriaResponse[].class);
    }

    @SneakyThrows
    public FeedbackCriteriaResponse[] getFeedbackCriteriaResponseByCategoryIdAndTypeSIMPLE(Integer categoryId) {
        return new ObjectMapper().readValue(new RestTemplate().getForEntity(serviceUrl+"/feedback-criteria/categoryId/"+categoryId+"/type/SIMPLE", String.class).getBody(),
                FeedbackCriteriaResponse[].class);
    }

}
