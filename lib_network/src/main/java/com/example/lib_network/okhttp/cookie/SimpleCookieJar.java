package com.example.lib_network.okhttp.cookie;

import org.jetbrains.annotations.NotNull;

import java.util.List;

import okhttp3.Cookie;
import okhttp3.CookieJar;
import okhttp3.HttpUrl;

/**
 * @author Sherlock
 */
public class SimpleCookieJar implements CookieJar {
    @NotNull
    @Override
    public List<Cookie> loadForRequest(@NotNull HttpUrl httpUrl) {
        return null;
    }

    @Override
    public void saveFromResponse(@NotNull HttpUrl httpUrl,@NotNull List<Cookie> list) {

    }
}
