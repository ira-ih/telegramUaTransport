package uatransport.telegrambot;

import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import uatransport.telegrambot.mappers.CategoryMapperCustom;
import uatransport.telegrambot.models.Category;
import uatransport.telegrambot.response.CategoryResponse;

@Configuration
public class ModelMapperConfig {



        @Bean
        public ModelMapper modelMapper() {
            ModelMapper modelMapper = new ModelMapper();
            modelMapper.createTypeMap(CategoryResponse.class, Category.class).setConverter(new CategoryMapperCustom());

            return modelMapper;
        }

}
