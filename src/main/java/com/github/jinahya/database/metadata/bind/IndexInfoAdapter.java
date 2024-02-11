package com.github.jinahya.database.metadata.bind;

import jakarta.xml.bind.annotation.adapters.XmlAdapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

class IndexInfoAdapter extends XmlAdapter<IndexInfoWrapper, Map<Boolean, Map<Boolean, List<IndexInfo>>>> {

    @Override
    public Map<Boolean, Map<Boolean, List<IndexInfo>>> unmarshal(final IndexInfoWrapper v) throws Exception {
        final Map<Boolean, Map<Boolean, List<IndexInfo>>> bound = new HashMap<>();
        v.getCategories().forEach(c -> {
            bound.computeIfAbsent(c.isUnique(), k -> new HashMap<>())
                    .computeIfAbsent(c.isApproximate(), k -> new ArrayList<>())
                    .addAll(c.getIndexInfo_());
        });
        return bound;
    }

    @Override
    public IndexInfoWrapper marshal(final Map<Boolean, Map<Boolean, List<IndexInfo>>> v) throws Exception {
        final IndexInfoWrapper value = new IndexInfoWrapper();
        v.forEach((u, m) -> {
            final IndexInfoCategory category = new IndexInfoCategory();
            category.setUnique(u);
            m.forEach((a, l) -> {
                category.setApproximate(a);
                category.getIndexInfo_().addAll(l);
            });
        });
        return value;
    }
}
