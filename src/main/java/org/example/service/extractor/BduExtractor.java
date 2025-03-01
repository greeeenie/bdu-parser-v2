package org.example.service.extractor;

import org.example.utils.JsoupUtils;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class BduExtractor {

    public String id(Element vulnerability) {
        return JsoupUtils.findChildByTag(vulnerability, "identifier")
                .map(Element::text)
                .orElseThrow(() -> new IllegalStateException("Identifier is not present"));
    }

    public String name(Element vulnerability) {
        return JsoupUtils.findChildByTag(vulnerability, "name")
                .map(Element::text)
                .orElseThrow(() -> new IllegalStateException("Name is not present"));
    }

    public String description(Element vulnerability) {
        return JsoupUtils.findChildByTag(vulnerability, "description")
                .map(Element::text)
                .orElseThrow(() -> new IllegalStateException("Description is not present"));
    }

    public List<Integer> cweIds(Element vulnerability) {
        return cwes(vulnerability).map(cwes ->
                        cwes.children().stream()
                                .map(el -> el.text().substring(4))
                                .map(Integer::parseInt)
                                .toList())
                .orElse(List.of());
    }

    public Element vulnerability(Element vulnerabilities, int index) {
        return vulnerabilities.child(index);
    }

    public Element vulnerabilities(Document document) {
        return document.getElementsByTag("vulnerabilities").first();
    }

    private Optional<Element> cwes(Element vulnerability) {
        return JsoupUtils.findChildByTag(vulnerability, "cwe");
    }
}
