package uatransport.telegrambot.converters;


import org.modelmapper.ModelMapper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import uatransport.telegrambot.models.Category;
import uatransport.telegrambot.response.CategoryResponse;

import java.util.*;

@Component
public class CategoryConverter {

    @Autowired
    private ModelMapper modelMapper;

    public ArrayList<Category> convertToCategoryArray(CategoryResponse[] categoryResponses){
        ArrayList<Category> categories = new ArrayList<>();
        for (int i = 0; i<categoryResponses.length; i++){
           categories.add(modelMapper.map(categoryResponses[i], Category.class));
        }
        return categories;
    }

    public Category convertSingleCategory(CategoryResponse[] categoryResponses){
        return modelMapper.map(categoryResponses[0],Category.class);
    }

}


