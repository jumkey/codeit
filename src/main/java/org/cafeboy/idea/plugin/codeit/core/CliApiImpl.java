package org.cafeboy.idea.plugin.codeit.core;

import com.google.common.collect.ImmutableMap;
import org.apache.http.util.TextUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import javax.swing.*;
import java.io.IOException;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Map;

/**
 * 草料
 *
 * @author jumkey
 */
public class CliApiImpl implements Api {
    private static final String HOST = "https://cli.im/api";
    private static final String QCODE = "/qrcode/code";

    private final Map<String, String> map = ImmutableMap.of(MODE_0, "",
            MODE_1, "vUbEWVm7mp0hPn0nLdc",
            MODE_2, "50TGWQy7k54hMHctLNBcMK4");

    @Override
    public Icon getCode(String text, String mode) {
        String url = buildUrl(text, map.get(mode));

        try {
            Document document = Jsoup.connect(url).get();
            Elements elements = document.body().getElementsByClass("qrcode_plugins_box_body");
            String img = elements.get(0).getElementsByTag("img").attr("src");
            if (TextUtils.isEmpty(img)) {
                return null;
            } else {
                URL urlI = new URL("https:" + img);
                ImageIcon icon = new ImageIcon(urlI);
                icon.setImage(icon.getImage().getScaledInstance(200, 200, 1));
                return icon;
            }
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    private static String buildUrl(String text, String mhid) {
        text = URLEncoder.encode(text, StandardCharsets.UTF_8);

        return HOST + QCODE + "?" + "text=" + text + "&mhid=" + mhid;
    }
}
