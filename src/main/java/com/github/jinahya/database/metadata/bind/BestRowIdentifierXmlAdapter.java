package com.github.jinahya.database.metadata.bind;

import jakarta.xml.bind.annotation.adapters.XmlAdapter;

import java.util.List;
import java.util.Map;

class BestRowIdentifierXmlAdapter
        extends XmlAdapter<BestRowIdentifierXmlValue, Map<Integer, Map<Boolean, List<BestRowIdentifier>>>> {

    @Override
    public Map<Integer, Map<Boolean, List<BestRowIdentifier>>> unmarshal(final BestRowIdentifierXmlValue v)
            throws Exception {
        return BestRowIdentifierAdapterUtils.toMap(v.getBestRowIdentifierWrappers());
    }

    @Override
    public BestRowIdentifierXmlValue marshal(final Map<Integer, Map<Boolean, List<BestRowIdentifier>>> v)
            throws Exception {
        final BestRowIdentifierXmlValue bound = new BestRowIdentifierXmlValue();
        bound.getBestRowIdentifierWrappers().addAll(BestRowIdentifierAdapterUtils.toList(v));
        return bound;
    }
}
