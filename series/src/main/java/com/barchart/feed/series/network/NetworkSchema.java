package com.barchart.feed.series.network;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.barchart.feed.api.series.network.Analytic;
import com.barchart.feed.api.series.network.NetworkDescriptor;
import com.barchart.feed.api.series.network.NodeDescriptor;
import com.barchart.feed.api.series.network.NodeType;

public class NetworkSchema implements NetworkDescriptor {
    /** the default filename of the file storing the local schema definitions */
    private static final String DEFAULT_SCHEMA_FILENAME = "networks.txt";
    
//    private static final String IDX_NETWORK_NAME = 0;
//    private static final String IDX_NODE_NAME = 0;
//    private static final String IDX_NETWORK_NAME = 0;
//    private static final String IDX_NETWORK_NAME = 0;
//    private static final String IDX_NETWORK_NAME = 0;
    
    /** Class-Wide storage of all unreified networks */
    private static Map<String,List<String[]>> networkDefinitions = new HashMap<String, List<String[]>>();
    /** Flat map of all descriptors */
    private static Map<String, AnalyticNodeDescriptor> allDescriptors = new LinkedHashMap<String, AnalyticNodeDescriptor>();
    /** Mapping of descriptors to their network names */
    private static Map<String, List<AnalyticNodeDescriptor>> descriptorsByNetwork = new LinkedHashMap<String, List<AnalyticNodeDescriptor>>();
    
    /** Holds the current filename source of the schema definitions */
    private static String schemaFileName = DEFAULT_SCHEMA_FILENAME;
    
    private String name;
    private List<AnalyticNodeDescriptor> nodes;
    
    private List<AnalyticNodeDescriptor> mainPublishers;
    
    
    public NetworkSchema(String name) {
        this(name, null);
    }
    
    public NetworkSchema(String name, List<AnalyticNodeDescriptor> nodes) {
        this.name = name;
        this.nodes = nodes;
    }
    
    @Override
    public String getSpecifier() {
        return name;
    }

    @Override
    public NodeType getType() {
        return NodeType.NETWORK;
    }
    
    @Override
    public String getNetworkName() {
        return name;
    }
    
    @Override
    public void setNetworkName(String name) {
        this.name = name;
    }
    
    @SuppressWarnings("unchecked")
    @Override
    public List<AnalyticNodeDescriptor> getNodeDescriptors() {
        return nodes;
    }
    
    @SuppressWarnings("unchecked")
    @Override
    public <T extends NodeDescriptor> void setNodeDescriptors(List<T> nodes) {
        this.nodes = (List<AnalyticNodeDescriptor>)nodes;
    }
    
    /**
     * Returns a list of {@link AnalyticNodeDescriptor}s which are not themselves
     * listed as input for subsequent nodes *<em>WITHIN THEIR NETWORK</em>*, 
     * (i.e. they are at the bottom of their respective network), and therefore 
     * represent the major nodes one would subscribe to in order to fully 
     * instantiate this entire network.
     * 
     * @return	a list of nodes required to instantiate the whole network if subscribed to.
     * @throws	IllegalStateException	if this network has no configured or loaded nodes.
     */
    @SuppressWarnings("unchecked")
    @Override
    public List<AnalyticNodeDescriptor> getMainPublishers() {
    	if(nodes == null || nodes.size() < 1) {
    		throw new IllegalStateException("Network: " + name + " was initialized with no nodes!");
    	}
    	
    	if(mainPublishers == null) {
    		mainPublishers = new ArrayList<AnalyticNodeDescriptor>();
    	}
    	
    	int len = nodes.size();
    	for(int i = 0;i < len;i++) {
    		AnalyticNodeDescriptor currCheck = nodes.get(i);
    		boolean isPublisher = true;
    		for(int j = 0;j < len;j++) {
    			AnalyticNodeDescriptor iter = nodes.get(j);
    			if(!(isPublisher = !iter.getInputNodeDescriptors().contains(currCheck))) {
    				break;
    			}
    		}
    		if(isPublisher) mainPublishers.add(currCheck);
    	}
    	return mainPublishers;
    }
    
    /**
     * Returns a flag indicating whether a configured network
     * by the name specified exists.
     * 
     * @param networkName		the name of the network to check
     * @return	true if so, false if not.
     */
    public static boolean hasNetworkByName(String networkName) {
        if(allDescriptors.size() < 1 || descriptorsByNetwork.size() < 1) {
            reloadDefinitions();
        }
        return descriptorsByNetwork.containsKey(networkName);
    }
    
    public static String getSchemaFilePath() {
        return schemaFileName;
    }
    
    public static void setSchemaFilePath(String filePath) {
    	System.out.println("schemaFilePath being set to: " + filePath);
        schemaFileName = filePath;
    }
    
    /**
     * Returns the {@link AnalyticNodeDescriptor} which is the schema for the
     * {@link Node} which will eventually produce the data described by the
     * specified {@link SeriesSubscription}.
     * 
     * @param name
     * @param timeFrames
     * @return
     */
    public static AnalyticNodeDescriptor lookup(String name, int timeFrames) {
        if (name.equals("IO")) return null;
        String key = timeFrames > 1 ? name + "-" + timeFrames : name;
        return allDescriptors.get(key);
    }
    
    /**
     * Returns the {@link AnalyticNodeDescriptor} which is the schema for the
     * {@link Node} which will eventually produce the data described by the
     * specified {@link SeriesSubscription}.
     * 
     * @param subscription      the output description of the Node who's descriptor 
     *                          will be returned.
     * @return                  the specified {@link AnalyticNodeDescriptor}.
     */
    public static AnalyticNodeDescriptor lookup(SeriesSubscription subscription) {
        return lookup(subscription.getAnalyticSpecifier(), subscription.getTimeFrames().length);
    }
    
    /**
     * Reloads mappings from the configured file locations.
     */
    public static void reloadDefinitions() {
        networkDefinitions.clear();
        allDescriptors.clear();
        descriptorsByNetwork.clear();
        loadDefinitionsFile(schemaFileName);
        loadAllDescriptors();
   }
    
    /**
     * Returns a {@code NetworkSchema} instance representing the network
     * specified by name.
     * @param name      the name of the network to return
     */
    public static NetworkSchema getNetwork(String name) {
        if(allDescriptors.size() < 1 || descriptorsByNetwork.size() < 1) {
            reloadDefinitions();
        }
        
        List<AnalyticNodeDescriptor> l = null;
        if((l = descriptorsByNetwork.get(name)) == null) {
            System.out.println("Warning: unable to locate network by the name: " + name);
            return null;
        }
        
        return new NetworkSchema(name, l);
    }
    
    @SuppressWarnings("unchecked")
    public static AnalyticNodeDescriptor loadDescriptor(String[] entry) {
        String[] argParts = entry[1].split(",");
        AnalyticNodeDescriptor nodeDescriptor = new AnalyticNodeDescriptor(entry[0], argParts[0]);
        if(argParts.length > 1) {
            nodeDescriptor.setTimeFrames(new String[argParts.length - 1]);
            String[] timeFrames = nodeDescriptor.getTimeFrames();
            for(int i = 1;i < argParts.length;i++) {
                timeFrames[i - 1] = argParts[i];
            }
        }
        try {
            nodeDescriptor.setAnalyticClass((Class<? extends Analytic>)Class.forName(entry[2]));
        } catch(ClassNotFoundException e) { 
            e.printStackTrace(); 
        }
        if(entry[3].length() > 0) {
            argParts = entry[3].split(",");
            int[] constructorArgs = new int[argParts.length];
            for(int i = 0;i < argParts.length;i++) {
                constructorArgs[i] = Integer.parseInt(argParts[i]);
            }
            nodeDescriptor.setConstructorArgs(constructorArgs);
        }
        nodeDescriptor.setOutputKey(entry[4]);
        for (int i = 5;i < entry.length;i++) {
            argParts = entry[i].split(",");
            nodeDescriptor.mapInputDescriptor(argParts[0], NetworkSchema.lookup(argParts[1], argParts.length - 2));
            if (argParts.length > 2) {
                String[] argTimeframes = new String[argParts.length - 2];
                for (int j = 2;j < argParts.length;j++) {
                    argTimeframes[j - 2] = argParts[j];
                }
                nodeDescriptor.mapTimeFrame(argParts[0], argTimeframes);
            }
        }
        
        return nodeDescriptor;
    }
    
    public static void loadAllDescriptors() {
        for(String networkName : networkDefinitions.keySet()) {
            for(String[] entry : networkDefinitions.get(networkName)) {
                List<AnalyticNodeDescriptor> networkDescriptors = null;
                if((networkDescriptors = descriptorsByNetwork.get(entry[0])) == null) {
                    descriptorsByNetwork.put(entry[0], networkDescriptors = new ArrayList<AnalyticNodeDescriptor>());
                }
                String[] argParts = entry[1].split(",");
                AnalyticNodeDescriptor nodeDescriptor = loadDescriptor(entry);
                networkDescriptors.add(nodeDescriptor);
                String key = nodeDescriptor.getSpecifier();
                if (argParts.length > 2) {
                    key += "-"+(argParts.length - 1);
                }
                allDescriptors.put(key, nodeDescriptor);
            }
        }
    }
    
    /**
     * Fills the raw network definitions map from the source specified
     * by the fileName passed in.
     * 
     * @param fileName
     */
    public static void loadDefinitionsFile(String fileName) {
        if(fileName == null || fileName.length() < 1) {
            throw new IllegalArgumentException("Cannot load null or empty file name.");
        }
        InputStream stream = NetworkSchema.class.getClassLoader().getResourceAsStream(fileName);
        loadFromStream(stream);
    }
    
    /**
     * Fills the raw network definitions map from the source specified
     * by the passed in stream.
     * 
     * @param stream    the stream provided by the source connection.
     */
    public static void loadFromStream(InputStream stream) {
        BufferedReader buf = null;
        try {
            buf = new BufferedReader(new InputStreamReader(stream));
            String line = null;
            String lastNetworkName = null;
            while((line = buf.readLine()) != null) {
                String[] lineParts = line.split(";");
                String[] argParts = new String[lineParts.length - 1];
                for(int i = 1;i < lineParts.length;i++) {
                    argParts[i - 1] = lineParts[i];
                }
                if(!lineParts[0].equals(lastNetworkName)) {
                    networkDefinitions.put(lineParts[0], new ArrayList<String[]>());
                    lastNetworkName = lineParts[0];
                }
                networkDefinitions.get(lastNetworkName).add(lineParts);
            }
        }catch(Exception e) {
            e.printStackTrace();
        }finally{
            try { buf.close(); }catch(Exception ignore) {}
        }
    }
}
