package org.wesignproject.controllers;

import com.fasterxml.jackson.databind.JsonNode;
import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import javax.inject.Inject;
import org.wesignproject.models.Vocabulary;
import org.wesignproject.translate.TranslateProcessor;
import org.wesignproject.translate.VideoSynthesizer;
import play.Logger;
import play.db.jpa.Transactional;
import play.libs.Json;
import play.mvc.Result;
import play.mvc.Results;


/**
 * Controller listens to Translation related HTTP API calls
 */
public class TranslateController extends BaseController {
  private static final String JSON_KEY_SENTENCE = "sentence";
  private static final String JSON_KEY_MODE = "mode";

  private final TranslateProcessor _translateProcessor;
  private final VideoSynthesizer _videoSynthesizer;

  private static final String OCTET_STREAM_TYPE = "application/octet-stream";

  @Inject
  public TranslateController(VideoSynthesizer videoSynthesizer,
      TranslateProcessor translateProcessor) {
    _videoSynthesizer = videoSynthesizer;
    _translateProcessor = translateProcessor;
  }

  /**
   * @api {post} /translateText translate a sentence to HK sign language text sequence
   * @apiName TranslateToText
   * @apiGroup Translate
   *
   * @apiParam (Request Fields) {String} sentence sentence to be translated
   * @apiParam (Request Fields) {String} mode translate mode
   *
   * @apiSuccess (200) this the translated sequence
   *
   * @apiError (500) message error
   */
  @Transactional
  public Result translateToText() {
    Map<String, String> sentence = parseJson(request().body().asJson());
    Logger.info("Sentence received: " + sentence.toString());

    return Optional.of(_translateProcessor.process(sentence.get(JSON_KEY_SENTENCE), sentence.get(JSON_KEY_MODE)))
        .map(Json::toJson)
        .map(Results::ok)
        .orElse(internalServerError());
  }

  /**
   * @api {post} /translate translate a sentence to video
   * @apiName TranslateToVideo
   * @apiGroup Translate
   *
   * @apiParam (Request Fields) {String} sentence sentence to be translated
   * @apiParam (Request Fields) {String} mode translate mode
   *
   * @apiSuccess (200) this the video stream
   *
   * @apiError (500) message error
   */
  @Transactional
  public Result translateToVideo() {
    Map<String, String> sentence = parseJson(request().body().asJson());
    Logger.info("Sentence received: " + sentence.toString());

    List<Vocabulary> processedText = _translateProcessor.process(sentence.get(JSON_KEY_SENTENCE), sentence.get(
        JSON_KEY_MODE));
    File video;
    try {
      video = _videoSynthesizer.synVideo(processedText);
    } catch (Exception e) {
      return internalServerError("unable to synthesize video");
    }

    return ok()
        .sendFile(video)
        .as(OCTET_STREAM_TYPE);
  }

  private Map<String, String> parseJson(JsonNode json) {
    Map<String, String> sentence = new HashMap<>();
    Optional.ofNullable(json)
        .map(jsonNode -> jsonNode.get(JSON_KEY_MODE))
        .ifPresent(jsonMode -> sentence.put(JSON_KEY_MODE, jsonMode.asText()));
    Optional.ofNullable(json)
        .map(jsonNode -> jsonNode.get(JSON_KEY_SENTENCE))
        .ifPresent(jsonSentence -> sentence.put(JSON_KEY_SENTENCE, jsonSentence.asText()));
    return sentence;
  }

}
