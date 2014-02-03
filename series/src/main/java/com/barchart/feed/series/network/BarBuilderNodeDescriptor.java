package com.barchart.feed.series.network;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.barchart.feed.api.series.Period;
import com.barchart.feed.api.series.PeriodType;
import com.barchart.feed.api.series.network.Analytic;
import com.barchart.feed.api.series.network.BarBuilderDescriptor;
import com.barchart.feed.api.series.network.NodeType;
import com.barchart.feed.api.series.network.Subscription;
import com.barchart.feed.series.TimeFrameImpl;

public class BarBuilderNodeDescriptor implements BarBuilderDescriptor {
    private static final String BASE_STEP_FILE = "/baseSteps.txt";
    
    private static List<PeriodType> baseTypeSteps;
    static {
    	loadFromFile(BASE_STEP_FILE);
    }
    
    /** The class of type {@link Analytic} to instantiate */
    private Class<? extends Analytic> analyticsClass;
    /** The constructor argument */
    private Subscription constructorArg;
    /** Constructor ags for conventional {@link Analytic} style instantiation. */
    private int[] constructorArgs;
    
    /** The output key */
    private String outputKey;
    
    
    /**
     * Constructs a new {@code BarBuilderNodeDescriptor}
     */
    public BarBuilderNodeDescriptor() {}
    
    /**
     * Returns the network name of this descriptor.
     * @return  the network name of this descriptor.
     */
    public String getNetworkName() {
        throw new UnsupportedOperationException(
            "Get/Set network name not supported by BarBuilderNodeDescriptor");
    }
    
    /**
     * Sets the network name of this descriptor.
     * @param  the network name of this descriptor.
     */
    public void setNetworkName(String name) {
        throw new UnsupportedOperationException(
            "Get/Set network name not supported by BarBuilderNodeDescriptor");
    }
    
    private static void loadFromFile(String path) {
        loadFromStream(BarBuilderNodeDescriptor.class.getResourceAsStream(path));
    }
    
    private static void loadFromStream(InputStream stream) {
        BufferedReader buff = null;
        try {
            buff = new BufferedReader(new InputStreamReader(stream));
            String line = null;
            if(baseTypeSteps == null) {
                baseTypeSteps = Collections.synchronizedList(new ArrayList<PeriodType>());
            }else{
                baseTypeSteps.clear();
            }
            
            while((line = buff.readLine()) != null) {
                baseTypeSteps.add(PeriodType.forString(line.trim()));
            }
           
        }catch(Exception e) {e.printStackTrace();}
    }
    
    public void reloadFile() {
        loadFromFile(BASE_STEP_FILE);
    }
    
    public void reloadStream(InputStream stream) {
        loadFromStream(stream);
    }
    
    /**
     * Returns the type which is a base type as configured by the application
     * which also has a {@link PeriodType} lower than the type specified or the
     * lowest PeriodType configured as a base type.
     * 
     * @param type
     * @return
     */
    static PeriodType getLowerBaseType(PeriodType type) {
        PeriodType retType = null;
        int idx = 0;
        while(idx < baseTypeSteps.size() && (retType == null || baseTypeSteps.get(idx).isLowerThan(type))) {
            retType = baseTypeSteps.get(idx++);
        }
        return retType;
    }
    
    /**
     * Returns a {@link Subscription} containing a  {@link TimeFrameImpl} whose {@link Period}
     * is of a lower type <em>AND</em> is pre-configured to be an acceptable bar building
     * {@link PeriodType} suitable for bar building.
     * 
     * @param input     the Subscription whose Period should be lowered
     * @return          a new {@link SeriesSubscription} containing the pre-approved 
     *                  lowered {@link Period} type.
     */
    public static SeriesSubscription getLowerSubscription(SeriesSubscription input) {
        Period next = null;
        if(input.getTimeFrames()[0].getPeriod().size() > 1) {
            next = new Period(input.getTimeFrames()[0].getPeriod().getPeriodType(), 1);
        }else{
            next = new Period(getLowerBaseType(input.getTimeFrames()[0].getPeriod().getPeriodType()), 1);
        }
        
        SeriesSubscription sSub = new SeriesSubscription(input);
        sSub.setTimeFrames(new TimeFrameImpl[] {
            new TimeFrameImpl(next, input.getTimeFrames()[0].getStartDate(), input.getTimeFrames()[0].getEndDate()) });
        return sSub;
    }
    
    public String toString() {
        return "BarBuilderNodeDescriptor";
    }

    @Override
    public Class<? extends Analytic> getAnalyticClass() {
        return analyticsClass;
    }

    @Override
    public void setAnalyticClass(Class<? extends Analytic> clazz) {
        this.analyticsClass = clazz;
    }

    @Override
    public int[] getConstructorArgs() {
        return constructorArgs;
    }

    /**
     * Intentionally not implemented.
     */
    @Override
    public void setConstructorArgs(int[] args) {
        this.constructorArgs = args;
    }
    
    /**
     * Returns the output key used by the underlying {@link Analytic}
     * @return  the output key
     */
    @Override
    public String getOutputKey() {
        return outputKey;
    }
    
    /**
     * Sets the output key used by the underlying {@link Analytic}
     * @param  key     the output key
     */
    @Override
    public void setOutputKey(String key) {
        this.outputKey = key;
    }

    
    @Override
    public Analytic instantiateAnalytic() {
        Constructor<?>[] constructorArray = analyticsClass.getConstructors();
        for (Constructor<?> constructor : constructorArray) {
            Class<?>[] parameterTypes = constructor.getParameterTypes();
            if(parameterTypes.length == constructorArgs.length) {
                Object[] parameters = new Object[parameterTypes.length];
                for (int i = 0;i < parameterTypes.length;i++) {
                    parameters[i] = new Integer(this.constructorArgs[i]);
                }
                try {
                    return (Analytic)constructor.newInstance(parameters);
                }catch(Exception e) { }
            }
        }
        throw new IllegalStateException("Unable to instantiate Analytic class using constructor containing " + 
            (constructorArgs == null ? 0:constructorArgs.length) + " arguments");
    }
    
    /**
     * Instantiates the sub type of the {@link Analytic} class specified.
     * 
     * @param s     a derivative of {@link Subscription}
     * @return      the instantiated Analytic
     */
    @Override
    public Analytic instantiateBuilderAnalytic() {
        Constructor<?>[] constructorArray = analyticsClass.getDeclaredConstructors();
        for (Constructor<?> constructor : constructorArray) {
            constructor.setAccessible(true);
            Class<?>[] parameterTypes = constructor.getParameterTypes();
            if(parameterTypes.length == 1 && (getConstructorArg().getClass().equals(parameterTypes[0]))) {
                try {
                    return (Analytic)constructor.newInstance(new Object[] { getConstructorArg() });
                }catch(Exception e) { e.printStackTrace(); }
            }
        }
        throw new IllegalStateException("Unable to instantiate Analytic class using constructor containing " + 
             "1 arguments of type <S extends Subscription> using argument: [s=" + getConstructorArg().getClass().getName() + "]");
    }

    @Override
    public String getSpecifier() {
        return NodeType.IO.toString();
    }

    @Override
    public NodeType getType() {
        return NodeType.IO;
    }

    @SuppressWarnings("unchecked")
    @Override
    public <T extends Subscription> T getConstructorArg() {
        return (T)constructorArg;
    }

    @Override
    public <T extends Subscription> void setConstructorArg(T arg) {
        this.constructorArg = arg;
    }

}
