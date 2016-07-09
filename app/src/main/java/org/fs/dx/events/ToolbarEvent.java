package org.fs.dx.events;

import android.support.v7.widget.Toolbar;

import org.fs.common.IEvent;

/**
 * Created by Fatih on 08/07/16.
 * as org.fs.dx.events.ToolbarEvent
 */
public final class ToolbarEvent implements IEvent {

    public final Toolbar toolbar;

    public ToolbarEvent(final Toolbar toolbar) {
        this.toolbar = toolbar;
    }
}