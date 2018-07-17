package uatransport.telegrambot.entity;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.experimental.Accessors;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.util.Date;

@Entity
@Data
@Accessors(chain = true)
@Table(name = "chatModel")
@RequiredArgsConstructor
public class ChatModel {

   @Id
    private Long chatId;

   private String categoryName;

   private Integer categoryId;

    @CreationTimestamp
    private Date date;


}
