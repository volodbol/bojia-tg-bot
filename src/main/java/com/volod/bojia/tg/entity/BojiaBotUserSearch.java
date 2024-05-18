package com.volod.bojia.tg.entity;

import com.volod.bojia.tg.constant.PostgresConstants;
import com.volod.bojia.tg.domain.vacancy.VacancyProvider;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.proxy.HibernateProxy;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name = PostgresConstants.BOJIA_BOT_USER_SEARCH)
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class BojiaBotUserSearch {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "bojia_bot_user_search_id_seq")
    @SequenceGenerator(name = "bojia_bot_user_search_id_seq", allocationSize = 1)
    private Long id;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    @ToString.Exclude
    private BojiaBotUser user;
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private VacancyProvider provider;
    @Column(nullable = false)
    private String keywords;

    public BojiaBotUserSearch(
            BojiaBotUser user,
            VacancyProvider provider,
            String keywords
    ) {
        this.user = user;
        this.provider = provider;
        this.keywords = keywords;
    }

    public List<String> getKeywordsSplit() {
        return Arrays.asList(this.keywords.split(" "));
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
