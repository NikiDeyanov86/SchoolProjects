from django.urls import include, path
from polls import views

urlpatterns = [
    path('', views.index),
    path('home', views.home),
    path('cars', views.cars),
    path('cars', views.cars),
]