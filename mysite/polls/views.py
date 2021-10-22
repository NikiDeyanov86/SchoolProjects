from polls.models import Car
from django.shortcuts import render

# Create your views here.

from django.http import HttpResponse

def index(request):
    return HttpResponse("Hello, world. You're at the polls index. To get to the home page add /home to the end of the domain.")

def home(request):
    return render(request, 'home.html')

def cars(request):
    get_cars = Car.objects.all()
    return render(request, 'cars.html', {'cars': get_cars})
