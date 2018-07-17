package uatransport.telegrambot.response;

import lombok.Data;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

@Data
@Getter
@Setter
@RequiredArgsConstructor
public class QuestionResponse implements Serializable {

    private Integer id;

    private String name;

    private Integer weight;

    private Integer priority;

    private String type;
}
