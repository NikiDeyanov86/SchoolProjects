from rest_framework import serializers
from polls.models import Car


class CarSerializer(serializers.ModelSerializer):
    class Meta:
        model = Car
        fields = ['id', 'color', 'brand', 'description', 'made']