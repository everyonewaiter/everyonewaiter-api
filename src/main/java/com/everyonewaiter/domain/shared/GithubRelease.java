package com.everyonewaiter.domain.shared;

import java.util.List;

public record GithubRelease(
    String url,
    String assets_url,
    String upload_url,
    String html_url,
    Long id,
    GithubAuthor author,
    String node_id,
    String tag_name,
    String target_commitish,
    String name,
    boolean draft,
    boolean immutable,
    boolean prerelease,
    String created_at,
    String updated_at,
    String published_at,
    List<GithubAsset> assets,
    String tarball_url,
    String zipball_url,
    String body
) {

}
