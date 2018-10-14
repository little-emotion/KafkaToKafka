#!/bin/bash

writetopiclist=('Test10_PP_KTP_CTY_Source_Pc50' 'Test10_PP_KTP_CTY_Source_Pc80' 'Test10_PP_KTP_CTY_Source_Pc100')
jar_dir='/root/kmx_group_test/copydata/'

# start change write topic name
for (( i = 0; i <= 2; i++ ));
do
    topic=${writetopiclist[$i]}
    echo "!start write topic $topic!"

    for (( j = 0; j <= 3; j++ ));
    do
        echo "start write topic $topic, num $j"
        java -jar importdata.jar error.props ${alliplist[0]} $topic $j
        sleep 1700s;
    done
done
echo "import end!"

