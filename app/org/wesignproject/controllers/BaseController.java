package org.wesignproject.controllers;

import com.google.common.collect.ImmutableMap;
import play.libs.Json;
import play.mvc.Controller;
import play.mvc.Result;


/**
 * Created by Ken Lai on 3/3/2017.
 */
public class BaseController extends Controller {
  private static final String VERSION_NO = "1.2";

  public Result getVersion() {
    return ok(Json.toJson(ImmutableMap.of("version", VERSION_NO)));
  }

  public Result alwaysTrue() {
    return ok();
  }
}
