package uatransport.telegrambot.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.RequiredArgsConstructor;
import lombok.experimental.Accessors;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.util.Date;

@Entity
@Data
@Accessors(chain = true)
@Table(name = "feedbackModel")
@EqualsAndHashCode(of = "id")
@RequiredArgsConstructor
public class FeedbackModel {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    private long chatId;

    private Integer transitId;

    private String uuid;

    private String answer;

    @ManyToOne
    @JoinColumn(name = "question_id")
    private Question question;

    @CreationTimestamp
    private Date  date;


}
