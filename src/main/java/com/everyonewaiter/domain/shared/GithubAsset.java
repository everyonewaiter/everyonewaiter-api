package com.everyonewaiter.domain.shared;

import jakarta.annotation.Nullable;

public record GithubAsset(
    String url,
    Long id,
    String node_id,
    String name,
    @Nullable String label,
    GithubAuthor uploader,
    String content_type,
    String state,
    Long size,
    String digest,
    Long download_count,
    String created_at,
    String updated_at,
    String browser_download_url
) {

}
