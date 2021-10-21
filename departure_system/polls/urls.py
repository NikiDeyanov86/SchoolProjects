from django.urls import include, path
from polls import views

urlpatterns = [
    path('', views.home),
]