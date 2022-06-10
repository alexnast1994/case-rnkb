package com.cognive.projects.casernkb.bpm;

import com.prime.db.rnkb.model.BaseDictionary;

public class TestUtils {
    public static BaseDictionary getBaseDictionary(String code) {
        BaseDictionary bd = new BaseDictionary();
        bd.setCode(code);
        return bd;
    }
}
