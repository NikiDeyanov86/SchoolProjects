import RPi.GPIO as gpio
import time

'''in1 = 22
in2 = 23
in3 = 24
in4 = 25
ena = 12
enb = 13'''

gpio.setmode(gpio.BCM)
gpio.setwarnings(False)

class MotorSide():
    def __init__(self, en, in1, in2):
        self.en = en
        self.in1 = in1
        self.in2 = in2
        gpio.setup(en, gpio.OUT)
        gpio.setup(in1, gpio.OUT)
        gpio.setup(in2, gpio.OUT)
        self.pwm = gpio.PWM(en, 100)
        self.pwm.start(0)
    
    def forward(self, speed):
        self.pwm.ChangeDutyCycle(speed)
        gpio.output(self.in1, True)
        gpio.output(self.in2, False)
        
    def reverse(self, speed):
        self.pwm.ChangeDutyCycle(speed)
        gpio.output(self.in1, False)
        gpio.output(self.in2, True)

    def stop(self):
        self.pwm.ChangeDutyCycle(0)
        gpio.output(self.in1, False)
        gpio.output(self.in2, False)
        
        
class MotorDriver():
    def __init__(self, left, right):
        self.right_side = right
        self.left_side = left
        
    def move_forward(self, sec, speed):
        self.left_side.forward(speed)
        self.right_side.forward(speed)
        time.sleep(sec)
        
    def move_backwards(self, sec, speed):
        self.left_side.reverse(speed)
        self.right_side.reverse(speed)
        time.sleep(sec)
        
    def turn_left(self, sec, speed):
        self.left_side.reverse(speed)
        self.right_side.forward(speed)
        time.sleep(sec)
        
    def turn_right(self, sec, speed):
        self.left_side.forward(speed)
        self.right_side.reverse(speed)
        time.sleep(sec)
        
    def stop(self):
        self.left_side.stop()
        self.right_side.stop()
    
    def tear_down(self):
        gpio.cleanup()

'''left = MotorSide(ena, in1, in2)
right = MotorSide(enb, in3, in4)
driver = MotorDriver(left, right)

print("forward")
driver.move_forward(2, 50)
time.sleep(1)
print("right")
driver.turn_right(2, 60)
time.sleep(1)
print("left")
driver.turn_left(2, 70)
time.sleep(1)
print("reverse")
driver.move_backwards(2, 100)
time.sleep(1)
gpio.cleanup()'''
