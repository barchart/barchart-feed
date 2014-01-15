package com.barchart.feed.series.network;

import static org.junit.Assert.assertNotNull;

import org.junit.Test;

import com.barchart.feed.api.series.network.Analytic;

public class AnalyticNodeDescriptorTest {

    @Test
    public void testInstantiateAnalytic() {
       String entry = "PivotPointOverlay;PP_S3L,Overlay,Base;com.barchart.feed.series.analytics.PivotPoint;2;Support3;Overlay,IO;Base,IO";
       AnalyticNodeDescriptor desc = NetworkSchema.loadDescriptor(entry.split(";"));
       assertNotNull(desc);
       
       Analytic analytic = desc.instantiateAnalytic();
       assertNotNull(analytic);
    }

}
