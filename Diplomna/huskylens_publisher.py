import sys
import paho.mqtt.client as mqtt
import time
import json
from huskylib import HuskyLensLibrary

clientName = "Huskylens"
serverAddress = "raspberrypi"  # 192.168.1.12
mqttClient = mqtt.Client(clientName)
topic = "movement/auto"

def connect(client, userdata, flags, rc):
    print("Huskylens connected!")

def on_publish(client, userdata, result):             
    print("Huskylens published \n")

motorSpeed = 80
leftOffset = 125
rightOffset = 185
topOffset = 80
bottomOffset = 160
optWidthLow = 50
optWidthHigh = 80
prev_target = None
counter = 0

hl = HuskyLensLibrary("SERIAL","/dev/ttyUSB0", 115200)
sec = 0
while hl.knock() != "Knock Recieved":
    if sec < 10:
        print("Waiting for connection...")
    else:
        print("Connection error")
        sys.exit()
        
    sec = sec + 1

hl.algorthim("ALGORITHM_OBJECT_TRACKING")

mqttClient.on_connect = connect
mqttClient.on_publish = on_publish 
mqttClient.will_set(topic, "disconnected", qos = 1, retain=False)                        
mqttClient.connect(serverAddress)  


while hl.knock() == "Knock Recieved":
    
    if hl.learnedBlocks() != None:
        target = hl.getObjectByID(1)
        if(target == None):
            continue
        if counter == 0:
           prev_target = target

        if target.width < optWidthLow:
            diff = optWidthLow - target.width
            mqttClient.publish(topic, "forward,{sec},{speed}".format(sec = diff/25, speed = motorSpeed))
            print("Huskylens published: forward,{sec},{speed}".format(sec = diff/25, speed = motorSpeed))
            
        elif target.width > optWidthHigh:
            diff = target.width - optWidthHigh
            mqttClient.publish(topic, "backward,{sec},{speed}".format(sec = diff/25, speed = motorSpeed))
            print("Huskylens published: backward,{sec},{speed}".format(sec = diff/25, speed = motorSpeed))

        if target.x < leftOffset:
            diff = leftOffset - target.x
            mqttClient.publish(topic, "left,{sec},{speed}".format(sec = diff/20, speed = 100))
            print("Huskylens published: left,{sec},{speed}".format(sec = diff/20, speed = 100))
            
        elif target.x > rightOffset:
            diff = target.x - rightOffset
            mqttClient.publish(topic, "right,{sec},{speed}".format(sec = diff/20, speed = 100))
            print("Huskylens published: right,{sec},{speed}".format(sec = diff/20, speed = 100))
        
        if target.y < topOffset:
            if target.width < prev_target.width:
               diff = topOffset - target.y
               mqttClient.publish(topic, "forward,{sec},{speed}".format(sec = diff/25, speed = motorSpeed))
               print("Huskylens published: forward,{sec},{speed}".format(sec = diff/25, speed = motorSpeed))
           
            elif target.width > prev_target.width:
               diff = topOffset - target.y
               mqttClient.publish(topic, "backward,{sec},{speed}".format(sec = diff/25, speed = motorSpeed))
               print("Huskylens published: backward,{sec},{speed}".format(sec = diff/25, speed = motorSpeed))
        
        elif target.y > bottomOffset:
            diff = target.y - bottomOffset
            mqttClient.publish(topic, "backward,{sec},{speed}".format(sec = diff/25, speed = motorSpeed))
            print("Huskylens published: backward,{sec},{speed}".format(sec = diff/25, speed = motorSpeed))
            
        prev_target = target
        counter = counter + 1

    '''else:
        print("No learned objects are visible")'''
