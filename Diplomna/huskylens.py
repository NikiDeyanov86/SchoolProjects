import sys
sys.path.append(".")
import time
import json
from huskylib import HuskyLensLibrary
from test_motors import MotorSide, MotorDriver

in1 = 22
in2 = 23
in3 = 24
in4 = 25
ena = 12
enb = 13

#hl = HuskyLensLibrary("I2C","", address=0x32, channel=1)
hl = HuskyLensLibrary("SERIAL","/dev/ttyUSB0", 115200)

left = MotorSide(ena, in1, in2)
right = MotorSide(enb, in3, in4)
driver = MotorDriver(left, right)

motorSpeed = 80
leftOffset = 125
rightOffset = 185
topOffset = 80
bottomOffset = 160
optWidthLow = 50
optWidthHigh = 80


sec = 0
while hl.knock() != "Knock Recieved":
    if sec < 10:
        print("Waiting for connection...")
    else:
        print("Connection error")
        sys.exit()
        
    sec = sec + 1

hl.algorthim("ALGORITHM_OBJECT_TRACKING")
#learned = hl.learnedBlocks()

prev_target = None
counter = 0

while hl.knock() == "Knock Recieved":
    
    if hl.learnedBlocks() != None:
        target = hl.getObjectByID(1)
        if(target == None):
            continue
        if counter == 0:
           prev_target = target
       
        print("<--------------------------------------------------------------------------------------------------->")
        print("TARGET -> ", "x = ", target.x, ", y = ",target.y ,", w(h) = ", target.width)
        print("PREVIOUS TARGET -> ", "x = ", prev_target.x, ", y = ", prev_target.y ,", w(h) = ", prev_target.width)

        #print("x = ", target.x, ", y = ",target.y ,", w(h) = ", target.width)
        if target.width < optWidthLow:
            diff = optWidthLow - target.width
            print("DIFF FORWARD WIDTH -> ", diff)
            driver.move_forward(diff/25, motorSpeed)
            print("forward: ", diff/25)
            driver.stop()
            
        elif target.width > optWidthHigh:
            diff = target.width - optWidthHigh
            print("DIFF BACKWARDS WIDTH -> ", diff)
            driver.move_backwards(diff/25, motorSpeed)
            print("backward: ", diff/25)
            driver.stop()

        if target.x < leftOffset:
            diff = leftOffset - target.x
            print("DIFF LEFT -> ", diff)
            driver.turn_left(diff/25, 100)
            print("left: ", diff/20)
            driver.stop()
            
        elif target.x > rightOffset:
            diff = target.x - rightOffset
            print("DIFF RIGHT-> ", diff)
            driver.turn_right(diff/20, 100)
            print("right: ", diff/20)
            driver.stop()
        
        if target.y < topOffset:
            if target.width < prev_target.width:
               diff = topOffset - target.y
               print("DIFF FORWARD TOP OFFSET-> ", diff)
               driver.move_forward(diff/25, motorSpeed)
               print("forward: ", diff/25)
               driver.stop()
           
            elif target.width > prev_target.width:
               diff = topOffset - target.y
               print("DIFF BACKWARDS TOP OFFSET -> ", diff)
               driver.move_backwards(diff/25, motorSpeed)
               print("backwards:", diff/25)
               driver.stop()
        
        elif target.y > bottomOffset:
            diff = target.y - bottomOffset
            print("DIFF BACKWARDS BOTTOM OFFSET-> ", diff)
            driver.move_backwards(diff/25, motorSpeed)
            print("backward: ", diff/25)
            driver.stop()
            
        # print("TARGET ->" + target)
        prev_target = target
        counter = counter + 1
	# print("PREVIOUS TARGET -> " + prev_target)

    else:
        driver.stop()
        print("No learned objects are visible")

driver.tear_down()
