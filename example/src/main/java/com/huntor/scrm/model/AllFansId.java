package com.huntor.scrm.model;

import java.util.List;

/**
 * Created by cao on 2015/6/26.
 */
public class AllFansId extends Response {
    public List<Integer> ids;

    @Override
    public String toString() {
        return "AllFansId{" +
                "ids=" + ids +
                '}';
    }
}
