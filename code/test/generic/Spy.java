package app.test.generic;

import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.stereotype.Service;

import app.api.service.EventsService;
import app.api.service.NewsService;
import app.api.service.WeatherService;
import app.manager.AccountManager;
import app.manager.QueryManager;

@Service
public class Spy {
    @SpyBean public AccountManager accountManager;
    @SpyBean public QueryManager queryManager;
    @SpyBean public WeatherService weatherService;
    @SpyBean public EventsService eventsService;
    @SpyBean public NewsService newsService;
}
