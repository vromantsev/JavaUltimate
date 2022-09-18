package com.bobocode.orm.session.queue;

import com.bobocode.orm.session.queue.enums.ActionType;

public interface Action {

    int getOrder();

    String createSQL();

    ActionType getActionType();

    Object getEntity();

}
