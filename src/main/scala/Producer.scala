package biz.appdata.kafkaproducertest

import org.apache.kafka.clients.producer.ProducerRecord
import org.apache.kafka.clients.producer.KafkaProducer
import kafka.producer.ProducerConfig
import java.util.Properties

object KafkaProducerTest {

  def main(args: Array[String]) = {
    val props = new Properties()

    props.put("producer.type", "async")
    props.put("client.id", "kafkatest")
    props.put("bootstrap.servers", "broker:9092")
    props.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer")
    props.put("value.serializer", "org.apache.kafka.common.serialization.StringSerializer")

    val producer = new KafkaProducer[AnyRef, AnyRef](props)

    producer.send(new ProducerRecord("hej", "tjo", "hoj"))

    System.exit(1)
  }
}
