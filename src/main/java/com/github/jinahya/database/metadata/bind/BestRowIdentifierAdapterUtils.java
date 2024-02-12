package com.github.jinahya.database.metadata.bind;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

final class BestRowIdentifierAdapterUtils {

    static List<BestRowIdentifierWrapper> toList(final Map<Integer, Map<Boolean, List<BestRowIdentifier>>> obj) {
        if (obj == null) {
            return null;
        }
        final List<BestRowIdentifierWrapper> adapted = new ArrayList<>();
        obj.forEach((s, m) -> m.forEach((n, l) -> {
            final BestRowIdentifierWrapper wrapper = new BestRowIdentifierWrapper();
            wrapper.setScope(s);
            wrapper.setNullable(n);
            wrapper.getWrapped().addAll(l);
            adapted.add(wrapper);
        }));
        return adapted;
    }

    static Map<Integer, Map<Boolean, List<BestRowIdentifier>>> toMap(final List<BestRowIdentifierWrapper> obj) {
        if (obj == null) {
            return null;
        }
        final Map<Integer, Map<Boolean, List<BestRowIdentifier>>> adapted = new HashMap<>();
        obj.forEach(w -> {
            adapted.computeIfAbsent(w.getScope(), k -> new HashMap<>())
                    .computeIfAbsent(w.isNullable(), k -> new ArrayList<>())
                    .addAll(w.getWrapped());
        });
        return adapted;
    }

    private BestRowIdentifierAdapterUtils() {
        throw new AssertionError("instantiation is not allowed");
    }
}
