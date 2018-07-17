package uatransport.telegrambot.models;

import lombok.Data;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Data
@Getter
@Setter
@RequiredArgsConstructor
public class Transit {

    private Integer id;
    private String name;
    private Integer categoryId;
    private String routeName;
    private String categoryIconURL;


}
