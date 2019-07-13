package org.wesignproject.translate;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import javaslang.control.Try;
import javax.inject.Inject;
import net.bramp.ffmpeg.FFmpeg;
import net.bramp.ffmpeg.FFmpegExecutor;
import net.bramp.ffmpeg.FFprobe;
import net.bramp.ffmpeg.builder.FFmpegBuilder;
import org.wesignproject.models.Vocabulary;
import org.wesignproject.utils.UUID;
import org.wesignproject.utils.VideoFetcher;

import static java.util.stream.Collectors.joining;


/**
 * Class responsible for Video Synthesis
 */
public class VideoSynthesizer {
  private final VideoFetcher _videoFetcher;
  private FFmpegExecutor _ffmpegExecutor;

  @Inject
  public VideoSynthesizer(VideoFetcher videoFetcher) {
    _videoFetcher = videoFetcher;
    try {
      _ffmpegExecutor = new FFmpegExecutor(new FFmpeg(), new FFprobe());
    } catch (IOException ex) {
      ex.printStackTrace();
    }
  }

  /**
   * Utilize FFMPEG to concat videos of vocabulary into one
   * @param input sorted order of vocabularies
   * @return the concatenated file
   * @throws IOException exception
   * @throws InterruptedException exception
   * @throws ExecutionException exception
   */
  public File synVideo(List<Vocabulary> input) throws IOException, InterruptedException, ExecutionException {
    List<String> files = new ArrayList<>();
    String id = new UUID().randomId();
    Path target = Paths.get("output-" + id + ".mp4");
    Path listOfFiles = Files.createTempFile(Paths.get("").toAbsolutePath(), "ffmpeg-list-", ".txt");

    for (Vocabulary v: input) {
      files.add(v.getPath());
    }

    List<File> downloadedVideos = _videoFetcher.downloadVideos(files);

    String filesStrings = downloadedVideos.stream()
        .map(File::getName)
        .map(p -> "file '" + p + "'")
        .collect(joining(System.getProperty("line.separator")));

    Try.of(() -> Files.write(listOfFiles, filesStrings.getBytes()));

    FFmpegBuilder builder = new FFmpegBuilder().setInput(listOfFiles.toAbsolutePath().toString())
        .setFormat("concat")
        .addOutput(target.toAbsolutePath().toString())
        .disableAudio()
        .setVideoCodec("copy")
        .done();

    _ffmpegExecutor.createJob(builder).run();

    Files.deleteIfExists(listOfFiles);
    for (File f: downloadedVideos) {
      Files.deleteIfExists(f.toPath());
    }

    return new File(target.toAbsolutePath().toString());
  }
}
