package com.volod.bojia.tg.entity;

import com.pengrad.telegrambot.model.Chat;
import com.pengrad.telegrambot.model.User;
import com.volod.bojia.tg.constant.PostgresConstants;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.proxy.HibernateProxy;

import java.util.Objects;

@Entity
@Table(name = PostgresConstants.BOJIA_BOT_USER_TABLE)
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class BojiaBotUser {
    @Id
    private Long id;
    @Column
    private Long chatId;
    @Column
    private String firstName;
    @Column
    private String prompt;

    public BojiaBotUser(User user, Chat chat) {
        this.id = user.id();
        this.chatId = chat.id();
        this.firstName = user.firstName();
    }

    @Override
    public final boolean equals(Object o) {
        if (this == o) return true;
        if (o == null) return false;
        var oEffectiveClass = o instanceof HibernateProxy proxy ?
                proxy.getHibernateLazyInitializer().getPersistentClass()
                : o.getClass();
        var thisEffectiveClass = this instanceof HibernateProxy proxy ?
                proxy.getHibernateLazyInitializer().getPersistentClass()
                : this.getClass();
        if (thisEffectiveClass != oEffectiveClass) return false;
        var that = (BojiaBotUser) o;
        return this.getId() != null && Objects.equals(this.getId(), that.getId());
    }

    @Override
    public final int hashCode() {
        return this instanceof HibernateProxy proxy ?
                proxy.getHibernateLazyInitializer().getPersistentClass().hashCode()
                : this.getClass().hashCode();
    }
}