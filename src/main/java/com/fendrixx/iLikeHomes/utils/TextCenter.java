package com.fendrixx.iLikeHomes.utils;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class TextCenter {
    // yeah i made an entire class just for center my text
    // AND WHAT IF I DO IT :sob:
    public static final int CHAT_WIDTH = 300;
    public static final Map<Character, Integer> FONT_LENGTH;

    static {
        Map<Character, Integer> map = new HashMap<>();
        Arrays.asList('i', 'l', '!', ':', ';', '\'', '|', '.', ',').forEach(c -> map.put(c, 1));
        map.put('`', 2);
        Arrays.asList('I', '[', ']', '"', ' ').forEach(c -> map.put(c, 3));
        Arrays.asList('f', 'k', 't', '(', ')', '{', '}', '<', '>').forEach(c -> map.put(c, 4));
        map.put('@', 6);
        FONT_LENGTH = Collections.unmodifiableMap(map);
    }

    public static String centerText(String text) {
        if (text == null)
            return "";
        int length = getFontLength(text);
        if (length >= CHAT_WIDTH)
            return text;

        int px = CHAT_WIDTH - length;
        if (px < 4)
            return text;

        int count = 0;
        while (px >= 4) {
            count++;
            px -= 4;
        }
        return " ".repeat(count) + text;
    }

    public static int getFontLength(String s) {
        int px = 0;
        boolean bold = false;
        for (int i = 0; i < s.length(); i++) {
            char c = s.charAt(i);
            if (c == '§' && i + 1 < s.length()) {
                char c1 = s.charAt(i + 1);
                if (c1 == 'l' || c1 == 'L')
                    bold = true;
                else if (c1 == 'r' || c1 == 'R')
                    bold = false;
                i++;
            } else {
                int length = FONT_LENGTH.getOrDefault(c, 5);
                px += bold ? length + 1 : length;
                px++;
            }
        }
        return px;
    }
}