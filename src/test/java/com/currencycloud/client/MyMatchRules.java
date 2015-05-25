package com.currencycloud.client;

import co.freeside.betamax.MatchRule;
import co.freeside.betamax.message.Request;

import java.util.Map;
import java.util.Objects;

public enum MyMatchRules implements MatchRule {
    includesHeaders {
        @Override
        public boolean isMatch(Request a, Request b) {
            // todo: which of a, b is which?
            Map<String, String> ah = a.getHeaders();
            Map<String, String> bh = b.getHeaders();
            for (String hdr : ah.keySet()) {
                if (!Objects.equals(ah.get(hdr), bh.get(hdr))) {
                    return false;
                }
            }
            return true;
        }
    },

    never {
        @Override public boolean isMatch(Request a, Request b) { return false; }
    }
}
