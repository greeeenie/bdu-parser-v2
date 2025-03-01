package org.example.utils;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.jsoup.nodes.Element;

import java.util.Optional;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class JsoupUtils {

    public static Optional<Element> findChildByTag(Element element, String tagName) {
        return element.children()
                .stream()
                .filter(child -> child.tagName().equals(tagName))
                .findFirst();
    }

    public static Optional<Element> findChildByClass(Element element, String className) {
        return element.children()
                .stream()
                .filter(child -> child.hasClass(className))
                .findFirst();
    }
}
