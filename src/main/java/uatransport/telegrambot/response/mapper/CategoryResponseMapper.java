package uatransport.telegrambot.response.mapper;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import uatransport.telegrambot.response.CategoryResponse;

@Service
public class CategoryResponseMapper {

    @Value("${service.url}")
    private String serviceUrl;


    @SneakyThrows
    public CategoryResponse[] getCategoryList() {
        return new ObjectMapper().readValue(new RestTemplate().getForEntity(serviceUrl+"/category/?firstNestedCategoryName=Lviv", String.class).getBody(),
                CategoryResponse[].class);
    }

    @SneakyThrows
    public CategoryResponse[] getSingleCategory(String name) {
        return new ObjectMapper().readValue(new RestTemplate().getForEntity(serviceUrl+"/category/?name="+name, String.class).getBody(),
                CategoryResponse[].class);
    }

}
