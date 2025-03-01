package org.example.service.extractor;

import org.example.utils.JsoupUtils;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.springframework.stereotype.Service;

@Service
public class CapecExtractor {

    public int id(Element attackPattern) {
        return Integer.parseInt(attackPattern.attr("ID"));
    }

    public String name(Element attackPattern) {
        return attackPattern.attr("Name");
    }

    public String likelihood(Element attackPattern) {
        return JsoupUtils.findChildByTag(attackPattern, "Likelihood_Of_Attack")
                .map(Element::text)
                .orElse("Unspecified");
    }

    public Element attackPattern(Element attackPatterns, int index) {
        return attackPatterns.child(index);
    }

    public Element attackPatterns(Document document) {
        return document.getElementsByTag("Attack_Patterns").first();
    }

}
