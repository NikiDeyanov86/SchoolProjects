import paho.mqtt.client as mqtt
# Import the motors library

clientName = "Movement"
serverAddress = "raspberrypi"
mqttClient = mqtt.Client(clientName)

def connect(client, userdata, flags, rc):
    print("subscribing")
    mqttClient.subscribe("movement/+")
    print("subscribed")
    
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
            # motor.move_forward_hl(seconds, speed)
            # motor.stop()
            pass
        elif direction == "backward":
            # motor.move_backwards_hl(seconds, speed)
            # motor.stop()
            pass
        elif direction == "left":
            # motor.turn_left_hl(seconds, speed)
            # motor.stop()
            pass
        elif direction == "right":
            # motor.turn_right_hl(seconds, speed)
            # motor.stop()
            pass
    
    elif topic == "movement/manual":
        # Data from remote client, ex. "left" ---- time ----> "stop"
        direction = message
        if direction == "forward":
            # motor.move_forward(speed)
            pass
        elif direction == "backward":
            # motor.move_backwards(speed)
            pass
        elif direction == "left":
            # motor.turn_left(speed)
            pass
        elif direction == "right":
            # motor.turn_right(speed)
            pass
        elif direction == "stop":
            # motor.stop()
            pass
        
mqttClient.on_connect = connect
mqttClient.on_message = messageDecoder

# Connect to the MQTT server & loop forever.
# CTRL-C will stop the program from running.
mqttClient.connect(serverAddress)
mqttClient.loop_forever()