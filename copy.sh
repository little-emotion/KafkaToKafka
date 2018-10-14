#!/bin/bash

alliplist=('192.168.35.21' '192.168.35.22' '192.168.35.23' '192.168.35.24' '192.168.35.25' '192.168.35.26' '192.168.35.27' '192.168.35.28' '192.168.35.29' '192.168.35.30' '192.168.35.31' '192.168.35.32')
datadirlist=('/data3/kmxtest/data' '/data4/kmxtest/data' '/data5/kmxtest/data' '/data6/kmxtest/data')
cassandra_extra_data='/home/cassandra/apache-cassandra-2.1.16/data'
jardir='/home/cassandra/kafka_storm_cassandra_test'
storm_url='192.168.35.41'
storm_dir='/root/apache-storm-0.9.6/bin'
storm_test_dir='/root/apache-storm-0.9.6/lta/jar'

# start loop and increase number of nodes
for (( i = 1; i <= 4; i++ ));
do
        # init ip list
        num=$((${i}*3))
        echo "There have $num cassandra servers for the test!"

        # stop all cassandra nodes
        for (( j = 0; j < 12; j++ ));
        do
                # stop cassandra
                ssh cassandra@${alliplist[$j]} "ps -ef | grep '/home/cassandra' | grep -v grep|awk '{print \$2}'|xargs kill -9"
        done
        # loop num nodes
        for (( j = 0; j < $num; j++ ));
        do
                # clean all data dir
                for datadir in ${datadirlist[@]};
                do
                        ssh cassandra@${alliplist[$j]} "rm -rf ${datadir}/*"
                        ssh cassandra@${alliplist[$j]} "rm -rf ${cassandra_extra_data}/*"
                done

                # start cassandra
                ssh cassandra@${alliplist[$j]} "/home/cassandra/apache-cassandra-2.1.16/bin/cassandra"
        done

        # wait awhile
        sleep 180s

        # register metadata"
        echo "It's time to init kmx!"
        ssh cassandra@${alliplist[0]} "cd ${jardir};java -jar kmx-init.jar ${alliplist[0]} 9042 SimpleStrategy 2"
        echo "It's time to import kmx metadata!"
        ssh cassandra@${alliplist[0]} "cd ${jardir};nohup java -cp importmetadata.jar Importer ${alliplist[0]} >excute_metadata.output 2>&1 &"
        sleep 120s
        ssh cassandra@${alliplist[0]} "ps aux | grep 'importmetadata.jar' | grep -v grep|awk '{print \$2}'|xargs kill -9"
        # start write for 24h
        for (( j = 1; j <= 22; j++ ));
        do
                #start write
                echo "It's time to excute storm!"
                ssh root@${storm_url} "cd /root/apache-storm-0.9.6/bin;storm jar ${storm_test_dir}/kafka_storm_kmx.jar run.Launcher kmx_test ${num} 2${j}"
#cd /root/apache-storm-0.9.6/bin;storm jar /root/apache-storm-0.9.6/lta/jar/kafka_storm_kmx.jar run.Launcher kmx_test 1 1
#ssh root@192.168.35.41 "/root/apache-storm-0.9.6/bin/storm jar /root/apache-storm-0.9.6/lta/jar/kafka_storm_kmx.jar run.Launcher kmx_test 3 1"

                # monitor statistics and make records for 180s
                sleep 3600s

                # kill it
                echo "It's time to kill storm!"
                ssh root@${storm_url} "storm kill kmx_test"
                sleep 35s
        done

        # stop all cassandra nodes
        for (( j = 0; j < $num; j++ ));
        do
                # stop cassandra
                ssh cassandra@${alliplist[$j]} "ps -ef | grep '/home/cassandra' | grep -v grep|awk '{print \$2}'|xargs kill -9"
        done
done


