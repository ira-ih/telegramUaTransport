package uatransport.telegrambot.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import javax.persistence.*;

@Entity
@Data
@Accessors(chain = true)
@Table(name = "question")
@EqualsAndHashCode(of = "id")
public class Question {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;
    private String  uuid;
    private Long chatId;
    private String name;
    private String type;
    private  Integer weight;
    private  Integer criterieId;
    @OneToOne(cascade = CascadeType.MERGE)
    @JoinColumn(name = "next_question_id")
    private Question nextQuestion;




}
