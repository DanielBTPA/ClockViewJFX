package com.alphabt.fx.control.clock.internal.work;

import java.util.HashMap;

/**
 * Created by danielbt on 27/06/16.
 */
public class ClockState extends ClockHandler {

    private static HashMap<Object, HashMap<String, Object>> objects;

    public static final Object GLOBAL_CONTEXT = new Object();

    static {
        initializeObjects();
    }

    private static void initializeObjects() {
        if (objects == null)
            objects = new HashMap<>();
    }

    protected ClockState() {
        super();
        initializeObjects();
    }

    protected void putInstanceState(String key, Object value) {
        putInstanceState(this, key, value);
    }

    protected Object getInstanceState(String key) {
        return getInstanceState(this, key);
    }

    public static void putInstanceState(Object context, String key, Object value) {
        HashMap<String, Object> objectHashMap;

        if (!objects.containsKey(context)) {
            objectHashMap = new HashMap<>();
            objects.put(context, objectHashMap);
        } else objectHashMap = objects.get(context);

        objectHashMap.put(key, value);
    }

    public static Object getInstanceState(Object context, String key) {
        if (!objects.containsKey(context) || !objects.get(context).containsKey(key))
            throw new NullPointerException("This object not exist.");

        return objects.get(context).get(key);
    }

    public static void putGlobalInstanceState(String key, Object value) {
        HashMap<String, Object> objectHashMap;

        if (!objects.containsKey(GLOBAL_CONTEXT)) {
            objectHashMap = new HashMap<>();
            objects.put(GLOBAL_CONTEXT, objectHashMap);
        } else objectHashMap = objects.get(GLOBAL_CONTEXT);

        objectHashMap.put(key, value);
    }

    public static Object getGlobalInstanceState(String key) {
        if (!objects.containsKey(GLOBAL_CONTEXT) || !objects.get(GLOBAL_CONTEXT).containsKey(key))
            throw new NullPointerException("This object not exist.");

        return objects.get(GLOBAL_CONTEXT).get(key);
    }

}
