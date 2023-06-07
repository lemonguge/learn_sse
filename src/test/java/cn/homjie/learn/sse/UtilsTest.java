package cn.homjie.learn.sse;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map.Entry;
import java.util.stream.Collectors;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

class UtilsTest {

    @Test
    void parse() {
        String text = "aabbcc";
        List<String> keys = Collections.singletonList("ab");
        List<Entry<String, String>> c = Utils.parse(text, keys);
        assertEquals(2, c.size());
        assertNull(c.get(0).getKey());
        assertEquals("a", c.get(0).getValue());
        assertEquals("ab", c.get(1).getKey());
        assertEquals("bcc", c.get(1).getValue());

        text = "aaaabbbcccaaabb";
        keys = Arrays.asList("aa", "aab");
        c = Utils.parse(text, keys);
        assertEquals(3, c.size());
        assertEquals("aa", c.get(0).getKey());
        assertEquals("", c.get(0).getValue());
        assertEquals("aab", c.get(1).getKey());
        assertEquals("bbccca", c.get(1).getValue());
        assertEquals("aab", c.get(2).getKey());
        assertEquals("b", c.get(2).getValue());
        assertEquals(text, c.stream().map(e -> e.getKey() + e.getValue()).collect(Collectors.joining()));

        text = "Thought: I need to find Leo DiCaprio's girlfriend and her current age.\n"
            + "Action: Search\n"
            + "Action Input: Leo DiCaprio's girlfriend\n"
            + "Observation: Camila Morrone\n"
            + "Thought: I will use the search engine to look for Leo DiCaprio's girlfriend and her current age.\n"
            + "Action: Search\n"
            + "Action Input: Leo DiCaprio's girlfriend, Camila Morrone\n"
            + "Observation: Camila Morrone's age is 26 years old";
        keys = Arrays.asList("Thought", "Action", "Action Input", "Observation", "Final Answer");
        c = Utils.parse(text, keys);
        assertEquals(8, c.size());
        assertEquals("Thought", c.get(0).getKey());
        assertEquals("Action", c.get(1).getKey());
        assertEquals(": Search\n", c.get(1).getValue());
        assertEquals("Observation", c.get(7).getKey());
        assertEquals(": Camila Morrone's age is 26 years old", c.get(7).getValue());
        assertEquals(text, c.stream().map(e -> e.getKey() + e.getValue()).collect(Collectors.joining()));
    }
}