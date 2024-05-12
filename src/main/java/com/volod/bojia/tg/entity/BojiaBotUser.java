package com.volod.bojia.tg.entity;

import com.pengrad.telegrambot.model.Update;
import com.volod.bojia.tg.constant.PostgresConstants;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.*;
import org.hibernate.proxy.HibernateProxy;

import java.util.Objects;

import static java.util.Objects.nonNull;

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
    @Column(nullable = false)
    private Long chatId;
    @Column(nullable = false)
    private String firstName;
    @Column
    private String prompt;

    public BojiaBotUser(Update update) {
        var user = update.message().from();
        var chat = update.message().chat();
        this.id = user.id();
        this.chatId = chat.id();
        this.firstName = user.firstName();
    }

    public BojiaBotUser(Update update, String prompt) {
        var user = update.message().from();
        var chat = update.message().chat();
        this.id = user.id();
        this.chatId = chat.id();
        this.firstName = user.firstName();
        this.prompt = prompt;
    }

    public String getPromptOrDefault() {
        if (nonNull(this.prompt)) {
            return this.prompt;
        } else {
            return "You don't have any prompt";
        }
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
