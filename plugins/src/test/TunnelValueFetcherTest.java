// package test;

import org.junit.Test;

import m32plugin.TunnelValueFetcher;

public class TunnelValueFetcherTest {
    @Test()
    public void testGetTunnelValue() {
        TunnelValueFetcher tvf = new TunnelValueFetcher();

        tvf.getTunnelValue(null, "TestTunnel");
    }
}
