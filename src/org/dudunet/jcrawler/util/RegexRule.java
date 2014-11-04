/*
 */
package org.dudunet.jcrawler.util;

import java.util.ArrayList;
import java.util.regex.Pattern;

/**
 *
 * @author dudu
 */
public class RegexRule {
    
    public RegexRule(){
        
    }
    
    public RegexRule(ArrayList<String> rules){
        for (String rule : rules) {
            addRule(rule);
        }
    }
    
    public boolean isEmpty(){
        return positive.isEmpty();
    }

    private ArrayList<String> positive = new ArrayList<String>();
    private ArrayList<String> negative = new ArrayList<String>();

    /**
     *
     * @param rule
     */
    public void addRule(String rule) {
        if (rule.length() == 0) {
            return;
        }
        char pn = rule.charAt(0);
        String realrule = rule.substring(1);
        if (pn == '+') {
            addPositive(realrule);
        } else if (pn == '-') {
            addNegative(realrule);
        } else {
            addPositive(rule);
        }
    }

    /**
     *
     * @param positiveregex
     */
    public void addPositive(String positiveregex) {
        positive.add(positiveregex);
    }

    /**
     *
     * @param negativeregex
     */
    public void addNegative(String negativeregex) {
        negative.add(negativeregex);
    }

    /**
     *
     * @return
     */
    public boolean satisfy(String str) {

        int state = 0;
        for (String nregex : negative) {
            if (Pattern.matches(nregex, str)) {
                return false;
            }
        }

        int count = 0;
        for (String pregex : positive) {
            if (Pattern.matches(pregex, str)) {
                count++;
            }
        }
        if (count == 0) {
            return false;
        } else {
            return true;
        }

    }
}
