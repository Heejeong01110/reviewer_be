package org.movie.reviewer.domain.rating.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.movie.reviewer.domain.movie.domain.Movie;
import org.movie.reviewer.domain.user.domain.User;
import org.movie.reviewer.global.common.BaseEntity;

@Entity
@Getter
@Builder
@Table(name = "rating")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class Rating extends BaseEntity {

  @Id
  @Column
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(length = 300)
  private String contents;

  @Column
  private Double rating;


  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(referencedColumnName = "id")
  private User user;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(referencedColumnName = "id")
  private Movie movie;

}
