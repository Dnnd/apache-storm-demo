package ru.mipt.datapipeline.multilang.simple.python;

import java.util.HashMap;
import java.util.Map;

import org.apache.storm.task.ShellBolt;
import org.apache.storm.topology.IRichBolt;
import org.apache.storm.topology.OutputFieldsDeclarer;
import org.apache.storm.tuple.Fields;

public class SimplePythonBolt extends ShellBolt implements IRichBolt {
	private static final String DEFAULT_BOLT_PATH = "bolts/simple.py";

	public SimplePythonBolt(String python, String bolt) {
		super(python, bolt);
	}

	public SimplePythonBolt(String python) {
		super(python, DEFAULT_BOLT_PATH);
	}

	@Override
	public void declareOutputFields(OutputFieldsDeclarer declarer) {
		declarer.declare(new Fields("tag", "timestamp"));

	}

	@Override
	public Map<String, Object> getComponentConfiguration() {
		return new HashMap<>();
	}

}
