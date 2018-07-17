package uatransport.telegrambot.mappers;

import org.modelmapper.Converter;
import org.modelmapper.spi.MappingContext;
import uatransport.telegrambot.models.Category;
import uatransport.telegrambot.response.CategoryResponse;

public class CategoryMapperCustom implements Converter<CategoryResponse, Category> {

    @Override
    public Category convert(MappingContext<CategoryResponse, Category> context) {
        CategoryResponse source = context.getSource();
        Category destination = context.getDestination();
        destination.setId(source.getId());
        destination.setName(source.getName());
        return destination;
    }
}
