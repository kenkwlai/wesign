package org.wesignproject.translate;

import com.google.common.base.Preconditions;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.Nonnull;
import org.wesignproject.models.IrrelevantCharacter;
import org.wesignproject.models.Vocabulary;
import play.Logger;


/**
 * Implementation class of applying grammar rules of Deaf
 */
public class DeafGrammarChecker {
  private final List<Vocabulary> _vocabularies;
  private final List<IrrelevantCharacter> _irrelevantCharacters;

  public DeafGrammarChecker(@Nonnull List<Vocabulary> vocabularies, @Nonnull List<IrrelevantCharacter> irrelevantCharacters) {
    Preconditions.checkNotNull(vocabularies, "vocabularies should not be null");
    Preconditions.checkNotNull(irrelevantCharacters, "irrelevantCharacters should not be null");
    _vocabularies = vocabularies;
    _irrelevantCharacters = irrelevantCharacters;
  }

  /**
   * A Naive approach to perform grammar check and translation from written Chinese to spoken Sign Language
   * @return the arranged list
   */
  public List<Vocabulary> getResult() {
    processUnusedWords();
    processAdjectiveAndNoun();
    processAdjectiveAndVerb();
    processWithNumber();
    processRuleThree();
    processRuleFour();
    processIgnoreList();
    return _vocabularies;
  }

  private void processUnusedWords() {
    // remove undesired 的
    List<Integer> unusedIndices = new ArrayList<>();
    for (int i = 0; i < _vocabularies.size(); i++) {
      if (_vocabularies.get(i).getWord().equals("的")) {
        int j = i - 1;
        if (!matchType(_vocabularies.get(j), "noun") &&
            !matchType(_vocabularies.get(j), "pronoun") &&
            !matchType(_vocabularies.get(j), "verb"))
          unusedIndices.add(i);
      }
      if (_vocabularies.get(i).getWord().equals("好")) {
        int j = i + 1;
        if (!matchType(_vocabularies.get(j), "noun"))
          unusedIndices.add(i);
      }
    }
    for (int i: unusedIndices) {
      _vocabularies.remove(i);
    }
  }

  private void processWithNumber() {
    // number behind what it describes
    List<Integer> fromIndex = new ArrayList<>();
    List<Integer> toIndex = new ArrayList<>();
    for (int i = 0; i < _vocabularies.size(); i++) {
      int j = i + 1;
      if (matchType(_vocabularies.get(i), "number")) {
        fromIndex.add(i);
        for (; j < _vocabularies.size(); j++) {
          if (matchType(_vocabularies.get(j), "noun")) {
            toIndex.add(j);
            break;
          }
        }
        i = j;
      }
    }
    swapNumberCollection(fromIndex, toIndex);
  }

  private void processAdjectiveAndNoun() {
    // Rule 1: adjective behind target group
    List<Integer> fromIndex = new ArrayList<>();
    List<Integer> toIndex = new ArrayList<>();
    for (int i = 0; i < _vocabularies.size(); i++) {
      int j = i + 1;
      if (j == _vocabularies.size()) {
        break;
      }
      if (matchType(_vocabularies.get(i), "adjective") &&
          !matchType(_vocabularies.get(i), "verb") &&
          !_vocabularies.get(j).getWord().equals("地")) {
        fromIndex.add(i);
        for (; j < _vocabularies.size(); j++) {
          if (matchType(_vocabularies.get(j), "noun") || matchType(_vocabularies.get(j), "pronoun")) {
            toIndex.add(j);
            break;
          }
        }
      }
    }
    swapCollection(fromIndex, toIndex);
  }

  private void processAdjectiveAndVerb() {
    // Rule 2: adverb behind target group
    List<Integer> fromIndex = new ArrayList<>();
    List<Integer> toIndex = new ArrayList<>();
    for (int i = 0; i < _vocabularies.size(); i++) {
      int j = i + 1;
      if (j == _vocabularies.size()) {
        break;
      }
      if (matchType(_vocabularies.get(i), "adjective") && !matchType(_vocabularies.get(i), "verb") && _vocabularies.get(j).getWord().equals("地")) {
        fromIndex.add(i);
        for (; j < _vocabularies.size(); j++) {
          if (matchType(_vocabularies.get(j), "verb")) {
            toIndex.add(j);
            break;
          }
        }
      }
    }
    swapCollection(fromIndex, toIndex);
  }

  private void processRuleThree() {
    // Rule 3: when active voice, change to passive voice, switch the group start with pronoun with a verb in between
    boolean needToProcess = false;
    for (Vocabulary word: _vocabularies) {
      if (word.getWord().equals("被")) {
        needToProcess = false;
        break;
      }
      if (word.getWord().equals("的")) {
        needToProcess = true;
      }
    }
    if (needToProcess) {
      Logger.info("need to process");
      List<Vocabulary> swappedList = new ArrayList<>();
      for (int i = 0; i < _vocabularies.size(); i++) {
        if (matchType(_vocabularies.get(i), "verb")) {
          int j = i + 1;
          if (j < _vocabularies.size()) {
            swappedList.addAll(_vocabularies.subList(j, _vocabularies.size()));
            swappedList.addAll(_vocabularies.subList(0, j));
            _vocabularies.clear();
            _vocabularies.addAll(swappedList);
            break;
          }
        }
      }
    }
  }

  private void processRuleFour() {
    // Rule 4: question words, put at the end
    Vocabulary questionWord = new Vocabulary();
    boolean needToAdd = false;
    for (Vocabulary word : _vocabularies) {
      if (matchType(word, "question")) {
        questionWord = word;
        needToAdd = true;
      }
    }
    if (needToAdd) {
      _vocabularies.remove(questionWord);
      _vocabularies.add(questionWord);
    }
  }

  private void processIgnoreList() {
    List<Vocabulary> toRemove = new ArrayList<>();
    for (Vocabulary v: _vocabularies) {
      for (IrrelevantCharacter ignore: _irrelevantCharacters) {
        if (v.getWord().equals(ignore.getWord())) {
          toRemove.add(v);
        }
      }
    }
    _vocabularies.removeAll(toRemove);
  }

  private void swapCollection(List<Integer> fromIndex, List<Integer> toIndex) {
    List<Vocabulary> swappedList = new ArrayList<>();
    int fromSize = fromIndex.size();
    int toSize = toIndex.size();
    if (fromSize != 0 && toSize != 0) {
      for (int i = 0; i < fromSize; i++) {
        for (int j = i; j < toSize; j++) {
          Integer from = fromIndex.get(i);
          Integer to = toIndex.get(j);
          swappedList.clear();
          swappedList.addAll(_vocabularies.subList(0, from));
          swappedList.addAll(_vocabularies.subList(from + 1, to));
          swappedList.add(_vocabularies.get(to));
          swappedList.add(_vocabularies.get(from));
          if (!(to + 1 >= _vocabularies.size())) {
            swappedList.addAll(_vocabularies.subList(to + 1, _vocabularies.size()));
          }
          _vocabularies.clear();
          _vocabularies.addAll(swappedList);
        }
      }
    }
  }

  private void swapNumberCollection(List<Integer> fromIndex, List<Integer> toIndex) {
    List<Vocabulary> swappedList = new ArrayList<>();
    int fromSize = fromIndex.size();
    int toSize = toIndex.size();
    if (fromSize != 0 && toSize != 0) {
      for (int i = 0; i < fromSize; i++) {
        for (int j = i; j < toSize; j++) {
          Integer from = fromIndex.get(i);
          Integer to = toIndex.get(j);
          swappedList.clear();
          swappedList.addAll(_vocabularies.subList(0, from));
          swappedList.add(_vocabularies.get(to));
          swappedList.addAll(_vocabularies.subList(from, to));
          if (!(to + 1 >= _vocabularies.size())) {
            swappedList.addAll(_vocabularies.subList(to + 1, _vocabularies.size()));
          }
          _vocabularies.clear();
          _vocabularies.addAll(swappedList);
        }
      }
    }
  }

  private boolean matchType(Vocabulary word, String type) {
    return (word.getMainType().equals(type) || word.getSubType().equals(type));
  }
}
