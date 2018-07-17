package uatransport.telegrambot.response;

import lombok.*;

import java.io.Serializable;

@Data
@Getter
@Setter
@RequiredArgsConstructor
public class CategoryResponse implements Serializable {

    private Integer id;

    private String name;

    private CategoryResponse nextLevelCategory;

    private String iconURL;

    private Integer countOfTransits;

    private Geotag geotag;
}

@Data
@Getter
@Setter
@RequiredArgsConstructor
class Geotag implements Serializable{
    private Integer id;

    private String name;

    private Double latitude;

    private Double longtitude;

}
