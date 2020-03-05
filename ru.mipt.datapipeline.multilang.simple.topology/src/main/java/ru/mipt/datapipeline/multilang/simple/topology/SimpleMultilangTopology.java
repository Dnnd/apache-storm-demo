package ru.mipt.datapipeline.multilang.simple.topology;

import java.util.Arrays;

import org.apache.storm.Config;
import org.apache.storm.StormSubmitter;
import org.apache.storm.generated.AlreadyAliveException;
import org.apache.storm.generated.AuthorizationException;
import org.apache.storm.generated.InvalidTopologyException;
import org.apache.storm.mqtt.bolt.MqttBolt;
import org.apache.storm.mqtt.common.MqttOptions;
import org.apache.storm.mqtt.mappers.StringMessageMapper;
import org.apache.storm.mqtt.spout.MqttSpout;
import org.apache.storm.topology.TopologyBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ru.mipt.datapipeline.multilang.simple.python.SimplePythonBolt;
import ru.mipt.datapipeline.multilang.simple.mqtt.SimpleTupleMapper;

public class SimpleMultilangTopology {
	private static Logger logger = LoggerFactory.getLogger(SimpleMultilangTopology.class);

	public static void main(String[] args) {
		TopologyBuilder builder = new TopologyBuilder();
		MqttOptions options = new MqttOptions();
		options.setTopics(Arrays.asList("test/topic/#"));
		options.setCleanConnection(false);
		options.setUrl("tcp://mqtt-broker:1883");
		MqttSpout spout = new MqttSpout(new StringMessageMapper(), options);
		builder.setSpout("mqtt-spout", spout);

		MqttOptions boltOptions = new MqttOptions();
		boltOptions.setTopics(Arrays.asList("output/topic"));
		boltOptions.setCleanConnection(false);
		boltOptions.setUrl("tcp://mqtt-broker:1883");
		MqttBolt bolt = new MqttBolt(boltOptions, new SimpleTupleMapper("output/topic"));

		SimplePythonBolt pb = new SimplePythonBolt("/apache-storm-2.1.0/storm-venv/venv/bin/python");

		builder.setBolt("python-bolt", pb).shuffleGrouping("mqtt-spout");

		builder.setBolt("mqtt-bolt", bolt).shuffleGrouping("python-bolt");
		Config config = new Config();
		config.setDebug(true);
		try {
			StormSubmitter.submitTopology("mqtt", config, builder.createTopology());
		} catch (AlreadyAliveException | InvalidTopologyException | AuthorizationException e) {
			logger.error("storm submit error", e);
		} catch (Exception e) {
			logger.error("storm submit error exception", e);
		}

	}

}
