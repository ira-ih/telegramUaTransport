package uatransport.telegrambot.response.mapper;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import uatransport.telegrambot.models.Transit;

@Component
public class TransitResponseMapper {

    @Value("${service.url}")
    private String serviceUrl;
    @SneakyThrows
    public Transit getTransitByNameAndNextLevelCategoryName(String name, Integer nextLevelCategoryName) {
        return new ObjectMapper().readValue(new RestTemplate().getForEntity(serviceUrl+"/transit/"+name+"/"+nextLevelCategoryName, String.class).getBody(),
                Transit.class);
    }
}
