package org.example.service.extractor;

import org.example.utils.JsoupUtils;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ScanReportExtractor {

    public String bduId(Element element) {
        return JsoupUtils.findChildByClass(element, "bdu")
                .map(Element::text)
                .map(text -> text.split(" ")[0])
                .orElseThrow(() -> new IllegalStateException("Bdu IDs are not present"));
    }

    public String fullBduIds(Element element) {
        return JsoupUtils.findChildByClass(element, "bdu")
                .map(Element::text)
                .orElseThrow(() -> new IllegalStateException("Bdu IDs are not present"));
    }

    public String description(Element element) {
        return JsoupUtils.findChildByClass(element, "desc")
                .map(Element::text)
                .orElseThrow(() -> new IllegalStateException("Description is not present"));
    }

    public List<Element> bdus(Document doc) {
        return doc.selectXpath("//*[@class='vulnerabilities']//*[@class='bdu']/..");
    }
}
