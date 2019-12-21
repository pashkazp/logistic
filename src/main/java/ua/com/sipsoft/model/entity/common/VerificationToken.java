package ua.com.sipsoft.model.entity.common;

import java.io.Serializable;
import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import ua.com.sipsoft.model.entity.user.User;
import ua.com.sipsoft.model.entity.utils.VerificationTokenConvertor;
import ua.com.sipsoft.utils.security.VerificationTokenType;

@EqualsAndHashCode(of = { "id" })
@ToString(exclude = { "token" })
@Getter
@Setter
@NoArgsConstructor
@Table(name = "users_verification_tokens")
@Entity
public class VerificationToken implements Serializable {

    private static final long serialVersionUID = -3605714338727098314L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "token_id")
    private Long id;

    @Column(name = "token", nullable = false, length = 200, unique = true)
    private String token;

    @Column
    @Convert(converter = VerificationTokenConvertor.class)
    private VerificationTokenType tokenType;

    @OneToOne(targetEntity = User.class, fetch = FetchType.EAGER)
    @JoinColumn(nullable = false, name = "user_id")
    private User user;

    private LocalDateTime expiryDate = LocalDateTime.now();

    /** The enabled. */
    @Column(name = "enabled", nullable = false)
    private Boolean used = false;

    private LocalDateTime usedDate;

    public VerificationToken(User user, String token, VerificationTokenType tokenType) {
	super();
	this.user = user;
	this.token = token;
	this.tokenType = tokenType;
	this.expiryDate = LocalDateTime.now().plusMinutes(60);
    }

}
