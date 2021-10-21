from rest_framework import serializers


class CarSerializer(serializers.ModelSerializer):
    class Meta:
        fields = ['id', 'color', 'brand', 'description', 'made']