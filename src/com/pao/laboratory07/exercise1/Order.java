package com.pao.laboratory07.exercise1;

import com.pao.laboratory07.exercise1.exceptions.CannotRevertInitialOrderStateException;

import java.util.Stack;

public class Order {
    private Stack<OrderState> history = new Stack<OrderState>();

    public Order(OrderState initial) {
        history.push(initial);
    }

    public void nextState() {
        history.push(history.peek().next());
        System.out.println("Order state updated to: " + history.peek());
    }

    public void cancel() {
        history.push(history.peek().cancelState());
        System.out.println("Order has been canceled.");
    }

    public void undoState() {
        if (history.empty())
            throw new CannotRevertInitialOrderStateException("Cannot undo the initial order state.");
        history.pop();
        System.out.println("Order state reverted to: " + history.peek());
    }
}
