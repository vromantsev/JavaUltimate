package com.bobocode.orm.session.queue;

import com.bobocode.orm.session.queue.enums.ActionType;
import com.bobocode.orm.utils.EntityUtils;
import lombok.RequiredArgsConstructor;

import java.util.List;

@RequiredArgsConstructor
public class InsertAction implements Action {

    private static final int INSERT_ACTION_ORDER = 0;
    private static final String INSERT_QUERY = "INSERT INTO %s %s VALUES %s";

    private final Object entity;

    @Override
    public int getOrder() {
        return INSERT_ACTION_ORDER;
    }

    @Override
    public String createSQL() {
        final List<String> columns = EntityUtils.getSortedColumnNamesWithoutId(entity);
        return String.format(
                INSERT_QUERY,
                EntityUtils.getTableName(entity.getClass()),
                "(" + EntityUtils.join(columns) + ")",
                "(" + EntityUtils.getMappings(columns) + ")"
        );
    }

    @Override
    public ActionType getActionType() {
        return ActionType.INSERT;
    }

    @Override
    public Object getEntity() {
        return this.entity;
    }
}
