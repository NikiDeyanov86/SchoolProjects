import paho.mqtt.client as mqtt
from motorslib import MotorSide, MotorDriver, in1, in2, in3, in4, ena, enb

left = MotorSide(ena, in1, in2)
right = MotorSide(enb, in3, in4)
motors = MotorDriver(left, right)

clientName = "Movement"
serverAddress = "raspberrypi" # 192.168.1.12
mqttClient = mqtt.Client(clientName)

def connect(client, userdata, flags, rc):
    print("subscribing")
    mqttClient.subscribe("movement/+")
    print("subscribed to <movement/+>")
    
def messageDecoder(client, userdata, msg):
    message = msg.payload.decode(encoding='UTF-8')
    topic = msg.topic
    # Syncronize auto and manual
    
    if topic == "movement/auto":
        # Data from Huskylens, ex. "<direction>,<seconds>,<speed>"
        split = message.split(",")
        direction = split[0]
        seconds = float(split[1])
        speed = int(split[2])
        
        if direction == "forward":
            motors.move_forward_hl(seconds, speed)
            motors.stop()
            
        elif direction == "backward":
            motors.move_backward_hl(seconds, speed)
            motors.stop()
            
        elif direction == "left":
            motors.turn_left_hl(seconds, speed)
            motors.stop()
            
        elif direction == "right":
            motors.turn_right_hl(seconds, speed)
            motors.stop()
            
        elif direction == "disconnected":
            # huskylens is down
            # tell user to switch to manual mode
            pass
    
    elif topic == "movement/manual":
        # Data from remote client, ex. "left" ---- time ----> "stop"
        direction = message
        if direction == "forward":
            # motors.forward(speed)
            pass
        elif direction == "backward":
            # motors.backward(speed)
            pass
        elif direction == "left":
            # motors.left(speed)
            pass
        elif direction == "right":
            # motors.right(speed)
            pass
        elif direction == "stop":
            # motors.stop()
            pass
        
mqttClient.on_connect = connect
mqttClient.on_message = messageDecoder

# Connect to the MQTT server & loop forever.
# CTRL-C will stop the program from running.
mqttClient.connect(serverAddress)
mqttClient.loop_forever()