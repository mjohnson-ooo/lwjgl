package org.lwjgl.opengl;

import java.util.ArrayDeque;
import java.util.Queue;

import org.lwjgl.input.IME.IMEEvent;
import org.lwjgl.input.IME.State;

/**
 * A queue for IME events.
 */
class IMEQueue {
    private static final int DEFAULT_QUEUE_SIZE = 4;

    private final ArrayDeque<IMEEvent> _queue = new ArrayDeque<IMEEvent>(DEFAULT_QUEUE_SIZE);

    private IMEEvent _currentEvent;

    public synchronized void putEvent (IMEEvent event)
    {
        if (_currentEvent == null) {
            _currentEvent = new IMEEvent();
            _queue.addLast(_currentEvent);
        }
        event.copy(_currentEvent);
        if (_currentEvent.state == State.END || _currentEvent.state == State.RESULT) {
            _currentEvent = null;
        }
        System.out.println("IMEEvent.putEvent, state=" + event.state.name() + ", size=" + _queue.size());
    }

    public synchronized void copyEvents (Queue<IMEEvent> queue)
    {
        for (IMEEvent event : _queue) {
            queue.add(event);
        }
        if (_queue.size() > 0) {
            System.out.println("IMEEvent.copyEvents, in size=" + _queue.size() + ", out size=" + queue.size());
        }
        _queue.clear();
        _currentEvent = null;
    }
}
