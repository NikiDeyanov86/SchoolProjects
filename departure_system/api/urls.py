from django.urls import include, path
from api import views

urlpatterns = [
    path('board', views.board_json),
]