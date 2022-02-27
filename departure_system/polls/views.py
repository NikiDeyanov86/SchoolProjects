from django.shortcuts import render
import requests
import urllib, json
from django.http import HttpResponse

def home(request):
    return render(request, 'home.html')

url = 'https://tues2022.proxy.beeceptor.com/my/api/test'

def return_temperatures(request):
    min = find_min_temperature()
    max = find_max_temperature()
    avg = find_avg_temperature()
    
    return render(request, 'temperature.html', {'temperatures' : {
                                                'min' : min,
                                                'max' : max,
                                                'avg' : avg
    } } )

def find_min_temperature():
    data = requests.post(url).json()
    min = int(data['data'][0]['temperature'])
    
    for i in range(len(data['data'])):
        if int(data['data'][i]['temperature']) < min:
            min = int(data['data'][i]['temperature'])
            
    return int(min)

def find_max_temperature():
    data = requests.post(url).json()
    max = int(data['data'][0]['temperature'])
    
    for i in range(len(data['data'])):
        if int(data['data'][i]['temperature']) > max:
            max = int(data['data'][i]['temperature'])
  
    return max

def find_avg_temperature():
    data = requests.post(url).json()
    avg = 0
    
    for i in range(len(data['data'])):
        avg += float(data['data'][i]['temperature'])
  
    return avg/len(data['data'])