from django.urls import include, path
from api import views

urlpatterns = [
    path('cars', views.cars_json),
]