package org.wesignproject.models;

import com.google.common.base.Preconditions;
import javax.annotation.Nonnull;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Index;
import javax.persistence.PrePersist;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.hibernate.validator.constraints.NotBlank;


/**
 * Core model that powers the application
 * A vocabulary inside database is indeed the metadata describing the corresponding SIGN's video
 */
@Entity
@Table(name = "vocabulary", indexes = @Index(columnList = "word"))
public class Vocabulary extends BaseModel<String> {

  @Id
  @SequenceGenerator(name = "vocabulary_id_seq",
    sequenceName = "vocabulary_id_seq",
    allocationSize = 1)
  @GeneratedValue(strategy = GenerationType.SEQUENCE)
  private Long id;

  @Column(unique = true)
  @NotBlank
  private String word;

  @NotBlank
  private String path;

  @NotBlank
  private String mainType;

  @NotBlank
  private String subType;

  private String prefix;

  @Column(nullable = false)
  @NotNull
  private Double duration;

  @Override
  public String getKey() {
    return word;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public Long getId() {
    return id;
  }

  public void setWord(@Nonnull String word) {
    Preconditions.checkArgument(StringUtils.isNotBlank(word), "word should not be blank");
    this.word = word;
  }

  public String getWord() {
    return word;
  }

  public void setDuration(@Nonnull Double duration) {
    this.duration = Preconditions.checkNotNull(duration, "duration should not be null");
  }

  public Double getDuration() {
    return duration;
  }

  @PrePersist
  private void setDuration() {
    this.duration = 0.0;
  }

  public void setPath(@Nonnull String path) {
    Preconditions.checkArgument(StringUtils.isNotBlank(path), "path should not be blank");
    this.path = path;
  }

  public String getPath() {
    return path;
  }

  public void setMainType(@Nonnull String mainType) {
    Preconditions.checkArgument(StringUtils.isNotBlank(mainType), "mainType should not be blank");
    this.mainType = mainType;
  }

  public String getMainType() {
    return mainType;
  }

  public void setSubType(@Nonnull String subType) {
    Preconditions.checkArgument(StringUtils.isNotBlank(subType), "subType should not be blank");
    this.subType = subType;
  }

  public String getSubType() {
    return subType;
  }

  public String getPrefix() {
    return prefix;
  }

  public void setPrefix(String prefix) {
    this.prefix = prefix;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }

    if (o == null || getClass() != o.getClass()) {
      return false;
    }

    Vocabulary vocabulary = (Vocabulary) o;

    return new EqualsBuilder().append(id, vocabulary.id)
        .append(word, vocabulary.word)
        .append(path, vocabulary.path)
        .append(mainType, vocabulary.mainType)
        .append(subType, vocabulary.subType)
        .append(duration, vocabulary.duration)
        .append(prefix, vocabulary.prefix)
        .isEquals();
  }

  @Override
  public int hashCode() {
    return new HashCodeBuilder().append(id)
        .append(word)
        .append(path)
        .append(mainType)
        .append(subType)
        .append(duration)
        .append(prefix)
        .toHashCode();
  }
}
