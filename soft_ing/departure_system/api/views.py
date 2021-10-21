from django.db.models.fields import DateTimeField
from django.shortcuts import render
import urllib, json
from django.template.loader import render_to_string
from api.models import Train
# Create your views here.

from api.serializers import CarSerializer
from django.http import JsonResponse, HttpResponse

url = "https://api-v3.mbta.com/predictions?page%5Boffset%5D=0&page%5Blimit%5D=10&sort=-departure_time&filter%5Bstop%5D=place-north"

def board_json(request):
    response = urllib.request.urlopen(url)
    data = json.loads(response.read())
    trains = []
    
    for train in data['data']:
        new_train = {}
        if train['relationships']['vehicle']['data'] != None:
            new_train['train_id'] = train['relationships']['vehicle']['data']['id']
        new_train['departure_time'] = train['attributes']['departure_time']
        new_train['destination'] = train['relationships']['route']['data']['id']
        new_train['status'] = train['attributes']['status']
        #print(new_train.__dict__)
        trains.append(new_train)

    print(trains)
  
    return render(request, 'board.html', {'trains' : trains})
     