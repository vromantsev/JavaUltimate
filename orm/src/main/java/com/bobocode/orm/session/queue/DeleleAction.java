package com.bobocode.orm.session.queue;

import com.bobocode.orm.session.queue.enums.ActionType;
import com.bobocode.orm.utils.EntityUtils;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class DeleleAction implements Action {

    private static final int DELETE_ACTION_ORDER = 2;
    private static final String DELETE_QUERY = "DELETE FROM %s WHERE %s";

    private final Object entity;

    @Override
    public int getOrder() {
        return DELETE_ACTION_ORDER;
    }

    @Override
    public String createSQL() {
        return String.format(
                DELETE_QUERY,
                EntityUtils.getTableName(entity.getClass()),
                EntityUtils.getColumnName(EntityUtils.getIdField(this.entity)) + "=?"
        );
    }

    @Override
    public ActionType getActionType() {
        return ActionType.DELETE;
    }

    @Override
    public Object getEntity() {
        return this.entity;
    }
}
