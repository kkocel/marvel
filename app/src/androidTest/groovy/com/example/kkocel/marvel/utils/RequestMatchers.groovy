package com.example.kkocel.marvel.utils

import io.appflate.restmock.utils.RequestMatcher
import okhttp3.mockwebserver.RecordedRequest

public class RequestMatchers {
    public static RequestMatcher pathEndsWith(final String endOfUrlPath) {
        return new RequestMatcher("url ends with: ${endOfUrlPath}") {
            protected boolean matchesSafely(RecordedRequest item) {
                String endOfPathSanitized = sanitizePath(endOfUrlPath)
                String recordedPathSanitized = sanitizePath(item.path)
                return recordedPathSanitized.endsWith(endOfPathSanitized)
            }
        }
    }

    public static RequestMatcher lastPathSegmentIsInteger() {
        return new RequestMatcher("last path segment is integer") {
            protected boolean matchesSafely(RecordedRequest item) {
                return sanitizePath(item.path).tokenize("/").last().isNumber()
            }
        }
    }

    private static String sanitizePath(String path) {
        try {
            return new URL("http", "localhost", path).path
        } catch (MalformedURLException e) {
            throw new RuntimeException(e)
        }
    }
}