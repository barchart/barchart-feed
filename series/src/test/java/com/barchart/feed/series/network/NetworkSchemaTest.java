package com.barchart.feed.series.network;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.List;

import org.junit.Test;

import com.barchart.feed.api.series.network.NetworkDescriptor;

public class NetworkSchemaTest {

    @Test
    public void testGetNetwork() {
    	NetworkSchema.setSchemaFilePath("networks.txt");
        NetworkDescriptor schema = NetworkSchema.getNetwork("PivotPointOverlay");
        assertNotNull(schema);
        
        assertEquals("PivotPointOverlay", schema.getNetworkName());
        
        List<AnalyticNodeDescriptor> descList = schema.getNodeDescriptors();
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
    
    @Test
    public void testGetPublishers() {
    	NetworkSchema.setSchemaFilePath("testNetworks.txt");
    	NetworkSchema.reloadDefinitions();
    	
    	NetworkDescriptor schema = NetworkSchema.getNetwork("PivotPoint");
    	assertNotNull(schema);
    	
    	assertEquals("PivotPoint", schema.getNetworkName());
    	assertEquals(8, schema.getNodeDescriptors().size());
    	
    	List<AnalyticNodeDescriptor> publishers = schema.getPublishers();
    	assertTrue(!publishers.isEmpty());
    	assertEquals(7, publishers.size());
    	for(AnalyticNodeDescriptor and : publishers) {
    		assertTrue(!and.getSpecifier().equals("PP_R2"));
    	}
    }

}
