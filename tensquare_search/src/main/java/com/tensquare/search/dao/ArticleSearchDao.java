package com.tensquare.search.dao;

import com.tensquare.search.pojo.Article;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

public interface ArticleSearchDao extends ElasticsearchRepository<Article,String> {
    /**
     * 关键字搜索文章
     * @param keywords
     * @param pageable
     * @return
     */
    Page<Article> findByTitleLike(String keywords, Pageable pageable);
}
