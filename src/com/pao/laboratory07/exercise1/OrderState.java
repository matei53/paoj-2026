package com.pao.laboratory07.exercise1;

import com.pao.laboratory07.exercise1.exceptions.CannotCancelFinalOrderException;
import com.pao.laboratory07.exercise1.exceptions.OrderIsAlreadyFinalException;

public enum OrderState {
    PLACED {
        public OrderState next() {
            return PROCESSED;
        }
    },
    PROCESSED {
        public OrderState next() {
            return SHIPPED;
        }
    },
    SHIPPED {
        public OrderState next() {
            return DELIVERED;
        }
    },
    DELIVERED {
        public OrderState next() {
            throw new OrderIsAlreadyFinalException("Order is already in a final state.");
        }

        @Override
        public OrderState cancelState() {
            throw new CannotCancelFinalOrderException("Cannot cancel a final state order.");
        }
    },
    CANCELED {
        public OrderState next() {
            throw new OrderIsAlreadyFinalException("Order is already in a final state.");
        }

        @Override
        public OrderState cancelState() {
            throw new CannotCancelFinalOrderException("Cannot cancel a final state order.");
        }
    };

    public abstract OrderState next();
    public OrderState cancelState() {
        return CANCELED;
    }
}
