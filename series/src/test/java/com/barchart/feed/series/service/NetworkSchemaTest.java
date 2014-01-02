package com.barchart.feed.series.service;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.List;

import org.junit.Test;

public class NetworkSchemaTest {

    @Test
    public void testGetNetwork() {
        NetworkSchema schema = NetworkSchema.getNetwork("PivotPointOverlay");
        assertNotNull(schema);
        
        assertEquals("PivotPointOverlay", schema.getNetworkName());
        
        List<AnalyticNodeDescriptor> descList = schema.getNetworkNodes();
        assertNotNull(descList);
        assertEquals(7, descList.size());
        assertEquals("PP_R3L", descList.get(0).getSpecifier());
        assertEquals("PP_R2L", descList.get(1).getSpecifier());
        assertEquals("PP_R1L", descList.get(2).getSpecifier());
        assertEquals("PP_PL", descList.get(3).getSpecifier());
        assertEquals("PP_S1L", descList.get(4).getSpecifier());
        assertEquals("PP_S2L", descList.get(5).getSpecifier());
        assertEquals("PP_S3L", descList.get(6).getSpecifier());
       
        assertEquals(2, descList.get(0).getTimeFrames().length);
        assertEquals("Overlay", descList.get(0).getTimeFrames()[0]);
        assertEquals("Base", descList.get(0).getTimeFrames()[1]);
    }

}