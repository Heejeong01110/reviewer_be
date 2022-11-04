package org.movie.reviewer.domain.user.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.movie.reviewer.global.common.BaseEntity;

@Entity
@Getter
@Table(name = "users")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class User extends BaseEntity {

  @Id
  @Column
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(nullable = false, unique = true, length = 30)
  private String email;

  @Column(nullable = false, length = 30)
  private String password;

  @Column(nullable = false, unique = true, length = 20)
  private String nickname;

  @Column(length = 50000)
  private String Introduction;

  @Column(length = 200)
  private String profileImage;

  @Builder
  public User(Long id, String email, String password, String nickname, String introduction,
      String profileImage) {
    this.id = id;
    this.email = email;
    this.password = password;
    this.nickname = nickname;
    Introduction = introduction;
    this.profileImage = profileImage;
  }
}
