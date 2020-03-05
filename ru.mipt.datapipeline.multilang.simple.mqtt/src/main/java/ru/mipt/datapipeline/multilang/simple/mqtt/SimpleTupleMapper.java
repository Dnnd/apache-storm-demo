package ru.mipt.datapipeline.multilang.simple.mqtt;

import org.apache.storm.mqtt.MqttMessage;
import org.apache.storm.mqtt.MqttTupleMapper;
import org.apache.storm.tuple.ITuple;

public class SimpleTupleMapper implements MqttTupleMapper {

	private final String outputTopic;

	public SimpleTupleMapper(String outputTopic) {
		this.outputTopic = outputTopic;
	}

	@Override
	public MqttMessage toMessage(ITuple tuple) {
		return new MqttMessage(outputTopic, tuple.getString(0).getBytes());
	}

}
