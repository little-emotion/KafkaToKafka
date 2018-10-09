package ty.error.recoder;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Properties;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import com.esotericsoftware.kryo.Kryo;

import edu.thss.monitor.rsc.service.push.PushResult;

public class Launcher {

	public static volatile Boolean notEnd = true;
	public static void main(String[] args) throws IOException {
		// 默认配置位置
		String path = "error.props";

		new Kryo().register(PushResult.class);
		// 读取配置文件
		if (args.length != 0)
			path = args[0];
		Properties props = new Properties();
		FileInputStream in = new FileInputStream(path);
		props.load(in);
		in.close();

		String mataDataPath = props.getProperty("metric.dir");
		Importer importer = new Importer(mataDataPath);
		importer.register();

		final ArrayList<Consumer> consumers = new ArrayList<Consumer>();
		ExecutorService executorService = Executors.newFixedThreadPool(50);
		for(int i = 0;i<50;i++){
			Consumer consumer = new Consumer(props);
			consumers.add(consumer);
			executorService.submit(consumer);
		}

		new Timer("timer - statistic").schedule(new TimerTask() {
			@Override
			public void run() {
				long totalPackage = 0;
				long totalPoint = 0;
				long totalTime = 0;
				long totalError = 0;
				for(int i= 0;i<50;i++){
					long tmpPointCount = consumers.get(i).getPonitCount();
					long tmpPackageCount = consumers.get(i).getPackageCount();
					long tmpErrorCount = consumers.get(i).getErrorCount();
					totalPoint+=tmpPointCount;
					totalPackage+=tmpPackageCount;
					totalError+=tmpErrorCount;
					totalTime = totalTime>consumers.get(i).getTotalTime()?totalTime:consumers.get(i).getTotalTime();
				}
				System.out.println("Total: total package = "+totalPackage+", total point = "+totalPoint+", total error = "+totalError+", total time = "+ totalTime);

			}
		}, 1000*60, 1000*60*(1));

		new Timer("timeer - stop").schedule(new TimerTask() {
			@Override
			public void run() {
				notEnd = false;
				System.out.println("end！！！");

			}
		}, 1000*60*50);
	}
}
