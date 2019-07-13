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
 * Irrelevant Character that needs not to be handled when perform translation
 */
@Entity
@Table(name = "irrelevant_character")
public class IrrelevantCharacter extends BaseModel<String> {
  @Id
  @SequenceGenerator(name = "irrelevant_character_id_seq",
    sequenceName = "irrelevant_character_id_seq",
    allocationSize = 1)
  @GeneratedValue(strategy = GenerationType.SEQUENCE)
  private Long id;

  @Column(unique = true)
  @NotBlank
  private String word;

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

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }

    if (o == null || getClass() != o.getClass()) {
      return false;
    }

    IrrelevantCharacter irrelevantCharacter = (IrrelevantCharacter) o;

    return new EqualsBuilder().append(id, irrelevantCharacter.id)
        .append(word, irrelevantCharacter.word)
        .isEquals();
  }

  @Override
  public int hashCode() {
    return new HashCodeBuilder().append(id)
        .append(word)
        .toHashCode();
  }
}
