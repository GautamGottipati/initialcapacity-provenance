package io.collective.articles;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.collective.restsupport.BasicHandler;
import org.eclipse.jetty.server.Request;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;

public class ArticlesController extends BasicHandler {
    private final ArticleDataGateway gateway;

    public ArticlesController(ObjectMapper mapper, ArticleDataGateway gateway) {
        super(mapper);
        this.gateway = gateway;
    }

    @Override
    public void handle(String target, Request request, HttpServletRequest servletRequest, HttpServletResponse servletResponse) {
        get("/articles", List.of("application/json", "text/html"), request, servletResponse, () -> {

            { // todo - query the articles gateway for *all* articles, map record to infos, and send back a collection of article infos
                List<ArticleRecord> records = gateway.findAll();
                List<ArticleInfo> articles = new ArrayList<>();
                for(ArticleRecord record: records){
                    articles.add(new ArticleInfo(record.getId(), record.getTitle()));
                }
                writeJsonBody(servletResponse, articles);
            }
        });

        get("/available", List.of("application/json"), request, servletResponse, () -> {

            { // todo - query the articles gateway for *available* articles, map records to infos, and send back a collection of article infos
                List<ArticleRecord> availableArticleRecords = gateway.findAvailable();
                List<ArticleInfo> articleInfos = new ArrayList<>();
                for(ArticleRecord articleRecord: availableArticleRecords)
                {
                    articleInfos.add(new ArticleInfo(articleRecord.getId(),articleRecord.getTitle()));
                }
                writeJsonBody(servletResponse, articleInfos);            }
        });
    }
}
