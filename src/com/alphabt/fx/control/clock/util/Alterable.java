package com.alphabt.fx.control.clock.util;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;

/**
 * Created by danielbt on 01/06/16.
 */
public interface Alterable<E> {

    Selector<E> selector();

    abstract class Selector<E> implements SelectorListener<E> {

        private E[] elements;
        private int position;

        public Selector() {
            elements = getElements();
            checkArray(elements);
        }

        protected abstract E[] getElements();

        public E change() {
            if (elements != null) {
                checkArray(elements);
                E object = elements[position];
                onChange(object);
                position = position < (elements.length - 1) ? position + 1 : 0;
                return object;
            }

            return null;
        }

        private void checkArray(E[] elements) {
            if (elements == null && elements.length == 0) {
                throw new NullPointerException("Use 'getElements' for return objects to be use.");
            }
        }
    }

    interface SelectorListener<E> {
        void onChange(E object);
    }
}
