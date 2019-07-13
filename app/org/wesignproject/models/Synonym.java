package org.wesignproject.models;

import com.google.common.base.Preconditions;
import javax.annotation.Nonnull;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.hibernate.validator.constraints.NotBlank;


/**
 * HK Sign Language is similar to our spoken Chinese,
 * a lot of words can be mapped to the same SIGN,
 * so a Synonym is a mapping between them
 */
@Entity
@Table(name = "synonym")
public class Synonym extends BaseModel<String> {
  @Id
  @SequenceGenerator(name = "synonym_id_seq",
    sequenceName = "synonym_id_seq",
    allocationSize = 1)
  @GeneratedValue(strategy = GenerationType.SEQUENCE)
  private Long id;

  @Column
  @NotBlank
  private String word;

  @Column
  @NotBlank
  private String synonym;

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

  public void setSynonym(@Nonnull String synonym) {
    Preconditions.checkArgument(StringUtils.isNotBlank(synonym), "synonym should not be blank");
    this.synonym = synonym;
  }

  public String getSynonym() {
    return synonym;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }

    if (o == null || getClass() != o.getClass()) {
      return false;
    }

    Synonym that = (Synonym) o;

    return new EqualsBuilder().append(id, that.id)
        .append(word, that.word)
        .append(synonym, that.synonym)
        .isEquals();
  }

  @Override
  public int hashCode() {
    return new HashCodeBuilder().append(id)
        .append(word)
        .append(synonym)
        .toHashCode();
  }
}
