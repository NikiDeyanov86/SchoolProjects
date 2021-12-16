from django.urls import path

from app import views

urlpatterns = [
    path('', views.index, name='index'),
    path('projects', views.projects),
    path('technologies', views.technologies)
]