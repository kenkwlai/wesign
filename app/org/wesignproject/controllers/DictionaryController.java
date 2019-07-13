package org.wesignproject.controllers;

import java.util.List;
import java.util.Optional;
import javax.inject.Inject;
import org.wesignproject.models.Vocabulary;
import org.wesignproject.stores.Store;
import org.wesignproject.stores.cached.VocabularyCachedDatabaseStore;
import play.Logger;
import play.db.jpa.Transactional;
import play.libs.Json;
import play.mvc.Result;
import play.mvc.Results;


/**
 * Controller listens to Dictionary related HTTP API calls
 */
public class DictionaryController extends BaseController {
  private final Store<String, Vocabulary> _store;

  @Inject
  public DictionaryController(VocabularyCachedDatabaseStore store) {
    _store = store;
  }

  /**
   * @api {get} /dictionary/:word get the metadata of the word corresponding vocabulary
   * @apiName GetWordFromDictionary
   * @apiGroup Dictionary
   *
   * @apiParam (Parameter) {String} word the word
   *
   * @apiSuccess (200) {Vocabulary} this the corresponding vocabulary
   *
   * @apiError (404) \(empty\)
   */
  @Transactional
  public Result getResponse(String word) {
    Logger.info("Word received: " + word);
    return Optional.of(getSign(word))
        .map(Json::toJson)
        .map(Results::ok)
        .orElse(notFound());
  }

  /**
   * @api {get} /dictionary get all vocabularies
   * @apiName GetAllWordFromDictionary
   * @apiGroup Dictionary
   *
   * @apiSuccess (200) {Vocabulary[]} this the vocabularies
   *
   * @apiError (500) \(empty\)
   */
  @Transactional(readOnly = true)
  public Result getAll() {
    return Optional.of(getAllSigns())
        .map(Json::toJson)
        .map(Results::ok)
        .orElse(internalServerError());
  }

  private Vocabulary getSign(String key) {
    Logger.info(String.format("Retrieving data from database, KEY: %s", key));
    return _store.get(key).getDataOnSuccess();
  }

  private List<Vocabulary> getAllSigns() {
    Logger.info("Retrieving data from database: getting all signs");
    return _store.getAllWithConstraint();
  }
}
