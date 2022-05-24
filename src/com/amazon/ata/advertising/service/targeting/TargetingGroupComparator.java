package com.amazon.ata.advertising.service.targeting;

import java.util.Comparator;
import java.util.function.Function;
import java.util.function.ToDoubleFunction;
import java.util.function.ToIntFunction;
import java.util.function.ToLongFunction;

public class TargetingGroupComparator implements Comparator<TargetingGroup> {

    @Override
    public int compare(TargetingGroup o1, TargetingGroup o2) {
        if (o1.getClickThroughRate() > o2.getClickThroughRate()) {
            return 1;
        } else {
            return -1;
        }
    }

}