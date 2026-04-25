package net.nullpointer.rstaffchat.plugin.util;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.stream.JsonReader;
import net.nullpointer.rstaffchat.plugin.RStaffChatPlugin;
import org.bukkit.Bukkit;

import java.io.InputStreamReader;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.time.Instant;
import java.util.concurrent.Executors;
import java.util.function.Consumer;
import java.util.logging.Level;

public final class UpdateChecker {
    private static final String LATEST_RELEASE_URL =
            "https://api.github.com/repos/SQuazar/RStaffChat/releases/latest";

    private static final HttpClient CLIENT = HttpClient.newBuilder()
            .connectTimeout(Duration.ofSeconds(5))
            .executor(Executors.newSingleThreadExecutor())
            .build();

    private UpdateChecker() {
    }

    public static void fetchUpdate(RStaffChatPlugin plugin, Consumer<VersionInfo> info) {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(LATEST_RELEASE_URL))
                .timeout(Duration.ofSeconds(5))
                .GET()
                .build();
        CLIENT.sendAsync(request, HttpResponse.BodyHandlers.ofInputStream()).thenAccept(res -> {
            if (res.statusCode() != 200) {
                plugin.getLogger().warning("Cannot get latest release info. HTTP code: " + res.statusCode());
                return;
            }
            try (JsonReader reader = new JsonReader(new InputStreamReader(res.body()))) {
                JsonObject object = JsonParser.parseReader(reader).getAsJsonObject();
                String name = object.get("name").getAsString();
                String tag = object.get("tag_name").getAsString();
                String downloadUrl = object.get("html_url").getAsString();
                Instant publishedAt = Instant.parse(object.get("published_at").getAsString());
                boolean preRelease = object.get("prerelease").getAsBoolean();
                Bukkit.getScheduler().runTask(plugin, () ->
                        info.accept(new VersionInfo(
                                name,
                                tag,
                                downloadUrl,
                                publishedAt,
                                preRelease
                        ))
                );
            } catch (Exception e) {
                plugin.getLogger().log(Level.WARNING, "Cannot read version info", e);
            }
        }).exceptionally(e -> {
            plugin.getLogger().log(Level.WARNING, "Cannot make request to GitHub API", e);
            return null;
        });
    }

    public record VersionInfo(
            String name,
            String tag,
            String downloadUrl,
            Instant publishedAt,
            boolean preRelease
    ) {
    }
}
