package uatransport.telegrambot.models;

import lombok.Data;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class FeedbackDTO {

    private Integer id;
    private String answer;
    private Integer userId;
    private Integer transitId;
    private Integer criteriaId;



}



@Data
@Getter
@Setter
@RequiredArgsConstructor
 class Answer{

    private Integer answer;
    private Integer weight;


    @Override
    public String toString() {
        return "answer" + answer +
                ", weight" + weight;
    }
}
