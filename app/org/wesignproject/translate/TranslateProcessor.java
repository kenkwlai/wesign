package org.wesignproject.translate;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import javax.annotation.Nonnull;
import javax.inject.Inject;
import org.wesignproject.models.IrrelevantCharacter;
import org.wesignproject.models.Synonym;
import org.wesignproject.models.Vocabulary;
import org.wesignproject.stores.Store;
import org.wesignproject.stores.cached.IrrelevantCharacterCachedDatabaseStore;
import org.wesignproject.stores.cached.SynonymCachedDatabaseStore;
import org.wesignproject.stores.cached.VocabularyCachedDatabaseStore;


/**
 * Created by Ken Lai on 7/1/2017.
 */
public class TranslateProcessor {
  private final Store<String, Vocabulary> _vocabularyStore;
  private final Store<String, IrrelevantCharacter> _irrelevantCharacterStore;
  private final Store<String, Synonym> _synonymStore;

  private List<Vocabulary> _vocabularies;
  private List<Synonym> _synonyms;
  private List<IrrelevantCharacter> _irrelevantCharacters;

  @Inject
  public TranslateProcessor(VocabularyCachedDatabaseStore vocabularyStore,
      IrrelevantCharacterCachedDatabaseStore irrelevantCharacterStore,
      SynonymCachedDatabaseStore synonymStore) {
    _vocabularyStore = vocabularyStore;
    _irrelevantCharacterStore = irrelevantCharacterStore;
    _synonymStore = synonymStore;
  }

  public List<Vocabulary> process(@Nonnull String sentence, @Nonnull String mode) {
    getDataFromDatabase();
    List<Vocabulary> segments;
    String segment;
    final int length = sentence.length();
    Boolean[][] wordBreak = new Boolean[length][length];

    for (int i = 0; i < length; i++) {
      for (int j = 0; j < length; j++) {
        wordBreak[i][j] = false;
      }
    }

    // parse
    for (int i = 0; i < length; i++) {
      for (int j = i; j < length; j++) {
        int subStrIndex = j + 1;
        segment = sentence.substring(i, subStrIndex);
        if (checkInt(segment)) {
          if (subStrIndex != length && checkInt(String.valueOf(sentence.charAt(subStrIndex)))) {
            continue;
          }
        }
        if ((findVocab(segment) || checkInt(segment)) && !wordBreak[i][j]) {
          wordBreak[i][j] = true;
          if (!wordBreak[i][i]) {
            wordBreak[i][i] = true;
          }
        }
      }
    }

    segments = getList(sentence, wordBreak, length);

    List<List<Vocabulary>> cutList = cutsOnDescriptive(segments);

    List<Vocabulary> result = new ArrayList<>();
    for (List<Vocabulary> subList: cutList) {
      List<Vocabulary> subResult = new ArrayList<>();
      // rearrange
      switch (mode) {
        case "chi":
          subResult = rearrangeInChi(subList);
          break;
        case "deaf":
          subResult = rearrangeInDeaf(subList);
          break;
      }
      result.addAll(subResult);
    }
    return result;
  }

  // TODO: add punctuation support
  private List<List<Vocabulary>> cutsOnDescriptive(List<Vocabulary> list) {
    List<List<Vocabulary>> cuts = new ArrayList<>();
    List<Integer> verbIndex = new ArrayList<>();
    boolean dVerbExist = list.stream().filter(word -> matchType(word, "dverb")).collect(Collectors.toList()).size() >= 1;
    boolean pronounExist = list.stream().filter(word -> matchType(word, "pronoun")).collect(Collectors.toList()).size() >= 2;
    if(dVerbExist && pronounExist) {
      for (int i = 0; i < list.size(); i++) {
        if (matchType(list.get(i), "dverb")) {
          verbIndex.add(i);
        }
      }
      List<Vocabulary> subListOne = list.subList(0, verbIndex.get(0) + 1);
      List<Vocabulary> subListTwo = list.subList(verbIndex.get(0) + 1, list.size());
      cuts.add(subListOne);
      cuts.add(subListTwo);
    }
    else {
      cuts.add(list);
    }
    return cuts;
  }

  private boolean checkInt(String segment) {
    try {
      Integer.parseInt(segment);
      return true;
    } catch (NumberFormatException ex) {
      return false;
    }
  }

  private boolean matchType(Vocabulary word, String type) {
    return (word.getMainType().equals(type) || word.getSubType().equals(type));
  }

  private boolean findVocab(String word) {
    boolean vocabFlag = false;
    boolean synonymFlag = false;
    for (Vocabulary v: _vocabularies) {
      if (v.getWord().equals(word)) {
        vocabFlag = true;
      }
    }
    for (Synonym s: _synonyms) {
      if (s.getSynonym().equals(word)) {
        synonymFlag = true;
      }
    }
    return (vocabFlag || synonymFlag);
  }

  private List<Vocabulary> getList(String sentence, Boolean[][] wordBreak, int length) {
    List<Vocabulary> lists = new ArrayList<>();
    for (int i = 0; i < length; i++) {
      int startIndex = -1;
      int endIndex = -1;
      for (int j = 0; j < length; j++) {
        if (wordBreak[i][j]) {
          startIndex = i;
          endIndex = j;
        }
      }
      if (endIndex > i) {
        i = endIndex;
      }
      if (startIndex != -1 && endIndex != -1) {
        endIndex += 1; // for substring
        String substring = sentence.substring(startIndex, endIndex);
        if (checkInt(substring)) {
          List<Vocabulary> numbers = getNumbers(substring);
          lists.addAll(numbers);
        } else {
          Vocabulary vocab = getVocab(substring);
          lists.add(vocab);
        }
      }
    }
    return lists;
  }

  private List<Vocabulary> getNumbers(String numberString) {
    // support 8 digits currently
    int numbers = Integer.parseInt(numberString);
    List<Vocabulary> numToWords = new ArrayList<>();

    LinkedList<Integer> numStack = new LinkedList<>();
    while (numbers > 0) {
      numStack.push(numbers % 10);
      numbers = numbers / 10;
    }

    int position = 1;
    switch (numStack.size()) {
      case 1:
      case 2:
        position = 4;
        break;
      case 3:
      case 4:
        position = 3;
        break;
      case 5:
      case 6:
        position = 2;
        break;
      case 7:
      case 8:
        position = 1;
        break;
    }
    while (!numStack.isEmpty()) {
      String wordToRetrieve = numStack.pop() + "-" + position;
      numToWords.add(getVocab(wordToRetrieve));
      if (position < 8) {
        position++;
      }
    }
    return numToWords;
  }

  private Vocabulary getVocab(String word) {
    Vocabulary wordToReturn = null;
    String synonymToWord = null;
    for (Synonym s: _synonyms) {
      if (s.getSynonym().equals(word)) {
        synonymToWord = s.getWord();
      }
    }
    for (Vocabulary v: _vocabularies) {
      if (v.getWord().equals(word)) {
        wordToReturn = v;
      }
    }
    if(Optional.ofNullable(synonymToWord).isPresent() && !Optional.ofNullable(wordToReturn).isPresent()) {
      wordToReturn = getVocab(synonymToWord);
    }

    return wordToReturn;
  }

  private List<Vocabulary> rearrangeInChi(List<Vocabulary> vocabularyList) {
    return vocabularyList;
  }

  private List<Vocabulary> rearrangeInDeaf(List<Vocabulary> vocabularies) {
    return new DeafGrammarChecker(vocabularies, _irrelevantCharacters)
        .getResult();
  }

  private void getDataFromDatabase() {
    _vocabularies = _vocabularyStore.getAll();
    _irrelevantCharacters = _irrelevantCharacterStore.getAll();
    _synonyms = _synonymStore.getAll();
  }
}
