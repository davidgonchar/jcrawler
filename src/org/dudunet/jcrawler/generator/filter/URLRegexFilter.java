/*
 * Copyright (C) 2014 hu
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA  02111-1307, USA.
 */
package org.dudunet.jcrawler.generator.filter;

import org.dudunet.jcrawler.generator.Generator;
import org.dudunet.jcrawler.model.CrawlingData;
import org.dudunet.jcrawler.util.RegexRule;

/**
 * @author dudu
 */
public class URLRegexFilter extends Filter {
    
    private RegexRule regexRule=null;

   
    /**
     * @param generator
     * @param regexRule
     */
    public URLRegexFilter(Generator generator, RegexRule regexRule) {
        super(generator);
        this.regexRule=regexRule;
    }

    
    
    /**
     * @return
     */
    @Override
    public CrawlingData next() {
        while (true) {
            CrawlingData crawldata = generator.next();
            if (crawldata == null) {
                return null;
            }
            
            String url = crawldata.getUrl();
            if(regexRule.satisfy(url)){
                return crawldata;
            }else{
                continue;
            }
            
        }
    }

}
