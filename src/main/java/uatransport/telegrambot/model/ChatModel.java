package uatransport.telegrambot.model;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.experimental.Accessors;

import javax.persistence.*;

@Entity
@Data
@Accessors(chain = true)
@Table(name = "chatModel")
@RequiredArgsConstructor
public class ChatModel {

   /* @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;*/

   @Id
    private Long chatId;

    private Integer transportType;

    private Integer transportNumber;
}
