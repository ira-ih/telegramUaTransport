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
public class FeedbackCriteriaResponse implements Serializable {
    private Integer id;
    private QuestionResponse[] questions;
    private String type;
}
