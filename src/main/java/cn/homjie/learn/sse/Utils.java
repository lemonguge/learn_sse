package cn.homjie.learn.sse;

import java.util.AbstractMap.SimpleEntry;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;
import java.util.stream.Collectors;

/**
 * @author jiehong.jh
 * @date 2023/6/7
 */
public class Utils {

    /**
     * 顺序切分字符串
     *
     * @param text
     * @param keys
     * @return
     */
    public static List<Entry<String, String>> parse(String text, List<String> keys) {
        Segment segment = new Segment();
        segment.str = text;
        List<Segment> c = new ArrayList<>();
        c.add(segment);
        keys.stream()
            .sorted(Comparator.comparingInt(String::length).reversed())
            .forEach(key -> {
                int l = key.length();
                List<Segment> c2 = new ArrayList<>();
                Iterator<Segment> iter = c.iterator();
                while (iter.hasNext()) {
                    Segment e = iter.next();
                    int i, j = -1, f = 0;
                    while ((i = e.str.indexOf(key, f)) > -1) {
                        if (j == -1) {
                            c2.add(Segment.of(e.key, e.str.substring(0, i), e.index));
                        } else {
                            c2.add(Segment.of(key, e.str.substring(j + l, i), e.index + j));
                        }
                        j = i;
                        f = i + l;
                    }
                    if (j != -1) {
                        iter.remove();
                        c2.add(Segment.of(key, e.str.substring(j + l), e.index + j));
                    }
                }
                c.addAll(c2);
            });
        return c.stream()
            .filter(Segment::isNotEmpty).sorted()
            .map(e -> new SimpleEntry<>(e.key, e.str))
            .collect(Collectors.toList());
    }

    private static class Segment implements Comparable<Segment> {

        String key;
        String str;
        int index;

        boolean isNotEmpty() {
            return index != 0 || key != null || !"".equals(str);
        }

        static Segment of(String key, String str, int index) {
            Segment segment = new Segment();
            segment.key = key;
            segment.str = str;
            segment.index = index;
            return segment;
        }

        @Override
        public int compareTo(Segment o) {
            return Integer.compare(index, o.index);
        }
    }
}
