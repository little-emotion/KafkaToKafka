package ty.error.recoder;

import java.util.*;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.clients.producer.Callback;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;
import ty.pub.TransPacket;

public class Consumer extends Thread{

	KafkaConsumer<String, byte[]> consumer;
	KafkaProducer<String, byte[]> producer;
	Properties props;
	String topic;
	TransPacket result;
	Long timeInterval;


	Long ponitCount;
	long packageCount;
	long totalTime;
	public Consumer(Properties p)
	{
		props=p;
		
		Properties produProps = new Properties();

		produProps.setProperty("bootstrap.servers", props.getProperty("store.broker"));
		produProps.setProperty("acks", props.getProperty("store.acks"));
		produProps.setProperty("retries", props.getProperty("store.retries"));
		produProps.setProperty("batch.size", props.getProperty("store.batch"));
		produProps.setProperty("linger.ms", props.getProperty("store.linger.ms"));
		produProps.setProperty("buffer.memory", props.getProperty("store.buffer"));
		produProps.setProperty("key.serializer", props.getProperty("store.keySerial"));
		produProps.setProperty("value.serializer", "org.apache.kafka.common.serialization.ByteArraySerializer");
		producer = new KafkaProducer<String, byte[]>(produProps);


		consumer = new KafkaConsumer<String, byte[]>(props);
		consumer.subscribe(Arrays.asList(props.getProperty("store.read.topic")));


		topic=props.getProperty("store.topic");
		timeInterval=Long.parseLong(props.getProperty("store.time"));

		ponitCount = 0l;
		packageCount =0;
		totalTime = 0l;

	}
	
	public void run()
	{
		long i = 0;
		long startTime = System.currentTimeMillis();

		while (Launcher.notEnd) {

			ConsumerRecords<String, byte[]> records = consumer.poll(1000);
			i++;

			System.out.println("poll :"+i);
			consumer.commitSync();

			for (ConsumerRecord<String, byte[]> record : records){
				packageCount++;
				//System.out.println(Thread.currentThread().getName()+" -"+i+" :"+record.toString()+" pavkage = " +packageCount);
				try{
					result = KryoUtil.deserialize(record.value(), TransPacket.class);
					ponitCount+=getPoint(result);
				}catch (Exception e){
					e.printStackTrace();
				}
				producer.send(new ProducerRecord<String, byte[]>(topic,record.value()), new Callback(){
					public void onCompletion(RecordMetadata arg0, Exception arg1) {
						// TODO Auto-generated method stub
						if(arg1 != null){
							producer.send(new ProducerRecord<String, byte[]>(topic, KryoUtil.serialize2byte(result)));
						}
						else{
//							System.out.println("success");
						}
					}

				});
					
			}//for
			totalTime = (System.currentTimeMillis()-startTime);
			System.out.println(Thread.currentThread().getName()+"--- package_count : "+ packageCount +" ; point_count :"+ponitCount+" total_time"+ (totalTime/1000)/60.0+"min");

		}
		long endTime = System.currentTimeMillis();
		System.out.println(Thread.currentThread().getName()+" end!!!,total packageCount = "+ packageCount +" ; total point = "+ponitCount);
		System.out.println("Time:"+(endTime-startTime)/1000+"s");

	}

	public int getPoint(TransPacket packet){
		int pointCount = 0;

		//----------------------存储数据-------------------------------
		//基础信息参数
		Iterator<?> iterator = packet.getBaseInfoMapIter();

		while (iterator.hasNext()) {
			iterator.next();
			pointCount++;
		}
		//工况参数
		iterator = packet.getWorkStatusMapIter();
		while (iterator.hasNext()) {
			iterator.next();
			pointCount++;
		}
		return pointCount;
	}

	public Long getPonitCount() {
		return ponitCount;
	}

	public long getPackageCount() {
		return packageCount;
	}

	public long getTotalTime() {
		return totalTime;
	}
}



