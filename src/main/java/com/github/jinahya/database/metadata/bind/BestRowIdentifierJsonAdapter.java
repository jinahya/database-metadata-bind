package com.github.jinahya.database.metadata.bind;

import jakarta.json.bind.adapter.JsonbAdapter;

import java.util.List;
import java.util.Map;

public class BestRowIdentifierJsonAdapter
        implements JsonbAdapter<Map<Integer, Map<Boolean, List<BestRowIdentifier>>>, List<BestRowIdentifierWrapper>> {

    @Override
    public List<BestRowIdentifierWrapper> adaptToJson(final Map<Integer, Map<Boolean, List<BestRowIdentifier>>> obj)
            throws Exception {
        return BestRowIdentifierAdapterUtils.toList(obj);
    }

    @Override
    public Map<Integer, Map<Boolean, List<BestRowIdentifier>>> adaptFromJson(final List<BestRowIdentifierWrapper> obj)
            throws Exception {
        return BestRowIdentifierAdapterUtils.toMap(obj);
    }
}
