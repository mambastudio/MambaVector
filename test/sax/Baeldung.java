/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sax;

import java.util.List;

/**
 *
 * @author jmburu
 */
public class Baeldung 
{
    protected List<BaeldungArticle> articleList;
    // usual getters and setters
    
    public void setArticleList(List<BaeldungArticle> articleList) {
        this.articleList = articleList;
    }

    public List<BaeldungArticle> getArticleList() {
        return this.articleList;
    }
}