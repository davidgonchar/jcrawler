/*
 */
package org.dudunet.jcrawler.parser;


import java.io.UnsupportedEncodingException;
import java.util.ArrayList;

import org.dudunet.jcrawler.model.Link;
import org.dudunet.jcrawler.model.Page;
import org.dudunet.jcrawler.util.CharsetDetector;
import org.dudunet.jcrawler.util.RegexRule;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

/**
 * @author dudu
 */
public class HtmlParser implements Parser {

    public RegexRule getRegexRule() {
        return regexRule;
    }

    public void setRegexRule(RegexRule regexRule) {
        this.regexRule = regexRule;
    }

    private Integer topN;
    private RegexRule regexRule=null;

    /**
     */
    public HtmlParser() {
        topN = null;
    }

    /**
     * @param topN
     */
    public HtmlParser(Integer topN) {
        this.topN = topN;
    }

    /**
     * @param page
     * @return
     * @throws java.io.UnsupportedEncodingException
     */
    @Override
    public ParseResult getParse(Page page) throws UnsupportedEncodingException {
        String url=page.getUrl();
        
        String charset = CharsetDetector.guessEncoding(page.getContent());
        
        String html=new String(page.getContent(), charset);
        page.setHtml(html);
        
        Document doc=Jsoup.parse(page.getHtml());
        doc.setBaseUri(url);      
        page.setDoc(doc);
        
        String title=doc.title();
        String text=doc.text();
        
        ArrayList<Link> links = null;
        if(topN!=null && topN==0){
            links=new ArrayList<Link>();
        }else{
            links=topNFilter(LinkUtils.getAll(page));
        }
        ParseData parsedata = new ParseData(url,title, links);
        ParseText parsetext=new ParseText(url,text);
        
        return new ParseResult(parsedata,parsetext);
    }

    private ArrayList<Link> topNFilter(ArrayList<Link> origin_links) {
        ArrayList<Link> result=new ArrayList<Link>();
        int updatesize;
        if (topN == null) {
            updatesize = origin_links.size();
        } else {
            updatesize = Math.min(topN, origin_links.size());
        }

        int sum = 0;
        for (Link origin_link : origin_links) {
            if (sum >= updatesize) {
                break;
            }
            if (!regexRule.satisfy(origin_link.getUrl())) {
                continue;
            }
            result.add(origin_link);
            sum++;
        }
        return result;
    }

}
