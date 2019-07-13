package org.wesignproject.utils.config;

import com.google.common.base.Preconditions;
import java.util.function.Function;
import org.apache.commons.lang3.StringUtils;
import play.Logger;


/**
 * Util class providing API to application level configs
 */
public enum ConfigUtils {
  INSTANCE;

  /**
   * Get config from env var
   * @param key env key
   * @param defaultValue default value if env key does not exist
   * @param parser type parser for the env var
   * @param <T> type
   * @return config
   */
  public static <T> T getEnvVarWithDefault(String key, T defaultValue, Function<String, T> parser) {
    try {
      String value = System.getenv(Preconditions.checkNotNull(key, "key cannot be null"));
      Preconditions.checkArgument(StringUtils.isNoneBlank(value));
      Logger.info(String.format("Found environment value for key=%s", key));
      return parser.apply(value);
    } catch (Exception e) {
      Logger.info(String.format("Cannot get environment variable with key=%s", key), e);
    }
    return defaultValue;
  }

  public static String getEnvVarWithDefault(String key, String defaultValue) {
    return getEnvVarWithDefault(key, defaultValue, Function.identity());
  }
}
