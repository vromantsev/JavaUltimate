package com.bobocode.orm.session.factory;

import com.bobocode.orm.session.Session;
import com.bobocode.orm.session.SessionImpl;
import lombok.RequiredArgsConstructor;

import javax.sql.DataSource;
import java.util.Objects;

/**
 * Factory that constructs instance(s) of {@link Session}.
 */
@RequiredArgsConstructor
public class SessionFactory {

    private final DataSource dataSource;

    /**
     * Creates an instance of {@link Session} every time this method is called.
     *
     * @return instance of {@link Session}
     */
    public Session createSession() {
        Objects.requireNonNull(this.dataSource, "Data source must be initialized first!");
        return new SessionImpl(this.dataSource);
    }
}
