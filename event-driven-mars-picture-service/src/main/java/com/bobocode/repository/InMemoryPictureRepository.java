package com.bobocode.repository;

import com.bobocode.exceptions.PictureNotFoundException;
import org.springframework.stereotype.Repository;

import java.net.URI;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

@Repository
public class InMemoryPictureRepository implements PictureRepository {

    private static final ConcurrentMap<String, String> STORAGE = new ConcurrentHashMap<>();

    @Override
    public void save(final String commandId, final String pictureUrl) {
        Objects.requireNonNull(commandId, "Parameter [commandId] must not be null!");
        Objects.requireNonNull(pictureUrl, "Parameter [pictureUrl] must not be null!");
        STORAGE.put(commandId, pictureUrl);
    }

    @Override
    public URI getPicture(final String commandId) {
        return Optional.ofNullable(STORAGE.get(commandId))
                .map(URI::create)
                .orElseThrow(() -> new PictureNotFoundException("No picture found by commandId='%s'".formatted(commandId)));
    }
}
