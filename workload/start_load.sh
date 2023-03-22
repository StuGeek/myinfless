loadGen_ip=$1
loadGen_port=$2
# ssd
# java -jar LoadGenSimClient.jar 600 1 9 1800 10 0 true Periodic.txt ./results/ $1 $2 &
# mobilenet
java -jar LoadGenSimClient.jar 600 1 9 1800 16 0 true Periodic.txt ./results/ $1 $2 &
# resnet-50
# java -jar LoadGenSimClient.jar 600 1 9 1800 14 0 true Periodic.txt ./results/ $1 $2 &
