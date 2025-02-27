package chill.script.templates.commands;

import org.junit.jupiter.api.Test;

import java.util.List;

import static chill.script.templates.TestHelpers.renderTemplate;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class ForCommandTest {

    @Test
    public void basicForCommand() {
        assertEquals("1\n2\n3\n", renderTemplate(
                "#for x in lst\n" +
                        "${x}\n" +
                        "#end", "lst", List.of(1, 2, 3)));
    }

    @Test
    public void basicForCommandWListLiteral() {
        assertEquals("\n1\n2\n3\n", renderTemplate(
                "\n" +
                        "#for x in [1, 2, 3]\n" +
                        "${x}\n" +
                        "#end"));
    }

    @Test
    public void doesNotPolluteGlobals() {
        assertEquals("\nHello\n" +
                "1\n" +
                "2\n" +
                "3\n" +
                "Hello\n" +
                "", renderTemplate(
                "\n" +
                        "${x}\n" +
                        "#for x in [1, 2, 3]\n" +
                        "${x}\n" +
                        "#end\n" +
                        "${x}\n", "x", "Hello"));
    }

}
