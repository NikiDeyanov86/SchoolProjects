from django.test import TestCase
from unittest.mock import Mock, patch
import json

from polls.views import find_avg_temperature, find_max_temperature, find_min_temperature

class TestC(TestCase):
    @patch("requests.post")
    def test_min_temp(self, mocked_requests):
        data = json.load(open('./polls/test_temp.json'))
        mocked_requests.return_value.json = Mock(return_value = data)
        response = find_min_temperature()
        assert response == -3
       
    @patch("requests.post") 
    def test_max_temp(self, mocked_requests):
        data = json.load(open('./polls/test_temp.json'))
        mocked_requests.return_value.json = Mock(return_value = data)
        response = find_max_temperature()
        assert response == 10
        
    @patch("requests.post")
    def test_avg_temp(self, mocked_requests):
        data = json.load(open('./polls/test_temp.json'))
        mocked_requests.return_value.json = Mock(return_value = data)
        response = find_avg_temperature()
        assert response == 3.2
