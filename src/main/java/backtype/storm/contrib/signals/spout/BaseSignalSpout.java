// Copyright (c) P. Taylor Goetz (ptgoetz@gmail.com)

package backtype.storm.contrib.signals.spout;

import backtype.storm.contrib.signals.SignalListener;
import backtype.storm.contrib.signals.StormSignalConnection;
import org.apache.storm.spout.SpoutOutputCollector;
import org.apache.storm.task.TopologyContext;
import org.apache.storm.topology.base.BaseRichSpout;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;

@SuppressWarnings("serial")
public abstract class BaseSignalSpout extends BaseRichSpout implements SignalListener {

    private static final Logger LOG = LoggerFactory.getLogger(BaseSignalSpout.class);
    private String name;
    private StormSignalConnection signalConnection;

    public BaseSignalSpout(String name) {
        this.name = name;
    }

    @SuppressWarnings("rawtypes")
    @Override
    public void open(Map conf, TopologyContext context, SpoutOutputCollector collector) {
        try {
            this.signalConnection = new StormSignalConnection(this.name, this);
            this.signalConnection.init(conf);
        } catch (Exception e) {
            LOG.error("Error creating SignalConnection.", e);
        }
    }

    public void sendSignal(String toPath, byte[] signal) throws Exception {
        this.signalConnection.send(toPath, signal);
    }

    @Override
    public void close() {
        super.close();
        this.signalConnection.close();
    }

}
