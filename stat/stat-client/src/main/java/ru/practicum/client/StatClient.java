package ru.practicum.client;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.util.DefaultUriBuilderFactory;
import ru.practicum.hit.HitDto;
import ru.practicum.stat.StatDto;

import java.util.List;

@Service
public class StatClient extends BaseClient {

    @Autowired
    public StatClient(@Value("http://stats-server:9090") String serverUrl, RestTemplateBuilder builder) {
        super(
                builder
                        .uriTemplateHandler(new DefaultUriBuilderFactory(serverUrl))
                        .requestFactory(HttpComponentsClientHttpRequestFactory::new)
                        .build()
        );
    }

    public ResponseEntity<Object> createHit(HitDto hitDto) {
        return post("/hit", hitDto);
    }

    public ResponseEntity<List<StatDto>> getStats(String start, String end,
                                                  String[] uris, Boolean unique) {
        StringBuilder sbPath = new StringBuilder("/stats?start=").append(start)
                .append("&end=").append(end);
        if (uris != null) {
            for (String uri : uris) {
                sbPath.append("&uris=").append(uri);
            }
        }
        String path = sbPath.append("&unique=").append(unique).toString();
        return getStats(path);
    }

}
