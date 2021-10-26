from django.db.models.fields import DateTimeField
from django.shortcuts import render
import urllib, json

# Create your views here.


url = "https://api-v3.mbta.com/predictions?page%5Boffset%5D=0&page%5Blimit%5D=10&sort=departure_time&include=schedule%2Ctrip&filter%5Bdirection_id%5D=0&filter%5Bstop%5D=place-north"

def board_json(request):
    response = urllib.request.urlopen(url)
    data = json.loads(response.read())
    trains = []
    #data е много общопонятно име

    for train in data['data']:
        new_train = {}
        if train['relationships']['route']['data']['id'] != None:
            new_train['train_id'] = train['relationships']['route']['data']['id']
        new_train['trip_id'] = train['relationships']['trip']['data']['id']
        new_train['status'] = train['attributes']['status']
        new_train['schedule_id'] = train['relationships']['schedule']['data']['id']
        trains.append(new_train)

    for i in data['included']:
        if i['type'] == "trip":
            for train in trains:
                if i['id'] == train['trip_id']:
                    train['destination'] = i['attributes']['headsign']
                    break
    #итератора 'i' не носи никаква информация какво съдържа.
    for i in data['included']:
        if i['type'] == "schedule":
            for train in trains:
                if i['id'] == train["schedule_id"]:
                    train['departure_time'] = i['attributes']['departure_time']
    #може да се оптимизират for циклите - вместо в три отделни може да е в един главен и два вложени
    print(trains)
    #тоя принт не става ясно за какво е?
    #нямаш управление на грешки
    return render(request, 'board.html', {'trains' : trains})
     