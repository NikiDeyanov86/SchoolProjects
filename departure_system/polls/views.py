from django.shortcuts import render

# Create your views here.

from django.http import HttpResponse

def home(request):
    return render(request, 'home.html')

import urllib, json

url = "https://tues2022.proxy.beeceptor.com/my/api/test"

def return_temperatures(request):
    min = find_min_temperature()
    max = find_max_temperature()
    avg = find_avg_temperature()
    
    return render(request, 'temperature.html', {'temperatures' : [min, max, avg]})

def find_min_temperature():
    response = urllib.request.urlopen(url)
    data = json.loads(response.read())
    print(type(data))
    min = data["data"]["temperature"]
    
    for i in data["data"]:
        if i["temperature"] < min:
            min = i["temperature"]
  
    return min

def find_max_temperature():
    response = urllib.request.urlopen(url)
    data = json.loads(response.read())
    max = data["data"]["temperature"]
    
    for i in data['data']:
        if i["temperature"] > max:
            max = i["temperature"]
  
    return max

def find_avg_temperature():
    response = urllib.request.urlopen(url)
    data = json.loads(response.read())
    avg = 0
    br = 0
    
    for i in data["data"]:
        avg += i["temperature"]
        br += 1
  
    return avg/br