package org.example.service.extractor;

import org.example.utils.JsoupUtils;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CweExtractor {

    public int id(Element element) {
        return Integer.parseInt(element.attr("ID"));
    }

    public String name(Element element) {
        return element.attr("Name");
    }

    public List<Integer> capecIds(Element element) {
        return relatedAttackPatterns(element)
                .map(patterns -> patterns.children()
                        .stream()
                        .map(el -> el.attr("CAPEC_ID"))
                        .map(Integer::parseInt)
                        .toList()
                )
                .orElse(List.of());
    }

    public Element weakness(Element weaknesses, int index) {
        return weaknesses.child(index);
    }

    public Element weaknesses(Document document) {
        return document.getElementsByTag("Weaknesses").first();
    }

    public Element category(Element categories, int index) {
        return categories.child(index);
    }

    public Element categories(Document document) { return document.getElementsByTag("Categories").first(); }

    public Element view(Element categories, int index) {
        return categories.child(index);
    }

    public Element views(Document document) { return document.getElementsByTag("Views").first(); }

    private Optional<Element> relatedAttackPatterns(Element element) {
        return JsoupUtils.findChildByTag(element, "Related_Attack_Patterns");
    }

}
