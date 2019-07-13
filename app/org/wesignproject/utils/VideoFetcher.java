package org.wesignproject.utils;

import com.google.common.io.Files;
import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import javax.inject.Inject;
import play.Logger;
import play.libs.ws.WSClient;
import play.libs.ws.WSRequest;
import play.libs.ws.WSResponse;


/**
 * Fetcher class making API calls to asset store for the videos
 */
public class VideoFetcher {
  private static final long REQUEST_TIMEOUT = 20000;

  private final UUID _uuid;
  private final WSClient _wsClient;

  @Inject
  public VideoFetcher(UUID uuid, WSClient wsClient) {
    _uuid = uuid;
    _wsClient = wsClient;
  }

  /**
   * Download videos from urls and write to files
   * @param urls urls to fetch
   * @return File list
   * @throws InterruptedException exception
   * @throws ExecutionException exception
   */
  public List<File> downloadVideos(List<String> urls) throws InterruptedException, ExecutionException {
    List<File> fileList = new ArrayList<>();
    for (String url: urls) {
      Logger.info("Path: " + url);
      WSRequest request = _wsClient.url(url);
      WSRequest complexRequest = request.setRequestTimeout(Duration.ofMillis(REQUEST_TIMEOUT)).setMethod("GET");

      String id = _uuid.randomId();
      Path path = Paths.get(id + ".mp4");
      File file = new File(path.toAbsolutePath().toString());

      complexRequest.get()
          .thenApply(WSResponse::asByteArray)
          .thenApply(bytes -> {
            try {
              Files.write(bytes, file);
              fileList.add(file);
            } catch (IOException e) {
              Logger.error(e.getMessage());
            }
            return null;
          })
          .toCompletableFuture()
          .get();
    }
    return fileList;
  }
}
